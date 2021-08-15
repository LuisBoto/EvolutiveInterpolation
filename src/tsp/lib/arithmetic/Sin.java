package tsp.lib.arithmetic;

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

}
