package interpolation.geneticAlgorithm;

public interface FitnessFunction {

	/**
	 * 
	 * @param individual the individual whose fitness is to be accessed.
	 * @return the individual's fitness value (the higher the better).
	 */
	double apply(Individual individual);
}
