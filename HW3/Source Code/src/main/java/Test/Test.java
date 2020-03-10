package Test;

import DataExploration.Explorer;
import Loader.CSVLoader;
import Ngrams.NgramFinder;
import TextRank.KeyWordTextRank;
import TextRank.Filter;
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

        // extract all text, filter out unnecessary words
        Filter filter = new Filter(csvLoader.stopWords);
        filter.filterOut(csvLoader.textRaw);

        List<List<String>> filteredAllTextList = filter.getAllText();
        List<List<String>> negativeTextList = filter.getNegativeText(csvLoader.negativeIdx);
        List<List<String>> positiveTextList = filter.getPositiveText(csvLoader.positiveIdx);

        // TextRank to find keywords
        KeyWordTextRank AllTextRank = new KeyWordTextRank(filteredAllTextList);
        AllTextRank.outputTopList(100,"all");

        KeyWordTextRank negativeTextRank = new KeyWordTextRank(negativeTextList);
        negativeTextRank.outputTopList(100,"negative");

        KeyWordTextRank positiveTextRank = new KeyWordTextRank(positiveTextList);
        positiveTextRank.outputTopList(100,"positive");

    }

}
