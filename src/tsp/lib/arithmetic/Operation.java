package tsp.lib.arithmetic;

public abstract class Operation {
	
	protected Operation firstOperator;
	protected Operation secondOperator;
	
	public Operation(Operation firstOperator, Operation secondOperator) {
		this.firstOperator = firstOperator;
		this.secondOperator = secondOperator;
	}
	
	public abstract double computeValue(double variableValue);
	
	public abstract String toString();
	
	public Operation getFirstOperator() {
		return this.firstOperator;
	}
	
	public Operation getSecondOperator() {
		return this.secondOperator;
	}

}
