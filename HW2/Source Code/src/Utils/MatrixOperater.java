package Utils;

public class MatrixOperater {

    // normalize a vector to unit 1

    public double [] average (double [] vector, double base){
        double [] new_vector = new double[vector.length];
        for (int i=0;i<vector.length;i++){
            new_vector[i] = vector[i]/base;
        }
        return new_vector;
    }

    public double getLength (double [] vector){
        double length = 0.0;
        for (double v:vector){
            length += v*v;
        }
        return Math.sqrt(length);
    }

    public double[] VectorSum (double[] x, double[] y) {
        if (x.length != y.length) return null;
        double sum[] = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            sum[i] = x[i] + y[i];
        }
        return sum;
    }

    public double [] normalize (double [] vector){
        double length = getLength(vector);
        return average(vector,length);
    }

}
