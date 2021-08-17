package interpolation.lib.arithmetic;

import interpolation.geneticAlgorithm.GeneticFunctions;

public class Cos extends Operation {

	public Cos(Operation firstOperator) {
		super(firstOperator, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return Math.cos(firstOperator.computeValue(variableValue));
	}

	@Override
	public String toString() {
		return "cos(" + firstOperator.toString() + ")";
	}

	@Override
	public int getLength() {
		return this.getFirstOperator().getLength() + 1;
	}

	@Override
	public Operation getSecondOperator() {
		return null;
	}
	
	@Override
	public Operation mutateOperator() {
		Operation mutated = GeneticFunctions.getRandomOperation();
		mutated.setFirstOperator(this.getFirstOperator());
		return mutated;
	}
}
