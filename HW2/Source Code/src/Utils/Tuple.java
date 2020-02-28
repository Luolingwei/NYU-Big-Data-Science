package Utils;

public class Tuple implements Comparable<Tuple>{

    public String Word;
    public double Tf_Idf;

    public Tuple(String word, double tf_idf){
        this.Word = word;
        this.Tf_Idf = tf_idf;
    }

    @Override
    public int compareTo(Tuple that) {
        if (this.Tf_Idf>that.Tf_Idf){
            return 1;
        } else if (this.Tf_Idf<that.Tf_Idf){
            return -1;
        }
        return 0;
    }
}
