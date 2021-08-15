package tsp.lib.arithmetic;

public class Variable extends Operation {
	
	public Variable() {
		super(null, null);
	}

	@Override
	public double computeValue(double variableValue) {
		return variableValue;
	}

	@Override
	public String toString() {
		return "x";
	}	
	
	@Override
	public int getLength() {
		return 1;
	}

}
