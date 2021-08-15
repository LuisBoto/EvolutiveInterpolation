package tsp.lib.arithmetic;

import tsp.geneticAlgorithm.GeneticFunctions;

public abstract class Operation {

	protected Operation firstOperator;
	protected Operation secondOperator;

	public Operation(Operation firstOperator, Operation secondOperator) {
		this.firstOperator = firstOperator;
		this.secondOperator = secondOperator;
	}

	public abstract double computeValue(double variableValue);

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

	public void mutateNumericValuesVariables() {
		if (this.getFirstOperator() != null) {
			if (this.firstOperator instanceof NumericValue || this.firstOperator instanceof Variable) {
				// Change numeric value or turn into a variable
				this.setFirstOperator(GeneticFunctions.getRandomVariableNumericValue());
			} else {
				this.getFirstOperator().mutateNumericValuesVariables();
			}
		}
		if (this.getSecondOperator() != null) {
			if (this.secondOperator instanceof NumericValue || this.secondOperator instanceof Variable) {
				// Change numeric value or turn into a variable
				this.setSecondOperator(GeneticFunctions.getRandomVariableNumericValue());
			} else {
				this.getSecondOperator().mutateNumericValuesVariables();
			}
		}
	}

	public int getLength() {
		return this.getFirstOperator().getLength() + this.getSecondOperator().getLength();
	}
}
