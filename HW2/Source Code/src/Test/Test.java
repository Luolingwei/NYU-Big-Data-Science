package Test;

import Cluster.Clustering;
import Cluster.Kmeans;
import Cluster.KmeansPP;
import Similarity.*;
import Tasks.*;
import Utils.MatrixOperater;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        System.out.println("\tFiles included in the cluster:");
        System.out.print(files);
        System.out.println("\tFequent keywords found in this cluster:");
        System.out.println("\t\t" + clusterKeyWords);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        // Get Input Parameters
        System.out.println("==========================================================================");
        System.out.println("Mini Documents Cluster Software, Author: Lingwei Luo (lingweiluo@nyu.edu)");
        System.out.println("Please enter your parameter instructions on the command line");
        System.out.println("==========================================================================");
        Scanner sc = new Scanner(System.in);
        // Select Cluster Algorithm
        String algorithmChoice = "";
        while (!algorithmChoice.matches("[01]")) {
            System.out.print("Please select clustering algorithm: (0 is K-means, 1 is K-means++) ");
            algorithmChoice = sc.nextLine();
        }
        String similarityChoice = "";
        while (!similarityChoice.matches("[01]")) {
            System.out.print("Please select similarity measure in the clustering algorithm: (0 is Euclidean, 1 is Cosine) ");
            similarityChoice = sc.nextLine();
        }
        String kCluster = "";
        while ((!kCluster.matches("\\d+")|| Integer.parseInt(kCluster)<2)) {
            System.out.print("Number of clusters k (k>=2): ");
            kCluster = sc.nextLine();
        }
        k = Integer.parseInt(kCluster);

        List<List<String>> documents;
        System.out.println("Absolute path to the directory containing the documents to analyze (do Not include \" at both ends of the path): ");
        String path = sc.nextLine();
        sc.close();

        Loader loader = new Loader();
        MatrixOperater matrixOperater = new MatrixOperater();
        PreProcessor preProcessor = new PreProcessor();
        // load txt files
        List<File> files = loader.getFiles(path);
        fileNames = loader.fileNames;
        // preprocess to get a wordBag per document
        List<List<String>> wordBags = preProcessor.getWordBags(files);
        // calculate tf-idf matrix
        DocumentTermMatrix documentTermMatrix = new DocumentTermMatrix();
        documentTermMatrix.calTfIdf(wordBags);

        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("Preprocessing Results");
        System.out.println("==========================================================================");
        System.out.println("Top 10 keywords in each document extracted by the Tf-Idf matrix:");

        for (int i = 0; i < fileNames.size(); i++) {
            List<String> keyWords = documentTermMatrix.getTopKeywords(i,10);
            keyWordList.add(keyWords);
            System.out.println("\t" + fileNames.get(i) + "\t" + keyWords);
        }

        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("Clustering Results");
        System.out.println("==========================================================================");

        double[][] matrix = documentTermMatrix.getTf_idf();
        double[][] normalizedTfIdf = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            normalizedTfIdf[i] = matrixOperater.normalize(matrix[i]);
        }

        Similarity similarity;
        Clustering model;
        if (similarityChoice.equals("0")) similarity = new Euclidean();
        else similarity = new Cosine();
        if (algorithmChoice.equals("0")) model = new Kmeans(normalizedTfIdf,similarity,k);
        else model = new KmeansPP(normalizedTfIdf,similarity,k);

        clusters = model.getClusters();
        // print Cluster Summary
        for (int i = 0; i < k; i++) {
            clusterSummarize(i);
        }

        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("Visualization Results");
        System.out.println("==========================================================================");

        // SVD
        double[][] centers = model.getCenters();
        double[][] normalizedCenters = new double[centers.length][centers[0].length];
        for (int i = 0; i < centers.length; i++) {
            normalizedCenters[i] = matrixOperater.normalize(centers[i]);
        }
        double[][] reducedTfIdf = SVD.reduce(normalizedTfIdf,2);
        double[][] reducedCenters = SVD.reduce(normalizedCenters, 2);

        // Visualize
        Visualization TfIdfPlot = new Visualization("Tf-Idf Distribution",reducedTfIdf);
        Visualization ClusterPlot = new Visualization("Clustered Tf-Idf",reducedTfIdf, reducedCenters, clusters);
        TfIdfPlot.draw();
        ClusterPlot.draw();
    }
}
