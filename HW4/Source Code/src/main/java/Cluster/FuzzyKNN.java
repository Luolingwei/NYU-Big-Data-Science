package Cluster;

import Similarity.Similarity;

import java.util.*;

public class FuzzyKNN {

    private List<List<String>> Kneighbors = new ArrayList<>();
    public FuzzyKNN(int K, Similarity similarity, double[][] tfIdfMatrix,  List<String> labels, List<double[]> labelTfIdf){

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
                    return Double.compare(o1[0],o2[0]);
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

    public List<List<String[]>> getLabels() {
        List<List<String[]>> labels = new ArrayList<>();
        for (List<String> curneighbors : Kneighbors) {
            Map<String,Integer> count = new HashMap<>();
            for (String neighbor: curneighbors){
                count.put(neighbor,count.getOrDefault(neighbor,0)+1);
            }
            List<String[]> labelset = new ArrayList<>();
            for (String label: count.keySet()){
                // calculate the proportion of each label in a set
                double proportion = (double) count.get(label)/curneighbors.size();
                labelset.add(new String[]{label,String.format("%.1f", proportion*100)+"%"});
            }
            labels.add(labelset);
        }
        return labels;
    }

}
