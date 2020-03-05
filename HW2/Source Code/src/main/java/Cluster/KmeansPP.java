package Cluster;

import Similarity.Euclidean;
import Similarity.Similarity;
import Utils.MatrixOperater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KmeansPP extends Clustering{

    public KmeansPP(double[][] matrix, Similarity similarity, int k) {
        super(matrix, similarity, k);
    }

    @Override
    public void initializeCenters() {
        Random rand = new Random();
        MatrixOperater matrixOperater = new MatrixOperater();
        centers[0] = matrixOperater.normalize(matrix[rand.nextInt(matrix.length)]);
        List<Double> distances = new ArrayList<>();
        int n = 1;
        for (int index = 1; index < k; index++) {
            distances.clear();
            for (int i = 0; i < matrix.length; i++) {
                double min = Double.POSITIVE_INFINITY;
                for (int j = 0; j < n; j++) {
                    min = Math.pow(Math.min(min, (new Euclidean()).distance(matrixOperater.normalize(matrix[i]), centers[j])), 2);
                }
                distances.add(min);
            }
            double sum = distances.stream().mapToDouble(Double::doubleValue).sum();
            List<Integer> probabilities = new ArrayList<>();
            for (int i = 0; i < distances.size(); i++) {
                int copies = (int) Math.round(distances.get(i) * 10000 / sum);
                probabilities.addAll(Collections.nCopies(copies, i));
            }
            Collections.shuffle(probabilities);
            centers[index] = matrixOperater.normalize(matrix[probabilities.get(rand.nextInt(probabilities.size()))]);
            n++;
        }
    }
}
