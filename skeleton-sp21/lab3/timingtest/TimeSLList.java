package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        // 用作 printTimingTable 参数的三个 AList
        AList<Integer> N = new AList<>();
        AList<Double> time = new AList<>();
        AList<Integer> opcount = new AList<>();

        int[] Narray = {1000,2000,4000,8000,16000,32000,64000,128000};
        for(int n : Narray){
            SLList<Integer> list = new SLList<>();
            for(int i = 0; i < n; i++){
                list.addLast(0);
            }
            int M = 10000;
            opcount.addLast(M);
            N.addLast(n);
            Stopwatch sw = new Stopwatch();
            for(int k = 0; k < M; k++){
                list.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            time.addLast(timeInSeconds);
        }
        printTimingTable(N,time,opcount);

    }

}
