// Multiplication: Strassen's Algorithm
import java.util.Random;
//Even case

public class Strassen_2 {

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
    System.out.println("HSIZE: " + hSize);
    int[][] ans = new int[n][n];

    // This is a potential optimization, but adding an extra set of 0s
    // creating all submatrices of X (ABCD) and Y (EFGH)
    int[][] A = new int[hSize][hSize];
    int[][] E = new int[hSize][hSize];
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        A[i][j] = X[i][j];
        E[i][j] = Y[i][j];
       }
    }
    int[][] B = new int[hSize][hSize];
    int[][] F = new int[hSize][hSize];

    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < half; j++) {
        B[i][j] = X[i][j+half]; // 
        F[i][j] = Y[i][j+half];
       }
    }

    int[][] C = new int[hSize][hSize];
    int[][] G = new int[hSize][hSize];

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < hSize; j++) {
        C[i][j] = X[i+hSize][j];
        G[i][j] = Y[i+hSize][j];
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

    System.out.println("A: " + print(A) + "B: " + print(B));
    System.out.println("C: " + print(C) + "D: " + print(D));

    System.out.println("E: " + print(E) + "F: " + print(F));
    System.out.println("G: " + print(G) + "H: " + print(H));
    
    // strassen subproblems
    int[][] p1 = strassen(A,sub(F, H)); 
    int[][] p2 = strassen(add(A,B), H); 
    int[][] p3 = strassen(add(C,D), E); 
    int[][] p4 = strassen(D, sub(G,E));
    int[][] p5 = strassen(add(A,D), add(E,H));
    int[][] p6 = strassen(sub(B,D), add(G,H)); 
    int[][] p7 = strassen(sub(C,A), add(E,F));

    System.out.println("p1" + print(p1));
    System.out.println("p2" + print(p2));
    System.out.println("p3" + print(p3));
    System.out.println("p4" + print(p4));
    System.out.println("p5" + print(p5));
    System.out.println("p6" + print(p6));
    System.out.println("p7" + print(p7));
    
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
        C[i][j] = A[i][j] + B[i][j];
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
        C[i][j] = A[i][j] - B[i][j];
      }
    }

    return C;
  }

  public static String print(int[][] A) {
    String toReturn = "[";
    for (int i = 0; i < A.length; i++) {
      for (int j = 0; j < A[0].length - 1; j++) {
        toReturn += A[i][j];
        toReturn += ", ";
      }
      toReturn += A[i][A[0].length - 1];
      toReturn += "\n";
    }
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
    //test();

    /*    int[][] x = new int[][] {
      {1,3,5},
      {2,3,5},
      {2,3,5},
    };
            int[][] y = new int[][] {
      {1,5,5},
      {2,1,5},
      {2,3,5},
    };

    System.out.println("Strassen's 2x2");
    System.out.println(print(x) + print(y));
    System.out.println(print(strassen(x,y,1)));
    System.out.println("Regular MM 2x2");
    System.out.println(print(standardMM(x,y)));*/
    
    int[][] oddA = new int[][] {
      {1,3,5,1,2},
      {2,3,1,2,3},
      {1,4,2,4,1},
      {0,4,1,5,2},
      {1,0,2,3,1},
    };
        int[][] oddB = new int[][] {
      {1,2,3,5,1},
      {2,3,1,0,1},
      {1,4,3,2,0},
      {2,1,0,5,3},
      {2,6,1,0,3}
    };
    System.out.println("Strassen's odd");
    System.out.println(print(strassen(oddA,oddB,1)));
    System.out.println("Regular MM odd");
    System.out.println(print(standardMM(oddA,oddB)));
  }  
}