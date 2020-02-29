package Test;

import Cluster.Clustering;
import Cluster.Kmeans;
import Cluster.KmeansPP;
import Similarity.*;
import Tasks.DocumentTermMatrix;
import Tasks.Loader;
import Tasks.PreProcessor;
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

    public static void printClusterInfo (int k) {
        String files = "";
        Set<String> clusterKeyWords = new HashSet<>();
        System.out.println("Cluster " + (k + 1) + ":");
        for (int i = 0; i < clusters.length; i++) {
            if (clusters[i]==k) {
                files += "\t\t" + fileNames.get(i) + "\n";
                clusterKeyWords.addAll(keyWordList.get(i));
           }
        }
        System.out.println("\tKeywords found in this cluster:");
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
            printClusterInfo(i);
        }
        // if measure == Eru, normalize matrix


        System.out.println("Success");
    }
}
