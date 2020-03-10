package Loader;
import com.opencsv.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVLoader {

    public List<String> textRaw = new ArrayList<>();
    public List<List<String>> textList = new ArrayList<>();
    public List<Integer> positiveIdx = new ArrayList<>();
    public List<Integer> negativeIdx = new ArrayList<>();
    public Set<String> stopWords;


    public CSVLoader() throws IOException {
        stopWords = getStopWords();
    }

    public void read(String filePath) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(filePath));

        int lineIdx = 0;
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            //["target","ids","date","flag","user","text"}
            String sentence = nextLine[5];
            String target = nextLine[0];
            if (target.equals("0")){
                negativeIdx.add(lineIdx);
            } else if (target.equals("4")){
                positiveIdx.add(lineIdx);
            }
            textRaw.add(sentence);
            List<String> splitedText = Arrays.asList(sentence.split("\\.| |,|\\?|!|;"));
            List<String> filteredText=splitedText.stream().filter(string -> !(string.isEmpty())).collect(Collectors.toList());
            textList.add(filteredText);
            lineIdx+=1;
        }
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




}
