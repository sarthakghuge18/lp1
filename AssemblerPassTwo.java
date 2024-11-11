import java.io.*; // Importing necessary libraries for file handling.

public class AssemblerPassTwo {
    public static void main(String[] args) throws Exception {
        // BufferedReader objects to read input files.
        BufferedReader inputReader = new BufferedReader(new FileReader("intermediate.txt")); // Intermediate code
        BufferedReader symReader = new BufferedReader(new FileReader("SYMTAB.txt")); // Symbol Table
        BufferedReader litReader = new BufferedReader(new FileReader("LITTAB.txt")); // Literal Table

        // BufferedWriter object to write the machine code to output file.
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("machine_code.txt"));

        String inputLine; // Line from intermediate file
        String symLine = null; // Line from SYMTAB
        String litLine = null; // Line from LITTAB

        // Reading the intermediate code line by line.
        while ((inputLine = inputReader.readLine()) != null) {
            // Split the line into tokens using `)` and `(` as delimiters.
            String[] tokens = inputLine.split("\\)\\(");
            StringBuilder outputLine = new StringBuilder(); // To build the output machine code for each line.

            // Processing each token in the intermediate line.
            for (String token : tokens) {
                // Remove surrounding parentheses from the token.
                token = token.replaceAll("[\\(\\)]", "");

                // Check if token refers to a Symbol (S).
                if (token.startsWith("S,")) {
                    if (symLine == null) {
                        symLine = symReader.readLine(); // Read the next line from SYMTAB.
                    }
                    if (symLine != null) {
                        String[] symTokens = symLine.split("\t"); // Split SYMTAB line into parts (e.g., symbol, address).
                        if (symTokens.length > 1) {
                            int address = Integer.parseInt(symTokens[1]); // Extract symbol address.
                            token = token.replace("S,", ""); // Remove 'S,' prefix.
                            token = getFormattedMachineCode(token, address); // Format as machine code.
                        }
                    }
                }
                // Check if token refers to a Literal (L).
                else if (token.startsWith("L,")) {
                    if (litLine == null) {
                        litLine = litReader.readLine(); // Read the next line from LITTAB.
                    }
                    if (litLine != null) {
                        String[] litTokens = litLine.split("\t"); // Split LITTAB line into parts (e.g., literal, address).
                        if (litTokens.length > 1) {
                            int address = Integer.parseInt(litTokens[1]); // Extract literal address.
                            token = token.replace("L,", ""); // Remove 'L,' prefix.
                            token = getFormattedMachineCode(token, address); // Format as machine code.
                        }
                    }
                }

                // Append the processed token to the output line, removing any commas.
                outputLine.append(token.replace(",", "")).append(" ");
            }

            // Remove any alphabetical characters from the final output line (opcode, register numbers only).
            outputLine = new StringBuilder(outputLine.toString().replaceAll("[A-Za-z]", ""));

            // Write the formatted machine code to the output file.
            outputWriter.write(outputLine.toString().trim());
            outputWriter.newLine();
        }

        // Close all file streams to release resources.
        inputReader.close();
        symReader.close();
        litReader.close();
        outputWriter.close();
    }

    /**
     * Formats the instruction and address into machine code.
     *
     * @param instruction The opcode and register information.
     * @param address     The memory address (symbol or literal).
     * @return Formatted machine code string.
     */
    private static String getFormattedMachineCode(String instruction, int address) {
        String[] parts = instruction.split(","); // Split the instruction into opcode and registers.
        String opcode = parts[0]; // Extract opcode.
        String[] operands = parts.length > 1 ? parts[1].split(" ") : new String[0]; // Extract registers if available.

        // Convert register indices into formatted register numbers.
        StringBuilder registerNumbers = new StringBuilder();
        for (String operand : operands) {
            int regIndex = Integer.parseInt(operand); // Convert to integer.
            registerNumbers.append(getRegisterNumber(regIndex)); // Append formatted register number.
        }

        // Return the final formatted machine code (opcode + register numbers + address).
        return opcode + " " + registerNumbers + " " + address;
    }

    /**
     * Maps register indices to specific register numbers.
     *
     * @param index The register index.
     * @return The formatted register number.
     */
    private static String getRegisterNumber(int index) {
        String[] regNumbers = {"01", "02", "03", "04"}; // Predefined register numbers.
        if (index >= 0 && index < regNumbers.length) {
            return regNumbers[index]; // Return corresponding register number.
        }
        return ""; // Return empty string if index is out of range.
    }
}
