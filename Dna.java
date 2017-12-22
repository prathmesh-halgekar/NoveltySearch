import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public class Dna {
    private StringBuffer genes = new StringBuffer();
    private float fitness = 0;
    private float sparseness = 0;

    public StringBuffer getGenes() {
        return genes;
    }

    public void setGenes(StringBuffer genes) {
        this.genes = genes;
    }

    public float getFitness() {
        return fitness;
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
        this.fitness = (score/targetString.length());
        return this.fitness;
    }

    public float calculateNovelty(StringBuffer targetString){
        // target and this.gene length should be the same
        float score =0;
        for(int i=0; i< this.genes.length() ;i++){
            if(this.genes.charAt(i) != targetString.charAt(i)){
                score += abs(Character.getNumericValue(this.genes.charAt(i)) - Character.getNumericValue(targetString.charAt(i)))/10;
            }
        }
        this.sparseness = score/(targetString.length());
        return this.sparseness;
    }

    public Dna crossover(Dna partner){

        Dna child = new Dna();
        int midpoint = new Random().nextInt(this.genes.length());
        System.out.println("Midpoint : "+midpoint);
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
            if( r < mutationRate){
                this.genes.setCharAt(i,randomSeriesForThreeCharacter());
            }
        }
    }

    public void initializeGene(int length){
        this.genes = new StringBuffer();
        for(int i =0 ; i<length;i++){
            this.genes.append(randomSeriesForThreeCharacter());
        }
    }
    public static char randomSeriesForThreeCharacter() {
        Random r = new Random();
        char random_3_Char = (char) (48 + r.nextInt(47));
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
        Dna dna = new Dna();
//        dna.initializeGene(5);
//        System.out.println(" : " + dna.getGenes().toString() );
        dna.setGenes(new StringBuffer("abcde"));
        Dna partner = new Dna();
        partner.setGenes(new StringBuffer("xyz09"));
        dna.mutate(0.09f);
        System.out.println("After mutation : "+ dna.getGenes().toString());

    }
}
