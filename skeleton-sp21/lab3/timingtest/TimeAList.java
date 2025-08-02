package timingtest;
import edu.princeton.cs.algs4.Stopwatch; // 由 StopwatchDemo 得到的启发，可以用来统计时间

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        // 创建用于存储结果的 AList
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        // 不同大小的数据结构
        // int[] count = {1000,2000,4000,8000,16000,32000,64000,128000};
        int[] count = {1000,2000,4000,8000,16000,32000,64000,128000,100000000}; // 测试更大的 `N`

        for(int n : count){
            Stopwatch swtime = new Stopwatch(); // 根据 StopwatchDemo 的示例计时
            // 创建一个空的 AList，用于进行 addLast 操作
            AList<Integer> testList = new AList<>();
            for(int i = 1; i <= n; i++){
                testList.addLast(0);
            }
            double time = swtime.elapsedTime();
            Ns.addLast(n);
            opCounts.addLast(n);
            times.addLast(time);
        }
        printTimingTable(Ns,times,opCounts);

    }
}
