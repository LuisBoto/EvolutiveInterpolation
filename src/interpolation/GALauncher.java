package interpolation;

import java.util.ArrayList;
import java.util.Collection;

import javax.script.ScriptException;

import interpolation.geneticAlgorithm.FitnessFunction;
import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import interpolation.geneticAlgorithm.InterpolationGeneticAlgorithm;

public class GALauncher {

	public static void main(String[] args) throws ScriptException {
		/*
		 * String points = args[0]; double[] pointList = new
		 * double[points.split(",").length]; int i = 0; for (String val :
		 * points.split("[")[1].split("]")[0].split(",")) { pointList[i] =
		 * Double.parseDouble(val); i++; }
		 * 
		 * int reproduce = Integer.parseInt(args[1]); int mutate =
		 * Integer.parseInt(args[2]); int popSize = Integer.parseInt(args[3]); double
		 * crossoverProbability = Double.parseDouble(args[4]); double
		 * mutationProbability = Double.parseDouble(args[5]); int maxTime =
		 * Integer.parseInt(args[6]) * 1000;
		 */

		double[] pointListX = new double[10];
		double[] pointListY = new double[10];
		double j=0;
		for (double i = 0; i < 10; i++) {
			pointListX[(int) i] = j;
			pointListY[(int) i] = j / Math.log(3.14) * Math.tan(Math.sqrt(Math.pow(j, -2 * j))) + j / 20 + Math.cos(j);
			j+=0.5;
		}
		double errorMargin = 0.01;
		int popSize = 600;
		double crossoverProbability = 0.85;
		double mutationProbability = 0.5;
		int maxTime = 0;
		double lengthPenalty = 0.2;
		boolean allowMultipleMutations = true;
		callGeneticAlgorithm(pointListX, pointListY, errorMargin, popSize, crossoverProbability, mutationProbability,
				lengthPenalty, allowMultipleMutations, maxTime);
	}

	private static void callGeneticAlgorithm(double[] pointListX, double[] pointListY, double errorMargin,
			int populationSize, double crossoverProbability, double mutationProbability, double lengthPenalty,
			boolean allowMultipleMutations, int maxTime) {

		System.out.println("--- Running Genetic Algorithm ---");
		FitnessFunction fitnessFunction = GeneticFunctions.getFitnessFunction(pointListX, pointListY, errorMargin, lengthPenalty);

		// Generate an initial population
		Collection<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < populationSize; i++)
			population.add(GeneticFunctions.generateRandomIndividual());
		InterpolationGeneticAlgorithm ga = new InterpolationGeneticAlgorithm(crossoverProbability, mutationProbability,
				maxTime);

		System.out.println("Starting evolution");
		Individual bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, allowMultipleMutations);
		System.out.println("\nBest Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("Fitness = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + populationSize);
		System.out.println("Iterations = " + ga.getGenerations());
		System.out.println("Took = " + ga.getRunningTimeInMilliseconds() + "ms.");
	}

}
