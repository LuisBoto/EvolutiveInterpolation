package test;

import org.junit.jupiter.api.Test;

import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import interpolation.lib.arithmetic.Multiplication;
import interpolation.lib.arithmetic.NumericValueVariable;
import interpolation.lib.arithmetic.Operation;
import interpolation.lib.arithmetic.Power;
import interpolation.lib.arithmetic.Sin;
import junit.framework.Assert;

class ArithmeticTest {

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
			System.out.print(initial+" -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+" -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+" -> ");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+" # ");
			initial = initial.removeLeafOperation();
			System.out.print(initial+" -> ");
			initial = initial.removeLeafOperation();
			System.out.print(initial+" -> ");
			initial = initial.removeLeafOperation();
			System.out.print(initial+"\n");
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
		for (int i=0; i<4; i++) {
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
		for (int i=0; i<4; i++) {
			sin = sin.removeAtRandomPoint();
			System.out.println(sin);
		}
	}

}
