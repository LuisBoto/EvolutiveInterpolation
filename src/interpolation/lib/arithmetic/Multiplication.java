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
		return firstOperator.toString() + " · " + secondOperator.toString();
	}

}
