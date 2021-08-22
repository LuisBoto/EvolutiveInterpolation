package interpolation.geneticAlgorithm;

import java.util.Random;

import interpolation.lib.arithmetic.Addition;
import interpolation.lib.arithmetic.Cos;
import interpolation.lib.arithmetic.Division;
import interpolation.lib.arithmetic.Log;
import interpolation.lib.arithmetic.Multiplication;
import interpolation.lib.arithmetic.NumericValueVariable;
import interpolation.lib.arithmetic.Operation;
import interpolation.lib.arithmetic.Power;
import interpolation.lib.arithmetic.Sin;
import interpolation.lib.arithmetic.SquareRoot;
import interpolation.lib.arithmetic.Subtraction;
import interpolation.lib.arithmetic.Tan;

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
		case 5: // Trigonometric
			int kind = rand.nextInt(3);
			if (kind == 0)
				operationToReturn = new Sin(operator1);
			if (kind == 1)
				operationToReturn = new Cos(operator1);
			if (kind == 2)
				operationToReturn = new Tan(operator1);
			break;
		case 6: // Log
			operationToReturn = new Log(operator1);
			break;
		case 7: // Square root
			operationToReturn = new SquareRoot(operator1);
			break;
		}
		return operationToReturn;
	}

	public static Operation getRandomVariableNumericValue() {
		// Random variable or numeric value
		Random rand = new Random();
		return new NumericValueVariable(rand.nextBoolean()); // 50% are variables
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
		case "Log":
			return new Log(cloneRepresentationRecursive(present.getFirstOperator()));
		case "SquareRoot":
			return new SquareRoot(cloneRepresentationRecursive(present.getFirstOperator()));
		case "NumericValueVariable":
			return new NumericValueVariable(((NumericValueVariable) present).getValue(),
					((NumericValueVariable) present).isVariable());
		default:
			return null;
		}
	}

	public static Operation mutateNumericValuesVariables(Operation representation) {
		if (representation.getFirstOperator() != null) {
			if (representation.getFirstOperator() instanceof NumericValueVariable) {
				// Change numeric value or turn into a variable
				((NumericValueVariable) representation.getFirstOperator()).mutate();
			} else {
				mutateNumericValuesVariables(representation.getFirstOperator());
			}
		}
		if (representation.getSecondOperator() != null) {
			if (representation.getSecondOperator() instanceof NumericValueVariable) {
				// Change numeric value or turn into a variable
				((NumericValueVariable) representation.getSecondOperator()).mutate();
			} else {
				mutateNumericValuesVariables(representation.getSecondOperator());
			}
		}
		return representation;
	}

	public static Operation mutateOperators(Operation representation) {
		if (representation.getFirstOperator() != null) {
			if (representation.getFirstOperator() instanceof NumericValueVariable) {
				// We're already on a mutable operation
				return representation.mutateOperator();
			} else {
				// Explore deeper
				representation.setFirstOperator(mutateOperators(representation.getFirstOperator()));
			}
		}
		if (representation.getSecondOperator() != null) {
			if (representation.getSecondOperator() instanceof NumericValueVariable) {
				// We're already on a mutable operation
				return representation.mutateOperator();
			} else {
				// Explore deeper if possible
				representation.setSecondOperator(mutateOperators(representation.getSecondOperator()));
			}
		}
		return representation;
	}

	public static InterpolationFitnessFunction getFitnessFunction(double[] pointsX, double[] pointsY, double errorMargin) {
		return new InterpolationFitnessFunction(pointsX, pointsY, errorMargin);
	}

	public static class InterpolationFitnessFunction implements FitnessFunction {

		private double[] pointListX;
		private double[] pointListY;
		private double errorMargin;

		public InterpolationFitnessFunction(double[] pointsX, double[] pointsY, double errorMargin) {
			this.pointListX = pointsX;
			this.pointListY = pointsY;
			this.errorMargin = errorMargin;
		}

		public double apply(Individual individual) {
			double totalError = 0;
			int landedPoints = 0;
			int size = pointListX.length;

			Operation representation = individual.getRepresentation();
			double coordinate, error;
			for (int i = 0; i < size; i++) {
				coordinate = pointListX[i];
				error = Math.abs(pointListY[i] - representation.computeValue(coordinate));
				if (error < errorMargin)
					landedPoints++;
				else
					totalError += error;
			}
			
			double length = representation.getLength();
			int variableNumber = representation.getVariableNumber();
			if (variableNumber == 0) // Not a function
				return Double.MAX_VALUE;
			
			//System.out.print(landedPoints+" ");
			double fitness = (totalError*(size-landedPoints));
			return fitness*(1.0+length/10.0); // Less fitness value is better
		}
	}

}
