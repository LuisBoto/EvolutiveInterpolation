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

	public boolean isRemovable() {
		// Returns whether the operation is shrinkable
		return !(this instanceof Variable || this instanceof NumericValue);
	}

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
		if (this.firstOperator != null) {
			if (this.firstOperator instanceof NumericValue || this.firstOperator instanceof Variable) {
				// Change numeric value or turn into a variable
				this.firstOperator = GeneticFunctions.getRandomVariableNumericValue();
			} else {
				this.firstOperator.mutateNumericValuesVariables();
			}
		}
		if (this.secondOperator != null) {
			if (this.secondOperator instanceof NumericValue || this.secondOperator instanceof Variable) {
				// Change numeric value or turn into a variable
				this.secondOperator = GeneticFunctions.getRandomVariableNumericValue();
			} else {
				this.secondOperator.mutateNumericValuesVariables();
			}
		}
	}

}
