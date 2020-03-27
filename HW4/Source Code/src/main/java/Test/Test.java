package Test;
import Cluster.FuzzyKNN;
import Cluster.KNN;
import Similarity.Cosine;
import Similarity.Euclidean;
import Tasks.*;
import Utils.MatrixOperater;
import java.io.*;
import java.util.*;

public class Test {

    public static int k = 6;
    public static List<String> fileNames;
    public static List<String> filelables;
    public static List<String> labelfileNames;

    public static void main(String[] args) throws IOException {

        //C1: Airline Safety
        //C4: Hoof and Mouth Disease
        //C7: Mortgage Rates

        //String path = "/Users/luolingwei/Desktop/Program/Classes/BDS/NYU-Big-Data-Science/HW4/data/dataset_3";
        //String labelpath = "/Users/luolingwei/Desktop/Program/Classes/BDS/NYU-Big-Data-Science/HW4/data/unknown";

        // Get Input Parameters
        System.out.println("==========================================================================");
        System.out.println("Mini Documents Clustering Software, Author: Lingwei Luo (lingweiluo@nyu.edu)");
        System.out.println("Please enter your parameter instructions on the command line");
        System.out.println("==========================================================================");

        Scanner sc = new Scanner(System.in);
        System.out.println("Absolute path to the directory containing the well-labeled documents (eg: The Path/LabelC4/article01.txt)(do Not include \" at both ends of the path): ");
        String path = sc.nextLine();
        System.out.println("Absolute path to the directory containing the documents waiting to label (eg: The Path/unknown01.txt)(do Not include \" at both ends of the path): ");
        String labelpath = sc.nextLine();
        sc.close();

        MatrixOperater matrixOperater = new MatrixOperater();

        /** generate corpus tfidf and labels **/
        Loader loader = new Loader();
        PreProcessor preProcessor = new PreProcessor();
        // load txt files
        List<File> files = loader.getFiles(path);
        fileNames = loader.fileNames;
        filelables = loader.filelabels;
        // preprocess to get a wordBag per document
        List<List<String>> wordBags = preProcessor.getWordBags(files);
        // calculate tf-idf matrix
        DocumentTermMatrix documentTermMatrix = new DocumentTermMatrix();
        documentTermMatrix.calTfIdf(wordBags);
        double[][] TfIdfMatrix = documentTermMatrix.getTf_idf();
        double[][] normalizedTfIdfMatrix = matrixOperater.normalizeMatrix(TfIdfMatrix);

        /** generate unknown files tfidf **/
        Loader labelloader = new Loader();
        PreProcessor labelpreProcessor = new PreProcessor();
        // load unknown files to label
        List<File> labelfiles = labelloader.getFiles(labelpath);
        labelfileNames = labelloader.fileNames;
        // preprocess to get a wordBag per document
        List<List<String>> labelwordBags = labelpreProcessor.getWordBags(labelfiles);
        // covert each unknown file to tfidf vector
        List<double[]> labelTfIdf = new ArrayList<>();
        for (List<String> labelwords: labelwordBags){
            labelTfIdf.add(matrixOperater.normalize(documentTermMatrix.Doc2TfIdf(labelwords)));
        }

        /** predict unknown files labels using KNN **/
        // KNN prediction
        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("KNN Label Results");
        System.out.println("==========================================================================");

        KNN model = new KNN(k,new Euclidean(),normalizedTfIdfMatrix,filelables,labelTfIdf);
        List<String> labels = model.getLabels();
        for (int i=0; i<labelfileNames.size();i++){
            System.out.println(labelfileNames.get(i)+" is labeled as "+labels.get(i));
        }

        // FuzzyKNN prediction
        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("FuzzyKNN Label Results");
        System.out.println("==========================================================================");

        FuzzyKNN model2 = new FuzzyKNN(k,new Euclidean(),normalizedTfIdfMatrix,filelables,labelTfIdf);
        List<List<String[]>> labels2 = model2.getLabels();
        for (int i=0; i<labelfileNames.size();i++){
            System.out.println(labelfileNames.get(i)+" is labeled as follows");
            for (int j=0; j<labels2.get(i).size();j++){
                System.out.print(labels2.get(i).get(j)[0]+":"+labels2.get(i).get(j)[1]+" ");
            }
            System.out.print('\n');
        }

    }
}
