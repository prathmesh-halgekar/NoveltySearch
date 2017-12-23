import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class Dna {
    private StringBuffer genes = new StringBuffer();
    private float fitness = 0;
    private float sparseness = 0;
    private float maxScore;

    public StringBuffer getGenes() {
        return this.genes;
    }

    public void setGenes(StringBuffer genes) {
        this.genes = genes;
    }

    public float getFitness() {
        return this.fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getSparseness() {
        return sparseness;
    }

    public void setSparseness(float sparseness) {
        this.sparseness = sparseness;
    }

    public float calculateFitness(StringBuffer targetString){
        // target and this.gene length should be the same
        float score =0;
        for(int i=0; i< this.genes.length() ;i++){
            if(this.genes.charAt(i) == targetString.charAt(i)){
                score++;
            }
        }
        //System.out.println("Characters matching : " +score);

        this.fitness = ((float)score/(float)targetString.length());
        //System.out.println("Fitness is : " +this.fitness);
        return this.fitness;
    }

    public float calculateNovelty(StringBuffer targetString){
        // target and this.gene length should be the same
        // In theory, difference should be calculated on phenotype and not genotype. But the below
        // comparision on a character by character basis is also close enough?
        float score =0;
        for(int i=0; i< this.genes.length() ;i++){
            if(this.genes.charAt(i) != targetString.charAt(i)){
                score += abs(Character.getNumericValue(this.genes.charAt(i)) - Character.getNumericValue(targetString.charAt(i)));
            }
        }
        //System.out.println("raw score is : "+score);
        //System.out.println("max score before compare is : "+this.maxScore);
        if(Float.compare(score,this.maxScore) > 0){
            //System.out.println("max score is : "+this.maxScore);
            score = this.maxScore;
        }
        this.sparseness = this.getNormalizedValue(score);
        //System.out.println("Sparseness is : " +this.sparseness);
        return this.sparseness;
    }

    public float getNormalizedValue(float score){
        float minScore = 0;
        //float maxScore = 20.0f; // Assumption
        if(Float.compare(score,0.0f)==0){
            // to avoid 'NaN' scenario.
            //System.out.println("raw score inside compare is : "+score);
            return 0;
        }else{
            float result = (score-minScore)/(this.maxScore-minScore);
            return result;
        }

    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public Dna crossover(Dna partner){

        Dna child = new Dna();
        child.setMaxScore(this.maxScore);
        int midpoint = new Random().nextInt(this.genes.length());
        //System.out.println("Midpoint : "+midpoint);
        for(int i=0 ; i<this.genes.length() ; i++){
            if(i > midpoint){
                child.setGenes(child.getGenes().append(this.genes.charAt(i)));
            }else{
                child.setGenes(child.getGenes().append(partner.getGenes().charAt(i)));
            }
        }
        return child;
    }

    public void mutate(float mutationRate){
        for(int i =0; i< this.genes.length(); i++){
            float r = (float)(new Random().nextInt(10))/10;
            if( Float.compare(r,mutationRate) < 0){
                this.genes.setCharAt(i,randomSeriesForThreeCharacter());
            }
        }
    }


    public void initializeGene(int length, int maxScoreForNovelty){
        this.genes = new StringBuffer();
        this.maxScore = maxScoreForNovelty;
        //System.out.println("While setting up , maxscore : "+this.maxScore);
        for(int i =0 ; i<length;i++){
            this.genes.append(randomSeriesForThreeCharacter());
        }
    }
    public static char randomSeriesForThreeCharacter() {
        Random r = new Random();
        int charVal = 63 + r.nextInt(59);
        if(charVal == 63)
            charVal = 32;
        if(charVal == 64)
            charVal = 46;

        char random_3_Char = (char) charVal;
        return random_3_Char;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dna dna = (Dna) o;

        if (Float.compare(dna.getFitness(), getFitness()) != 0) return false;
        if (Float.compare(dna.getSparseness(), getSparseness()) != 0) return false;
        return getGenes().equals(dna.getGenes());
    }

    @Override
    public int hashCode() {
        int result = getGenes().hashCode();
        result = 31 * result + (getFitness() != +0.0f ? Float.floatToIntBits(getFitness()) : 0);
        result = 31 * result + (getSparseness() != +0.0f ? Float.floatToIntBits(getSparseness()) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dna{" +
                "genes=" + genes +
                ", fitness=" + fitness +
                ", sparseness=" + sparseness +
                '}';
    }

    public static void main(String[] args){
//        Dna dna = new Dna();
////        dna.initializeGene(5);
////        System.out.println(" : " + dna.getGenes().toString() );
//        dna.setGenes(new StringBuffer("abcde"));
//        Dna partner = new Dna();
//        partner.setGenes(new StringBuffer("xyz09"));
//        dna.mutate(0.09f);
//        System.out.println("After mutation : "+ dna.getGenes().toString());
        Dna dna = new Dna();
        dna.setGenes(new StringBuffer("hello"));
        for(int i=0;i<10;i++){
            dna.mutate(0.5f);

        }
    }
}
