 
public class Triangle {

  public static void main(String[] args) {
    triangle();
  }

  public static void triangle() {

    int trials = 10;
    double[] p = new double[] {0.01, 0.02, 0.03, 0.04, 0.05};
    int n = 1024;
    // generate graph
    for (double prob : p) {
      //System.out.println("PROBABILITY: " + p);
      double numTriangles = 0;
     for (int i = 0; i < trials; i++) {
      int[][] A = new int[n][n];
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
      //System.out.print("graph generated " + i);
      int[][] a3 = Main_Opt2.finalStrassen(Main_Opt2.finalStrassen(A,A),A);

      for (int j = 0; j < n; j++) {
        numTriangles += a3[j][j];
    
      }
    }
      double avgTriangles = numTriangles/6.0/trials;

      
      // compare to expected (1024 choose 3)p^3; 178433024 * p^3
      double expected = 178433024 * Math.pow(prob, 3);
      System.out.println("TRIAL W PROBABILITY: " + prob);
      System.out.println("EXPECTED: " + expected);
      System.out.println("OURS: " + avgTriangles);

      
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