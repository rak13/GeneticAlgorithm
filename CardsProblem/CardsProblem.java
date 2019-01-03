import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/*
    You have 10 cards numbered 1 to 10
    You have to divide them into two piles so that:
     - The sum of the first pile is as close as possible to 36.
     - And the product of all in the second pile is as close as possible to 360.
 */

public class CardsProblem {

    public int numberOfCards = 10;
    public int requiredProduct = 360;
    public int requiredSum = 36;

    Random random = new Random();

    //percentage
    double crossoverProbability = 20, mutationProbability = 70;

    public int populationSize = 7;
    int mostFitIndex = populationSize - 1;
    int leastFitIndex = 0;


    /*
        One population has many chromosomes
        Each chromosome is represented by an integer
        The binary form of this chromosome represents the genes
        A Gene has a value of 0 or 1
     */
    Integer[] population = new Integer[populationSize];

    /*
        We are looking for the best chromosome
        with maximum fitness
     */
    int bestChromosome;

    CardsProblem() {
        initializePopulation();
    }

    boolean checkBit(int n, int pos) {
        return (n & (1 << pos)) != 0;
    }

    int setBit(int n, int pos, boolean val) {
        return val ? (n | (1 << pos)) : (n & ~(1 << pos));
    }

    double fitness(int chromosome) {
        double product = 1, sum = 0;
        for (int i = 0; i < numberOfCards; i++) {
            if (checkBit(chromosome, i)) {
                //1 means on the products side
                product *= (i + 1);
            } else {
                sum += (i + 1);
            }
        }
        return 0 - (Math.abs(requiredProduct - product) + Math.abs(requiredSum - sum));
        //now greater value of fitness means more fit
    }

    final Comparator<Integer> populationComparator = (Integer o1, Integer o2) -> (int) (fitness(o1) - fitness(o2));

    int crossOver(int chromosome1, int chromosome2) {
        int newChromosome = 0;
        int partitionPoint = random.nextInt(numberOfCards);

        if (random.nextInt() % 2 == 0) {
            //swap
            int tmp = chromosome1;
            chromosome1 = chromosome2;
            chromosome2 = tmp;
        }

        for (int i = 0; i < numberOfCards; i++) {
            //take genes before partitionPoint from chromosome1
            //and rest from chromosome2
            int bitMask = (i < partitionPoint) ? chromosome1 : chromosome2;
            newChromosome = setBit(newChromosome, i, checkBit(bitMask, i));
        }
        return newChromosome;
    }

    int mutation(int chromosome) {
        //reverse a random bit
        int mutationPosition = random.nextInt(numberOfCards);
        boolean bitVal = checkBit(chromosome, mutationPosition);
        return setBit(chromosome, mutationPosition, !bitVal);
    }

    public void initializePopulation() {
        int maxVal = (int) Math.pow(2, numberOfCards);
        for (int i = 0; i < populationSize; i++) {
            population[i] = random.nextInt(maxVal);
        }

        //sort chromosomes according to their fitness
        Arrays.sort(population, populationComparator);
        bestChromosome = population[mostFitIndex];
//        printAllFitness();
    }

    public void generatePopulation() {
        for (int i = 0; i < populationSize; i++) {
            for (int j = i + 1; j < populationSize; j++) {

                int chromosome1 = population[i];
                int chromosome2 = population[j];

                if (random.nextInt(101) <= crossoverProbability) {
                    int newChromosome = crossOver(chromosome1, chromosome2);
                    double fitnessOfNew = fitness(newChromosome);
                    double fitnessMinOfOld = fitness(population[leastFitIndex]);

                    if (fitnessOfNew > fitnessMinOfOld) {
                        //replace the least fit chromosome
                        population[leastFitIndex] = newChromosome;

                        Arrays.sort(population, populationComparator);
                        if (fitness(population[mostFitIndex]) > fitness(bestChromosome)) {
                            bestChromosome = population[mostFitIndex];
                            System.out.println("Better chromosome after Crossover:");
                            printCards(bestChromosome);
                            printAllFitness();
                        }
                    }
                }
            }
            if (random.nextInt(101) <= mutationProbability) {
                population[i] = mutation(population[i]);

                Arrays.sort(population, populationComparator);
                if (fitness(population[mostFitIndex]) > fitness(bestChromosome)) {
                    bestChromosome = population[mostFitIndex];
                    System.out.println("Better chromosome after Mutation :");
                    printCards(bestChromosome);
                    printAllFitness();
                }
            }
        }
    }

    public void printAllFitness() {
        for (int j = 0; j < populationSize; j++) {
            System.out.print("\t| " + fitness(population[j]));
        }
        System.out.println("\t|\n");
    }

    public void printCards(int chromosome) {
        double product = 1, sum = 0;

        StringBuilder productsCards = new StringBuilder("\t|\tProduct Cards :\t");
        StringBuilder sumCards = new StringBuilder("\t\t\t|\tSum Cards :\t\t");
        StringBuilder binaryForm = new StringBuilder();
        for (int i = 0; i < numberOfCards; i++) {
            if (checkBit(chromosome, i)) {
                //1 means on the products side
                product *= (i + 1);
                productsCards.append("\t| " + (i + 1));
                binaryForm.append("1");
            } else {
                sum += (i + 1);
                sumCards.append("\t| " + (i + 1));
                binaryForm.append("0");
            }
        }
        productsCards.append(" |");
        sumCards.append(" |");

        System.out.println("Chromosome as Integer : " + chromosome);
        System.out.println("Chromosome as Binary (reversed / used): " + binaryForm);
        System.out.println("Chromosome as Binary: " + binaryForm.reverse());
        System.out.println("Product = " + product + " " + productsCards);
        System.out.println("Sum = " + sum + " " + sumCards);
        System.out.println("Fitness = " + fitness(chromosome));
    }

    public void printChromosome(int index) {
        int cards = population[index];
        printCards(cards);
    }

    void printAll() {
        for (int i = 0; i < population.length; i++) {
            printChromosome(i);
        }
    }

    public static void main(String[] args) {
        CardsProblem ga = new CardsProblem();
        for (int i = 0; i < 1000; i++) {
            ga.generatePopulation();
        }
        System.out.println("******************* BEST RESULT *******************");
        ga.printCards(ga.bestChromosome);
    }
}
