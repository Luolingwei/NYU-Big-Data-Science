package Tasks;

import Utils.Tuple;

import java.util.*;

public class DocumentTermMatrix {

    public static List<List<String>> terms = new ArrayList<>();
    public static int[][] count;
    public static double[][] tf_idf;

    public void calTfIdf(List<List<String>> wordBags){
        // initialize matrix
        Set<String> TotalWords = new HashSet<>();
        for (List<String> wordBag:wordBags){
            TotalWords.addAll(wordBag);
        }
        int docN=wordBags.size(),termN=TotalWords.size();
        count = new int[docN][termN];
        tf_idf = new double[docN][termN];

        // calculate docTermN, termDocN map
        Map<String, Integer> termDocN= new HashMap<>();
        for (int i=0;i<docN;i++){
            List<String> wordBag = wordBags.get(i);
            Map<String, Integer> docTermN= new HashMap<>();
            for (String word: wordBag){
                docTermN.put(word,docTermN.getOrDefault(word,0)+1);
            }
            List<String> words = new ArrayList(docTermN.keySet());
            int j=0;
            for (;j<words.size();j++){
                String curword = words.get(j);
                int curwordN = docTermN.get(curword);
                termDocN.put(curword,termDocN.getOrDefault(curword,0)+1);
                count[i][j] = curwordN;
            }
            terms.add(words);
        }

        // calculate tf-idf
        for (int i=0;i<docN;i++){
            for (int j=0;j<termN;j++){
                if (j< terms.get(i).size()){
                    double tf = (double) count[i][j]/wordBags.get(i).size();
                    double idf = Math.log((double) wordBags.size()/termDocN.get(terms.get(i).get(j)));
                    tf_idf[i][j] = tf*idf;
                } else {
                    tf_idf[i][j] = 0.0;
                }
            }
        }

        System.out.println("Success");

    }

    public List<String> getTopKeywords(int docID, int topN){
        if (docID>tf_idf.length){
            return null;
        }
        // if topN exceeds unique word number in doc, only extract all words.
        topN = Math.min(topN, terms.get(docID).size());
        List<String> words = terms.get(docID);
        List<Tuple> tupleList = new ArrayList<>();
        for (int i=0;i<words.size();i++){
            tupleList.add(new Tuple(words.get(i),tf_idf[docID][i]));
        }
        Collections.sort(tupleList,Collections.reverseOrder());
        List<String> keyWords = new ArrayList<>();
        for (int i=0; i<topN; i++){
            keyWords.add(tupleList.get(i).Word);
        }
        return keyWords;
    }


}
