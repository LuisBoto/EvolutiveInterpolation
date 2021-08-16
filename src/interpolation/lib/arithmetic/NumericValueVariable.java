package interpolation.lib.arithmetic;

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
	
	public NumericValueVariable(boolean isVariable) {
		super(null, null); // No operands just a single value
		Random rand = new Random();
		if (rand.nextBoolean())
			this.value = rand.nextDouble()*10;
		else
			this.value = rand.nextInt(10);
		this.isVariable = isVariable;
	}

	public void setValue(double newValue) {
		this.value = newValue;
	}

	public double getValue() {
		return this.value;
	}

	public boolean isVariable() {
		return this.isVariable;
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
		isVariable = r.nextBoolean();
		if (r.nextBoolean())
			value = r.nextDouble() * 1000.0;
		else
			value = r.nextInt(1000);
	}

}
