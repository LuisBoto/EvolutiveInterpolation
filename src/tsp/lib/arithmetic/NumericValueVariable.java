package tsp.lib.arithmetic;

import java.text.DecimalFormat;
import java.util.Random;

public class NumericValueVariable extends Operation {

	private double value;
	private boolean isVariable;

	public NumericValueVariable(double value, boolean isVariable) {
		super(null, null); // No operands just a single value
		this.value = value;
		this.isVariable = isVariable;
	}

	public void setValue(double newValue) {
		this.value = newValue;
	}

	public void setVariable(boolean isVariable) {
		this.isVariable = isVariable;
	}

	@Override
	public double computeValue(double variableValue) {
		if (isVariable)
			return variableValue;
		return this.value;
	}

	@Override
	public String toString() {
		if (isVariable)
			return "x";
		DecimalFormat df = new DecimalFormat("####.##");
		return df.format(this.value);
	}

	@Override
	public int getLength() {
		return 1;
	}
	
	public void mutate() {
		Random r = new Random();
		this.isVariable = r.nextBoolean();
		if (r.nextBoolean())
			this.value= r.nextDouble() * 1000.0;
		else
			this.value = r.nextInt(1000);
	}

}
