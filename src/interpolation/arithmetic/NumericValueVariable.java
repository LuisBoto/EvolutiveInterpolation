package interpolation.arithmetic;

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
		this.setRandomValue();
		this.isVariable = isVariable;
	}

	public void setValue(double newValue) {
		this.value = newValue;
	}

	public void setRandomValue() {
		Random rand = new Random();
		if (rand.nextBoolean())
			this.value = rand.nextDouble() * 10 + 1;
		else
			this.value = rand.nextInt(10) + 1;
		if (rand.nextBoolean()) // Whether it'll be negative
			this.value *= -1;
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
		DecimalFormat df = new DecimalFormat("####.###");
		return df.format(this.value);
	}

	@Override
	public int getLength() {
		return 1;
	}

	public void mutate() {
		Random r = new Random();
		isVariable = r.nextBoolean();
		this.setRandomValue();
	}

	@Override
	public int getVariableNumber() {
		if (this.isVariable)
			return 1;
		return 0;
	}

}
