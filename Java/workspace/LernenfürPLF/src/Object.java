import java.io.*;
import java.util.TreeSet;

public class Object
{

    public void save(TreeSet<Athlet> sportler, String file) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(sportler);
        }
    }

    public TreeSet<Athlet> load(String file) throws IOException, ClassNotFoundException {

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))){
            return (TreeSet<Athlet>) ois.readObject();
        }


    }

}
