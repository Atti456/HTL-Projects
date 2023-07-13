import jdk.nashorn.api.tree.Tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class CSV
{
    String s = "";
    String[] elems;

    private TreeSet<Athlet> sportler = new TreeSet<>();

    public TreeSet readCSV(String file) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            br.readLine();

            while(br.readLine() != null)
            {
                elems = br.readLine().split(";");
                sportler.add(new Athlet(elems[0],elems[1],Integer.parseInt(elems[2]),elems[3],Float.parseFloat(elems[4])));
            }
        }

        return sportler;
    }

}
