package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ngrams {

    public List<String> ngrams = new ArrayList<>();

    public Ngrams(List<String> words){
        this.ngrams.addAll(words);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ngrams ngrams1 = (Ngrams) o;
        return ngrams.equals(ngrams1.ngrams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ngrams);
    }


}
