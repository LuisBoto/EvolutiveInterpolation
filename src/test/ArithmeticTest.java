package test;

import org.junit.jupiter.api.Test;

import interpolation.geneticAlgorithm.GeneticFunctions;
import interpolation.geneticAlgorithm.Individual;
import interpolation.lib.arithmetic.Operation;
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
	void addingToLeafTest() {
		Operation initial;
		for (int i = 0; i < 10; i++) {
			initial = GeneticFunctions.getRandomOperation();
			System.out.print(initial+"\t->");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+"\t->");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+"\t->");
			initial.addOperationToLeaf(GeneticFunctions.getRandomOperation());
			System.out.print(initial+"\n");
		}
	}

}
