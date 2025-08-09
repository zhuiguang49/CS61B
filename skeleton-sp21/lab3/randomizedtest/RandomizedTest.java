package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomizedTest {

    @Test
    public void testRandomOperations(){
        AListNoResizing<Integer> L_right = new AListNoResizing<>();
        BuggyAList<Integer> L_maywrong = new BuggyAList();
        int n = 5000;
        for(int i = 0; i < n; i++){
            int right_size = L_right.size();
            int mayWrong_size = L_maywrong.size();
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L_right.addLast(randVal);
                L_maywrong.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                System.out.println("size of AListNoResizing: " + right_size);
                System.out.println("size of BuggyAList: " + mayWrong_size);
            } else if (operationNumber == 2){
                // getLast
                if(right_size == 0 && mayWrong_size == 0){
                    continue;
                }
                int getitemOfAListNoResizing = L_right.getLast();
                int getitemOfBuggyList = L_maywrong.getLast();
                assertEquals(getitemOfAListNoResizing,getitemOfBuggyList);
            } else if (operationNumber == 3){
                // removeLast
                if(right_size == 0 && mayWrong_size == 0){
                    continue;
                }
                int removeItemOfAListNoResizing = L_right.removeLast();
                int removeItemOfBuggyAList = L_maywrong.removeLast();
                assertEquals(removeItemOfAListNoResizing,removeItemOfBuggyAList);
            }
        }
    }
}
