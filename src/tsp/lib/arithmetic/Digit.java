package tsp.lib.arithmetic;

public class Digit extends Operation {
	
	private double value;
	
	public Digit(double value) {
		super(null, null); // No operands just a single numeric value
		this.value = value;
	}

	@Override
	public double computeValue(double variableValue) {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}	

}
