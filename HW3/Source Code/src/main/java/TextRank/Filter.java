package TextRank;

import edu.stanford.nlp.simple.*;

import java.util.*;

public class Filter {

    public List<List<String>> filteredAllText;
    public Set<Character> unusefulIcon = new HashSet<>(Arrays.asList('&', '#','@'));
    public Set<String> stopWords;

    public Filter(Set<String> StopWords){
        stopWords = StopWords;
    }

    public List<List<String>> ExtractAdjNoun(List<String> textRaw){
        List<List<String>> outTextList = new ArrayList<>();
        for (String text:textRaw){
            List<String> outWords = new ArrayList<>();
            Sentence sentence = new Sentence(text);
            List<String> tags = sentence.posTags();
            List<String> words = sentence.words();
            for (int i=0;i<words.size();i++){
                if (tags.get(i).startsWith("J") || tags.get(i).startsWith("N")){
                    outWords.add(words.get(i));
                }
            }
            outTextList.add(outWords);
        }
        return outTextList;
    }

    public List<List<String>> filterUnusefulWords(List<List<String>> textList){
        List<List<String>> newTextList = new ArrayList<>();
        for (List<String> words: textList){
            List<String> newWords = new ArrayList<>();
            for (String word: words){
                if (!(stopWords.contains(word.toLowerCase()) || unusefulIcon.contains(word.charAt(0)) || word.startsWith("http"))){
                    newWords.add(word);
                }
            }
            newTextList.add(newWords);
        }
        return newTextList;
    }

    public void filterOut(List<String> textRaw){
        List<List<String>> AdjNouns = ExtractAdjNoun(textRaw);
        filteredAllText = filterUnusefulWords(AdjNouns);
    }

    public List<List<String>> getAllText(){
        return filteredAllText;
    }

    public List<List<String>> getPositiveText(List<Integer> positiveIdx){
        List<List<String>> positiveText = new ArrayList<>();
        for (int idx: positiveIdx){
            positiveText.add(filteredAllText.get(idx));
        }
        return positiveText;
    }

    public List<List<String>> getNegativeText(List<Integer> negativeIdx){
        List<List<String>> negativeText = new ArrayList<>();
        for (int idx: negativeIdx){
            negativeText.add(filteredAllText.get(idx));
        }
        return negativeText;
    }


}
