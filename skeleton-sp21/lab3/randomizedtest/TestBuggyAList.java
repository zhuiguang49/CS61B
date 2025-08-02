package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void testThreeAddThreeRemove(){
    AListNoResizing<Integer> correct = new AListNoResizing<>();
    BuggyAList<Integer> buggy = new BuggyAList<>();
    // 分别进行三次 addaLast() 操作
    correct.addLast(4);
    correct.addLast(5);
    correct.addLast(6);

    buggy.addLast(4);
    buggy.addLast(5);
    buggy.addLast(6);

    // 检查三次 removeLast 操作
    assertEquals(correct.removeLast(), buggy.removeLast());
    assertEquals(correct.removeLast(), buggy.removeLast());
    assertEquals(correct.removeLast(), buggy.removeLast());
  }
}
