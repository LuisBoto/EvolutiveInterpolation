package tsp.geneticAlgorithm;

import java.util.Random;

import tsp.lib.arithmetic.Addition;
import tsp.lib.arithmetic.Cos;
import tsp.lib.arithmetic.Division;
import tsp.lib.arithmetic.Multiplication;
import tsp.lib.arithmetic.NumericValueVariable;
import tsp.lib.arithmetic.Operation;
import tsp.lib.arithmetic.Power;
import tsp.lib.arithmetic.Sin;
import tsp.lib.arithmetic.Subtraction;
import tsp.lib.arithmetic.Tan;

public class GeneticFunctions {

	public static Individual generateRandomIndividual() {
		return new Individual(getRandomOperation());
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
		if (rand.nextBoolean())
			return new NumericValueVariable(rand.nextDouble() * 1000.0, rand.nextBoolean());
		return new NumericValueVariable(rand.nextInt(1000), rand.nextBoolean());
	}
	
	public static Operation cloneRepresentationRecursive(Operation present) {
		String className = present.getClass().getSimpleName(); 
		switch (className) {
		case "Addition":
			return new Addition(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Subtraction":
			return new Subtraction(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Multiplication":
			return new Multiplication(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Division":
			return new Division(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Power":
			return new Power(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Sin":
			return new Sin(cloneRepresentationRecursive(present.getFirstOperator()));
		case "Cos":
			return new Cos(cloneRepresentationRecursive(present.getFirstOperator()));
		case "Tan":
			return new Tan(cloneRepresentationRecursive(present.getFirstOperator()));
		case "NumericValueVariable":
			return new NumericValueVariable(((NumericValueVariable) present).getValue(),
					((NumericValueVariable) present).isVariable());
		default:
			return null;
		}
	}

	public static InterpolationFitnessFunction getFitnessFunction() {
		return new InterpolationFitnessFunction();
	}

	public static class InterpolationFitnessFunction implements FitnessFunction {

		private double[] pointList;

		public void setPointList(double[] points) {
			this.pointList = points;
		}

		public double apply(Individual individual) {
			double fitness = 0;
			int size = pointList.length;

			for (int i = 0; i < size; i++) {
				fitness += Math.abs(pointList[i] - individual.getRepresentation().computeValue(i));
			}

			return fitness*individual.getRepresentation().getLength(); // Less fitness value is better
		}
	}

}
