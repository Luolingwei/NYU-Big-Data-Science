package Loader;
import com.opencsv.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVLoader {

    public List<List<String>> textList = new ArrayList<>();
    public Set<String> stopWords;
    public Set<Character> unusefulIcon = new HashSet<>(Arrays.asList('&', '#','@'));

    public CSVLoader() throws IOException {
        stopWords = getStopWords();
    }

    public void read(String filePath) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(filePath));

        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            //["target","ids","date","flag","user","text"}
            String sentence = nextLine[5];
            List<String> splitedText = Arrays.asList(sentence.split("\\.| |,|\\?|!|;"));
            List<String> filteredText=splitedText.stream().filter(string -> !(string.isEmpty())).collect(Collectors.toList());
            textList.add(filteredText);
        }
    }


    public List<List<String>> readFilterTweet(String filePath, String pattern) throws IOException {

        List<List<String>> outTweets = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(filePath));

        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            //["target","ids","date","flag","user","text"}
            String patternId = nextLine[0];
            String sentence = nextLine[5];
            if (patternId.equals(pattern)){
                List<String> splitedText = Arrays.asList(sentence.split("\\.| |,|\\?|!|;"));
                List<String> filteredText=splitedText.stream().filter(word -> !(word.isEmpty() || stopWords.contains(word)|| unusefulIcon.contains(word.charAt(0)) || word.startsWith("http"))).collect(Collectors.toList());
                outTweets.add(filteredText);
            }
        }
        return outTweets;
    }


    public Set<String> getStopWords() throws IOException {
        Set<String> stopWords = new HashSet<>();
        InputStream stream = CSVLoader.class.getClassLoader().getResourceAsStream("stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String word = bufferedReader.readLine();
        while (word!=null){
            stopWords.add(word);
            word = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stopWords;
    }

    public List<List<String>> filterStopWords(List<List<String>> textList){
        List<List<String>> newTextList = new ArrayList<>();
        for (List<String> words: textList){
            List<String> newWords = new ArrayList<>();
            for (String word: words){
                if (!(stopWords.contains(word) || unusefulIcon.contains(word.charAt(0)) || word.startsWith("http"))){
                    newWords.add(word);
                }
            }
            newTextList.add(newWords);
        }
        return newTextList;
    }


}
