public class SearchStrategy {

    public static void startSearch(){
        Population population = new Population();
        StringBuffer target = new StringBuffer("TARGET");
        population.setUpSearch(200, 0.02f,  target);
        population.initialize();
        population.calculateFitness();
        population.naturalSelection();
        population.evaluate();
    }
    public static void main(String args[]){
        startSearch();
    }
}
