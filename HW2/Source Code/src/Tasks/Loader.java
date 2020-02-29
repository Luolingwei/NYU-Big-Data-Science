package Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Loader {

    public List<File> files = new ArrayList<>();
    public List<String> fileNames = new ArrayList<>();

    public void getTxtFiles(File file){
        if (!file.isDirectory()){
            String filePath = file.toString();
            if (filePath.endsWith(".txt")){
                files.add(file);
                fileNames.add(filePath.substring(filePath.length()-16));
            }
        } else {
            for (File subfile: file.listFiles()){
                getTxtFiles(subfile);
            }
        }
    }

    public List<File> getFiles(String path){
        files.clear();
        File rootPath = new File(path);
        getTxtFiles(rootPath);
        System.out.format("We found %d txt files in total",files.size());
        return files;
    }

}
