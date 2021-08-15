package tsp.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tsp.lib.Util;
import tsp.lib.arithmetic.NumericValueVariable;
import tsp.lib.arithmetic.Operation;
import tsp.metricFramework.Algorithm;

public class GeneticAlgorithm extends Algorithm {

	protected double crossoverProbability;
	protected double mutationProbability;
	protected int maxTime;
	protected Random random;

	public GeneticAlgorithm(double crossoverProbability, double mutationProbability, int maxTime) {
		this(crossoverProbability, mutationProbability, maxTime, new Random());
	}

	public GeneticAlgorithm(double crossoverProbability, double mutationProbability, int maxTime, Random random) {
		super(); // Calls createTrackers and thread
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.maxTime = maxTime;
		this.random = random;

		assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
		assert (this.crossoverProbability >= 0.0 && this.crossoverProbability <= 1.0);
	}

	@Override
	protected void createTrackers() {
		this.addProgressTracker(Algorithm.ITERATIONS);
		this.addProgressTracker(Algorithm.TIME_IN_MILLISECONDS);
		this.addProgressTracker("bestFitness");
		this.addProgressTracker("averageFitness");
		this.addProgressTracker("mutations");
		this.addProgressTracker("crossovers");
	}

	@Override
	protected boolean stopCondition() {
		// return getTimeInMilliseconds() > this.maxTime;
		return getIterations() >= maxTime || metrics.getValue("bestFitness").equals("0.0");
	}

	@Override
	protected boolean saveCondition() {
		// long module10s = getTimeInMilliseconds() % 10000;
		// return (module10s > 9950 || module10s < 50); // Every 10s save metrics, with
		// an error margin
		return getIterations() % 10 == 0;
	}

	public Individual geneticAlgorithm(Collection<Individual> initPopulation, FitnessFunction fitnessFn) {
		// Initial values
		metrics.setValue("mutations", 0);
		metrics.setValue("crossovers", 0);
		Individual bestIndividual = null;

		// Create a local copy of the population to work with
		List<Individual> population = new ArrayList<>(initPopulation);
		validatePopulation(population);

		updateMetrics(population, 0);
		this.calculateFitness(initPopulation, fitnessFn); // Must be called so fitness values are available
		bestIndividual = retrieveBestIndividual(initPopulation);
		this.startTime = System.currentTimeMillis();
		int itCount = 0;

		do {
			updateMetrics(population, ++itCount);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			metrics.setValue(Algorithm.ITERATIONS, itCount);
			metrics.setValue("bestFitness", bestIndividual.getFitness());
			metrics.setValue("averageFitness", averageFitness(population));
			printStatus(bestIndividual);
			metricsDumpCheck();

			population = nextGeneration(population, bestIndividual);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			this.calculateFitness(population, fitnessFn);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			bestIndividual = retrieveBestIndividual(population);
		} while (!this.stopCondition());

		metricsDumpCheck();
		return bestIndividual;
	}

	private void printStatus(Individual bestIndividual) {
		// Monitor average and best fitness, time, iteration etc.
		System.out.println("\nTime: " + getTimeInMilliseconds() + " Gen: " + getIterations() + " Best f: "
				+ metrics.getValue("bestFitness") + " Best individual: " + bestIndividual.toString());
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
			Individual y = randomSelection(population);
			Individual child = x;
			if (random.nextDouble() <= crossoverProbability)
				child = this.reproduce(x, y);

			if (random.nextDouble() <= mutationProbability) {
				child = this.mutate(child);
			}
			newPopulation.add(child);
		}
		newPopulation.add(bestBefore); // Adding elite individual not iterated on loop
		return newPopulation;
	}

	protected Individual randomSelection(List<Individual> population) {
		// Default result is last individual to avoid problems with rounding errors
		Individual selected = population.get(population.size() - 1);

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		double minFitness = population.get(0).getFitness();
		for (int i = 0; i < population.size(); i++) {
			fValues[i] = population.get(i).getFitness();
			if (minFitness > fValues[i])
				minFitness = fValues[i];
		}

		// Fitness scalation: Every individual is sustracted lowest fitness
		for (int i = 0; i < population.size(); i++) {
			fValues[i] -= minFitness;
		}
		fValues = Util.normalize(fValues);

		double prob = random.nextDouble();
		double totalSoFar = 0.0;
		for (int i = 0; i < fValues.length; i++) {
			totalSoFar += fValues[i];
			if (prob <= totalSoFar) {
				selected = population.get(i);
				break;
			}
		}

		selected.incDescendants();
		return selected;
	}

	protected Individual reproduce(Individual x, Individual y) {
		Operation child = GeneticFunctions.getRandomOperation();
		child.setFirstOperator(x.getRepresentation());
		child.setSecondOperator(y.getRepresentation());
		metrics.incrementIntValue("crossovers");
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
		// 30% to change one existing operator type
		// 30% to remove last half of the equation
		// 30% to change numeric value and/or equation variables
		Operation mutatedRepresentation = child.getRepresentation();
		int mutationType = random.nextInt(3);
		if (mutationType == 0) { // Remove last operation if possible
			if (child.getRepresentation().getLength() > 1)
				mutatedRepresentation = child.getRepresentation().getFirstOperator();
		}
		if (mutationType == 1) { // Change operator
			Operation newOperator = GeneticFunctions.getRandomOperation();
			if (mutatedRepresentation.getFirstOperator() != null)
			newOperator.setFirstOperator(mutatedRepresentation.getFirstOperator());
			if (mutatedRepresentation.getSecondOperator() != null)
				newOperator.setSecondOperator(mutatedRepresentation.getSecondOperator());
			mutatedRepresentation = newOperator;
		}
		if (mutationType == 2) { // Change numeric/variable
			//mutateNumericValuesVariables(mutatedRepresentation);
		}

		metrics.incrementIntValue("mutations");
		return new Individual(mutatedRepresentation);
	}
	
	private void mutateNumericValuesVariables(Operation representation) {
		if (representation.getFirstOperator() != null) {
			if (representation.getFirstOperator() instanceof NumericValueVariable) {
				// Change numeric value or turn into a variable
				((NumericValueVariable) representation.getFirstOperator()).mutate();
			} else {
				mutateNumericValuesVariables(representation.getFirstOperator());
			}
		}
		if (representation.getSecondOperator() != null) {
			if (representation.getSecondOperator() instanceof NumericValueVariable) {
				// Change numeric value or turn into a variable
				((NumericValueVariable) representation.getSecondOperator()).mutate();
			} else {
				mutateNumericValuesVariables(representation.getSecondOperator());
			}
		}
	}

	protected int randomOffset(int length) {
		return random.nextInt(length);
	}

	protected void validatePopulation(Collection<Individual> population) {
		if (population.size() < 1) {
			throw new IllegalArgumentException("Must start with at least a population of size 1");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String getExecutionFilename() {
		return "GAInterpolation_" + getPopulationSize() + "_" + crossoverProbability + "_" + mutationProbability + "_"
				+ maxTime + "_GMT_" + new Date().toGMTString().replace(':', '_').replace(" ", "_")
				+ random.nextInt(10000) + ".csv";
	}

}