package interpolation;

import java.util.ArrayList;
import java.util.Collection;
import javax.script.ScriptException;

import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import interpolation.geneticAlgorithm.InterpolationFitnessFunction;
import interpolation.geneticAlgorithm.InterpolationGeneticAlgorithm;

public class GALauncher {

	public static void main(String[] args) throws ScriptException {
		double[] pointListX = null, pointListY = null;
		int popSize = 0, maxTime = 0;
		double errorMargin = 0, crossoverRate = 0, mutationRate = 0, lengthPenalty = 0;
		boolean allowMultipleMutations = false;
		System.out.println("Parsing parameter data...");
		try {
			pointListX = new double[args[0].split(" ").length];
			pointListY = new double[args[0].split(" ").length];
			String[] pointsx = args[0].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", " ").split(" ");
			String[] pointsy = args[1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", " ").split(" ");
			for (int i = 0; i < pointsx.length; i++) {
				pointListX[i] = Double.parseDouble(pointsx[i]);
				pointListY[i] = Double.parseDouble(pointsy[i]);
			}
			errorMargin = Double.parseDouble(args[2]);
			popSize = Integer.parseInt(args[3]);
			crossoverRate = Double.parseDouble(args[4]);
			mutationRate = Double.parseDouble(args[5]);
			lengthPenalty = Double.parseDouble(args[6]);
			allowMultipleMutations = Integer.parseInt(args[7]) == 1;
			maxTime = Integer.parseInt(args[8]) * 1000;
		} catch (Exception e) {
			System.out.println("Error parsing parameters, please check your input:");
			e.printStackTrace();
			System.exit(1);
		}

		callGeneticAlgorithm(pointListX, pointListY, errorMargin, popSize, crossoverRate, mutationRate, lengthPenalty,
				allowMultipleMutations, maxTime);
	}

	private static void callGeneticAlgorithm(double[] pointListX, double[] pointListY, double errorMargin,
			int populationSize, double crossoverRate, double mutationRate, double lengthPenalty,
			boolean allowMultipleMutations, int maxTime) {

		System.out.println("--- Running Interpolation Genetic Algorithm ---");
		InterpolationFitnessFunction fitnessFunction = GeneticFunctions.getFitnessFunction(pointListX, pointListY,
				errorMargin, lengthPenalty);

		// Generate an initial population
		Collection<Individual> population = new ArrayList<Individual>();
		for (int i = 0; i < populationSize; i++)
			population.add(GeneticFunctions.generateRandomIndividual());
		InterpolationGeneticAlgorithm ga = new InterpolationGeneticAlgorithm(crossoverRate, mutationRate, maxTime);
		System.out.println("Starting evolution");
		Individual bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, allowMultipleMutations);

		// Printing results
		System.out.println("\nBest Individual:\n" + bestIndividual.getRepresentation());
		String pointsOg = "Originals = ";
		for (int i = 0; i < pointListX.length; i++) {
			pointsOg += pointListY[i] + " ; ";
		}
		System.out.println(pointsOg);
		String points = "Points = ";
		for (int i = 0; i < pointListX.length; i++) {
			points += bestIndividual.getRepresentation().computeValue(pointListX[i]) + " ; ";
		}
		System.out.println(points);
		System.out.println("Population Size = " + populationSize);
		System.out.println("Iterations = " + ga.getGenerations());
		System.out.println("Took = " + ga.getRunningTimeInMilliseconds() + "ms.");
	}

}
