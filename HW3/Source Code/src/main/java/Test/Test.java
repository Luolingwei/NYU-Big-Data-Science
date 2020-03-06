package Test;

import DataExploration.Explorer;
import Loader.CSVLoader;
import Ngrams.NgramFinder;
import TextRank.KeyWordTextRank;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException {

        System.out.println("==========================================================================");
        System.out.println("Mini Documents Explore and TextRank Software, Author: Lingwei Luo (lingweiluo@nyu.edu)");
        System.out.println("Please enter your parameter instructions on the command line");
        System.out.println("==========================================================================");

        Scanner sc = new Scanner(System.in);
        System.out.println("Absolute path of the csv file to analyze (do Not include \" at both ends of the path): ");
        String filePath = sc.nextLine();
        sc.close();

        // load files
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.read(filePath);

        // find top# and top@
        Explorer explorer = new Explorer(csvLoader.textList);
        explorer.outputTopHashTags(20);
        explorer.outputTopMentions(20);

        // find top Ngrams
        NgramFinder ngramFinder = new NgramFinder();
        ngramFinder.outputTopNgrams(csvLoader.textList);

        // TextRank to find keywords
        List<List<String>> filteredTextList = csvLoader.filterStopWords(csvLoader.textList);
        KeyWordTextRank AllTextRank = new KeyWordTextRank(filteredTextList);
        AllTextRank.outputTopList(100,"all");

        List<List<String>> negativeTextList = csvLoader.readFilterTweet(filePath,"0");
        KeyWordTextRank negativeTextRank = new KeyWordTextRank(negativeTextList);
        negativeTextRank.outputTopList(100,"negative");

        List<List<String>> positiveTextList = csvLoader.readFilterTweet(filePath,"4");
        KeyWordTextRank positiveTextRank = new KeyWordTextRank(positiveTextList);
        positiveTextRank.outputTopList(100,"positive");

    }

}
