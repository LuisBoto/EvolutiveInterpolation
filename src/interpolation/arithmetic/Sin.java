package interpolation.arithmetic;

import interpolation.geneticAlgorithm.GeneticFunctions;

public class Sin extends Operation {

	public Sin(Operation firstOperator) {
		super(firstOperator, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return Math.sin(firstOperator.computeValue(variableValue));
	}

	@Override
	public String toString() {
		return "sin(" + firstOperator.toString() + ")";
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
			return new NumericValueVariable(Math.sin(value), false);
		}
		return simplifiedOperation;
	}
	
	@Override
	public void setSecondOperator(Operation secondOperator) {
		this.secondOperator = null;
	}

}
