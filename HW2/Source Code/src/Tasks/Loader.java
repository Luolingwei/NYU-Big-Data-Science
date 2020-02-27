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


    public void getTxtFiles(File file){
        if (!file.isDirectory()){
            if (file.toString().endsWith(".txt")){
                files.add(file);
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
