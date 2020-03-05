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
        // words in all wordBags distribute evenly
        terms = new ArrayList<>(allWordsSet);
        termsNum = terms.size();
        // calculate DocTerm map, termDoc Map
        count = new int[docNum][termsNum];
        Map<String, Integer> termDocN= new HashMap<>();
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

    double[] getFolderTfIdf(List<Integer> docIDList){
        double[] newTfIdf = new double[termsNum];
        for (int id: docIDList){
            for (int i=0;i<termsNum;i++){
                newTfIdf[i]=newTfIdf[i]+tf_idf[id][i];
            }
        }
        return newTfIdf;
    }

    public Set<String> getTopKeywords (List<Integer> docIDList, int n) {
        List<Tuple> tupleList = new ArrayList<>();
        Set<String> keyWords = new HashSet<>();
        double[] folderTfIdf = getFolderTfIdf(docIDList);
        for (int i=0;i<termsNum;i++){
            if (folderTfIdf[i]>0){
                tupleList.add(new Tuple(i,folderTfIdf[i]));
            }
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