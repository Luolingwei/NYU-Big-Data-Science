package Similarity;

import Utils.MatrixOperater;

public class Cosine extends Similarity {

    @Override
    public double distance(double[] x, double[] y) {
        MatrixOperater oper = new MatrixOperater();
        double mul = 0.0;
        for (int i=0;i<x.length;i++){
            mul+=x[i]*y[i];
        }
        return mul/oper.getLength(x)*oper.getLength(y);
    }
}
