package interpolation.lib.arithmetic;

import interpolation.geneticAlgorithm.GeneticFunctions;

public class SquareRoot extends Operation {

	public SquareRoot(Operation firstOperator) {
		super(firstOperator, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return Math.sqrt(firstOperator.computeValue(variableValue));
	}

	@Override
	public int getLength() {
		return this.getFirstOperator().getLength() + 1;
	}

	@Override
	public String toString() {
		return "sqrt(" + firstOperator.toString() + ")";
	}

	@Override
	public Operation simplify() {
		Operation simplifiedOperation = super.simplify(); // Super implementation handles recursivity
		// Order is important! Check simple cases first
		if (super.isZeroOrNegative(this.getFirstOperator())) {
			// If zero or negative
			return new NumericValueVariable(0, false);
		}
		if (this.getFirstOperator() instanceof NumericValueVariable
				&& !((NumericValueVariable) this.getFirstOperator()).isVariable())
			return new NumericValueVariable(this.computeValue(1), false);
		if (this.getFirstOperator() instanceof Power) {
			if (isVariableValue(this.getFirstOperator().getSecondOperator())
					&& ((NumericValueVariable) this.getFirstOperator().getSecondOperator()).getValue() == 2)
				return this.getFirstOperator().getFirstOperator();
		}
		return simplifiedOperation;
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
	public void setSecondOperator(Operation secondOperator) {
		this.secondOperator = null;
	}

}
