package Test;

import Tasks.DocumentTermMatrix;
import Tasks.Loader;
import Tasks.PreProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        String rootPath = "/Users/luolingwei/Desktop/Program/Classes/BDS/NYU-Big-Data-Science/HW2/datasetTest";
        List<File> files = loader.getFiles(rootPath);
        PreProcessor preProcessor = new PreProcessor();
        List<List<String>> wordBags = preProcessor.getWordBags(files);
        DocumentTermMatrix documentTermMatrix = new DocumentTermMatrix();
        documentTermMatrix.calTfIdf(wordBags);
        List<String> keyWords = documentTermMatrix.getTopKeywords(0,10);
    }
}
