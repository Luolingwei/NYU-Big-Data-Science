package Loader;
import com.csvreader.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

//    public List<String> Tweets = new ArrayList<>();
    public List<String> textList = new ArrayList<>();


    public void read(String filePath){

        try {
            CsvReader csvReader = new CsvReader(filePath);

            // set header
            String[] header = new String[]{"target","ids","date","flag","user","text"};
            csvReader.setHeaders(header);
            while (csvReader.readRecord()){
                // get column "text"
                textList.add(csvReader.get("text"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
