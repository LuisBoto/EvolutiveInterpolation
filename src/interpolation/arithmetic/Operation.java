package interpolation.arithmetic;

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
		Random r = new Random();
		if (this instanceof NumericValueVariable || r.nextBoolean())
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

	protected boolean isZeroOrNegative(Operation value) {
		if (value instanceof NumericValueVariable)
			if (!((NumericValueVariable) value).isVariable())
				return ((NumericValueVariable) value).getValue() <= 0.00001;
		return false;
	}

	protected boolean isZero(Operation value) {
		if (value instanceof NumericValueVariable)
			if (!((NumericValueVariable) value).isVariable())
				return ((NumericValueVariable) value).getValue() == 0;
		return false;
	}

	protected boolean isVariableValue(Operation value) {
		if (value instanceof NumericValueVariable)
			return ((NumericValueVariable) value).isVariable();
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
		if (isLeaf(this.getSecondOperator())) {
			operationToAdd.setFirstOperator(this.getSecondOperator());
			this.setSecondOperator(operationToAdd);
			return;
		}
		if (isLeaf(this.getFirstOperator())) {
			operationToAdd.setFirstOperator(this.getFirstOperator());
			this.setFirstOperator(operationToAdd);
			return;
		}
		// If none are leafs, recursive deeper call on a side.
		Random r = new Random();
		if (r.nextBoolean() && this.getSecondOperator() != null)
			this.getSecondOperator().addOperationToLeaf(operationToAdd);
		else
			this.getFirstOperator().addOperationToLeaf(operationToAdd);
	}

	public Operation removeLeafOperation() {
		if (this instanceof NumericValueVariable)
			return this; // Nothing to do
		Random r = new Random();
		boolean isFirstLeaf = isLeaf(this.getFirstOperator());
		boolean isSecondLeaf = isLeaf(this.getSecondOperator());

		if (r.nextBoolean() && isFirstLeaf) {
			if (this.getSecondOperator() != null)
				return this.getSecondOperator();
			else // Single operator is leaf
				return this.getFirstOperator();
		}
		
		if (r.nextBoolean() && isSecondLeaf)
			return this.getFirstOperator(); // Can't be null

		// Recursive deeper call on a side.
		if (r.nextBoolean() && this.getSecondOperator() != null)
			this.setSecondOperator(getSecondOperator().removeLeafOperation());
		else
			this.setFirstOperator(getFirstOperator().removeLeafOperation());

		return this;
	}
	
	public Operation removeAtRandomPoint() {
		if (this instanceof NumericValueVariable)
			return this; // Nothing to do
		Random r = new Random();
		int roll = r.nextInt(3);
		if (this.getFirstOperator()!= null)
			if (roll == 0)
				return this.getFirstOperator();
		if (this.getSecondOperator()!= null)
			if (roll == 1)
				return this.getSecondOperator();
		if (roll == 2 && this.getSecondOperator()!=null)
			return this.getSecondOperator().removeAtRandomPoint();
		else
			return this.getFirstOperator().removeAtRandomPoint();
	}

	protected boolean isLeaf(Operation opToTest) {
		return (opToTest != null && opToTest instanceof NumericValueVariable);
	}
}
