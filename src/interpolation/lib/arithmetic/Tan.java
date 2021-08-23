package interpolation.lib.arithmetic;

import interpolation.geneticAlgorithm.GeneticFunctions;

public class Tan extends Operation {

	public Tan(Operation firstOperator) {
		super(firstOperator, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return Math.tan(firstOperator.computeValue(variableValue));
	}

	@Override
	public String toString() {
		return "tan(" + firstOperator.toString() + ")";
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
	
	@Override
	public Operation simplify() {
		Operation simplifiedOperation = super.simplify();
		if (this.getFirstOperator() instanceof NumericValueVariable) {
			double value = ((NumericValueVariable) getFirstOperator()).getValue();
			return new NumericValueVariable(Math.tan(value), false);
		}
		return simplifiedOperation;
	}
}
