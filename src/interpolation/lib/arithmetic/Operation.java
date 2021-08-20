package interpolation.lib.arithmetic;

import java.util.Random;

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

	public void addOperationToLeaf(Operation operationToAdd) {
		// This method adds parameter operation to a leaf node.
		// Parameter must be an operation containing a desired second operator.
		// Ex: null+x, null/3.14, etc.
		// This is used to prevent situations where operations are unbalanced
		// and structured like this example, which difficults simplification:
		// (x / 4 + 1) - (1) // Hard to simplify +1-1!
		if (this instanceof NumericValueVariable)
			return; // Nothing to do
		// Operation shall be added to first leaf found, with preference for the second.
		if (this.getSecondOperator() != null) {
			if (this.getSecondOperator() instanceof NumericValueVariable) {
				operationToAdd.setFirstOperator(this.getSecondOperator());
				this.setSecondOperator(operationToAdd);
				return;
			}
		}
		if (this.getFirstOperator() != null) {
			if (this.getFirstOperator() instanceof NumericValueVariable) {
				operationToAdd.setFirstOperator(this.getFirstOperator());
				this.setFirstOperator(operationToAdd);
				return;
			}
		}
		// If none are leafs, recursive deeper call on a random side.
		Random r = new Random();
		if (r.nextBoolean() && this.getFirstOperator() != null)
			this.getFirstOperator().addOperationToLeaf(operationToAdd);
		else if (this.getSecondOperator() != null)
			this.getSecondOperator().addOperationToLeaf(operationToAdd);
	}
}
