package interpolation;

import java.util.ArrayList;
import java.util.Collection;
import javax.script.ScriptException;

import interpolation.geneticAlgorithm.FitnessFunction;
import interpolation.geneticAlgorithm.GeneticAlgorithm;
import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import interpolation.geneticAlgorithm.GeneticFunctions.InterpolationFitnessFunction;

public class GALauncher {

	public static void main(String[] args) throws ScriptException {
		/*String points = args[0];
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
		int maxTime = Integer.parseInt(args[6]) * 1000;*/

		
		double[] pointList = { 90, 40, 231, 147, 159, 201, 16}; 
		int popSize = 1000; 
		double crossoverProbability = 0.9;
		double mutationProbability = 1;
		int maxTime = 5000; // 5000 iterations 
		callGeneticAlgorithm(pointList, popSize, crossoverProbability, mutationProbability, maxTime);
	}

	private static void callGeneticAlgorithm(double[] pointList, int populationSize, double crossoverProbability,
			double mutationProbability, int maxTime) {

		System.out.println("--- GeneticAlgorithm ---");
		FitnessFunction fitnessFunction = GeneticFunctions.getFitnessFunction();
		((InterpolationFitnessFunction) fitnessFunction).setPointList(pointList);

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