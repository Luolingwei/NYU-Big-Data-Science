package Test;

import Tasks.Loader;
import Tasks.PreProcessor;
import edu.stanford.nlp.simple.Sentence;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Test {


    public static void main(String[] args) throws IOException {
        Loader loader = new Loader();
        String rootPath = "/Users/luolingwei/Desktop/Program/Classes/BDS/NYU-Big-Data-Science/HW2/dataset_3";
        List<File> files = loader.getFiles(rootPath);
        PreProcessor preProcessor = new PreProcessor();
        List<List<Sentence>> docs = preProcessor.getNewDocument(files);
        System.out.println("Success");
    }
}
