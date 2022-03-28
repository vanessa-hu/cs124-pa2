import java.io.IOException;
//import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

public class strassen {
  // this will be the final one

  public static void main(String[] args) throws IOException {
    // flag dimension inputFile

     if (args.length == 3) {
       // ints?
      // https://www.includehelp.com/java/difference-between-next-and-nextline-methods.aspx
       // http://www.ivoronline.com/Coding/Languages/JAVA/Tutorials/JAVA%20-%20File%20-%20ASCII%20-%20Read%20-%20BufferedReader.php
  
        System.out.println("flag " + args[0]);
       int dim;
        try {
            // Parse the string argument into an integer value.
            dim = Integer.parseInt(args[1]);
       System.out.println("dim " + dim);
        }
        catch (NumberFormatException nfe) {
            System.out.println("The first argument must be an integer.");
            System.exit(1);
        }
       
        double[][] A = new double[dim][dim];
       double[][] B = new double[dim][dim];
       // reading in new file
          FileReader fileReader = new FileReader(args[2]);
          BufferedReader bufferReader = new BufferedReader(fileReader);
          int count = 0;
          String line = "";
          while( (line = bufferReader.readLine()) != null){
            // populate them
          }

        
      // a list of the values of the diagonal entries c0;0; c1;1; : : : ; cd􀀀1;d􀀀1, one per line, including a trailing newline.
   }
       
        
        else System.out.println("Need flag, dimension, inputFile as arguments");
    
	 //  Scanner sc = new Scanner(System.in);
	 //  System.out.println("Enter you Skills");
	 //  String skills = sc.next();
	 //  System.out.println("your skills are " + skills);
  // }
}