package tsp.geneticAlgorithm;

import java.util.Random;

import tsp.lib.arithmetic.Addition;
import tsp.lib.arithmetic.Cos;
import tsp.lib.arithmetic.Division;
import tsp.lib.arithmetic.Multiplication;
import tsp.lib.arithmetic.NumericValue;
import tsp.lib.arithmetic.Operation;
import tsp.lib.arithmetic.Power;
import tsp.lib.arithmetic.Sin;
import tsp.lib.arithmetic.Subtraction;
import tsp.lib.arithmetic.Tan;
import tsp.lib.arithmetic.Variable;

public class GeneticFunctions {

	public static Individual<String> generateRandomIndividual() {
		return new Individual<>(getRandomOperation());
	}

	public static Operation getRandomOperation() {
		Random rand = new Random();
		Operation operator1 = getRandomVariableNumericValue();
		Operation operator2 = getRandomVariableNumericValue();
		// Random type of operation
		int type = rand.nextInt(8); // 8 kinds of basic operation
		Operation operationToReturn = null;
		switch (type) {
		case 0: // Addition
			operationToReturn = new Addition(operator1, operator2);
			break;
		case 1: // Subtraction
			operationToReturn = new Subtraction(operator1, operator2);
			break;
		case 2: // Multiplication
			operationToReturn = new Multiplication(operator1, operator2);
			break;
		case 3: // Division
			operationToReturn = new Division(operator1, operator2);
			break;
		case 4: // Power
			operationToReturn = new Power(operator1, operator2);
			break;
		case 5: // Sin
			operationToReturn = new Sin(operator1);
			break;
		case 6: // Cos
			operationToReturn = new Cos(operator1);
			break;
		case 7: // Tan
			operationToReturn = new Tan(operator1);
			break;
		}
		return operationToReturn;
	}

	public static Operation getRandomVariableNumericValue() {
		// Random variable or numeric value
		Random rand = new Random();
		int type = rand.nextInt(2);
		Operation operator = null;
		switch (type) {
		case 0: // Variable
			operator = new Variable();
			break;
		case 1: // Numeric value
			operator = new NumericValue(rand.nextDouble() * 1000.0);
			break;
		}
		return operator;
	}

	public static InterpolationFitnessFunction getFitnessFunction() {
		return new InterpolationFitnessFunction();
	}

	public static class InterpolationFitnessFunction implements FitnessFunction<String> {

		private double[] pointList;

		public void setPointList(double[] points) {
			this.pointList = points;
		}

		public double apply(Individual<String> individual) {
			double fitness = 0;
			int size = pointList.length;

			for (int i = 0; i < size; i++) {
				fitness += Math.abs(pointList[i] - individual.getRepresentation().computeValue(i));
			}

			return fitness; // Less fitness is better
		}
	}

}
