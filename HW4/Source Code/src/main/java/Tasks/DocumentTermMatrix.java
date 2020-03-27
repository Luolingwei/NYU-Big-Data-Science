package Tasks;
import java.util.*;

public class DocumentTermMatrix {

    private int docNum, termsNum;
    private List<List<String>> wordBags;
    private List<String> terms;
    private Map<String, Integer> termDocN;
    private int[][] count;
    private double[][] tf_idf;

    public void calTfIdf (List<List<String>> wordBags) {
        this.wordBags = wordBags;
        docNum = wordBags.size();
        // add all words to set
        Set<String> allWordsSet = new HashSet<>();
        for (List<String> wordBag : wordBags) {
            allWordsSet.addAll(wordBag);
        }
        // words in all wordBags distribute evenly
        terms = new ArrayList<>(allWordsSet);
        termsNum = terms.size();
        // calculate DocTerm map, termDoc Map
        count = new int[docNum][termsNum];
        termDocN= new HashMap<>();
        for (int i = 0; i < docNum; i++) {
            List<String> wordBag = wordBags.get(i);
            Set<String> wordBagSet = new HashSet<>(wordBag);
            for (String word : wordBag) {
                // each word in a wordBag is mixed in allWordSet
                count[i][terms.indexOf(word)]++;
            }
            for (String uniqueWord: wordBagSet){
                termDocN.put(uniqueWord,termDocN.getOrDefault(uniqueWord,0)+1);
            }
        }

        tf_idf = new double[docNum][termsNum];
        for (int i = 0; i < docNum; i++) {
            for (int j = 0; j < termsNum; j++) {
                double tf = (double) count[i][j] / wordBags.get(i).size();
                double idf = 0.0;
                if (termDocN.get(terms.get(j))>3 && termDocN.get(terms.get(j))<18){ // filter out words too unique or too common
                    idf = Math.log((double) wordBags.size()/termDocN.get(terms.get(j)));
                }
                tf_idf[i][j] = tf*idf;
            }
        }
    }

    public double[][] getTf_idf(){
        return tf_idf;
    }

    public double[] Doc2TfIdf(List<String> words){
        double[] curTfIdf = new double[termsNum];
        Map<String,Integer> curCount = new HashMap<>();
        for (String word : words) {
            // count appearance of word in curBag
            curCount.put(word,curCount.getOrDefault(word,0)+1);
        }

        for (int j = 0; j < termsNum; j++) {
            double tf = (double) curCount.getOrDefault(terms.get(j),0) / words.size();
            double idf = 0.0;
            if (termDocN.get(terms.get(j))>3 && termDocN.get(terms.get(j))<18){ // filter out words too unique or too common
                idf = Math.log((double) docNum/termDocN.get(terms.get(j)));
            }
            curTfIdf[j] = tf*idf;
        }
        return curTfIdf;
    }

}