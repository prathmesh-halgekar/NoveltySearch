import java.util.*;

public class Population {

    private int generation = 0;
    private float mutationRate = 0.0f;
    private float noveltyThreshold = 0.0f;
    private int populationCount;
    private boolean searchFinished = false;
    private boolean isNoveltySearch = false;
    private Dna bestDna;
    private StringBuffer targetPhrase = new StringBuffer();

    private List<Dna> dnaList = new ArrayList<>();
    private List<Dna> matingPool = new ArrayList<>();

    private static List<Dna> noveltyArchive = new ArrayList<>();


    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setUpSearch(int population, float mutationRate, StringBuffer targetPhrase, boolean isNoveltySearch, float noveltyThreshold){
        this.populationCount = population;
        this.mutationRate = mutationRate;
        this.targetPhrase = targetPhrase;
        this.isNoveltySearch = isNoveltySearch;
        this.noveltyThreshold = noveltyThreshold;
    }

    public int getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public StringBuffer getTargetPhrase() {
        return targetPhrase;
    }

    public void setTargetPhrase(StringBuffer targetPhrase) {
        this.targetPhrase = targetPhrase;
    }

    public List<Dna> getDnaList() {
        return dnaList;
    }

    public void setDnaList(List<Dna> dnaList) {
        this.dnaList = dnaList;
    }

    public List<Dna> getMatingPool() {
        return matingPool;
    }

    public void setMatingPool(List<Dna> matingPool) {
        this.matingPool = matingPool;
    }

    public boolean isSearchFinished() {
        return this.searchFinished;
    }

    public void setSearchFinished(boolean searchFinished) {
        this.searchFinished = searchFinished;
    }

    public Dna getBestDna() {
        return bestDna;
    }

    public void setBestDna(Dna bestDna) {
        this.bestDna = bestDna;
    }

    @Override
    public String toString() {
        return "Population{" +
                "generation=" + generation +
                ", mutationRate=" + mutationRate +
                ", populationCount=" + populationCount +
                ", targetPhrase=" + targetPhrase +
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
        if (Float.compare(that.getMutationRate(), getMutationRate()) != 0) return false;
        if (getPopulationCount() != that.getPopulationCount()) return false;
        if (!getTargetPhrase().equals(that.getTargetPhrase())) return false;
        if (!getDnaList().equals(that.getDnaList())) return false;
        return getMatingPool().equals(that.getMatingPool());
    }

    @Override
    public int hashCode() {
        int result = getGeneration();
        result = 31 * result + (getMutationRate() != +0.0f ? Float.floatToIntBits(getMutationRate()) : 0);
        result = 31 * result + getPopulationCount();
        result = 31 * result + getTargetPhrase().hashCode();
        result = 31 * result + getDnaList().hashCode();
        result = 31 * result + getMatingPool().hashCode();
        return result;
    }

    public void initialize(){
        for(int i=0 ;i<populationCount;i++){
            Dna dna = new Dna();
            dna.initializeGene(this.targetPhrase.length(), this.targetPhrase.length()*10); // usually this is a good number based on observation. Length of char's * 10.
            this.dnaList.add(dna);
        }
    }
    public void calculateFitness(){
        //System.out.println(" In calculateFitness,  Generation no. : " + this.getGeneration());
        for(Dna dna : this.dnaList){
            if(!isNoveltySearch){
                dna.calculateFitness(this.targetPhrase);
            }
            else{
                dna.calculateNovelty(this.targetPhrase);
            }
        }
    }
    public void naturalSelection(){
        float maxFitness = 0;

        if (!isNoveltySearch) {

            for (Dna dna : this.dnaList) {
                if (dna.getFitness() > maxFitness) {
                    maxFitness = dna.getFitness();
                }
            }
            //System.out.println(" In naturalSelection : "+maxFitness);
            for (Dna dna : this.dnaList) {
                //normalize the fitness to a value between 0-10.
                float tmp = dna.getFitness() * 100;
                for (int i = 0; i < tmp; i++) {
                    this.matingPool.add(dna);
                }
            }
        }else{
            List<Dna> tempArchiveList = new ArrayList<>();
            for(Dna dna : this.dnaList){
                float noveltyScore = 0;
                for(Dna dna1 : this.dnaList){
                    noveltyScore +=dna.calculateNovelty(dna1.getGenes());
                }
                for (Dna dnaArchive : noveltyArchive){
                    noveltyScore +=dna.calculateNovelty(dnaArchive.getGenes());
                }
                //System.out.println("novelty score : "+noveltyScore);
                if(noveltyScore > this.noveltyThreshold){
                    tempArchiveList.add(dna);
                }

                //normalize the sparseness to a value between 0-100.
                float tmp = dna.getSparseness() * 100;
                for (int i = 0; i < tmp; i++) {
                    this.matingPool.add(dna);
                }
            }

            Collections.sort(tempArchiveList, Collections.reverseOrder(new Comparator<Dna>() {
                @Override
                public int compare(Dna d1, Dna d2) {
                    return Double.compare(d1.getSparseness(), d2.getSparseness());
                }
            }));
            int count = 0;
            for (Dna dna : tempArchiveList){
                if(count < 15){
                    noveltyArchive.add(dna);
                }else{
                    // just avoiding traditional looping using for loop.
                    break;
                }
                count++;
            }

//            for(Dna dna : noveltyArchive) {
//                // gets Priority to be added again in the mutation pool
//                // pick only first 10?
//                float tmp = dna.getSparseness() * 100;
//                for (int i = 0 ; i < tmp ; i++) {
//                    this.matingPool.add(dna);
//                }
//            }
        }

    }
    public void generate(){
        for(int i=0; i< this.dnaList.size(); i++){
            int indexOfPartnerA = new Random().nextInt(this.matingPool.size());
            int indexOfPartnerB = new Random().nextInt(this.matingPool.size());
            Dna partnerA = this.matingPool.get(indexOfPartnerA);
            Dna partnerB = this.matingPool.get(indexOfPartnerB);
            Dna child = partnerA.crossover(partnerB);
            child.mutate(this.mutationRate);
            //System.out.println(" In generate : new child is "+child);
            this.dnaList.set(i,child);
        }
        this.generation++;
    }

    public void evaluate() {
        float bestDnaRecord = 0.0f;
        Dna bestDna = null;
        if (!isNoveltySearch) {
            for (Dna dna : this.dnaList) {
                if (Float.compare(dna.getFitness(), bestDnaRecord) > 0) {

                    bestDnaRecord = dna.getFitness();
                    //System.out.println(" In evaluate : "+bestDnaRecord);
                    this.bestDna = dna;
                }
            }
            if (Float.compare(bestDnaRecord, 1.0f) == 0) {
                this.searchFinished = true;
            }
        } else {
            for (Dna dna : this.dnaList) {
                if (Float.compare(dna.getSparseness(), bestDnaRecord) > 0) {

                    bestDnaRecord = dna.getSparseness();
                    //System.out.println(" In evaluate : "+bestDnaRecord);
                    this.bestDna = dna;
                }
            }

            for (Dna dna : this.dnaList) {
                float maxScore = dna.calculateFitness(this.targetPhrase);
                if (Float.compare(maxScore, 1.0f) == 0) {
                    this.bestDna = dna;
                    this.searchFinished = true;
                }
            }
        }


    }
    public void showCurrentStatistics(){
        System.out.println("Current Generation : "+this.getGeneration());
        float totalFitness = 0.0f;
        for(Dna dna : this.dnaList){
            totalFitness += dna.getFitness();
        }
        System.out.println("Novelty Archive size : "+noveltyArchive.size());
        System.out.println("Average Fitness : "+ (totalFitness/this.dnaList.size()));
        System.out.println("Best Dna : "+ this.bestDna);
    }


}
