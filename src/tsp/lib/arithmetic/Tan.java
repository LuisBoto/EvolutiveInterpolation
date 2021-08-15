package tsp.lib.arithmetic;

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
		return "cos(" + firstOperator.toString() + ")";
	}

}
