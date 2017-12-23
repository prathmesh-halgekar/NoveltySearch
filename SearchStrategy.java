public class SearchStrategy {

    public static void startSearch(){
        Population population = new Population();
        StringBuffer target = new StringBuffer("To be");
        population.setUpSearch(400, 0.1f,  target, true, 40);
        population.initialize();
        population.calculateFitness();
        do {
            population.naturalSelection();
            population.generate();
            population.calculateFitness();
            population.evaluate();
            population.showCurrentStatistics();
        }while (!population.isSearchFinished());
    }
    public static void main(String args[]){
        startSearch();
    }
}
