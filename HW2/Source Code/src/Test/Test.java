package Test;

import Cluster.Clustering;
import Cluster.KmeansPP;
import Similarity.*;
import Tasks.*;
import Utils.MatrixOperater;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

    public static int k = 3;
    public static int[] clusters;
    public static List<String> fileNames;
    public static List<List<String>> keyWordList = new ArrayList<>();

    public static void clusterSummarize (int k) {
        String files = "";
        Set<String> clusterKeyWords = new HashSet<>(); // Top KeyWords appeared in this cluster more than once
        Set<String> KeyWordsOnce = new HashSet<>(); // Top KeyWords appeared once in this cluster before
        System.out.println("Cluster " + (k + 1) + ":");
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i]==k) {
                files += "\t\t" + fileNames.get(i) + "\n";
                for (String word: keyWordList.get(i)){
                    if (KeyWordsOnce.contains(word))
                        clusterKeyWords.add(word);
                    else
                        KeyWordsOnce.add(word);
                }
           }
        }
        System.out.println("\tFequent keywords found in this cluster:");
        System.out.println("\t\t" + clusterKeyWords);
        System.out.println("\tFiles in the cluster:");
        System.out.print(files);
    }

    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        MatrixOperater matrixOperater = new MatrixOperater();
        String rootPath = "/Users/luolingwei/Desktop/Program/Classes/BDS/NYU-Big-Data-Science/HW2/dataset_3";
        List<File> files = loader.getFiles(rootPath);
        fileNames = loader.fileNames;
        PreProcessor preProcessor = new PreProcessor();
        List<List<String>> wordBags = preProcessor.getWordBags(files);
        DocumentTermMatrix documentTermMatrix = new DocumentTermMatrix();
        documentTermMatrix.calTfIdf(wordBags);
        for (int i = 0; i < fileNames.size(); i++) {
            List<String> keyWords = documentTermMatrix.getTopKeywords(i,10);
            keyWordList.add(keyWords);
            System.out.println("\t" + fileNames.get(i) + "\t" + keyWords);
        }

        double[][] matrix = documentTermMatrix.getTf_idf();
        double[][] normalizedTfIdf = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            normalizedTfIdf[i] = matrixOperater.normalize(matrix[i]);
        }

        Clustering model = new KmeansPP(normalizedTfIdf,new Cosine(),3);
        clusters = model.getClusters();
        for (int i = 0; i < k; i++) {
            clusterSummarize(i);
        }

        // SVD and Visualize
        double[][] centers = model.getCenters();
        double[][] normalizedCenters = new double[centers.length][centers[0].length];
        for (int i = 0; i < centers.length; i++) {
            normalizedCenters[i] = matrixOperater.normalize(centers[i]);
        }

        double[][] reducedTfIdf = SVD.reduce(normalizedTfIdf,2);
        double[][] reducedCenters = SVD.reduce(normalizedCenters, 2);


        Visualization TfIdfPlot = new Visualization("Tf-Idf Distribution",reducedTfIdf);
        Visualization centersPlot = new Visualization("Clustered Tf-Idf",reducedTfIdf, reducedCenters, clusters);
        TfIdfPlot.createPlot();
        centersPlot.createPlot();

        System.out.println("Success");
    }
}
