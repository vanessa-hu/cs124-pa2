// Multiplication: Strassen's Algorithm
import java.util.Random;
//Even case

public class Strassen {

  public static int[][] strassen(int[][] X, int[][] Y) {
    return strassen(X, Y, 10);
  }
// c is the crossover point
  public static int[][] strassen(int[][] X, int[][] Y, int c) {
    // must be same length; assume always square
    int n = X.length;

    int m = Y.length;

    
    if (n != m) return null;
    if (n <= c) return standardMM(X,Y);
    boolean isOdd = n % 2 == 1;
    int half = n/2; // floor
    int hSize = n/2 + (isOdd ? 1 : 0); //
    int[][] ans = new int[n][n];

     //  0 1 2 3 4 5
    //  1
    //  2
    //  3
    //  4
    //  5
    // A B
    // C D

    // creating all submatrices of X (ABCD) and Y (EFGH)
    int[][] A = new int[hSize][hSize];
    int[][] E = new int[hSize][hSize];
    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        A[i][j] = X[i][j];
        E[i][j] = Y[i][j];
       }
    }
    int[][] B = new int[hSize][hSize];
    int[][] F = new int[hSize][hSize];

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        B[i][j] = X[i][j+half]; // 
        F[i][j] = Y[i][j+half];
       }
    }

    int[][] C = new int[hSize][hSize];
    int[][] G = new int[hSize][hSize];

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        C[i][j] = X[i+half][j];
        G[i][j] = Y[i+half][j];
       }
    }

    int[][] D = new int[hSize][hSize];
    int[][] H = new int[hSize][hSize];

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        D[i][j] = X[i+half][j+half];
        H[i][j] = Y[i+half][j+half];
       }
    }
    // strassen subproblems
    int[][] p1 = strassen(A,sub(F, H)); 
    int[][] p2 = strassen(add(A,B), H); 
    int[][] p3 = strassen(add(C,D), E); 
    int[][] p4 = strassen(D, sub(G,E)); ; 
    int[][] p5 = strassen(add(A,D), add(E,H)); ; 
    int[][] p6 = strassen(sub(B,D), add(G,H)); 
    int[][] p7 = strassen(sub(C,A), add(E,F)); 
    
   // populate top left
    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        ans[i][j] = -p2[i][j] + p4[i][j] + p5[i][j] + p6[i][j];
       }
    }
    // top right
    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        ans[i][j+half] = p1[i][j] + p2[i][j];
       }
    }  

    // bottom left
    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        ans[i+half][j] = p3[i][j] + p4[i][j];
       }
    } 


    // bottom right

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        ans[i+half][j+half] = p1[i][j] - p3[i][j] + p5[i][j] + p7[i][j];
       }
    }  
    return ans;
  }

  //Simple implementation
  public static int[][] standardMM(int[][] A, int[][] B) {
    int[][] toReturn = new int[A.length][A.length];
    for (int r = 0; r < A.length; r++) {
      for (int c = 0; c < A.length; c++) {
        //sum of every element in n's row r and element 
        toReturn[r][c] = 0;
        for (int i = 0; i < A.length; i++) {
          toReturn[r][c] += A[r][i]*B[i][c];
        }
      }
    }
    return toReturn;
  }

  public static int[][] add(int[][] A, int[][] B) {
    // assume same dimensions
    // modifying A?
    int r = A.length;
    int c = A[0].length;
    int[][] C = new int[r][c];
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        C[i][j] = A[i][j] - B[i][j];
      }
    }

    return C;
  }
  
  public static int[][] sub(int[][] A, int[][] B) {
    // assume same dimensions
    // modifying A?
    int r = A.length;
    int c = A[0].length;
    int[][] C = new int[r][c];
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        C[i][j] = A[i][j] + B[i][j];
      }
    }

    return C;
  }

  public static String print(int[][] A) {
    String toReturn = "[";
    for (int i = 0; i < A.length; i++) {
      for (int j = 0; j < A.length - 1; j++) {
        toReturn += A[i][j];
        toReturn += ", ";
      }
      toReturn += A[i][A.length - 1];
      toReturn += "\n";
    }
    System.out.println(toReturn);
    return toReturn;
  }

  public static int[][] generate(int n) {
    int[] intArray = {0, 1, -1};
    int[][] toReturn = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int e = new Random().nextInt(intArray.length);
        toReturn[i][j] = e;
      }
    }
    return toReturn;
  }

  public static void test() {
    int trials = 10;
    int[] ns = {10, 100, 520, 1024};
    for(int n : ns) {
      double avg = 0;
      for(int i = 0; i < trials; i++){
        long startTime = System.nanoTime();
        
        strassen(generate(n), generate(n));
        
        long endTime = System.nanoTime(); 
        long millisElapsed = (endTime - startTime) / 1000000;
        avg += millisElapsed;
        System.out.println("Trial: " + i + " n: " + n + " time: " + millisElapsed);
      }
      System.out.println("Average for " + n + " :" + avg/trials);
    }    
  }

  public static void main(String[] args) {
    // Testing the functions
    test();
  }

  
}