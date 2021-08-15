package tsp.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tsp.lib.Util;
import tsp.lib.arithmetic.Addition;
import tsp.lib.arithmetic.Cos;
import tsp.lib.arithmetic.Division;
import tsp.lib.arithmetic.Multiplication;
import tsp.lib.arithmetic.Operation;
import tsp.lib.arithmetic.Power;
import tsp.lib.arithmetic.Sin;
import tsp.lib.arithmetic.Subtraction;
import tsp.lib.arithmetic.Tan;
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
		return getTimeInMilliseconds() > this.maxTime;
	}

	@Override
	protected boolean saveCondition() {
		long module10s = getTimeInMilliseconds() % 10000;
		return (module10s > 9950 || module10s < 50); // Every 10s save metrics, with an error margin
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
			printStatus();
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

	private void printStatus() {
		// Monitor average and best fitness, time, iteration etc.
		System.out.println("\nTime: " + getTimeInMilliseconds() + " Gen: " + getIterations() + " Best f: "
				+ metrics.getValue("bestFitness") + " Average f:" + metrics.getValue("averageFitness"));
	}

	public Individual<A> retrieveBestIndividual(Collection<Individual<A>> population) {
		Individual<A> bestIndividual = null;
		double bestSoFarFValue = 0;

		for (Individual<A> individual : population) {
			double fValue = individual.getFitness();
			if (fValue > bestSoFarFValue) {
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

	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		// Random type of operation
		int type = random.nextInt(8); // 8 kinds of basic operation
		Operation childOperation = null;
		switch (type) {
		case 0: // Addition
			childOperation = new Addition(x.getRepresentation(), y.getRepresentation());
			break;
		case 1: // Subtraction
			childOperation = new Subtraction(x.getRepresentation(), y.getRepresentation());
			break;
		case 2: // Multiplication
			childOperation = new Multiplication(x.getRepresentation(), y.getRepresentation());
			break;
		case 3: // Division
			childOperation = new Division(x.getRepresentation(), y.getRepresentation());
			break;
		case 4: // Power
			childOperation = new Power(x.getRepresentation(), y.getRepresentation());
			break;
		// Single operator operations leave second parent out...
		case 5: // Sin
			childOperation = new Sin(x.getRepresentation());
			break;
		case 6: // Cos
			childOperation = new Cos(x.getRepresentation());
			break;
		case 7: // Tan
			childOperation = new Tan(x.getRepresentation());
			break;
		}
		metrics.incrementIntValue("crossovers");
		return new Individual<A>(childOperation);
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
		// Independent coin toss to change numeric value and/or equation variables
		Operation mutatedRepresentation;
		if (random.nextBoolean()) { // Add operation
			mutatedRepresentation = GeneticFunctions.getRandomOperation();
			mutatedRepresentation.setFirstOperator(child.getRepresentation());
		} else { // Remove last operation
			mutatedRepresentation = child.getRepresentation().getFirstOperator();
		}

		if (random.nextBoolean())
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
		return "GAInterpolation_" + getPopulationSize() + "_"
				+ crossoverProbability + "_" + mutationProbability + "_" + maxTime + "_GMT_"
				+ new Date().toGMTString().replace(':', '_').replace(" ", "_") + random.nextInt(10000) + ".csv";
	}

}