 
public class Triangle {

  public static void main(String[] args) {
    
  }

  public static void triangle() {


    double[] p = new double[] {0.01, 0.02, 0.03, 0.04, 0.05};
    int n = 1024;

    for (double prob : p) {
    // based on the probability, create graph size 1024
      int[][] A = new int[n][n]; //todo
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
          if (Math.random() < prob) {
            A[r][c] = 1;
          }
          else {
            A[r][c] = 0;
          }
        }
      }
      
      int[][] a3 = hi.strassen(hi.strassen(A,A),A);
        System.out.println("TRIAL W PROBABILITY: " + prob);
              System.out.println("ours: " + prob);
      System.out.println("expected: " + 178433024 * Math.pow(prob, 3));
      // compare to (1024 c 3)p^3; 178433024 * p^3
      
    }

    
    
  }

//   public static int nCr(int n, int r)
// {
//     return fact(n) / (fact(r) *
//                   fact(n - r));
// }
 
// // Returns factorial of n
// public static int fact(int n)
// {
//     int res = 1;
//     for (int i = 2; i <= n; i++)
//         res = res * i;
//     return res;
// }
  
}