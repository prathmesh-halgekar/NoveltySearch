import java.util.ArrayList;
import java.util.List;

public class SearchStrategy {

    public static void startSearch(){
        Population population = new Population();
        StringBuffer target = new StringBuffer("To be or not to be, what can I say.");
        Point goal = new Point(3,3);
        Point p1 = new Point(0,2);
        Point p2 = new Point(1,1);
        Point p3 = new Point(2,1);
        List<Point> obstacles = new ArrayList<>();
        obstacles.add(p1);
        obstacles.add(p2);
        obstacles.add(p3);

        population.setUpSearch(10, 0.09,  goal, obstacles, 4,4,false, 90.0);
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
