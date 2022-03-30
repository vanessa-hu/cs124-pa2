import java.io.IOException;
//import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;


public class Strassen {
  // this will be the final one

  public static void main(String[] args) throws IOException {
    // flag dimension inputFile

     if (args.length == 3) {
  
        System.out.println("flag " + args[0]);
       int dim;
        try {
            dim = Integer.parseInt(args[1]);
       System.out.println("dim " + dim);
                  int[][] A = new int[dim][dim];
       int[][] B = new int[dim][dim];
       // reading in new file
          FileReader fileReader = new FileReader(args[2]);
          BufferedReader bufferReader = new BufferedReader(fileReader);
          int count = 0;
          int entries = (int)Math.pow(dim,2);
          int r = 0;
          int c = 0;
          String line = "";

          // matrix A.
          while(count < entries){
            line = bufferReader.readLine();
            int val = Integer.parseInt(line);
            A[r][c] = val;
            if (c == dim-1) {
              c = 0;
              r++;
            }
            else c++;
            count++;
          }
          //matrix B
          r = 0;
          c = 0;
          while(r < dim && (line = bufferReader.readLine()) != null){
          int val = Integer.parseInt(line);
            B[r][c] = val;
            if (c == dim-1) {
              c = 0;
              r++;
            }
            else c++;
            }
          bufferReader.close();
    System.out.println("read in:");
    Main_Opt2.print(A);
    Main_Opt2.print(B);
    int[][] ans = Main_Opt2.finalStrassen(A,B);
          System.out.println("final ans: ");
        Main_Opt2.print(ans);
      for (int i = 0; i < dim; i++) {
      System.out.println(ans[i][i]);
    }

        }
        catch (NumberFormatException nfe) {
            System.out.println("the dimension (second arg) must be an integer.");
            System.exit(1);
        }
   }
       
        
        else System.out.println("Need flag, dimension, inputFile as arguments");
}
}