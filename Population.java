import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    private int generation = 0;
    private float mutationRate = 0.0f;
    private int populationCount;
    private boolean searchFinished = false;
    private Dna bestDna;
    private StringBuffer targetPhrase = new StringBuffer();

    private List<Dna> dnaList = new ArrayList<>();
    private List<Dna> matingPool = new ArrayList<>();


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

    public void setUpSearch(int population, float mutationRate, StringBuffer targetPhrase){
        this.populationCount = population;
        this.mutationRate = mutationRate;
        this.targetPhrase = targetPhrase;
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
            dna.initializeGene(this.targetPhrase.length());
            this.dnaList.add(dna);
        }
    }
    public void calculateFitness(){
        System.out.println(" In calculateFitness,  Generation no. : " + this.getGeneration());
        for(Dna dna : this.dnaList){
            dna.calculateFitness(this.targetPhrase);
            dna.calculateNovelty(this.targetPhrase);
        }
    }
    public void naturalSelection(){
        float maxFitness = 0;
        for(Dna dna : this.dnaList){
            if(dna.getFitness() > maxFitness){
                maxFitness = dna.getFitness();
            }
        }
        System.out.println(" In naturalSelection : "+maxFitness);
        for(Dna dna : this.dnaList){
            //normalize the fitness to a value between 0-10.
            float tmp = dna.getFitness()*10;
            for(int i=0; i< dna.getFitness()*10; i++){
                this.matingPool.add(dna);
            }
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
            System.out.println(" In generate : new child is "+child);
            this.dnaList.set(i,child);
        }
        this.generation++;
    }

    public void evaluate(){
        float bestDnaRecord = 0.0f;
        Dna bestDna = null;
        for(Dna dna : this.dnaList){
            if(Float.compare(dna.getFitness(),bestDnaRecord) > 0){

                bestDnaRecord = dna.getFitness();
                System.out.println(" In evaluate : "+bestDnaRecord);
                bestDna = dna;
            }
        }
        if(Float.compare(bestDnaRecord, 1.0f) == 0){
            this.searchFinished =  true;
        }
    }


}
