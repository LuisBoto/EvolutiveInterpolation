package interpolation;

import java.util.ArrayList;
import java.util.Collection;

import javax.script.ScriptException;

import interpolation.geneticAlgorithm.FitnessFunction;
import interpolation.geneticAlgorithm.GeneticAlgorithm;
import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.GeneticFunctions.InterpolationFitnessFunction;
import interpolation.geneticAlgorithm.Individual;

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

		double[] pointListX = new double[50];
		double[] pointListY = new double[50];
		for (double i=0; i<50; i++) {
			pointListX[(int) i] = i;
			pointListY[(int) i] = ((Math.pow(i, 2.0)*Math.sin(4.0*i)))/((4.0*i*i)+1);
		}
		int popSize = 100;
		double crossoverProbability = 0.9;
		double mutationProbability = 0.8;
		int maxTime = 5000; // 5000 iterations
		callGeneticAlgorithm(pointListX, pointListY, popSize, crossoverProbability, mutationProbability, maxTime);
	}

	private static void callGeneticAlgorithm(double[] pointListX, double[] pointListY, int populationSize,
			double crossoverProbability, double mutationProbability, int maxTime) {

		System.out.println("--- GeneticAlgorithm ---");
		FitnessFunction fitnessFunction = GeneticFunctions.getFitnessFunction();
		((InterpolationFitnessFunction) fitnessFunction).setPointList(pointListX, pointListY);

		// Generate an initial population
		Collection<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < populationSize; i++)
			population.add(GeneticFunctions.generateRandomIndividual());

		GeneticAlgorithm ga = new GeneticAlgorithm(crossoverProbability, mutationProbability, maxTime);
		System.out.println("Starting evolution");
		Individual bestIndividual = ga.geneticAlgorithm(population, fitnessFunction);

		System.out.println("\nBest Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("Fitness = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations = " + ga.getIterations());
		System.out.println("Took = " + ga.getTimeInMilliseconds() + "ms.");
	}

}
