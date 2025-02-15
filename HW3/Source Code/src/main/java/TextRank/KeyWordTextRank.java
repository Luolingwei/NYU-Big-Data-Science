package TextRank;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;

public class KeyWordTextRank {

    private List<List<String>> docs;
    private Map<String, List<String>> words = new HashMap<>();
    private Map<String, Double> vertex = new HashMap<>();
    private Double d;
    private Integer maxIter;
    private Double minDiff;
    private Map<String, Double> top = new LinkedHashMap<>();

    public KeyWordTextRank(List<List<String>> docs) {
        this.docs = docs;
        this.d = 0.85;
        this.maxIter = 200;
        this.minDiff = 0.001;
        solve();
    }

    public void solve() {
        for (List<String> doc : docs) {
            List<String> que = new ArrayList<>();
            for (String ch : doc) {
                String word = ch.toString();
                List<String> value = this.words.get(word);
                if (value == null) {
                    value = new ArrayList<String>();
                    this.words.put(word, value);
                    this.vertex.put(word, 1.0);
                }
                que.add(word);
                if (que.size() > 5) {
                    que.remove(0);
                }
                for (String w1 : que) {
                    for (String w2 : que) {
                        if (w1.equals(w2)) {
                            continue;
                        }
                        this.words.get(w1).add(w2);
                        this.words.get(w2).add(w1);
                    }
                }
            }
        }
        Integer iterNum = 0;
        while (iterNum < this.maxIter) {
            iterNum++;
            Map<String, Double> m = new HashMap<>();
            double maxDiff = 0;
            for (Map.Entry<String, List<String>> entry : this.words.entrySet()) {
                String key = entry.getKey();
                m.put(key, 1 - this.d);
                for (String j : entry.getValue()) {
                    if (key.equals(j) || this.words.get(j).size() == 0) {
                        continue;
                    }
                    m.put(key, m.get(key) + (this.d / this.words.get(j).size() * this.vertex.get(j)));
                }
                if (Math.abs(m.get(key) - this.vertex.get(key)) > maxDiff) {
                    maxDiff = Math.abs(m.get(key) - this.vertex.get(key));
                }
            }
            this.vertex = m;
            if (maxDiff <= this.minDiff) {
                break;
            }
        }
        List<Map.Entry<String, Double>> list = new ArrayList<>(this.vertex.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Iterator<Entry<String, Double>> it = list.iterator(); it.hasNext();) {
            Entry<String, Double> entry = it.next();
            this.top.put(entry.getKey(), entry.getValue());
        }
    }


    public void outputTopList(Integer limit, String fileName) throws FileNotFoundException {

        PrintStream file = new PrintStream(fileName + "TextRank.txt");
        System.setOut(file);

        System.out.println();
        System.out.println("Printing the top " + limit + " TextRank keywords for "+ fileName + " Tweets");
        System.out.println();

        int i=0;
        for (String word:this.top.keySet()){
            if (i>=limit) break;
            System.out.println("\t"+word);
            i++;
        }
        file.close();
    }

}