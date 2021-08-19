# EvolutiveInterpolation
This evolutionary algorithm uses two-dimension coordinates (encoded as 'X' and 'Y' arrays of double type values), to try and find a one variable arithmetic equation that interpolates them as closely as possible.

## Launching
The algorithm expects the following parameters:

`java -jar EvolutiveInterpolation.jar [pointListX] [pointListY] [errorMargin] [populationSize] [crossoverProbability] [mutationProbability] [maxTime] [saveExecutionData]`

* **pointListX**: Array containing double values that represent the X coordinate. Must be the same lenght as *pointListY*. Syntax example: "[14, 14.15, 14.84, 15.01, 15.03]".
* **pointListY**: Array containing double values that represent the Y coordinate. Must be the same lenght as *pointListX*. Syntax example: "[9.0, 4.5, 2.01, 0, 2.01]".
* **errorMargin**: Double value that sets the error margin an equation has to determine it hits the provided points. 
* **populationSize**: Integer value that sets the evolutionary algorithm's population size.
* **crossoverProbability**: Double value in the [0-1] range that indicates how often population individuals reproduce between generations.
* **mutationProbability**: Double value in the [0-1] range that indicates how often population individuals mutate between generations.
* **maxTime**: Maximum allowed execution time in seconds. If set to 0 no limit is imposed.
* **saveExecutionData**: Boolean 0/1 value to indicate whether to save a CSV file with execution data upon reaching execution time limit.