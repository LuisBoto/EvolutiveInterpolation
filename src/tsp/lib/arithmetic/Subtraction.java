package tsp.lib.arithmetic;

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

}
