package interpolation.geneticAlgorithm;

import interpolation.arithmetic.Operation;

public class InterpolationFitnessFunction {

	private double[] pointListX;
	private double[] pointListY;
	private double errorMargin;
	private double lengthPenalty;

	public InterpolationFitnessFunction(double[] pointsX, double[] pointsY, double errorMargin, double lengthPenalty) {
		this.pointListX = pointsX;
		this.pointListY = pointsY;
		this.errorMargin = errorMargin;
		this.lengthPenalty = lengthPenalty;
	}

	public double apply(Individual individual) {
		double totalError = 0;
		int landedPoints = 0;
		int size = pointListX.length;

		Operation representation = individual.getRepresentation();
		int variableNumber = representation.getVariableNumber();
		if (variableNumber == 0) // Not a function, however this allows individuals like f(x) = x - x
			return Double.MAX_VALUE;

		double coordinate, error;
		for (int i = 0; i < size; i++) {
			coordinate = pointListX[i];
			error = Math.abs(pointListY[i] - representation.computeValue(coordinate));
			if (error < errorMargin)
				landedPoints++;
			else
				totalError += error;
		}

		double pointShapeOriginal, pointShapeIndividual, point1, point2;
		double pointShapeError = 0.0;
		for (int i = 1; i < size; i++) {
			pointShapeOriginal = pointListY[i] - pointListY[i - 1];
			point1 = representation.computeValue(pointListX[i]);
			point2 = representation.computeValue(pointListX[i - 1]);
			pointShapeIndividual = point1 - point2;

			error = Math.abs(pointShapeOriginal - pointShapeIndividual);
			if (error > errorMargin)
				pointShapeError += error;
		}

		double length = representation.getLength();
		double fitness = (totalError + (pointShapeError)); // * (size - landedPoints);
		// Less fitness value is better
		if (length < 5)
			return fitness;
		return fitness * (1.0 + (length-5) * lengthPenalty); // To encourage shorter equations, each additional length
															// piece from 5 represents a certain % fitness increment
	}
}
