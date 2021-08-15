package tsp.geneticAlgorithm;

import tsp.lib.arithmetic.Operation;

public class Individual {
	private Operation representation;
	private double fitness;

	public Individual(Operation representation) {
		this.representation = representation;
	}

	public Operation getRepresentation() {
		return this.representation;
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