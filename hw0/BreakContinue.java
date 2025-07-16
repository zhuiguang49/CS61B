public class BreakContinue {
  public static void windowPosSum(int[] a, int n) {
    int end = 0;
    int sum;
    for(int i=0;i<a.length;i++){
      sum = 0;
      if(a[i]<0){
        continue;
      }
      /* 下面这部分可能不是课程想要的实现，没有用到 break
      if(i+n>=a.length){
        end = a.length-1;
      }else{
        end = i+n;
      }
      for(int j=i;j<=end;j++){
        sum = sum + a[j];
      }
      a[i] = sum;
      */

     for(int j=i;j<=i+n;j++){
      sum = sum + a[j];
      if(j==a.length-1){
        break;
      }
     }
     a[i] = sum;
    }
  }

  public static void main(String[] args) {
    int[] a = {1, 2, -3, 4, 5, 4};
    int n = 3;
    windowPosSum(a, n);

    // Should print 4, 8, -3, 13, 9, 4
    System.out.println(java.util.Arrays.toString(a));
  }
}