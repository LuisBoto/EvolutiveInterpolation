package test;

import java.util.Random;

import org.junit.jupiter.api.Test;

import interpolation.arithmetic.Multiplication;
import interpolation.arithmetic.NumericValueVariable;
import interpolation.arithmetic.Operation;
import interpolation.arithmetic.Power;
import interpolation.arithmetic.Sin;
import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
class ArithmeticTest {
	
	// Please note these are ad-hoc tests made on the fly to assist development of several features.
	// Do not interpret these as proper functionality testing or expected behaviour.

	@Test
	void simplificationTest() {
		Operation initial;
		Operation simplified;
		double value1, value2;
		for (int i = 0; i < 100000; i++) {
			initial = GeneticFunctions.getRandomOperation();
			simplified = GeneticFunctions.cloneRepresentationRecursive(initial).simplify();
			for (int j = 0; j < 1000; j++) {
				value1 = initial.computeValue(j);
				value2 = simplified.computeValue(j);
				if (Double.isNaN(value1))
					value1 = value2;
				Assert.assertEquals(value1, value2);
			}
		}
	}

	@Test
	void addingRemovingLeafTest() {
		Operation initial;
		for (int i = 0; i < 10; i++) {
			initial = GeneticFunctions.getRandomOperation();
			System.out.print(initial + " -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial + " -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial + " -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial + " # ");
			initial = initial.removeLeafOperation();
			System.out.print(initial + " -> ");
			initial = initial.removeLeafOperation();
			System.out.print(initial + " -> ");
			initial = initial.removeLeafOperation();
			System.out.print(initial + "\n");
		}
	}

	@Test
	void leafRemovalSimpleTest() {
		Operation two = new NumericValueVariable(2, false);
		Operation var = new NumericValueVariable(true);
		Operation mult = new Multiplication(two, var);
		Operation pow = new Power(new NumericValueVariable(3.065, false), mult);
		Operation sin = new Sin(pow);
		System.out.println(sin);
		for (int i = 0; i < 4; i++) {
			sin = sin.removeLeafOperation();
			System.out.println(sin);
		}
	}

	@Test
	void randomPointRemovalSimpleTest() {
		Operation two = new NumericValueVariable(2, false);
		Operation var = new NumericValueVariable(true);
		Operation mult = new Multiplication(two, var);
		Operation pow = new Power(new NumericValueVariable(3.065, false), mult);
		Operation sin = new Sin(pow);
		System.out.println(sin);
		for (int i = 0; i < 4; i++) {
			sin = sin.removeAtRandomPoint();
			System.out.println(sin);
		}
	}

	@Test
	void mutateIndividualTest() {
		Individual mutant = new Individual(GeneticFunctions.getRandomOperation());
		for (int i = 0; i < 50; i++) {
			System.out.print(mutant.getRepresentation() + " \n ");
			mutant = mutate(mutant);
		}
	}

	private static Individual mutate(Individual child) {
		Random random = new Random();
		Operation mutatedRepresentation = child.getRepresentation();
		int mutationType = random.nextInt(5);
		switch (mutationType) {
		case 0: // Remove leaf operation or half operation
			if (random.nextBoolean())
				mutatedRepresentation = mutatedRepresentation.removeLeafOperation();
			else {
				mutatedRepresentation = mutatedRepresentation.removeAtRandomPoint();
			}
			break;
		case 1: // Add new operation to a leaf operation
			mutatedRepresentation.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			break;
		case 2: // Change this Operation type or use a new operation
			Operation newOperator = GeneticFunctions.getRandomOperation();
			if (mutatedRepresentation.getFirstOperator() != null)
				newOperator.setFirstOperator(mutatedRepresentation.getFirstOperator());
			if (mutatedRepresentation.getSecondOperator() != null)
				newOperator.setSecondOperator(mutatedRepresentation.getSecondOperator());
			mutatedRepresentation = newOperator;
			break;
		case 3: // Change numeric values and variables
			mutatedRepresentation = GeneticFunctions.mutateNumericValuesVariables(mutatedRepresentation);
			break;
		case 4: // Change some operators randomly while keeping numeric values & variables
			mutatedRepresentation = GeneticFunctions.mutateOperators(mutatedRepresentation);
			break;
		}
		if (mutatedRepresentation == null)
			mutatedRepresentation = GeneticFunctions.getRandomOperation();
		// if (random.nextBoolean()) // Mutate again
		// return mutate(new Individual(mutatedRepresentation));
		return new Individual(mutatedRepresentation);
	}

}
