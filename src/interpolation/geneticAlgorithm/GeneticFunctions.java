package interpolation.geneticAlgorithm;

import java.util.Random;

import interpolation.arithmetic.Addition;
import interpolation.arithmetic.Cos;
import interpolation.arithmetic.Division;
import interpolation.arithmetic.Log;
import interpolation.arithmetic.Multiplication;
import interpolation.arithmetic.NumericValueVariable;
import interpolation.arithmetic.Operation;
import interpolation.arithmetic.Power;
import interpolation.arithmetic.Sin;
import interpolation.arithmetic.SquareRoot;
import interpolation.arithmetic.Subtraction;
import interpolation.arithmetic.Tan;

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

	public static InterpolationFitnessFunction getFitnessFunction(double[] pointsX, double[] pointsY,
			double errorMargin) {
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
			int variableNumber = representation.getVariableNumber();
			if (variableNumber == 0) // Not a function
				return Double.MAX_VALUE;

			double coordinate, error;
			for (int i = 0; i < size; i++) {
				coordinate = pointListX[i];
				error = Math.abs(pointListY[i] - representation.computeValue(coordinate));
				if (error < errorMargin)
					landedPoints++;
				else
					totalError += error;
			}

			double pointShapeOriginal, pointShapeIndividual, point1, point2;
			double pointShapeError = 0.0;
			for (int i = 1; i < size; i++) {
				pointShapeOriginal = pointListY[i] - pointListY[i - 1];
				point1 = representation.computeValue(pointListX[i]);
				point2 = representation.computeValue(pointListX[i - 1]);
				pointShapeIndividual = point1 - point2;

				error = Math.abs(pointShapeOriginal - pointShapeIndividual);
				if (error > errorMargin)
					pointShapeError += error;
			}

			double length = representation.getLength();
			// pointShape weights 3 times more than basic error importance-wise
			double fitness = (totalError * (pointShapeError * 3.0)); // * (size - landedPoints);
			// Less fitness value is better
			return fitness * (1.0 + length / 5.0); // To encourage shorter equations, each additional
													// length piece represents +20% fitness increment
		}
	}

}
