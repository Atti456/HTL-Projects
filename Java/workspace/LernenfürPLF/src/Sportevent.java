import java.io.IOException;
import java.util.TreeSet;

public class Sportevent
{

    private TreeSet<Athlet> athleten = new TreeSet<>();

    public void printall(){

        for (Athlet sportler:athleten) {
            System.out.println(sportler);
        }




    }

    public  void print()
    {
        Athlet at = new Athlet();
        at.print();
    }

    public  void sportform()
    {
        Athlet at = new Athlet();
        at.sportform();
    }


    public  void importevents(String file) throws IOException {
        CSV at = new CSV();
        at.readCSV(file);
    }

    public  void save( String file) throws IOException {
        Object at = new Object();
        at.save(athleten, file);
    }

    public  void load(String file) throws IOException, ClassNotFoundException {
    Object at = new Object();
    at.load(file);
    }


}
