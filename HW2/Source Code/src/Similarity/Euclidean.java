package Similarity;

public class Euclidean extends Similarity{

    @Override
    public double distance(double[] x, double[] y) {
        double dist = 0.0;
        for (int i=0;i<x.length;i++){
            dist += (x[i]-y[i])*(x[i]-y[i]);
        }
        return Math.sqrt(dist);
    }
}
