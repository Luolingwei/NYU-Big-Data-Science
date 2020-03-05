package Ngrams;
import java.io.*;
import java.util.*;
import Utils.Ngrams;

import static java.util.stream.Collectors.toMap;

public class NgramFinder {

    int[] NgramsNumbers = new int[]{1,2,3,4};

    public void calTopNgrams(int N, int topN, List<List<String>> Totaltext){
        Map<Ngrams,Integer> countNgrams = new HashMap<>();
        List<Ngrams> validNgrams = new ArrayList<>();
        for (List<String> text: Totaltext){
            for (int i=0;i<=text.size()-N;i++){
                List<String> subtext = text.subList(i,i+N);
                if (!subtext.contains("")){
                    Ngrams curNgram = new Ngrams(subtext);
                    countNgrams.put(curNgram,countNgrams.getOrDefault(curNgram,0)+1);
                }
            }
        }
        // sort countNgrams by value
        Map<Ngrams, Integer> sortedNgrams = countNgrams
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        System.out.println();
        System.out.println("Printing the top " + topN + " most frequent " + N + "-grams");
        System.out.println();

        int i=0;
        for (Ngrams ngrams: sortedNgrams.keySet()){
            if (i>=topN) break;
            System.out.println(ngrams.ngrams + " : " + sortedNgrams.get(ngrams));
            i++;
        }
    }

    // 4 Use a sliding window approach to merge remaining phrases that belong together.
    public void outputTopNgrams(List<String> texts) throws FileNotFoundException {

        List<List<String>> Totaltext = new ArrayList<>();
        for (String sentence: texts){
            Totaltext.add(Arrays.asList(sentence.split(" ")));
        }

        // calculate 1Ngrams, 2Ngrams, 3Ngrams, 4Ngrams
        for (int N: NgramsNumbers){
            String filename = "outFiles/top20_" + N + "gram.txt";
            PrintStream fileOut = new PrintStream(filename);
            System.setOut(fileOut);
            calTopNgrams(N,20,Totaltext);
            fileOut.close();
        }
    }

}
