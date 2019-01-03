# Genetic Algorithm - Card Problem - Java

## Problem

You have 10 cards numbered 1 to 10
You have to divide them into two piles so that:
 - The sum of the first pile is as close as possible to 36.
 - And the product of all in the second pile is as close as possible to 360.

 
## Strategy

>One population has many chromosomes.
Each chromosome is represented by an integer.
The binary form of this chromosome represents the genes.
A gene can have a value of 0 or 1.
A gene with a value of 1 means it's the products side (second pile) and vice versa.


## Implementation
Make a new population of chromosomes (repeatedly)
- Randomly (based on crossover probability) select two chromosomes, perform crossover to make a new chromosome
- If new chromosome is has greater fitness then replace the least fit chromosome in the existing population
- Randomly select a chromosome in population and do a mutation on it
		
Keep track of the best chromosome whenever a new chromosome is generated.