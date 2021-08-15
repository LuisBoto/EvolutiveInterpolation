package tsp.lib.arithmetic;

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
		return "((" + firstOperator.toString() + ") ^ (" + secondOperator.toString() + "))";
	}

}
