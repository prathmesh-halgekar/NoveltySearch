import java.util.ArrayList;
import java.util.List;

public class SearchStrategy {

    public static void startSearch(){
        Population population = new Population();
        StringBuffer target = new StringBuffer("To be or not to be, what can I say.");
        Point goal = new Point(6,0);
        Point p1 = new Point(5,0);
        Point p2 = new Point(4,1);
        Point p3 = new Point(3,2);
        Point p4 = new Point(2,3);
        Point p5 = new Point(1,4);
        Point p6 = new Point(1,5);
        Point p7 = new Point(2,3);
        Point p8 = new Point(5,1);
        Point p9 = new Point(3,1);
        Point p10 = new Point(2,2);
        Point p11 = new Point(1,3);//this makes it very hard
        Point p12 = new Point(4,3);
        Point p13 = new Point(5,4);
        Point p14 = new Point(5,5);
        Point p15 = new Point(5,3);
        Point p16 = new Point(4,2);
        List<Point> obstacles = new ArrayList<>();
        obstacles.add(p1);
        obstacles.add(p2);
        obstacles.add(p3);
        obstacles.add(p4);
        obstacles.add(p5);
        obstacles.add(p6);
        obstacles.add(p7);
        obstacles.add(p8);
        obstacles.add(p9);
        obstacles.add(p10);
        obstacles.add(p11);
//        obstacles.add(p12);
//        obstacles.add(p13);
//        obstacles.add(p14);
//        obstacles.add(p15);
//        obstacles.add(p16);

        population.setUpSearch(200, 0.1,  goal, obstacles, 7,7,true, 0.7);
        population.initialize();
        population.calculateFitness();
        do {
            population.naturalSelection();
            population.generate();
            population.calculateFitness();
            population.evaluate();
            population.showCurrentStatistics();
        }while ((!population.isSearchFinished()) && (!population.isStuck()));
    }
    public static void main(String args[]){
        startSearch();
    }
}
