import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Block> chain = new LinkedList<>();
        String[] data = {"first"};
        String[] data2 = {"first2"};

        Block genesis = new Block(0, data);
        chain.add(genesis);
        System.out.println(genesis.getHash());

        for (int i = 0; i < 15; i++) {
            Block newB = new Block(chain.get(i).getHash(), data2);
            chain.add(newB);
            System.out.println("Hash of Block "+ (chain.indexOf(newB)+1) + ": " + newB.getHash());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
