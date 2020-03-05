package Cluster;

import Similarity.*;
import Utils.MatrixOperater;

import java.util.Arrays;

public abstract class Clustering {

    public int k;
    public double[][] matrix;
    public Similarity similarity;
    public int[] clusters;
    public double[][] centers;


    public Clustering (double[][] matrix, Similarity similarity, int k) {
        this.k = k;
        this.matrix = matrix;
        this.similarity = similarity;
        this.clusters = new int[matrix.length];
        this.centers = new double[k][matrix[0].length];
        docluster();
    }

    public abstract void initializeCenters ();

    public void docluster () {
        Arrays.fill(clusters, 0);
        initializeCenters();
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < matrix.length; i++) {
                double[] boundary = {Double.NaN, -1};
                for (int j = 0; j < k; j++) {
                    // update Boundary
                    double[] newBoundary = {similarity.distance(matrix[i], centers[j]), j};
                    if (!Double.isNaN(boundary[0]) && ((similarity instanceof Euclidean && boundary[0] < newBoundary[0]) || (similarity instanceof Cosine && boundary[0] > newBoundary[0]))) {
                        newBoundary = boundary;
                    }
                    boundary = newBoundary;
                }
                if (clusters[i] != (int) boundary[1]) {
                    clusters[i] = (int) boundary[1];
                    flag = true;
                }
            }
            // update Centers
            MatrixOperater matrixOperater = new MatrixOperater();
            for (int j = 0; j < k; j++) {
                double[] sum = new double[matrix[0].length];
                Arrays.fill(sum, 0.0);
                int count = 0;
                for (int i = 0; i < matrix.length; i++) {
                    if (clusters[i] == j) {
                        sum = matrixOperater.VectorSum(sum, matrix[i]);
                        count++;
                    }
                }
                if (similarity instanceof Euclidean)
                    centers[j] = matrixOperater.average(sum, count);
                else
                    centers[j] = matrixOperater.normalize(sum);
            }
        }
    }


    public int[] getClusters () {
        return clusters;
    }

    public double[][] getCenters () {
        return centers;
    }

}
