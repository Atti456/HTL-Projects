import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        Data data = new Data();

        data.importData("./Lvl1/text1.txt");
        System.out.println(data.toString());
        System.out.println();
        data.print();
    }
}
