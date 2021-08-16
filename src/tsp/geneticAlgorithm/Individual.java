package tsp.geneticAlgorithm;

import tsp.lib.arithmetic.Operation;

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
		return GeneticFunctions.cloneRepresentationRecursive(this.representation);
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
}