package tsp.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tsp.lib.Util;
import tsp.lib.arithmetic.Operation;
import tsp.metricFramework.Algorithm;

public class GeneticAlgorithm<A> extends Algorithm<A> {

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
		return getIterations()>=maxTime || metrics.getValue("bestFitness").equals("0.0");
	}

	@Override
	protected boolean saveCondition() {
		// long module10s = getTimeInMilliseconds() % 10000;
		// return (module10s > 9950 || module10s < 50); // Every 10s save metrics, with
		// an error margin
		return getIterations() % 10 == 0;
	}

	public Individual<A> geneticAlgorithm(Collection<Individual<A>> initPopulation, FitnessFunction<A> fitnessFn) {
		// Initial values
		metrics.setValue("mutations", 0);
		metrics.setValue("crossovers", 0);
		Individual<A> bestIndividual = null;

		// Create a local copy of the population to work with
		List<Individual<A>> population = new ArrayList<>(initPopulation);
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

	private void printStatus(Individual<A> bestIndividual) {
		// Monitor average and best fitness, time, iteration etc.
		System.out.println("\nTime: " + getTimeInMilliseconds() + " Gen: " + getIterations() + " Best f: "
				+ metrics.getValue("bestFitness") + " Best individual: " + bestIndividual.toString());
	}

	public Individual<A> retrieveBestIndividual(Collection<Individual<A>> population) {
		Individual<A> bestIndividual = null;
		double bestSoFarFValue = Double.POSITIVE_INFINITY;

		for (Individual<A> individual : population) {
			double fValue = individual.getFitness();
			if (fValue < bestSoFarFValue) {
				bestIndividual = individual;
				bestSoFarFValue = fValue;
			}
		}

		return bestIndividual;
	}

	protected void calculateFitness(Collection<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		for (Individual<A> individual : population)
			individual.setFitness(fitnessFn.apply(individual));
	}

	protected List<Individual<A>> nextGeneration(List<Individual<A>> population, Individual<A> bestBefore) {
		List<Individual<A>> newPopulation = new ArrayList<>(population.size());
		for (int i = 0; i < population.size() - 1; i++) { // -1 for elitism
			Individual<A> x = randomSelection(population);
			Individual<A> y = randomSelection(population);
			Individual<A> child = x;
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

	protected Individual<A> randomSelection(List<Individual<A>> population) {
		// Default result is last individual to avoid problems with rounding errors
		Individual<A> selected = population.get(population.size() - 1);

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
		double totalSoFar = 1.0;
		for (int i = 0; i < fValues.length; i++) {
			totalSoFar -= fValues[i];
			if (prob >= totalSoFar) {
				selected = population.get(i);
				break;
			}
		}

		selected.incDescendants();
		return selected;
	}

	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		Operation child = GeneticFunctions.getRandomOperation();
		child.setFirstOperator(x.getRepresentation());
		child.setSecondOperator(y.getRepresentation());
		metrics.incrementIntValue("crossovers");
		return new Individual<A>(child);
	}

	protected double averageFitness(List<Individual<A>> population) {
		double totalFitness = 0.0;
		for (int i = 0; i < population.size(); i++) {
			totalFitness += population.get(i).getFitness();
		}
		return totalFitness / population.size();
	}

	protected Individual<A> mutate(Individual<A> child) {
		// 50% to add new operation to existing equation
		// 50% to remove last operator
		// Independent 10% to change numeric value and/or equation variables
		Operation mutatedRepresentation = child.getRepresentation();
		if (random.nextBoolean()) { // Add operation
			mutatedRepresentation = GeneticFunctions.getRandomOperation();
			mutatedRepresentation.setFirstOperator(child.getRepresentation());
		} else if (child.getRepresentation().isRemovable()) { // Remove last operation if possible
			mutatedRepresentation = child.getRepresentation().getFirstOperator();
		}

		if (random.nextDouble() * 100 < 10)
			mutatedRepresentation.mutateNumericValuesVariables();

		metrics.incrementIntValue("mutations");
		return new Individual<A>(mutatedRepresentation);
	}

	protected int randomOffset(int length) {
		return random.nextInt(length);
	}

	protected void validatePopulation(Collection<Individual<A>> population) {
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