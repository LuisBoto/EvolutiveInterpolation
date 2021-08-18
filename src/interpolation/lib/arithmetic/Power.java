package interpolation.lib.arithmetic;

public class Power extends Operation {

	public Power(Operation firstOperator, Operation secondOperator) {
		super(firstOperator, secondOperator);
	}

	@Override
	public double computeValue(double variableValue) {
		return Math.pow(firstOperator.computeValue(variableValue), secondOperator.computeValue(variableValue));
	}

	@Override
	public String toString() {
		return "((" + firstOperator.toString() + ")^(" + secondOperator.toString() + "))";
	}

	@Override
	public Operation simplify() {
		Operation simplifiedOperation = super.simplify(); // Super implementation handles recursivity
		// Order is important! Check simple cases first
		if (this.getFirstOperator() instanceof NumericValueVariable
				&& this.getSecondOperator() instanceof NumericValueVariable
				&& !((NumericValueVariable) this.getFirstOperator()).isVariable()
				&& !((NumericValueVariable) this.getSecondOperator()).isVariable()
				&& (!super.isZero(this.getFirstOperator()) || !super.isZero(this.getSecondOperator()))) {
			// Both operators are numeric values and at least one is not zero
			double value1 = ((NumericValueVariable) this.getFirstOperator()).getValue();
			double value2 = ((NumericValueVariable) this.getSecondOperator()).getValue();
			return new NumericValueVariable(Math.pow(value1, value2), false);
		}
		if (super.isZero(this.getFirstOperator()) || super.isZero(this.getSecondOperator())) {
			if (super.isZero(this.getFirstOperator()) && super.isZero(this.getSecondOperator()))
				return new NumericValueVariable(1, false); // Interpreting 0⁰ as equal to 1 for now
			if (super.isZero(this.getFirstOperator())) // Zero to some power
				return new NumericValueVariable(0, false);
			if (super.isZero(this.getSecondOperator())) // Something to the power of zero
				return new NumericValueVariable(1, false);
		}
		return simplifiedOperation;
	}

}
