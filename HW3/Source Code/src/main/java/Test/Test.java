package Test;


import DataExploration.Explorer;
import Loader.CSVLoader;
import Ngrams.NgramFinder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        CSVLoader csvLoader = new CSVLoader();
        String filePath = "/Users/luolingwei/Desktop/Classes/BigData/Homework/HW3/sentiment140_test.csv";
        csvLoader.read(filePath);

        Explorer explorer = new Explorer(csvLoader.textList);

        explorer.outputTopHashTags(20);
        explorer.outputTopMentions(20);

        NgramFinder ngramFinder = new NgramFinder();
        ngramFinder.outputTopNgrams(csvLoader.textList);

        System.out.println("Success");

    }

}
