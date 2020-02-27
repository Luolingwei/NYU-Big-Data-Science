package Tasks;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.simple.*;

public class PreProcessor {

    public List<File> files = new ArrayList<>();

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
                    if (!stopWords.contains(word))
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
    // return documents after NER, List<List<Sentence>>
    public List<List<Sentence>> NER(List<List<Sentence>> documents){
        List<List<Sentence>> new_documents = new ArrayList<>();

        for (List<Sentence> doc: documents){
            List<Sentence> newDoc = new ArrayList<>();
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
                    newWords.add(newWord);
                }
                newDoc.add(new Sentence(newWords));
            }
            new_documents.add(newDoc);
        }
        return new_documents;
    }


    public List<List<Sentence>> getNewDocument(List<File> loadfiles) throws IOException {
        files = loadfiles;
        Set<String> stopWords = getStopWords();
        List<List<Sentence>> documents = filterStopWords(stopWords);
        List<List<Sentence>> NERdocuments = NER(documents);
        return NERdocuments;

    }
}
