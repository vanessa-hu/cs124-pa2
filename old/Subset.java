public class Subset {
  int start_r; 
  int start_c;
  int end_r; 
  int end_c;

  int[][] full;

  // end_r and end_c are not inclusive 
  public Subset(int[][] X, int start_r, int start_c, int end_r, int end_c) {
    this.start_r = start_r;
    this.start_c = start_c;
    this.end_r = end_r;
    this.end_c = end_c;

    this.full = X;
  }

  public int[][] get() {
    int[][] A = new int[end_r - start_r][end_c - start_c];
    for (int r = 0; r < end_r - start_r; r++) {
      for (int c = 0; c < end_c - start_c; c++) {
        A[r][c] = full[r][c];
       }
    }
    return A;
  }
  
}