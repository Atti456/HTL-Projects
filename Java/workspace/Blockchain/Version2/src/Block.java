import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Block
{
    private Data data;
    private int hash;
    private int prevHash;
    private Object[] contens;


    public Block() {
    }

    public Block(int prevHash, Data data) {
        this.data = data;
        this.prevHash = prevHash;

        contens = new Object[]{data.hashCode(), prevHash};
        this.hash = Arrays.hashCode(contens);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
        contens[0] = data.hashCode();
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(int prevHash) {
        this.prevHash = prevHash;
        contens[1]=prevHash;
    }


    public void generateHash(){
        this.hash = Arrays.hashCode(contens);
    }
}
