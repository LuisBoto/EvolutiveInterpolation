package tsp;

import java.util.HashSet;
import java.util.Set;

import javax.script.ScriptException;

import tsp.geneticAlgorithm.FitnessFunction;
import tsp.geneticAlgorithm.GeneticAlgorithm;
import tsp.geneticAlgorithm.Individual;
import tsp.geneticAlgorithm.GeneticFunctions;
import tsp.geneticAlgorithm.GeneticFunctions.InterpolationFitnessFunction;

public class GALauncher {

	public static void main(String[] args) throws ScriptException {
		String points = args[0];
		double[] pointList = new double[points.split(",").length];
		int i = 0;
		for (String val : points.split("[")[1].split("]")[0].split(",")) {
			pointList[i] = Double.parseDouble(val);
			i++;
		}
		
		int reproduce = Integer.parseInt(args[1]);
		int mutate = Integer.parseInt(args[2]);
		int popSize = Integer.parseInt(args[3]);
		double crossoverProbability = Double.parseDouble(args[4]);
		double mutationProbability = Double.parseDouble(args[5]);
		int maxTime = Integer.parseInt(args[6]) * 1000;

		/*
		 * double[] pointList = { 0.0, 1.0, 2.0, 3.0, 4.0}; int popSize = 100; double
		 * crossoverProbability = 0.8; double mutationProbability = 0.2; int maxTime =
		 * 1000 * 60 * 1; int reproduce = 1; int mutate = 1;
		 */

		callGeneticAlgorithm(pointList, popSize, crossoverProbability, mutationProbability, maxTime, reproduce, mutate);
	}

	private static void callGeneticAlgorithm(double[] pointList, int populationSize, double crossoverProbability,
			double mutationProbability, int maxTime, int reproduceOperator, int mutationOperator) {

		System.out.println("--- GeneticAlgorithm ---");
		FitnessFunction<String> fitnessFunction = GeneticFunctions.getFitnessFunction();
		((InterpolationFitnessFunction) fitnessFunction).setPointList(pointList);

		// Generate an initial population
		Set<Individual<String>> population = new HashSet<>();
		for (int i = 0; i < populationSize; i++)
			population.add(GeneticFunctions.generateRandomIndividual());

		GeneticAlgorithm<String> ga = new GeneticAlgorithm<>(crossoverProbability, mutationProbability, maxTime);
		System.out.println("Starting evolution");
		Individual<String> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction);

		System.out.println("\nBest Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("Fitness = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations = " + ga.getIterations());
		System.out.println("Took = " + ga.getTimeInMilliseconds() + "ms.");
	}

}
