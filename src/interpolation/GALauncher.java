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
		for (double i = 1; i < 11; i++) {
			pointListX[(int) i-1] = i;
			pointListY[(int) i-1] = Math.log(Math.pow(i, 3.14*i));
		}
		double errorMargin = 0.1;
		int popSize = 10;
		double crossoverProbability = 0.4;
		double mutationProbability = 0.9;
		int maxTime = 0; 
		boolean saveExecutionData = false;
		callGeneticAlgorithm(pointListX, pointListY, errorMargin, popSize, crossoverProbability, mutationProbability,
				maxTime, saveExecutionData);
	}

	private static void callGeneticAlgorithm(double[] pointListX, double[] pointListY, double errorMargin,
			int populationSize, double crossoverProbability, double mutationProbability, int maxTime, boolean saveExecutionData) {

		System.out.println("--- GeneticAlgorithm ---");
		FitnessFunction fitnessFunction = GeneticFunctions.getFitnessFunction(pointListX, pointListY, errorMargin);

		// Generate an initial population
		Collection<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < populationSize; i++)
			population.add(GeneticFunctions.generateRandomIndividual());
		InterpolationGeneticAlgorithm ga = new InterpolationGeneticAlgorithm(crossoverProbability, mutationProbability, maxTime);
		
		System.out.println("Starting evolution");
		Individual bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, saveExecutionData);
		System.out.println("\nBest Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("Fitness = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + populationSize);
		System.out.println("Iterations = " + ga.getGenerations());
		System.out.println("Took = " + ga.getRunningTimeInMilliseconds() + "ms.");
	}

}
