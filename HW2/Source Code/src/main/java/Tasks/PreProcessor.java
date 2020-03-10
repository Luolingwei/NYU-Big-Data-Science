package Tasks;
import java.io.*;
import java.util.*;
import Utils.Ngrams;
import edu.stanford.nlp.simple.*;

public class PreProcessor {

    public List<File> files = new ArrayList<>();
    public static int NgramsThreshold = 3;


    public Set<String> getStopWords() throws IOException {
        Set<String> stopWords = new HashSet<>();
        InputStream stream = PreProcessor.class.getClassLoader().getResourceAsStream("stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String word = bufferedReader.readLine();
        while (word!=null){
            stopWords.add(word);
            word = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stopWords;
    }

    // 1 Filter and removestopwords
    // 2 Apply tokenization,stemming and lemmatization
    // return parsed documents, List<List<Sentence>>
    public List<List<Sentence>> filterStopWords (Set<String> stopWords) throws IOException {
        List<List<Sentence>> documents = new ArrayList<>();
        for (File file:files){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String article = "";
            String line = bufferedReader.readLine();
            while (line!=null){
                article+=" ";
                if (line.isEmpty()){
                    article+=".";
                } else {
                    article+=line;
                };
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            Document doc = new Document(article);
            List<Sentence> parsedDoc = new ArrayList<>();
            for (Sentence sentence: doc.sentences()){
                List<String> validWords = new ArrayList<>();
                for (String word : sentence.lemmas()){
                    if (!stopWords.contains(word.toLowerCase()))
                        validWords.add(word);
                }
                if (!validWords.isEmpty()){
                    parsedDoc.add(new Sentence(validWords));
                }
            }
            documents.add(parsedDoc);
        }
        return documents;
    }

    // 3 Apply named-entity extraction (NER). (Connect words with same tag as 1 word)
    // return documents after NER, List<List<String>>
    public List<List<String>> NER(List<List<Sentence>> documents){
        List<List<String>> new_documents = new ArrayList<>();

        for (List<Sentence> doc: documents){
            List<String> newDoc = new ArrayList<>();
            for (Sentence sentence: doc){
                List<String> nerTags = sentence.nerTags();
                List<String> words = sentence.words();
                List<String> newWords = new ArrayList<>();
                int i=0;
                while (i<words.size()){
                    String newWord = words.get(i);
                    String newTag = nerTags.get(i);
                    i++;
                    switch (newTag){
                        case "COUNTRY":
                            while (i<words.size() && nerTags.get(i).equals("COUNTRY")){
                                newWord = newWord+"_"+words.get(i);
                                i++;
                            }
                            break;
                        case "CITY":
                            while (i<words.size() && nerTags.get(i).equals("CITY")){
                                newWord = newWord+"_"+words.get(i);
                                i++;
                            }
                            break;
                        case "LOCATION":
                            while (i<words.size() && nerTags.get(i).equals("LOCATION")){
                                newWord = newWord+"_"+words.get(i);
                                i++;
                            }
                            break;
                        case "ORGANIZATION":
                            while (i<words.size() && nerTags.get(i).equals("ORGANIZATION")){
                                newWord = newWord+"_"+words.get(i);
                                i++;
                            }
                            break;
                        case "PERSON":
                            while (i<words.size() && nerTags.get(i).equals("PERSON")){
                                newWord = newWord+"_"+words.get(i);
                                i++;
                            }
                            break;
                        default:
                            break;
                    }
                    newDoc.add(newWord);
                }
            }
            new_documents.add(newDoc);
        }
        return new_documents;
    }

    public List<Ngrams> calNgrams(int N, int threshold, List<String> TotalWordDic){
        Map<Ngrams,Integer> countNgrams = new HashMap<>();
        List<Ngrams> validNgrams = new ArrayList<>();
        for (int i=0;i<=TotalWordDic.size()-N;i++){
            Ngrams curNgram = new Ngrams(TotalWordDic.subList(i,i+N));
            countNgrams.put(curNgram,countNgrams.getOrDefault(curNgram,0)+1);
        }
        for (Ngrams ngrams: countNgrams.keySet()){
            if (countNgrams.get(ngrams)>=threshold){
                validNgrams.add(ngrams);
            }
        }
        return validNgrams;
    }

    // 4 Use a sliding window approach to merge remaining phrases that belong together.
    public List<List<String>> mergeNgrams(List<List<String>> NERdocuments){

        List<String> TotalWordDic = new ArrayList<>();
        for (List<String> doc:NERdocuments){
            TotalWordDic.addAll(doc);
        }

        // calculate 2Ngrams, 3Ngrams, 4Ngrams
        List<Ngrams> ngramsList2 = calNgrams(2,NgramsThreshold,TotalWordDic);
        List<Ngrams> ngramsList3 = calNgrams(3,NgramsThreshold,TotalWordDic);
        List<Ngrams> ngramsList4 = calNgrams(4,NgramsThreshold,TotalWordDic);

        // connect all Ngrams in documents
        List<List<String>> wordBags = new ArrayList<>();
        for (List<String> doc: NERdocuments){
            String S = doc.toString().replaceAll("[\\[\\]\\,]", "");
            // replace from large to samll Ngrams
            for (Ngrams ngrams: ngramsList4){
                S = S.replaceAll(ngrams.toString(),ngrams.toConnect());
            }
            for (Ngrams ngrams: ngramsList3){
                S = S.replaceAll(ngrams.toString(),ngrams.toConnect());
            }
            for (Ngrams ngrams: ngramsList2){
                S = S.replaceAll(ngrams.toString(),ngrams.toConnect());
            }
            List<String> wordBag = new ArrayList<>();
            for (String s : S.split(" ")){
                wordBag.add(s.toLowerCase());
            }
            wordBags.add(wordBag);
        }
        return wordBags;
    }

    public List<List<String>> getWordBags(List<File> loadfiles) throws IOException {
        files = loadfiles;
        Set<String> stopWords = getStopWords();
        List<List<Sentence>> documents = filterStopWords(stopWords);
        List<List<String>> NERdocuments = NER(documents);
        List<List<String>> wordBags = mergeNgrams(NERdocuments);
        return wordBags;

    }
}
