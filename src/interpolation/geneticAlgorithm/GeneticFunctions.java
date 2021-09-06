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
			double errorMargin, double lengthPenalty) {
		return new InterpolationFitnessFunction(pointsX, pointsY, errorMargin, lengthPenalty);
	}
}
