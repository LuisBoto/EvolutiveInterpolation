package tsp.lib.arithmetic;

public class Variable extends Operation {
	
	public Variable(Operation firstOperator) {
		super(firstOperator, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return variableValue;
	}

	@Override
	public String toString() {
		return "x";
	}	

}
