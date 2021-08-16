package tsp.geneticAlgorithm;

import tsp.lib.arithmetic.Addition;
import tsp.lib.arithmetic.Cos;
import tsp.lib.arithmetic.Division;
import tsp.lib.arithmetic.Multiplication;
import tsp.lib.arithmetic.NumericValueVariable;
import tsp.lib.arithmetic.Operation;
import tsp.lib.arithmetic.Power;
import tsp.lib.arithmetic.Sin;
import tsp.lib.arithmetic.Subtraction;
import tsp.lib.arithmetic.Tan;

public class Individual {
	private Operation representation;
	private double fitness;

	public Individual(Operation representation) {
		this.representation = representation;
	}

	public Operation getRepresentation() {
		// Representation needs to return a copy and not the actual representation,
		// since otherwise new individuals which mutate from an original individual will
		// modify the original individual's representation along the way, due to the
		// nature of the Operation class.
		return cloneRepresentationRecursive(this.representation);
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double newFit) {
		this.fitness = newFit;
	}

	@Override
	public String toString() {
		return representation.toString();
	}

	private Operation cloneRepresentationRecursive(Operation present) {
		String className = present.getClass().getSimpleName(); 
		switch (className) {
		case "Addition":
			return new Addition(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Subtraction":
			return new Subtraction(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Multiplication":
			return new Multiplication(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Division":
			return new Division(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Power":
			return new Power(cloneRepresentationRecursive(present.getFirstOperator()),
					cloneRepresentationRecursive(present.getSecondOperator()));
		case "Sin":
			return new Sin(cloneRepresentationRecursive(present.getFirstOperator()));
		case "Cos":
			return new Cos(cloneRepresentationRecursive(present.getFirstOperator()));
		case "Tan":
			return new Tan(cloneRepresentationRecursive(present.getFirstOperator()));
		case "NumericValueVariable":
			return new NumericValueVariable(((NumericValueVariable) present).getValue(),
					((NumericValueVariable) present).isVariable());
		default:
			return null;
		}
	}
}