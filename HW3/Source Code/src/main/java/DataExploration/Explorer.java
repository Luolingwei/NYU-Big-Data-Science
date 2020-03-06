package DataExploration;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import static java.util.stream.Collectors.toMap;

public class Explorer {

    public List<List<String>> textList;
    public HashMap<String,Integer> HashTagCount = new HashMap<>();
    public HashMap<String,Integer> MentionCount = new HashMap<>();


    public Explorer(List<List<String>> texts){
        this.textList = texts;
        collectHashMention();
    }

    public void collectHashMention(){
        for (List<String> text:textList){
            for (String word: text){
                if (!word.isEmpty()){
                    if (word.charAt(0)=='#'){
                        HashTagCount.put(word,HashTagCount.getOrDefault(word,0)+1);
                    }
                    if (word.charAt(0)=='@'){
                        MentionCount.put(word,MentionCount.getOrDefault(word,0)+1);
                    }
                }
            }
        }
    }

    public void outputTopHashTags(int N) throws FileNotFoundException {
        // sort HashTagCount by value
        Map<String, Integer> sortedHashTags = HashTagCount
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        PrintStream file1 = new PrintStream("top#.txt");
        System.setOut(file1);

        System.out.println();
        System.out.println("Printing the top " + N + " most frequent hashtags");
        System.out.println();

        int i=0;
        for (String word:sortedHashTags.keySet()){
            if (i>=N) break;
            if (word.length()>1){
                System.out.println(word + " : " + sortedHashTags.get(word));
                i++;
            }
        }
        file1.close();
    }


    public void outputTopMentions(int N) throws FileNotFoundException {
        // sort MentionCount by value
        Map<String, Integer> sortedMentions = MentionCount
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        PrintStream file2 = new PrintStream("top@.txt");
        System.setOut(file2);

        System.out.println();
        System.out.println("Printing the top " + N + " most frequent @-mentions");
        System.out.println();

        int i=0;
        for (String word:sortedMentions.keySet()){
            if (i>=N) break;
            if (word.length()>1){
                System.out.println(word + " : " + sortedMentions.get(word));
                i++;
            }
        }
        file2.close();
    }



}
