package Utils;

public class Tuple implements Comparable<Tuple>{

    public int WordId;
    public double Tf_Idf;

    public Tuple(int word, double tf_idf){
        this.WordId = word;
        this.Tf_Idf = tf_idf;
    }

    @Override
    public int compareTo(Tuple that) {
        if (this.Tf_Idf>that.Tf_Idf){
            return 1;
        } else if (this.Tf_Idf<that.Tf_Idf){
            return -1;
        } else {
            return Integer.compare(this.WordId,that.WordId);
        }
    }
}
