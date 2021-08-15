package tsp.geneticAlgorithm;

import tsp.lib.arithmetic.Operation;

public class Individual<A> {
	private Operation representation;
	private double fitness;
	private int descendants; // for debugging!

	public Individual(Operation representation) {
		this.representation = representation;
	}

	public Operation getRepresentation() {
		return representation;
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double newFit) {
		this.fitness = newFit;
	}

	/**
	 * Should be called by the genetic algorithm whenever the individual is selected
	 * to produce a descendant.
	 */
	public void incDescendants() {
		descendants++;
	}

	public int getDescendants() {
		return descendants;
	}

	@Override
	public String toString() {
		return representation.toString() + descendants;
	}
}