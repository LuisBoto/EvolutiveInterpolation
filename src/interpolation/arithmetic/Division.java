package interpolation.arithmetic;

public class Division extends Operation {

	public Division(Operation firstOperator, Operation secondOperator) {
		super(firstOperator, secondOperator);
	}

	@Override
	public double computeValue(double variableValue) {
		return firstOperator.computeValue(variableValue) / secondOperator.computeValue(variableValue);
	}

	@Override
	public String toString() {
		return "((" + firstOperator.toString() + ") / (" + secondOperator.toString() + "))";
	}

	@Override
	public Operation simplify() {
		Operation simplifiedOperation = super.simplify(); // Super implementation handles recursivity
		// Order is important! Check simple cases first
		if (this.getFirstOperator() instanceof NumericValueVariable
				&& this.getSecondOperator() instanceof NumericValueVariable
				&& !((NumericValueVariable) this.getFirstOperator()).isVariable()
				&& !((NumericValueVariable) this.getSecondOperator()).isVariable()) {
			// Both operators are numeric values
			double value1 = ((NumericValueVariable) this.getFirstOperator()).getValue();
			double value2 = ((NumericValueVariable) this.getSecondOperator()).getValue();
			return new NumericValueVariable(value1 / value2, false);
		}
		if (super.isZero(this.getFirstOperator()) || super.isZero(this.getSecondOperator())) {
			if (super.isZero(this.getFirstOperator()))
				return new NumericValueVariable(0, false);
			if (super.isZero(this.getSecondOperator()))
				return new NumericValueVariable(Double.POSITIVE_INFINITY, false); // infinite
		}
		if (this.getFirstOperator().toString().equals(this.getSecondOperator().toString())) {
			// Both operators encode the same arithmetic operation (ex: x/x turns into 1)
			return new NumericValueVariable(1, false);
		}
		if (isVariableValue(this.getSecondOperator())
				&& ((NumericValueVariable) this.getSecondOperator()).getValue() == 1)
			return this.getFirstOperator();
		return simplifiedOperation;
	}

}
