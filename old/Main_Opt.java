// Multiplication: Strassen's Algorithm
import java.util.Random;
//Even case

public class Main_Opt {

  static int[][] X;
  static int[][] Y;
  static int N;
  static int C = 0; //crossover point

  //telescoped matrices
  static int[][] p1 = new int[N][N];
  static int[][] p2 = new int[N][N];
  static int[][] p3 = new int[N][N];
  static int[][] p4 = new int[N][N];
  static int[][] p5 = new int[N][N];
  static int[][] p6 = new int[N][N];
  static int[][] p7 = new int[N][N];

  //static int[][] A;
  static int[][] subFH = new int[N][N];
  static int[][] addAB = new int[N][N];
  //static int[][] H;
  static int[][] addCD = new int[N][N];
  //static int[][] E;
  //static int[][] D;
  static int[][] subGE = new int[N][N];
  static int[][] addAD = new int[N][N];
  static int[][] addEH = new int[N][N];
  static int[][] subBD = new int[N][N];
  static int[][] addGH = new int[N][N];
  static int[][] subCA = new int[N][N];
  static int[][] addEF = new int[N][N];

// for cleanliness, should make into object so we can be more flexible!
  public Main_Opt(int[][] x, int[][] y, int n, int c) {
    X = x;
    Y = y;
    N = n;
    C = c;
    // change all arrays to non-static & initialize them to new int[N][N];
  }
  
  // This function will multiply the subset of M1 starting from [r1,c1] with length dim and M2 starting from [r2, c2] with length dim together and put the result in Goal starting from [0, total n - dim]. 
  // first call strassen(X,Y,0,0,0,0,n,X)

  
  public static void strassen(int[][] M1, int[][] M2, int r1, int c1, int r2, int c2, int[][] Goal, int dim) {
       if (dim <= C) {
         standardMM(M1, M2, r1, c1, r2, c2, Goal, dim); 
         return; 
      }
      int half = dim/2;
    if (dim == 1) {
      Goal[dim-1][dim-1] = M1[r1][c1] * M2[r2][c2];
      print(Goal);
      return;
    }
    //A E: r1, c1
    //B F: r1, c1 + half
    //C G: r1 + half, c1
    //D H: r1 + half, c1 + half

    // In order to access any of the sub/add/p elements, we need to get to index: (0, N - 2*dim)
    // UPDATE P1
    //int[][] p1 = strassen(A,sub(F, H),c); 
    sub(M2, M2, r2, c2+half, r2+half, c2+half, subFH, half);
        System.out.println("subFH, halving" + dim);
    print(subFH);
    // for any sub strassen we call, if it's on a sub/add one the indices that go into strassen should be 0 & "off" i think :O

   // strassen(M1, subFH, r1, c1, r2+half, c2+half, p1, half); used to be this
        //QUESTION: why is it r2+half, c+2half? trying:
        int off = N - 2*half; // 0 bc we store them horizontally
    strassen(M1, subFH, r1, c1, 0, off, p1, half);
    System.out.println("p1, halving" + dim);
    print(p1);
    
    //UPDATE P2 
    // int[][] p2 = strassen(add(A,B), H,c); 
    add(M1, M1, r1, c1, r1, c1 + half, addAB, half);
    // r1+half, c1+half for addAB -> 0, off
    strassen(addAB, M2, 0, off, r2+half, c2+half, p2, half);
        System.out.println("p2, halving" + dim);
    print(p2);

    // UPDATE P3
    // int[][] p3 = strassen(add(C,D), E, c); 
    add(M1, M1, r1 + half, c1, r1 + half, c1 + half, addCD, half);
    // changed r1+half, c1+half to 0, off
    strassen(addCD, M2, 0, off, r2+half, c2+half, p3, half);
        System.out.println("p3, halving" + dim);
    print(p3);

    // UPDATE P4
    // int[][] p4 = strassen(D, sub(G,E), c);
    // r2+half, c2+half -> 0, off
    sub(M2, M2, r2 + half, c2, r2, c2, subGE, half);
    strassen(M1, subGE, r1+half, c1+half, 0, off, p4, half);
        System.out.println("p4, halving" + dim);
    print(p4);

    //UPDATE P5
    // int[][] p5 = strassen(add(A,D), add(E,H), c);
    add(M1, M1, r1, c1, r1+half, c1+half, addAD, half);
    add(M2, M2, r2, c2, r2+half, c2+half, addEH, half);
    // r1+half, c1+half, r2+half, c2+half
    strassen(addAD, addEH, 0, off, 0, off, p5, half);
        System.out.println("p5, halving" + dim);
    print(p5);

    //UPDATE P6
    // int[][] p6 = strassen(sub(B,D), add(G,H), c);
    sub(M1, M1, r1, c1+half, r1+half, c1+half, subBD, half);
    add(M2, M2, r2+half, c2, r2+half, c2+half, addGH, half);
    // r1+half, c1+half, r2+half, c2+half
    strassen(subBD, addGH, 0, off, 0, off, p6, half);
        System.out.println("p6, halving" + dim);
    print(p6);

     //UPDATE P7
    // int[][] p7 = strassen(sub(C,A), add(E,F), c); 
    sub(M1, M1, r1+half, c1, r1, c1, subCA, half);
    add(M2, M2, r2, c2, r2, c2+half, addEF, half);
    // r1+half, c1+half, r2+half, c2+half
    strassen(subCA, addEF, 0, off, 0, off, p7, half);
        System.out.println("p7,, halving" + dim);
    print(p7);

    //COMBINE Ps
    int cOffset = N - dim;
    System.out.println("Offset" + cOffset + " " + N);
    // when strassens is first called, dim = N, and cOffset is 0 which is what we want. 
    // next level, dim = N/2, and offset is N/2 which is what we want

    // i will always be 0
    // j will be offset by 
    //top left 
    for (int i = 0; i < half; i++) {
      for (int j = cOffset; j < half; j++) {
        Goal[i][j] = -p2[i][j] + p4[i][j] + p5[i][j] + p6[i][j];
       }
    }
    
    // top right
    for (int i = 0; i < half; i++) {
      for (int j = cOffset; j < half; j++) {
        Goal[i][j+half] = p1[i][j] + p2[i][j];
       }
    }  

    // bottom left
    for (int i = 0; i < half; i++) {
      for (int j = cOffset; j < half; j++) {
        Goal[i+half][j] = p3[i][j] + p4[i][j];
       }
    } 

    // bottom right
    for (int i = 0; i < half; i++) {
      for (int j = cOffset; j < half; j++) {
        Goal[i+half][j+half] = p1[i][j] - p3[i][j] + p5[i][j] + p7[i][j];
       }
    }
  }

  
  // dim is the dimension of our output matrix
  // this function subtracts the subparts of M1 and M2 of dimension dim
  public static void sub(int[][] M1, int[][] M2, int r1, int c1, int r2, int c2, int[][] Goal, int dim) {
    //     System.out.println("SUB START dim " + dim);
    // print(M1);
    // print(M2);
    // System.out.println(r1 + " " + c1);
    //     System.out.println(r1 + " " + c2);

    // Treat origin to be (0,N - 2*dim) for goal
    int cOffset = N - 2*dim;
    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        Goal[r][c + cOffset] = M1[r+r1][c+c1] - M2[r+r2][c+c2];
      }
    }
    //     System.out.println("subtracted answer: "); 
    // String g = print(Goal);
    //  System.out.println("END SUB");
  }

  // dim is the dimension of our output matrix
  public static void add(int[][] M1, int[][] M2, int r1, int c1, int r2, int c2, int[][] Goal, int dim) {
    // Treat origin to be (0,N - 2*dim) for goal
    int cOffset = N - 2*dim;
    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        Goal[r][c + cOffset] = M1[r+r1][c+c1] + M2[r+r2][c+c2];
      }
    }
  }

// c is the crossover point
  
  //Simple implementation
  public static void standardMM(int[][] M1, int[][] M2, int r1, int c1, int r2, int c2, int[][] Goal, int dim) {
    int cOffset = N - dim;
    for (int r = 0; r < dim; r++) {
      for (int c = 0; c < dim; c++) {
        //sum of every element in n's row r and element 
        for (int i = 0; i < dim; i++) {
          Goal[r][c+cOffset] += M1[r+r1][i+c1]*M2[i+r2][c+c2];
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

  public static void initialize(int n) {
    N = n;
    p1 = new int[N][N];
    p2 = new int[N][N];
    p3 = new int[N][N];
    p4 = new int[N][N];
    p5 = new int[N][N];
    p6 = new int[N][N];
    p7 = new int[N][N];

    subFH = new int[N][N];
    addAB = new int[N][N];
    addCD = new int[N][N];
    subGE = new int[N][N];
    addAD = new int[N][N];
    addEH = new int[N][N];
    subBD = new int[N][N];
    addGH = new int[N][N];
    subCA = new int[N][N];
    addEF = new int[N][N];
  }

  public static void correctness() {
      X = new int[][] {
      {1,3,5,1},
      {2,3,1,2},
      {1,4,2,4},
      {0,4,1,5},
    };
      Y = new int[][] {
      {1,2,3,5},
      {2,3,1,0},
      {1,4,3,2},
      {2,1,0,5},
    };
   // N = Y.length; 

    initialize(Y.length);
    
    //System.out.println("Strassen's odd");
    //strassen(X,Y,0,0,0,0,X,N);
    //print(X);
    System.out.println("Regular MM odd");
    standardMM(X,Y,0,0,0,0,X,N);
    print(X);
  }

  public static void test() {
    int trials = 10;
    int[] ns = {11, 100, 520, 1024};
    
    for(int c = 14; c < 100; c += 2) {
      int total_time = 0;
      for(int n : ns) {//(int n = 11; n < 1200; n += 201) {
        N = n;
        double avg = 0;
        for(int i = 0; i < trials; i++){
          long startTime = System.nanoTime();
          X = generate(n);
          Y = generate(n);
          strassen(X,Y,0,0,0,0,X,N);
          
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
    //test();
    //correctness();
          X = new int[][] {
      {1,3,5,1},
      {2,3,1,2},
      {1,4,2,4},
      {0,4,1,5},
    };
      Y = new int[][] {
      {1,2,3,5},
      {2,3,1,0},
      {1,4,3,2},
      {2,1,0,5},
    };
    //     N = 4;
    //     int[][] ans = new int[4][4];
    // initialize(4);
    //  strassen(A, B,  0,  0,  0, 0, ans, 4);
              X = new int[][] {
      {1,3},
      {2,3},
    };
      Y = new int[][] {
      {3,5},
      {1,0},
    };
    N = 2;
    int[][] ans = new int[N][N];
    initialize(N);
    strassen(X, Y,  0, 0, 0, 0, ans, N);
    //sub(X, Y, 0, 0, 0, 0, ans, N/2);
    print(ans);
    //     int[][] subAns = new int[4][4];
    // sub(A, B,  2,  0,  2,  0, subAns,  2);
    

        //int[][] ans = new int[1][1];
//     initialize(1);
//       X = new int[][] {
//       {5},
//     };
//       Y = new int[][] {
//       {10},
//     };
// // 
//      strassen(X, Y,  0,  0,  0, 0, ans, 1);
    
  }

  
}