package interpolation.lib.arithmetic;

import interpolation.geneticAlgorithm.GeneticFunctions;

public abstract class Operation {

	protected Operation firstOperator;
	protected Operation secondOperator;

	public Operation(Operation firstOperator, Operation secondOperator) {
		this.firstOperator = firstOperator;
		this.secondOperator = secondOperator;
	}

	public abstract double computeValue(double variableValue);

	public Operation mutateOperator() {
		if (this instanceof NumericValueVariable)
			return this;
		Operation mutated = GeneticFunctions.getRandomOperation();
		mutated.setFirstOperator(this.getFirstOperator());
		mutated.setSecondOperator(this.getSecondOperator());
		return mutated;
	}

	public abstract String toString();

	public void setFirstOperator(Operation operator) {
		this.firstOperator = operator;
	}

	public void setSecondOperator(Operation operator) {
		this.secondOperator = operator;
	}

	public Operation getFirstOperator() {
		return this.firstOperator;
	}

	public Operation getSecondOperator() {
		return this.secondOperator;
	}

	public int getLength() {
		return this.getFirstOperator().getLength() + this.getSecondOperator().getLength();
	}

	public int getVariableNumber() {
		if (this.getSecondOperator() != null)
			return this.getFirstOperator().getVariableNumber() + this.getSecondOperator().getVariableNumber();
		return this.getFirstOperator().getVariableNumber();
	}

	// Attempts to recursively simplify operations like (x + x) into 2*x etc
	public Operation simplify() {
		if (this.getFirstOperator() != null) {
			this.setFirstOperator(this.getFirstOperator().simplify());
		}
		if (this.getSecondOperator() != null) {
			this.setSecondOperator(this.getSecondOperator().simplify());
		}
		return this;
	}

	protected boolean isZero(Operation value) {
		if (value instanceof NumericValueVariable)
			return ((NumericValueVariable) value).getValue() <= 0.00001;
		return false;
	}

}
