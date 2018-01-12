import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

public class Dna2 {
    private List<String> moveSequence = new ArrayList<>();// gene
    private Point currentPoint = new Point(0,0); // starting position
    private double fitness = 0.00;
    private double sparseness = 0.00;
    private double distanceFromGoal = 0.00;


    private static int[][] MAZE = new int[0][0];
    private static int max_x;
    private static int max_y;
    private static int OBSTACLE = 1;
    private static int GOAL_VALUE = 2;
    private static int FOOT_STEPS = 7;
    private static Point goal;
    private static List<Point> obstacles;


    public Dna2(){

    }

    public Dna2(int x, int y, Point goal, List<Point> obstacles){
        this.max_x=x;
        this.max_y=y;
        this.goal=goal;
        this.obstacles=obstacles;

    }

    public List<String> getMoveSequence() {
        return moveSequence;
    }

    public void setMoveSequence(List<String> moveSequence) {
        this.moveSequence = moveSequence;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getSparseness() {
        return sparseness;
    }

    public void setSparseness(double sparseness) {
        this.sparseness = sparseness;
    }

    public static int[][] getMAZE() {
        return MAZE;
    }

    public static void setMAZE(int[][] MAZE) {
        Dna2.MAZE = MAZE;
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    public double getDistanceFromGoal() {
        return distanceFromGoal;
    }

    public void setDistanceFromGoal(double distanceFromGoal) {
        this.distanceFromGoal = distanceFromGoal;
    }

    @Override
    public String toString() {
        return "Dna2{" +
                "moveSequence=" + moveSequence +
                ", currentPoint=" + currentPoint +
                ", fitness=" + fitness +
                ", sparseness=" + sparseness +
                ", max_x=" + max_x +
                ", max_y=" + max_y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dna2 dna2 = (Dna2) o;

        if (Double.compare(dna2.getFitness(), getFitness()) != 0) return false;
        if (Double.compare(dna2.getSparseness(), getSparseness()) != 0) return false;
        if (getMax_x() != dna2.getMax_x()) return false;
        if (getMax_y() != dna2.getMax_y()) return false;
        if (getMoveSequence() != null ? !getMoveSequence().equals(dna2.getMoveSequence()) : dna2.getMoveSequence() != null)
            return false;
        return getCurrentPoint() != null ? getCurrentPoint().equals(dna2.getCurrentPoint()) : dna2.getCurrentPoint() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getMoveSequence() != null ? getMoveSequence().hashCode() : 0;
        result = 31 * result + (getCurrentPoint() != null ? getCurrentPoint().hashCode() : 0);
        temp = Double.doubleToLongBits(getFitness());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSparseness());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getMax_x();
        result = 31 * result + getMax_y();
        return result;
    }

    public void initializeMAZE(int max_x, int max_y, List<Point> obstacles, Point goal) {
        this.max_x = max_x;
        this.max_y = max_y;
        MAZE = new int[max_x][max_y];
        for (Point point : obstacles) {
            if (checkIfPointValid(point)) {
                MAZE[point.getPositionX()][point.getPositionY()] = OBSTACLE; // represents an obstacle
            }
        }
        if(checkIfPointValid(goal)){
            MAZE[goal.getPositionX()][goal.getPositionY()] = GOAL_VALUE;// goal value
        }
    }
    public void initializeGene(int populationSize) {
        Random r = new Random();
        this.moveSequence = new ArrayList<>();
        for (int j = 0; j < this.max_x * this.max_y; j++) {
            this.moveSequence.add(Move.values()[r.nextInt(8)].toString());// since we have 8 possible directions to go
        }
        //
        initializeMAZE(this.max_x,this.max_y,this.obstacles, this.goal);

    }
    public double calculateFitness(Point goal){
        double max_score = this.max_x * 1.4133; // max distance - Assuming the maze is always a square.
        double score = 0.0;

        Point endpoint = getEndingPosition(this.moveSequence);
        this.distanceFromGoal = findDistance(endpoint, goal);
        score = max_score - this.distanceFromGoal;
        this.fitness = score/max_score;
        return this.fitness;
    }
    public double calculateNovelty(List<Dna2> population, List<Dna2> noveltyArchive){
        // In theory, difference should be calculated on phenotype and not genotype.
        double score =0.0;
        double result = 0.0;
        double max_score = this.max_x * 1.4133; // max distance - Assuming the maze is always a square.
        Point endpoint = getEndingPosition(this.moveSequence);// this updates the 'this.currentPoint' as well
        for(Dna2 dna: population){
            if(this != dna) {
                score = max_score - findDistance(endpoint, dna.getCurrentPoint());// more the distance is, lesser the score is.
                score = 1 - (score / max_score);// just normalizing it to 0-1 range.
                //this.sparseness = score;
                result += score;
            }
            // need a maximization not minimization
        }
        for(Dna2 dna: noveltyArchive){
            if(this != dna) {
                score = max_score - findDistance(endpoint, dna.getCurrentPoint());// more the distance is, lesser the score is.
                score = 1 - (score / max_score);// just normalizing it to 0-1 range.
                //this.sparseness = score;
                result += score;
            }
            // need a maximization not minimization
        }

        this.sparseness = result/(population.size() == 1?1:population.size()-1) + (noveltyArchive.size() == 1?1:noveltyArchive.isEmpty()?0:(noveltyArchive.size()-1));
        //System.out.println("Novelty score : "+this.sparseness);
        return this.sparseness;
//        for(int i=0; i< this.genes.length() ;i++){
//            if(this.genes.charAt(i) != targetString.charAt(i)){
//                score += abs(Character.getNumericValue(this.genes.charAt(i)) - Character.getNumericValue(targetString.charAt(i)));
//            }
//        }
//        //System.out.println("raw score is : "+score);
//        //System.out.println("max score before compare is : "+this.maxScore);
//        if(Double.compare(score,this.maxScore) > 0){
//            //System.out.println("max score is : "+this.maxScore);
//            score = this.maxScore;
//        }
//        this.sparseness = this.getNormalizedValue(score);
//        //System.out.println("Sparseness is : " +this.sparseness);
//        return this.sparseness;
    }

    public Dna2 crossover(Dna2 parent){
        Dna2 child= new Dna2();
        int midpoint = new Random().nextInt(this.moveSequence.size());
        for(int i=0 ; i<this.moveSequence.size() ; i++){
            if(i > midpoint){
                List<String> temp = child.getMoveSequence();
                temp.add(this.moveSequence.get(i));
                child.setMoveSequence(temp);
            }else{
                List<String> temp = child.getMoveSequence();
                temp.add(parent.getMoveSequence().get(i));
                child.setMoveSequence(temp);
            }
        }
        return child;
    }
    public void mutate(double mutationRate){
        Random random =new Random();
        for(int i =0; i< this.moveSequence.size(); i++){
            double r = ((double)(new Random().nextInt(10)))/(double)9.99;
            //System.out.println("Division val : "+r);
            if( Double.compare(r,mutationRate) < 0){
                this.moveSequence.set(i,Move.values()[random.nextInt(8)].toString());
            }
        }
    }

    public double findDistance(Point current, Point goal) {
        return Math.sqrt((goal.getPositionX() - current.getPositionX()) * (goal.getPositionX() - current.getPositionX())
                + (goal.getPositionY() - current.getPositionY()) * (goal.getPositionY() - current.getPositionY()));
    }

    public Point getEndingPosition(List<String> moves){
        for(String move : moves){
            // check in maze if
               // if move on currentPosition is valid THEN currentPosition = point(move) and continue loop;
            Point temp = null;
            if(checkIfPointValid(temp = getMovedPoint(move))){
                this.currentPoint = (Point)temp.getClone();
                continue; // continue traversing
            }else {
                // Performing the move ends up in an invalid position.
                // this means that navigator has hit either a block or the wall.
                //System.out.println("Cannot go "+move +" from "+this.getCurrentPoint().getPositionX()+" ,"+this.getCurrentPoint().getPositionY());
                break;
            }
               // else return currentPoint;
            // return currentPosition - should be outside for loop
        }
        return this.currentPoint;
    }
    public Point getMovedPoint(String move){
        Point temp = (Point)this.currentPoint.getClone();

        if(!checkIfPointValid(this.currentPoint)){
            //The navigator is already in an invalid position, so returning the same without further moving.
            System.out.println("The navigator is already in an invalid position to go "+move+", so returning the same without further moving.");
            return this.currentPoint;
        }

        switch (move){
            case "FORWARD":
                temp.setPositionX(currentPoint.getPositionX()+1);
                break;
            case "FORWARD_RIGHT":
                temp.setPositionX(currentPoint.getPositionX()+1);
                temp.setPositionY(currentPoint.getPositionY()-1);
                break;
            case "FORWARD_LEFT":
                temp.setPositionX(currentPoint.getPositionX()+1);
                temp.setPositionY(currentPoint.getPositionY()+1);
                break;
            case "BACKWARD":
                temp.setPositionX(currentPoint.getPositionX()-1);
                break;
            case "BACKWARD_RIGHT":
                temp.setPositionX(currentPoint.getPositionX()-1);
                temp.setPositionY(currentPoint.getPositionY()-1);
                break;
            case "BACKWARD_LEFT":
                temp.setPositionX(currentPoint.getPositionX()-1);
                temp.setPositionY(currentPoint.getPositionY()+1);
                break;
            case "LEFT":
                temp.setPositionY(currentPoint.getPositionY()+1);
                break;
            case "RIGHT":
                temp.setPositionY(currentPoint.getPositionY()-1);
                break;
            default:
                break;
        }
        return temp;
    }
    public boolean checkIfPointValid(Point currentPoint){
        if(currentPoint.getPositionX() >= max_x || currentPoint.getPositionY() >= max_y || currentPoint.getPositionX() < 0 || currentPoint.getPositionY() < 0)
            return false;
        if(MAZE[currentPoint.getPositionX()][currentPoint.getPositionY()] == OBSTACLE)
            return false;
        return true;
    }

    public void drawFinalPath(){
       this.currentPoint = new Point(0,0);
       for(String move : this.getMoveSequence()){
           this.currentPoint = getMovedPoint(move);
           if(MAZE[this.currentPoint.getPositionX()][this.currentPoint.getPositionY()] == GOAL_VALUE ){
               break;
           }
           MAZE[this.currentPoint.getPositionX()][this.currentPoint.getPositionY()] = FOOT_STEPS;
       }
    }


    public static void main(String args[]){
        Dna2 dn = new Dna2();
        Dna2 dn1 = new Dna2();
//        List<String> tempMoves = new ArrayList<>();
//        tempMoves.add(Move.FORWARD.toString());
//        tempMoves.add(Move.FORWARD.toString());
//        tempMoves.add(Move.FORWARD.toString());
//        tempMoves.add(Move.LEFT.toString());
//        List<String> tempMoves1 = new ArrayList<>();
//        tempMoves1.add(Move.BACKWARD.toString());
//        tempMoves1.add(Move.BACKWARD.toString());
//        tempMoves1.add(Move.BACKWARD_LEFT.toString());
//        tempMoves1.add(Move.LEFT.toString());
//        dn.setMoveSequence(tempMoves);
//        dn1.setMoveSequence(tempMoves1);
//        //System.out.println(dn1.crossover(dn));
//        dn1.mutate(0.5);
//        System.out.println(dn1.getMoveSequence());

        Point goal = new Point(2,2);
        Point p1 = new Point(0,2);
        Point currPoint = new Point(0,0);
        dn.setCurrentPoint(currPoint);
        Point p2 = new Point(0,1);
        List<Point> obstacles = new ArrayList<>();
        obstacles.add(p1);//obstacles.add(p2);
        dn.initializeMAZE(4,4, obstacles, goal);
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                System.out.print("  "+MAZE[i][j]);
            }
            System.out.println(" ");
        }

        List<String> tempMoves = new ArrayList<>();
        tempMoves.add(Move.FORWARD.toString());
        tempMoves.add(Move.FORWARD.toString());
        tempMoves.add(Move.FORWARD.toString());
        tempMoves.add(Move.LEFT.toString());
        List<String> tempMoves1 = new ArrayList<>();
        tempMoves.add(Move.BACKWARD.toString());
        tempMoves.add(Move.BACKWARD.toString());
        tempMoves.add(Move.BACKWARD_LEFT.toString());
        tempMoves.add(Move.LEFT.toString());
//        tempMoves.add(Move.BACKWARD.toString());
//        tempMoves.add(Move.BACKWARD.toString());
//        tempMoves.add(Move.BACKWARD.toString());
//        tempMoves.add(Move.LEFT.toString());

        Point tmp = dn.getEndingPosition(tempMoves);
dn.setMoveSequence(tempMoves);
        System.out.println("Curr position : "+tmp);
        System.out.println(" : " +dn.calculateFitness(goal));

    }

}
