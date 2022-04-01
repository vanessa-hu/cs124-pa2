// Multiplication: Strassen's Algorithm
import java.util.Random;
import java.util.Arrays;
//Even case


public class Main_Opt2 {

    public static int[][] finalStrassen(int[][] X, int[][] Y) {
    return finalStrassen(X, Y, 15); // calculated one as default
  }

  public static int[][] finalStrassen(int[][] X, int[][] Y, int c) {
    // optionally pads with zeroes
    X = padZeros(X);
    Y = padZeros(Y);
    int size = X.length;
    int[][] Goal = new int[size][size];
    strassen(X,Y,c,Goal);
    return Goal;
  }

    public static int[][] finalStandard(int[][] X, int[][] Y) {
    int size = X.length;
    int[][] Goal = new int[size][size];
    standardMM(X,Y,Goal);
    return Goal;
  }
// c is the crossover point
  public static void strassen(int[][] X, int[][] Y, int c, int[][] Goal) {
    // must be same length; assume always square
    int n = X.length;
    int m = Y.length;

    if (n != m) return;
    if (n <= c){  standardMM(X,Y,Goal); return; }
    if (n == 1) {
      Goal[0][0] = X[0][0] * Y[0][0];
    }
    boolean isOdd = n % 2 == 1;
    int half = n/2; // floor
    int hSize = n/2 + (isOdd ? 1 : 0); 

    // strassen subproblems
    int[][] left = new int[half][half];
    int[][] right = new int[half][half];
    int[][] subAns = new int[half][half];

    //populate right with subtract
    //sub(F, H, right);    
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        //populate with F - H
        right[i][j] = Y[i][j+hSize] - Y[i+hSize][j+hSize];
        //populate with A
        left[i][j] = X[i][j];
       }
    }
    //calculate p1
    strassen(left, right, c, subAns); 
    //populate ans (add p1) in top right and bottom right
    populateAns(0,half,half,Goal,subAns,false);
    populateAns(half,half,half,Goal,subAns,false);

    // clear subAns
    clear(subAns);
    //int[][] p2 = strassen(add(A,B), H,c); 
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        //populate with F - H
        right[i][j] = Y[i+hSize][j+hSize];
        //populate with A
        left[i][j] = X[i][j] + X[i][j+hSize];
       }
    }

    strassen(left, right, c, subAns); 
    populateAns(0,0,half,Goal,subAns,true);
    populateAns(0,half,half,Goal,subAns,false);
    clear(subAns);
    //int[][] p3 = strassen(add(C,D), E, c); 
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        left[i][j] = X[i+hSize][j] + X[i+hSize][j+hSize];
        right[i][j] = Y[i][j];
       }
    }

    strassen(left, right, c, subAns); 
    populateAns(half,0,half,Goal,subAns,false);
    populateAns(half,half,half,Goal,subAns,true);
    clear(subAns);
    //int[][] p4 = strassen(D, sub(G,E), c);
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        left[i][j] = X[i+hSize][j+hSize];
        right[i][j] = Y[i+hSize][j] - Y[i][j];
       }
    }

    strassen(left, right, c, subAns); 
    populateAns(half,0,half,Goal,subAns,false);
    populateAns(0,0,half,Goal,subAns,false);
    clear(subAns);
    //int[][] p5 = strassen(add(A,D), add(E,H), c);
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        left[i][j] = X[i][j] + X[i+hSize][j+hSize];
        right[i][j] = Y[i][j] + Y[i+hSize][j+hSize];
       }
    }

    strassen(left, right, c, subAns); 
    populateAns(half,half,half,Goal,subAns,false);
    populateAns(0,0,half,Goal,subAns,false);
    clear(subAns);
    //int[][] p6 = strassen(sub(B,D), add(G,H), c); 
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        left[i][j] = X[i][j+hSize] - X[i+hSize][j+hSize];
        right[i][j] = Y[i+hSize][j] + Y[i+hSize][j+hSize];
       }
    }

    strassen(left, right, c, subAns); 
    populateAns(0,0,half,Goal,subAns,false);
    clear(subAns);
    //int[][] p7 = strassen(sub(C,A), add(E,F), c); 
    for (int i = 0; i < hSize; i++) {
      for (int j = 0; j < hSize; j++) {
        left[i][j] = X[i+hSize][j] - X[i][j];
        right[i][j] = Y[i][j] + Y[i][j+hSize];
       }
    }
    strassen(left, right, c, subAns); 
    populateAns(half,half,half,Goal,subAns,false);
  }

  public static void clear(int[][] M) {
    int n = M.length;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        M[i][j] = 0;
       }
    }
  }

  public static int[][] padZeros(int[][] M) {
    //assume square
    int size = M.length;
    int twoPower = 1;
    while (size > twoPower) twoPower *= 2;
    if (size == twoPower) return M;

    int[][] ans = new int[twoPower][twoPower];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        ans[i][j] = M[i][j];
      }
    }
    return ans;
  }

  public static void populateAns(int r, int c, int dim, int[][] goal, int[][] answer, boolean negate) {
    //take answer matrix and populate ans in the position.
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        if (negate) {
          goal[i+r][j+c] -= answer[i][j];
        }
        else {
          goal[i+r][j+c] += answer[i][j];
        }
       }
    }  
  }


  public static void standardMM(int[][] A, int[][] B, int[][] goal) {
    int size = A.length;
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        //sum of every element in n's row r and element 
        goal[r][c] = 0;
        for (int i = 0; i < size; i++) {
          goal[r][c] += A[r][i]*B[i][c];
        }
      }
    }
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
    //int[] intArray = {0, 1, -1, 2, 3, 4, 5};
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
      {3,5},
      {1,0},
    };

    int N = x.length;
    int[][] goal = new int[N][N];

    System.out.println("Strassen's 2x2");
    //strassen(x,y,1,goal);
    //print(goal);
    goal = new int[N][N];
    //System.out.println("Regular MM 2x2");
    //standardMM(x,y,goal);
    //print(goal);
    
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
    N = evenB.length;
    goal = new int[N][N];
    // should be below;
//     [14, 33, 18, 7
    // 13, 21, 15, 11
    // 11, 15, 9, 13
// 11, 21, 12, 7
    System.out.println("Strassen's even");
    //strassen(evenA,evenB,1,goal);
    //print(goal);
    System.out.println("Regular MM even");
    //standardMM(evenA,evenB,goal);
    //print(goal);
    
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
    //print(strassen(oddA,oddB,1));
    System.out.println("Regular MM odd");
    //print(standardMM(oddA,oddB));

    int len = 289;
    int[][] X = generate(len);
    int[][] Y = generate(len);
    goal = new int[len][len];

    System.out.println("Strassen's odd");
    int[][] goal1 = finalStrassen(X,Y,1);
    //goal = new int[7][7];
    X = padZeros(X);
    Y = padZeros(Y);

    //goal = new int[X.length][X.length];

    System.out.println("Regular MM odd");
    goal = padZeros(goal);
    standardMM(X,Y,goal);

    System.out.println("DID IT WORK? " + (Arrays.deepEquals(goal1, goal)));
  }
  
  public static void test() {
    int trials = 10;
    int[] ns = {128, 256, 512, 1024};
    
    for(int c = 15; c < 200; c += 5) {
      int total_time = 0;
      for(int n : ns) {//(int n = 11; n < 1200; n += 201) {
        double avg = 0;
        for(int i = 0; i < trials; i++){
          long startTime = System.nanoTime();
          finalStrassen(generate(n), generate(n), c);
          
          long endTime = System.nanoTime(); 
          long millisElapsed = (endTime - startTime) / 1000000;
          avg += millisElapsed;
          total_time += millisElapsed;
          //System.out.println("Trial: " + i + " n: " + n + " time: " + millisElapsed);
        }
        System.out.println("Average for " + n + " size, crossover:" + c + " avg: " + avg/trials);
      }
      System.out.println("CROSSOVER " + c + ": time in millis " + total_time/trials/ns.length);
    }    
  }

  public static void main(String[] args) {
    // Testing the functions
   // test();
    testOdd();
    testEven();
    //correctness();
  }

  public static void testEven() {
    int trials = 3;
    int[] ns = {128, 256, 512, 1024};
    
    for(int c = 16; c < 150; c += 4) {
      int total_time = 0;
      for(int n : ns) {//(int n = 11; n < 1200; n += 201) {
        double avg = 0;
        for(int i = 0; i < trials; i++){
          long startTime = System.nanoTime();
          finalStrassen(generate(n), generate(n), c);
          
          long endTime = System.nanoTime(); 
          long millisElapsed = (endTime - startTime) / 1000000;
          avg += millisElapsed;
          total_time += millisElapsed;
          //System.out.println("Trial: " + i + " n: " + n + " time: " + millisElapsed);
        }
        System.out.println("Average for " + n + " size, crossover:" + c + " avg: " + avg/trials);
      }
      System.out.println("CROSSOVER " + c + ": time in millis " + total_time/trials/ns.length);
    }    
  }

  public static void testOdd() {
    int trials = 5;
    //int[] ns = {121, 249, 505, 1017};
    int[] ns = {4000, 2000, 1000};
    
    for(int c = 16; c < 256; c *= 2) {
      int total_time = 0;
      for(int n : ns) {//(int n = 11; n < 1200; n += 201) {
        double avg = 0;
        for(int i = 0; i < trials; i++){
          long startTime = System.nanoTime();
          finalStrassen(generate(n), generate(n), c);
          
          long endTime = System.nanoTime(); 
          long millisElapsed = (endTime - startTime) / 1000000;
          avg += millisElapsed;
          total_time += millisElapsed;
          //System.out.println("Trial: " + i + " n: " + n + " time: " + millisElapsed);
        }
        System.out.println("Average for " + n + " size, crossover:" + c + " avg: " + avg/trials);
      }
      System.out.println("CROSSOVER " + c + ": time in millis " + total_time/trials/ns.length);
    }    
  }

  
}