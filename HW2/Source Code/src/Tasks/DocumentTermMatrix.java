package Tasks;

import Utils.Tuple;

import java.util.*;

public class DocumentTermMatrix {

    private int docNum, termsNum;
    private List<List<String>> wordBags;
    private List<String> terms;
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
        terms = new ArrayList<>(allWordsSet);
        termsNum = terms.size();
        // calculate DocTerm map, termDoc Map
        count = new int[docNum][termsNum];
        Map<String, Integer> termDocN= new HashMap<>();
        for (int i = 0; i < docNum; i++) {
            List<String> wordBag = wordBags.get(i);
            Set<String> wordBagSet = new HashSet<>(wordBag);
            for (String word : wordBag) {
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
                double idf = Math.log((double) wordBags.size()/termDocN.get(terms.get(j)));
                tf_idf[i][j] = tf*idf;
            }
        }
    }


    public List<String> getTopKeywords (int docID, int n) {
        if (docID >= wordBags.size()) return null;
        List<Tuple> tupleList = new ArrayList<>();
        List<String> wordBag = wordBags.get(docID);
        List<String> keyWords = new ArrayList<>();
        for (int i = 0; i < wordBag.size(); i++) {
            int termID = terms.indexOf(wordBag.get(i));
            Tuple tuple = new Tuple(termID, tf_idf[docID][termID]);
            if (!tupleList.contains(tuple)) tupleList.add(tuple);
        }
        Collections.sort(tupleList, Collections.reverseOrder());
        // no more than size of tupleList
        int m = Math.min(n,tupleList.size());
        for (int j=0;j<m;j++) {
            keyWords.add(terms.get(tupleList.get(j).WordId));
        }
        return keyWords;
    }

    public double[][] getTf_idf(){
        return tf_idf;
    }

}