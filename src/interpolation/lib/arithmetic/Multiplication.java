package interpolation.lib.arithmetic;

public class Multiplication extends Operation {

	public Multiplication(Operation firstOperator, Operation secondOperator) {
		super(firstOperator, secondOperator);
	}

	@Override
	public double computeValue(double variableValue) {
		return firstOperator.computeValue(variableValue) * secondOperator.computeValue(variableValue);
	}

	@Override
	public String toString() {
		return "(" + firstOperator.toString() + ")*(" + secondOperator.toString() + ")";
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
			return new NumericValueVariable(value1 * value2, false);
		}
		if (super.isZero(this.getFirstOperator()) || super.isZero(this.getSecondOperator())) {
			// Either operator being zero means 0
			return new NumericValueVariable(0, false);
		}
		if (this.getFirstOperator().toString().equals(this.getSecondOperator().toString())) {
			// Both operators encode the same arithmetic operation (ex: x*x turns into x²)
			return new Power(this.getFirstOperator(), new NumericValueVariable(2, false));
		}
		
		if (isVariableValue(this.getFirstOperator())
				&& ((NumericValueVariable) this.getFirstOperator()).getValue() == 1)
			return this.getSecondOperator();
		if (isVariableValue(this.getSecondOperator())
				&& ((NumericValueVariable) this.getSecondOperator()).getValue() == 1)
			return this.getFirstOperator();
		return simplifiedOperation;
	}

}
