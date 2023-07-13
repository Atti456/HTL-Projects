import java.util.LinkedList;
import java.util.List;

public class ChainManagement
{
    private Data data = new Data("Franz", "Rüdiger", 10);
    private Data data2 = new Data("Franz", "Herbert", 100);
    private List<Block> chain = new LinkedList<>();
    private boolean bool = true;

    public void init() {
        Block genesis = new Block(0, data);
        chain.add(genesis);
        System.out.println(genesis.getHash());

        for (int i = 0; i < 15; i++) {
            System.out.println("Creating Block Number: "+(i+2));
            Block newB = new Block(chain.get(i).getHash(), data2);  //Blocknumber = i+2

            if (i == 4 && bool) {
                System.out.println("insert Error");
                chain.get(i).setData(new Data("Rüdiger", "Herbert", 1));
                chain.get(i).generateHash();

                bool = false;
            }


            if (check(newB)) {   //Check if Previous hash of new Block is Hash of Previous Block in Chain
                chain.add(newB);
                System.out.println("Block with Index: " + (chain.indexOf(newB) + 1) + " has been added");
            } else {
                System.out.println("Error - Hash of Block: " + chain.size() +
                        " and Previous Hash of Block with Index: " + (chain.size() + 1) +
                        " are not equal. Block was not added to the chain");
                i--;
            }

            System.out.println("----------------------------");
            System.out.println();

        }
    }


    private void sleep(int millies) {
        try {
            Thread.sleep(millies);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean check(Block newB) {
        boolean state = false;
        System.out.println("Checking...");

        for (int i = 0; i < chain.size(); i++) {
            if (i==0){
                if (chain.get(i).getPrevHash() == 0){
                    state=true;
                }
            }else if((i+1) == chain.size()){
                if (newB.getPrevHash() == chain.get(chain.size()-1).getHash()){
                    state=true;
                }else state=false;
            }else {
                if (chain.get(i).getPrevHash() == chain.get(i-1).getHash()){
                    state=true;
                }else {
                    state=false;
                }
            }

        }

        sleep(2000);    //sleep for millies

        return state;
    }


    public Block generateBlock(){
        return new Block(chain.get((chain.size()-1)).getHash(), data2);
    }
}
