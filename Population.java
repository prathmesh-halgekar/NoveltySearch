import java.util.*;

public class Population {

    private int generation = 0;
    private Double mutationRate = 0.0;
    private Double noveltyThreshold = 0.0;
    private int populationCount;
    private boolean searchFinished = false;
    private boolean isStuck = false;
    private boolean isNoveltySearch = false;
    private Dna2 bestDna;
    private Point goal;
    private int max_x;
    private int max_y;
    private List<Point> obstacles;
    private List<Dna2> localBests = new ArrayList<>();

    private List<Dna2> dnaList = new ArrayList<>();//List of all sequences of moves
    private List<Dna2> matingPool = new ArrayList<>();

    private static List<Dna2> noveltyArchive = new ArrayList<>();


    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setUpSearch(int population, Double mutationRate, Point goal, List<Point> obstacles, int max_x, int max_y, boolean isNoveltySearch, Double noveltyThreshold){
        this.populationCount = population;
        this.mutationRate = mutationRate;
        this.goal = goal;
        this.isNoveltySearch = isNoveltySearch;
        this.noveltyThreshold = noveltyThreshold;
        this.max_x=max_x;
        this.max_y=max_y;
        this.obstacles=obstacles;
    }

    public int getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public Point getGoal() {
        return goal;
    }

    public void setGoal(Point goal) {
        this.goal = goal;
    }

    public List<Dna2> getDnaList() {
        return dnaList;
    }

    public void setDnaList(List<Dna2> dnaList) {
        this.dnaList = dnaList;
    }

    public List<Dna2> getMatingPool() {
        return matingPool;
    }

    public void setMatingPool(List<Dna2> matingPool) {
        this.matingPool = matingPool;
    }

    public boolean isSearchFinished() {
        return this.searchFinished;
    }

    public void setSearchFinished(boolean searchFinished) {
        this.searchFinished = searchFinished;
    }

    public Dna2 getBestDna() {
        return bestDna;
    }

    public void setBestDna(Dna2 bestDna) {
        this.bestDna = bestDna;
    }

    public boolean isStuck() {
        return isStuck;
    }

    public void setStuck(boolean stuck) {
        isStuck = stuck;
    }

    @Override
    public String toString() {
        return "Population{" +
                "generation=" + generation +
                ", mutationRate=" + mutationRate +
                ", populationCount=" + populationCount +
                ", goal=" + goal +
                ", dnaList=" + dnaList +
                ", matingPool=" + matingPool +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Population that = (Population) o;

        if (getGeneration() != that.getGeneration()) return false;
        if (Double.compare(that.getMutationRate(), getMutationRate()) != 0) return false;
        if (getPopulationCount() != that.getPopulationCount()) return false;
        if (!getGoal().equals(that.getGoal())) return false;
        if (!getDnaList().equals(that.getDnaList())) return false;
        return getMatingPool().equals(that.getMatingPool());
    }

    @Override
    public int hashCode() {
        int result = getGeneration();
        result = 31 * result + (getMutationRate() != +0.0f ? Double.hashCode(getMutationRate().doubleValue()) : 0);
        result = 31 * result + getPopulationCount();
        result = 31 * result + getGoal().hashCode();
        result = 31 * result + getDnaList().hashCode();
        result = 31 * result + getMatingPool().hashCode();
        return result;
    }

    public void initialize(){
        for(int i=0 ;i<populationCount;i++){
            Dna2 dna = new Dna2(this.max_x, this.max_y, this.goal, this.obstacles);
            dna.initializeGene(this.getPopulationCount()); // usually this is a good number based on observation. Length of char's * 10.
            this.dnaList.add(dna);
        }
    }
    public void calculateFitness(){
        //System.out.println(" In calculateFitness,  Generation no. : " + this.getGeneration());
        for(Dna2 dna : this.dnaList){

                dna.calculateFitness(this.goal);

            if(isNoveltySearch){
                dna.calculateNovelty(this.dnaList, noveltyArchive);
            }
        }
    }
    public void naturalSelection(){
        double maxFitness = 0;
        this.matingPool = new ArrayList<>();
        if (!isNoveltySearch) {

            for (Dna2 dna : this.dnaList) {
                if (dna.getFitness() > maxFitness) {
                    maxFitness = dna.getFitness();
                }
            }
            //System.out.println(" In naturalSelection : "+maxFitness);
            for (Dna2 dna : this.dnaList) {
                //normalize the fitness to a value between 0-10.
                double tmp = dna.getFitness() * 100;
                for (int i = 0; i < tmp; i++) {
                    this.matingPool.add(dna);
                }
            }
        }else{
            List<Dna2> tempArchiveList = new ArrayList<>();
            for(Dna2 dna : this.dnaList){
                double noveltyScore = 0.0;
                noveltyScore = dna.calculateNovelty(this.dnaList,noveltyArchive);

                //System.out.println("novelty score : "+noveltyScore);
                if(noveltyScore > this.noveltyThreshold){
                    tempArchiveList.add(dna);
                }

                //normalize the sparseness to a value between 0-100.
                Double tmp = dna.getSparseness() * 100;
                for (int i = 0; i < tmp; i++) {
                    this.matingPool.add(dna);
                }
            }

            Collections.sort(tempArchiveList, Collections.reverseOrder(new Comparator<Dna2>() {
                @Override
                public int compare(Dna2 d1, Dna2 d2) {
                    return Double.compare(d1.getSparseness(), d2.getSparseness());
                }
            }));
            int count = 0;
            for (Dna2 dna : tempArchiveList){
                if(count < 15){
                    noveltyArchive.add(dna);
                }else{
                    // just avoiding traditional looping using for loop.
                    break;
                }
                count++;
            }
        }

    }
    public void generate(){
        for(int i=0; i< this.dnaList.size(); i++){
            int indexOfPartnerA = new Random().nextInt(this.matingPool.size());
            int indexOfPartnerB = new Random().nextInt(this.matingPool.size());
            Dna2 partnerA = this.matingPool.get(indexOfPartnerA);
            Dna2 partnerB = this.matingPool.get(indexOfPartnerB);
            Dna2 child = partnerA.crossover(partnerB);
            child.mutate(this.mutationRate);
            //System.out.println(" In generate : new child is "+child);
            this.dnaList.set(i,child);
        }
        this.generation++;
    }

    public void evaluate() {
        Double bestDnaRecord = 0.0;
        Dna bestDna = null;
        if (!isNoveltySearch) {
            for (Dna2 dna : this.dnaList) {
                if (Double.compare(dna.getFitness(), bestDnaRecord) > 0) {

                    bestDnaRecord = dna.getFitness();
                    //System.out.println(" In evaluate : "+bestDnaRecord);
                    this.bestDna = dna;
                }
            }
//            localBests.add(this.bestDna);
//            // checking if the navigator is stuck.
//            Double fitness = 0.0;
//            int count=0;
//            if(localBests.size() > 2000){ // 20000 generation gives a good measure to ensure whether ir not the navigator is stuck.
//                for(int i=0 ; i <= 40 ; i++){
//                    if(Double.compare(localBests.get(i).getFitness(),fitness) != 0){
//                        fitness = localBests.get(i).getFitness();
//                    }else if(Double.compare(localBests.get(i).getFitness(),fitness) == 0){
//                        count++;
//                    }
//                }
//            }

//            for(Dna2 dna2: localBests){
//                if(Double.compare(dna2.getFitness(),fitness) != 0){
//                    fitness = dna2.getFitness();
//                }else if(Double.compare(dna2.getFitness(),fitness) == 0){
//                    count++;
//                }
//            }
//            if(count >= 40){
//                this.isStuck = true;
//                System.out.println("Navigator stuck for the following maze.");
//                for(int i=0; i<this.max_x; i++){
//                    for(int j=0; j<this.max_y; j++){
//                        System.out.print("  "+Dna2.getMAZE()[i][j]);
//                    }
//                    System.out.println(" ");
//                }
//            }
            if (Double.compare(bestDnaRecord, 1.0) == 0) {
                System.out.println("Search finished for the following maze.");
                this.bestDna.drawFinalPath();
                for(int i=0; i<this.max_x; i++){
                    for(int j=0; j<this.max_y; j++){
                        System.out.print("  "+Dna2.getMAZE()[i][j]);
                    }
                    System.out.println(" ");
                }

                this.searchFinished = true;
            }
        } else {
            for (Dna2 dna : this.dnaList) {
                if (Double.compare(dna.getFitness(), bestDnaRecord) > 0) {

                    bestDnaRecord = dna.getFitness();
                    //System.out.println(" In evaluate : "+bestDnaRecord);
                    this.bestDna = dna;
                }
            }

//            for (Dna2 dna : this.dnaList) {
//                Double maxScore = dna.calculateFitness(this.goal);
//                if (Double.compare(maxScore, 1.0) == 0) {
//                    this.bestDna = dna;
//                    bestDnaRecord = this.bestDna.getFitness();
//                    //this.searchFinished = true;
//                }
//            }

            if (Double.compare(bestDnaRecord, 1.0) == 0) {
                System.out.println("Search finished for the following maze.");
                this.bestDna.drawFinalPath();
                for(int i=0; i<this.max_x; i++){
                    for(int j=0; j<this.max_y; j++){
                        System.out.print("  "+Dna2.getMAZE()[i][j]);
                    }
                    System.out.println(" ");
                }

                this.searchFinished = true;
            }

        }


    }
    public void showCurrentStatistics(){
        System.out.println("Current Generation : "+this.getGeneration());
        Double totalFitness = 0.0;
        Double totalSparseness = 0.0;
        for(Dna2 dna : this.dnaList){
            totalFitness += dna.getFitness();
            totalSparseness +=dna.getSparseness();
        }
        System.out.println("Novelty Archive size : "+noveltyArchive.size());
        System.out.println("Average Fitness : "+ (totalFitness/this.dnaList.size()));
        System.out.println("Average Sparseness : "+ (totalSparseness/this.dnaList.size()));
        System.out.println("Best Dna : "+ this.bestDna);
    }


}
