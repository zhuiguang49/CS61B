package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import tester.ArrayDequeSolution;
import edu.princeton.cs.introcs.StdRandom;

public class TestArrayDequeEC {

    private static final int TEST_TIMES = 10000;
    private static final int OPERATIONS_PER_TEST = 100;

    @Test
    public void testRandomOperations() {
        for (int t = 0; t < TEST_TIMES; t++) {
            StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
            ArrayDequeSolution<Integer> correctDeque = new ArrayDequeSolution<>();
            StringBuilder operations = new StringBuilder();

            for (int i = 0; i < OPERATIONS_PER_TEST; i++) {
                double rand = StdRandom.uniform();
                int value = StdRandom.uniform(1000);

                if (rand < 0.25) {
                    // addFirst
                    studentDeque.addFirst(value);
                    correctDeque.addFirst(value);
                    operations.append("addFirst(").append(value).append(")\n");
                } else if (rand < 0.5) {
                    // addLast
                    studentDeque.addLast(value);
                    correctDeque.addLast(value);
                    operations.append("addLast(").append(value).append(")\n");
                } else if (rand < 0.75 && !studentDeque.isEmpty()) {
                    // removeFirst
                    Integer sVal = studentDeque.removeFirst();
                    Integer cVal = correctDeque.removeFirst();
                    operations.append("removeFirst()\n"); // 只记录操作，不记录返回值
                    assertEquals(operations.toString(), cVal, sVal);
                } else if (!studentDeque.isEmpty()) {
                    // removeLast
                    Integer sVal = studentDeque.removeLast();
                    Integer cVal = correctDeque.removeLast();
                    operations.append("removeLast()\n"); // 只记录操作，不记录返回值
                    assertEquals(operations.toString(), cVal, sVal);
                }

                // 测试size
                int sSize = studentDeque.size();
                int cSize = correctDeque.size();
                assertEquals(operations.toString(), cSize, sSize);

                // 测试get方法
                if (sSize > 0 && StdRandom.uniform() < 0.3) {
                    int index = StdRandom.uniform(sSize);
                    Integer sGet = studentDeque.get(index);
                    Integer cGet = correctDeque.get(index);
                    operations.append("get(").append(index).append(")\n"); // 只记录操作，不记录返回值
                    assertEquals(operations.toString(), cGet, sGet);
                }
            }
        }
    }
}