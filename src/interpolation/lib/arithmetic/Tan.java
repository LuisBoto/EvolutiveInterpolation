package interpolation.lib.arithmetic;

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
		return "tan(" + firstOperator.toString() + ")";
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
