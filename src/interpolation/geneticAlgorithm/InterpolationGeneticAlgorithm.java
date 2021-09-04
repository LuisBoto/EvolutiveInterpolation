package interpolation.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import interpolation.arithmetic.Operation;

public class InterpolationGeneticAlgorithm {

	protected double crossoverProbability;
	protected double mutationProbability;
	protected int maxTime;
	protected Random random;
	private Individual bestIndividual;
	private double startTime;
	private int generations;
	private boolean allowMultipleMutations;

	public InterpolationGeneticAlgorithm(double crossoverProbability, double mutationProbability, int maxTime) {
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.maxTime = maxTime;
		this.random = new Random();

		assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
		assert (this.crossoverProbability >= 0.0 && this.crossoverProbability <= 1.0);
	}

	private boolean stopCondition() {
		if (maxTime <= 0)
			return this.bestIndividual.getFitness() == 0.0;
		return this.bestIndividual.getFitness() == 0.0 || this.getRunningTimeInMilliseconds() >= maxTime;
	}

	public double getRunningTimeInMilliseconds() {
		return System.currentTimeMillis() - this.startTime;
	}

	public Individual geneticAlgorithm(Collection<Individual> initPopulation, FitnessFunction fitnessFn,
			boolean allowMultipleMutations) {
		this.allowMultipleMutations = allowMultipleMutations;
		// Create a local copy of the population to work with
		List<Individual> population = new ArrayList<>(initPopulation);
		validatePopulation(population);

		this.calculateFitness(initPopulation, fitnessFn); // Must be called so fitness values are available
		this.bestIndividual = retrieveBestIndividual(initPopulation);
		this.startTime = System.currentTimeMillis();
		this.generations = 0;

		do {
			population = nextGeneration(population, bestIndividual);
			this.validatePopulation(population);
			this.calculateFitness(population, fitnessFn);
			this.bestIndividual = retrieveBestIndividual(population);
			if (this.getGenerations() % 100 == 0)
				printStatus();
			this.generations++;
		} while (!this.stopCondition());

		return bestIndividual;
	}

	private void printStatus() {
		// Monitor fitness, time, iteration etc.
		System.out.println("\nTime: " + this.getRunningTimeInMilliseconds() + " Gen: " + this.generations + " Best f: "
				+ bestIndividual.getFitness() + " Best individual: " + bestIndividual.toString());
	}

	public Individual retrieveBestIndividual(Collection<Individual> population) {
		Individual bestIndividual = null;
		double bestSoFarFValue = Double.MAX_VALUE;

		for (Individual individual : population) {
			double fValue = individual.getFitness();
			if (fValue < bestSoFarFValue) {
				bestIndividual = individual;
				bestSoFarFValue = fValue;
			}
		}
		return bestIndividual;
	}

	protected void calculateFitness(Collection<Individual> population, FitnessFunction fitnessFn) {
		for (Individual individual : population)
			individual.setFitness(fitnessFn.apply(individual));
	}

	protected List<Individual> nextGeneration(List<Individual> population, Individual bestBefore) {
		List<Individual> newPopulation = new ArrayList<>(population.size());
		for (int i = 0; i < population.size() - 1; i++) { // -1 for elitism
			Individual x = randomSelection(population);
			Individual child = x;
			if (random.nextDouble() <= crossoverProbability) {
				Individual y = randomSelection(population);
				child = this.reproduce(x, y);
			}

			if (random.nextDouble() <= mutationProbability) {
				child = this.mutate(child);
			}
			newPopulation.add(child);
		}
		newPopulation.add(bestBefore); // Adding elite individual not iterated on loop
		return newPopulation;
	}

	protected Individual randomSelection(List<Individual> population) {
		if (random.nextInt(100) == 0)
			return this.retrieveBestIndividual(population);
		// Default result is last individual to avoid problems with rounding errors
		Individual selected = population.get(population.size() - 1);

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		double minFitness = Double.POSITIVE_INFINITY;
		double maxFitness = 0;
		for (int i = 0; i < population.size(); i++) {
			fValues[i] = population.get(i).getFitness();
			if (Double.isInfinite(fValues[i]) || Double.isNaN(fValues[i])) {
				fValues[i] = Double.NaN; // Special cases
				continue;
			}
			if (minFitness > fValues[i])
				minFitness = fValues[i];
			if (maxFitness < fValues[i])
				maxFitness = fValues[i];
		}

		// Fitness escalation: Every individual is subtracted lowest fitness
		for (int i = 0; i < population.size(); i++) {
			if (!Double.isNaN(fValues[i]))
				fValues[i] -= minFitness;
		}
		maxFitness -= minFitness;

		// Tournament
		double prob = random.nextDouble() * maxFitness;
		double totalSoFar = 0.0;
		for (int i = 0; i < fValues.length; i++) {
			if (Double.isInfinite(fValues[i]) || Double.isNaN(fValues[i]))
				continue;
			totalSoFar += maxFitness - fValues[i];
			if (prob <= totalSoFar) {
				selected = population.get(i);
				break;
			}
		}

		return selected;
	}

	protected Individual reproduce(Individual x, Individual y) {
		// 3/4 to fuse a half from each
		// 1/4 to combine both into a longer equation
		Random r = new Random();
		int type = r.nextInt(4);
		Operation child;
		if (type == 3) {
			child = GeneticFunctions.getRandomOperation();
			child.setFirstOperator(x.getRepresentation());
			child.setSecondOperator(y.getRepresentation());
		} else {
			child = x.getRepresentation();
			Operation second = y.getRepresentation();
			if (second.getFirstOperator() != null)
				child.setFirstOperator(second.getFirstOperator());
		}

		return new Individual(child);
	}

	protected double averageFitness(List<Individual> population) {
		double totalFitness = 0.0;
		for (int i = 0; i < population.size(); i++) {
			totalFitness += population.get(i).getFitness();
		}
		return totalFitness / population.size();
	}

	protected Individual mutate(Individual child) {
		Operation mutatedRepresentation = child.getRepresentation();
		int mutationType = random.nextInt(5);
		switch (mutationType) {
		case 0: // Remove leaf operation or half operation
			if (random.nextBoolean())
				mutatedRepresentation = mutatedRepresentation.removeLeafOperation();
			else {
				mutatedRepresentation = mutatedRepresentation.removeAtRandomPoint();
			}
			break;
		case 1: // Add new operation to a leaf operation
			mutatedRepresentation.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			break;
		case 2: // Change this Operation type or use a new operation
			Operation newOperator = GeneticFunctions.getRandomOperation();
			if (mutatedRepresentation.getFirstOperator() != null)
				newOperator.setFirstOperator(mutatedRepresentation.getFirstOperator());
			if (mutatedRepresentation.getSecondOperator() != null)
				newOperator.setSecondOperator(mutatedRepresentation.getSecondOperator());
			mutatedRepresentation = newOperator;
			break;
		case 3: // Change numeric values and variables
			mutatedRepresentation = GeneticFunctions.mutateNumericValuesVariables(mutatedRepresentation);
			break;
		case 4: // Change some operators randomly while keeping numeric values & variables
			mutatedRepresentation = GeneticFunctions.mutateOperators(mutatedRepresentation);
			break;
		}
		if (mutatedRepresentation == null) // Failsafe in case mutations mess individual
			mutatedRepresentation = GeneticFunctions.getRandomOperation();

		if (this.allowMultipleMutations && random.nextBoolean()) // Allows multiple mutations to happen return
			mutate(new Individual(mutatedRepresentation));

		return new Individual(mutatedRepresentation);
	}

	protected int randomOffset(int length) {
		return random.nextInt(length);
	}

	protected void validatePopulation(Collection<Individual> population) {
		if (population.size() < 1) {
			throw new IllegalArgumentException("Must start with at least a population of size 1");
		}
		for (int i = 0; i < population.size(); i++) {
			((ArrayList<Individual>) population).get(i).simplifyRepresentation();
		}
	}

	public int getGenerations() {
		return this.generations;
	}

}