import java.io.*;
import java.util.Arrays;

public class Data
{
    private File file;
    private String[] elem;

    public Data(){

    }

    public String importData(String path){
        file = new File(path);
        String st = "";

        int i = 0;

        String instruction = "";
        int amount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            instruction = br.readLine();
            amount = Integer.parseInt(br.readLine());

            String[] elems = new String[amount];

            while ((st = br.readLine()) != null) {
                elems[i]=st;
                i++;
            }

            elem = elems;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return st;
    }


    public int parseToInt(String text){
        int numb = 0;

        numb = Integer.parseInt(text);
        return numb;
    }


    @Override
    public String toString() {
        return "Data{" +
                "elem=" + Arrays.toString(elem) +
                '}';
    }

    public void print(){
        for (int i = 0; i < elem.length; i++) {
            System.out.println(elem[i]);
        }
    }
}

