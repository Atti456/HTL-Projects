import com.example.battleships_solitaire.Model.Board;

public class Main
{
    public static void main(String[] args) {
        Board board = new Board();

        String path = "D:\\Alex\\Schule\\Diplomarbeit\\easylevels\\";
        //app\src\main\res\raw\
        //Battleships_Solitaire\

        board.generateLevels(6, path);
    }
}
