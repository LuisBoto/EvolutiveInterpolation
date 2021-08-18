package interpolation.lib.arithmetic;

public class Subtraction extends Operation {

	public Subtraction(Operation firstOperator, Operation secondOperator) {
		super(firstOperator, secondOperator);
	}

	@Override
	public double computeValue(double variableValue) {
		return firstOperator.computeValue(variableValue) - secondOperator.computeValue(variableValue);
	}

	@Override
	public String toString() {
		return firstOperator.toString() + " - " + secondOperator.toString();
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
			return new NumericValueVariable(value1 - value2, false);
		}
		if (super.isZero(this.getFirstOperator()) || super.isZero(this.getSecondOperator())) {
			// Case where both are 0 handled above as 0-0 returns 0
			if (super.isZero(this.getFirstOperator()))
				return this.getSecondOperator();
			if (super.isZero(this.getSecondOperator()))
				return this.getFirstOperator();
		}
		if (this.getFirstOperator().toString().equals(this.getSecondOperator().toString())) {
			// Both operators encode the same arithmetic operation (ex: x/2 - x/2 turns into 0)
			return new NumericValueVariable(0, false);
		}
		return simplifiedOperation;
	}

}
