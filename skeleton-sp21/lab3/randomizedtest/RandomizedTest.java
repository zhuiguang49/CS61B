package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomizedTest {

    @Test
    public void testRandomOperations(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        int n = 500;
        for(int i = 0; i < n; i++){
            int size = L.size();
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                System.out.println("size: " + size);
            } else if (operationNumber == 2){
                // getLast
                if(size == 0){
                    continue;
                }
                int getitem = L.getLast();
                System.out.println("getLast(" + getitem + ")");
            } else if (operationNumber == 3){
                // removeLast
                if(size == 0){
                    continue;
                }
                int item = L.removeLast();
                System.out.println("RemoveLast(" + item + ")");
            }
        }
    }

}
