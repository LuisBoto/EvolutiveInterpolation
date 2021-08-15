package tsp.lib.arithmetic;

import java.text.DecimalFormat;

public class NumericValue extends Operation {

	private double value;

	public NumericValue(double value) {
		super(null, null); // No operands just a single numeric value
		this.value = value;
	}

	public void setValue(double newValue) {
		this.value = newValue;
	}

	@Override
	public double computeValue(double variableValue) {
		return this.value;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("####.##");
		return df.format(this.value);
	}

}
