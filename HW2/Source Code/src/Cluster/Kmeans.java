package Cluster;

import Similarity.*;
import Utils.MatrixOperater;

import java.util.Random;

public class Kmeans extends Clustering {

    public Kmeans(double[][] matrix, Similarity similarity, int k) {
        super(matrix, similarity, k);
    }

    @Override
    public void initializeCenters() {

        Random rand = new Random();
        MatrixOperater matrixOperater = new MatrixOperater();
        for (int i = 0; i < k; i++) {
            centers[i] = matrix[rand.nextInt(matrix.length)];
        }
        if (similarity instanceof Cosine) {
            for (int i = 0; i < k; i++) {
                centers[i] = matrixOperater.normalize(centers[i]);
            }
        }
    }

}
