import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Block
{
    private String[] data;
    private int hash;
    private int prevHash;

    public Block() {
    }

    public Block(int prevHash, String[] data) {
        this.data = data;
        this.prevHash = prevHash;

        Object[] contens = {Arrays.hashCode(data), prevHash};
        this.hash = Arrays.hashCode(contens);
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
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
    }
}
