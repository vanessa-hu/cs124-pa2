// Multiplication: Strassen's Algorithm
import java.util.Random;
//Even case

public class hi_optimizations {

  public int[][] X;
  public int[][] Y;

  int curr_size;
  int[][] p1 = 

  public static int[][] strassen(int[][] X, int[][] Y) {
    // default 10, can change later
    return strassen(X, Y, 10);
  }
// c is the crossover point
  // n is the current size of the subset we are trying to apply strassens to. 
  // X_r and X_c are the coordinates for the top left part of the box we are currently looking at in X
  // 
  public static int[][] strassen(int[][] X, int[][] Y, int c) {
    // must be same length; assume always square
    int n = X.length;
    int m = Y.length;

    
    if (n != m) return null;
    if (n <= c) return standardMM(X,Y);
    boolean isOdd = n % 2 == 1;
    int half = n/2; // floor
    int hSize = n/2 + (isOdd ? 1 : 0); 
    int nSize = n + (isOdd ? 1 : 0); 
    
    int[][] ans = new int[n][n];

    // creating all submatrices of X (ABCD) and Y (EFGH)
    Subset A = new Subset(X, 0, 0, hSize, hSize){};
    Subset B = new Subset(X, 0, hSize, hSize, nSize){};
    Subset C = new Subset(X, hSize, 0, nSize, hSize){};
    Subset D = new Subset(X, hSize, hSize, nSize, nSize){};

    Subset E = new Subset(Y, 0, 0, hSize, hSize){};
    Subset F = new Subset(Y, 0, hSize, hSize, nSize){};
    Subset G = new Subset(Y, hSize, 0, nSize, hSize){};
    Subset H = new Subset(Y, hSize, hSize, nSize, nSize){};
      
    if (n == 5) {
    System.out.println("CHECKING QUARTERS");
    print(A.get());
    print(B.get());
    print(C.get());
    print(D.get());
      }
    // strassen subproblems

    int[][] p1 = strassen(A.get(),sub(F.get(), H.get()),c); 
    int[][] p2 = strassen(add(A.get(),B.get()), H.get(),c); 
    int[][] p3 = strassen(add(C.get(),D.get()), E.get(), c); 
    int[][] p4 = strassen(D.get(), sub(G.get(),E.get()), c);
    int[][] p5 = strassen(add(A.get(),D.get()), add(E.get(),H.get()), c);
    int[][] p6 = strassen(sub(B.get(),D.get()), add(G.get(),H.get()), c); 
    int[][] p7 = strassen(sub(C.get(),A.get()), add(E.get(),F.get()), c); 
    
   // populate top left
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        ans[i][j] = -p2[i][j] + p4[i][j] + p5[i][j] + p6[i][j];
       }
    }
    // top right
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < half; j++) {
        ans[i][j+hSize] = p1[i][j] + p2[i][j];
       }
    }  

    // bottom left
    for (int i = 0; i < half; i++) {
      for (int j = 0; j < hSize; j++) {
        ans[i+hSize][j] = p3[i][j] + p4[i][j];
       }
    } 


    // bottom right

    for (int i = 0; i < half; i++) {
      for (int j = 0; j < half; j++) {
        ans[i+hSize][j+hSize] = p1[i][j] - p3[i][j] + p5[i][j] + p7[i][j];
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

  public static void correctness() {
    int[][] x = new int[][] {
      {1,3},
      {2,3},
    };
            int[][] y = new int[][] {
      {1,5,},
      {2,1,},
    };
    print(x);
    print(y);
    System.out.println("Strassen's 2x2");
    print(strassen(x,y,1));
    System.out.println("Regular MM 2x2");
    print(standardMM(x,y));
    int[][] evenA = new int[][] {
      {1,3,5,1},
      {2,3,1,2},
      {1,0,2,4},
      {1,3,2,1},
    };
        int[][] evenB = new int[][] {
      {1,3,5,1},
      {2,3,1,1},
      {1,4,2,0},
      {2,1,0,3}
    };
    // should be below; strassen's is wrong currently
//     [14, 33, 18, 7
// 13, 21, 15, 11
// 11, 15, 9, 13
// 11, 21, 12, 7
    print(evenA);
    print(evenB);
    System.out.println("Strassen's even");
    print(strassen(evenA,evenB,1));
    System.out.println("Regular MM even");
    print(standardMM(evenA,evenB));


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
    print(oddA);
    print(oddB);
    System.out.println("Strassen's odd");
    print(strassen(oddA,oddB,1));
    System.out.println("Regular MM odd");
    print(standardMM(oddA,oddB));
  }

  public static void test() {
    int trials = 10;
    int[] ns = {16, 100, 520, 1024};
    
    for(int c = 10; c < 100; c += 2) {
      int total_time = 0;
      for(int n : ns) {//(int n = 11; n < 1200; n += 201) {
        double avg = 0;
        for(int i = 0; i < trials; i++){
          long startTime = System.nanoTime();
          
          strassen(generate(n), generate(n), c);
          
          long endTime = System.nanoTime(); 
          long millisElapsed = (endTime - startTime) / 1000000;
          avg += millisElapsed;
          total_time += millisElapsed;
          //System.out.println("Trial: " + i + " n: " + n + " time: " + millisElapsed);
        }
        //System.out.println("Average for " + n + " trials, c:" + c + " avg: " + avg/trials);
      }
      System.out.println("Crossover: " + c + " " + total_time/trials/ns.length);
    }    
  }

  public static void main(String[] args) {
    // Testing the functions
    //test();
    correctness();
  }

  
}