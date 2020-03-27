package Tasks;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Loader {

    public List<File> files = new ArrayList<>();
    public List<String> filelabels = new ArrayList<>();
    public List<String> fileNames = new ArrayList<>();

    public void getTxtFiles(File file){
        if (!file.isDirectory()){
            String filePath = file.toString();
            if (filePath.endsWith(".txt")){
                files.add(file);
                int idx = filePath.lastIndexOf("/");
                int idx2 = filePath.lastIndexOf("/",idx-1);
                filelabels.add(filePath.substring(idx2+1,idx));
                fileNames.add(filePath.substring(idx+1));
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
        System.out.format("We found %d txt files in total\n",files.size());
        return files;
    }

}
