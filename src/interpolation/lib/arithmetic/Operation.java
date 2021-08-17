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

}
