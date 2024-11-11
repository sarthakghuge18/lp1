import java.io.*; // Importing necessary classes for input and output operations.
// import java.util.Scanner;
import java.util.StringTokenizer;

// Class for Pass One of an Assembler
public class assemblerPassone {
    // Arrays to store Instruction Set (IS), Assembler Directives (AD), Declarative Statements (DL), and Condition Codes (CC)
    static String is[] = {"STOP", "ADD", "SUB", "MULT", "MOVER", "MOVEM", "COMP", "BC", "DIV", "READ", "PRINT"};
    static String ad[] = {"START", "END", "ORIGIN", "EQU", "LTORG"};
    static String dl[] = {"DC", "DS"};
    static String cc[] = {"LT", "LE", "EQ", "GT", "GE", "ANY"};

    // Variables to track symbol and literal tables
    static int symcounter = 0;
    static int litcounter = 0;
    static String sym[][] = new String[100][2]; // Symbol Table: stores symbols and their addresses
    static String lit[][] = new String[100][2]; // Literal Table: stores literals and their addresses

    public static void main(String args[]) throws Exception {
        int locate = 0; // To track the location counter
        int litcount = 0; // Number of literals in a particular block

        // File readers and writers for input and output files
        BufferedReader reader = new BufferedReader(new FileReader("input.asm")); // Input assembly program
        BufferedWriter writer = new BufferedWriter(new FileWriter("intermediate.txt")); // Intermediate code
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("SYMTAB.txt")); // Symbol table
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("LITTAB.txt")); // Literal table

        String st; // Holds each line from the input file
        String y, prev = null; // `y` stores tokens, `prev` tracks the last token
        String ans; // To store processed line
        String buffer = ""; // Buffer for the intermediate code output
        String buffer1 = ""; // Buffer for the symbol table
        String buffer2 = ""; // Buffer for the literal table

        // Reading the input file line by line
        while ((st = reader.readLine()) != null) {
            int isflag = 0; // Flag to check if the token is an instruction
            StringTokenizer splitted = new StringTokenizer(st); // Splits the line into tokens
            ans = ""; // Reset for the current line

            // Process each token
            while (splitted.hasMoreTokens()) {
                y = splitted.nextToken();

                // Process `START` directive
                if (y.equals("START")) {
                    locate = Integer.parseInt(splitted.nextToken()); // Set the location counter
                    ans = "(AD,01)(C," + locate + ")"; // Add START directive to intermediate code
                    break;
                }
                // Check if token is an instruction
                else if (searchis(y)) {
                    if (y.equals("STOP")) {
                        // Handle STOP instruction
                    }
                    ans += "(IS," + indexis(y) + ")"; // Add instruction to intermediate code
                    isflag = 1;
                    locate += 1; // Increment location counter
                }
                // Check if token is an assembler directive
                else if (searchad(y)) {
                    // Handle assembler directives like LTORG, ORIGIN, END, EQU
                }
                // Check if token is a declarative statement
                else if (searchdl(y)) {
                    // Handle DS and DC declarative statements
                }
                // Handle symbols, literals, registers, and condition codes
                else {
                    // Process symbols and literals
                }
            }
            ans += "\n";
            buffer += ans; // Append the processed line to the buffer
        }

        // Write Symbol Table to file
        String ans1 = "";
        for (int i = 0; i < symcounter; i++) {
            ans1 += sym[i][0] + "\t" + sym[i][1] + "\n";
        }
        buffer1 += ans1;

        // Write Literal Table to file
        String ans2 = "";
        for (int i = 0; i < litcounter; i++) {
            ans2 += lit[i][0] + "\t" + lit[i][1] + "\n";
        }
        buffer2 += ans2;

        // Write buffers to respective files
        writer.write(buffer);
        writer1.write(buffer1);
        writer2.write(buffer2);

        // Close all file streams
        reader.close();
        writer.close();
        writer1.close();
        writer2.close();

        System.out.println("Program finished...");
    }

    // Helper methods to search and identify tokens
    public static boolean searchis(String s) {
        for (String instruction : is) {
            if (instruction.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchad(String s) {
        for (String directive : ad) {
            if (directive.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchdl(String s) {
        for (String declaration : dl) {
            if (declaration.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static int indexis(String s) {
        for (int i = 0; i < is.length; i++) {
            if (is[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }
}
