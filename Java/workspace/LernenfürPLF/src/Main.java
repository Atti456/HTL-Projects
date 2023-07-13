import java.io.IOException;

public class Main
{

    public static void main(String[] args) {

        Sportevent sp = new Sportevent();

        try
        {
            sp.importevents("athlethen.csv");
            sp.printall();
            sp.save("sportler.dat");
            sp.load("sportler.dat");
            sp.printall();

            sp.print();
            sp.sportform();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
