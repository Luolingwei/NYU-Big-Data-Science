package Cluster;

import Similarity.Similarity;
import java.util.*;
import java.util.List;

public class KNN {

    private List<List<String>> Kneighbors = new ArrayList<>();
    public KNN(int K, Similarity similarity, double[][] tfIdfMatrix, List<String> labels, List<double[]> labelTfIdf){

        for (double[] labelvector: labelTfIdf){
            // distances ([distance, index])
            List<double[]> distances = new ArrayList<>();
            for (int i=0;i<tfIdfMatrix.length;i++) {
                distances.add(new double[]{similarity.distance(labelvector, tfIdfMatrix[i]),(double)i});
            }
            // sort based on distance (small to large)
            distances.sort(new Comparator<double[]>() {
                @Override
                public int compare(double[] o1, double[] o2) {
                    return Double.compare(o1[0], o2[0]);
                }
            });
            // if K is larger than our sample size, reduce it to sample size
            K = Math.min(K,distances.size());
            // collect K nearest labels
            List<String> curNeighbors = new ArrayList<>();
            for (int i=0;i<K;i++){
                curNeighbors.add(labels.get((int) distances.get(i)[1]));
            }
            Kneighbors.add(curNeighbors);
        }

    }

    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        for (List<String> curneighbors : Kneighbors) {
            Map<String,Integer> count = new HashMap<>();
            int maxfreq = 0;
            String label = "";
            for (String neighbor: curneighbors){
                int curfreq = count.getOrDefault(neighbor,0)+1;
                count.put(neighbor,curfreq);
                if (curfreq>maxfreq){
                    maxfreq = curfreq;
                    label = neighbor;
                }
            }
            labels.add(label);
        }
        return labels;
    }

}
