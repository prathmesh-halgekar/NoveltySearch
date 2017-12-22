public class SearchStrategy {

    public static void startSearch(){
        Population population = new Population();
        StringBuffer target = new StringBuffer("TARGET");
        population.setUpSearch(200, 0.08f,  target);
        population.initialize();
        population.calculateFitness();
        do {
            population.naturalSelection();
            population.generate();
            population.calculateFitness();
            population.evaluate();
        }while (!population.isSearchFinished());
    }
    public static void main(String args[]){
        startSearch();
    }
}
