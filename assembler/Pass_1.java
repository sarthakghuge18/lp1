package assembler;


import java.io.*;
import java.util.ArrayList;

class Assembler{

    /*
    Assumptions
    1) Operands separated by commas in input
    2) Literals represented in format ='2'
    3) Input code is syntactically correct, and starts from a START statement
    4) Symbol table and literal table is not sorted on the basis of address (which isn't really a rule
       but still to mention)
    */

    String MOT[][];
    String REG[][];
    int LC; // Indicates current LC
    BufferedReader input;
    BufferedWriter output;
    ArrayList<String[]> sym_tab;
    int sym_tab_index; // Indicates last_index+1 of the sym_tab
    ArrayList<String[]> lit_tab;
    int lit_tab_index; // Indicates last_index+1 of the literals
//    BufferedWriter pool;
    ArrayList<String> back_patch;

    protected Assembler() throws IOException {
        // Initializing MOT
        MOT = new String[][]{
                {"STOP", "IS", "00"},
                {"ADD", "IS", "01"},
                {"SUB", "IS", "02"},
                {"MULT", "IS", "03"},
                {"MOVER", "IS", "04"},
                {"MOVEM", "IS", "05"},
                {"COMP", "IS", "06"},
                {"BC", "IS", "07"},
                {"DIV", "IS", "08"},
                {"READ", "IS", "09"},
                {"PRINT", "IS", "10"},
                {"START", "AD", "01"},
                {"END", "AD", "02"},
                {"ORIGIN", "AD", "03"},
                {"EQU", "AD", "04"},
                {"LTORG", "AD", "05"},
                {"DS", "DL", "01"},
                {"DC", "DL", "02"}
        };

        REG = new String[][] {
                {"AREG","RG","01"},
                {"BREG","RG","02"},
                {"CREG","RG","03"}
        };

        input = new BufferedReader(new FileReader("D:\\Avdhoot\\GitHub\\AG1713\\Practicals\\LP1\\Assembler\\Input"));
        output = new BufferedWriter(new FileWriter("D:\\Avdhoot\\GitHub\\AG1713\\Practicals\\LP1\\Assembler\\Intermediate_code"));
        sym_tab = new ArrayList<>();
        sym_tab_index = 0;
        lit_tab = new ArrayList<>();
        lit_tab_index = 0;
        back_patch = new ArrayList<>();
        // Making sym_tab and lit_tab as arraylist so that it would be easier to search for operands and their address

    }

    protected void generateIC() throws IOException {
        // Input statements can contain at least one part (in case of END, LTORG) or at max 4 parts
        // (in case of <Label> <Operator> <Operand1> <Operand2>)

        /*
        This means, the types of statements are
        1. Length = 1 (Eg, LTORG, STOP)
        2. Length = 2 (Eg, START 100, ORIGIN NXT+4)
        3. Length = 3
            a) <operator> <operand1> <operand2> (Eg, ADD AREG, BREG)
            b) <operand1> <operator> <operand2> (Eg, A DS 1)
            c) <label> <operator> <operand> (Eg, LABEL1 PRINT C)
        4. Length = 4
            a) <label> <operator> <operand1> <operand2>
            b) <label> <operand1> <operator> <operand2>
        */

        // Setting LC first
        String card = input.readLine();
        String[] parts = card.split(" ");
        output.write("(AD,01),(C," + parts[1] + ")\n");
        output.flush();
        LC = Integer.parseInt(parts[1]);

        // Moving to first line after start
        card = input.readLine();

        while (!card.equals("END")){
            card = card.replace(",", ""); // Removing commas
            parts = card.split(" ");
            LC++;

            if (parts.length == 1){
                // Means it is either a STOP, or LTORG
                String[] operator = getMOTEntry(parts[0]);
                String line = "(" + operator[1] + "," + operator[2] + ")";
                output.write(line);
                output.flush();

                if (parts[0].equals("LTORG")){
                    handleLiterals();
                }

            }

            else if (parts.length == 2) {
                // Format: <operator> <operand>
                String[] operator = getMOTEntry(parts[0]);
                output.write("(" + operator[1] + "," + operator[2] + ")"); // Note I haven't use '\n' to go to next line
                output.flush();

                if (parts[0].equals("ORIGIN")) {
                    if (parts[1].contains("+")) {
                        // As in ORIGIN NXT+4
                        // Clearly, label + constant
                        String[] operands = parts[1].split("\\+");
                        output.write("(" + "C," + Integer.parseInt(getLabelAddress(operands[0])) + Integer.parseInt(operands[1]) + ")\n");
                        output.flush();
                        LC = Integer.parseInt(getLabelAddress(operands[0])) + Integer.parseInt(operands[1]);
                    }
                    else {
                        if (parts[1].matches(".*[a-zA-Z].*")){
                            // Label
                            output.write("(C," + getLabelAddress(parts[1]) + ")");
                            output.flush();
                        }
                        else {
                            // Operand
                            output.write("(" + "C," + parts[1] + ")\n");
                            output.flush();
                            LC = Integer.parseInt(parts[1]);
                        }
                    }
                }
                else {
                    addOperand(parts[1]);
                }

            }
            else if (parts.length == 3) {
                // a) <operator> <operand1> <operand2> (Eg, ADD AREG, BREG)
                // b) <operand1> <operator> <operand2> (Eg, A DS 1)
                // c) <label> <operator> <operand> (Eg, LABEL1 PRINT C)

                String[] operator_info = getMOTEntry(parts[0]);

                if (operator_info == null){
                    operator_info = getMOTEntry(parts[1]);
                    output.write("(" + operator_info[1] + "," + operator_info[2] + ")");
                    output.flush();

                    if (operator_info[1].equals("DL")){
                        // c)
                        updateOrCreateLabelAddress(parts[0]);
                        if (parts[2].contains("'")) addOperand(parts[2]);
                        else {
                            output.write("(C," + parts[2] + ")");
                            output.flush();
                        }
                    }
                    else {
                        // b)
                        addOperand(parts[0]);
                        addOperand(parts[2]);
                    }
                }
                else {
                    // a)
                    output.write("(" + operator_info[1] + "," + operator_info[2] + ")");
                    output.flush();
                    addOperand(parts[1]);
                    addOperand(parts[2]);
                }

            }
            else if (parts.length == 4){
                // a) <label> <operator> <operand1> <operand2>
                // b) <label> <operand1> <operator> <operand2>

                updateOrCreateLabelAddress(parts[0]);
                String[] operator_info = getMOTEntry(parts[1]);

                if (operator_info == null){
                    // b)
                    addOperand(parts[1]);
                    addOperand(parts[3]);
                }
                else {
                    addOperand(parts[2]);
                    addOperand(parts[3]);
                }

            }

            card = input.readLine();
            output.write("\n");
            output.flush();
        }

        // End encountered
        handleLiterals();
        generateTables();
        output.write("(AD,02)");
        output.flush();

    }

    private String[] getMOTEntry(String operator){
        for (String[] operator_info : MOT) {
            if (operator_info[0].equals(operator)) {
                return operator_info;
            }
        }
        return null;
    }

    private String getLabelAddress(String label){
        // NOTE: We are using this function only to get the address of the label,
        //       ONLY when we are using ORIGIN. Rest of the times we are assigning indexes

        for (String[] label_info : sym_tab){
            if (label_info[0].equals(label)){
                return label_info[1];
            }
        }
        return "";
    }

    private void addOperand(String operand) throws IOException {
        // The operand may be a literal, register or a label

        // Case 1: Literal
        if (operand.contains("='")){
            output.write("(L," + lit_tab_index + ")");
            output.flush();
            back_patch.add(operand);
            lit_tab_index++;
            return;
        }

        // Case 2: Register
        for (int i=0 ; i<REG.length ; i++){
            if (REG[i][0].equals(operand)){
                output.write("(RG," + REG[i][2] + ")");
                output.flush();
                return;
            }
        }

        // Case 3: Label
        for (int i=0 ; i<sym_tab_index ; i++){
            if (sym_tab.get(i)[0].equals(operand)){
                output.write("(S," + i + ")");
                output.flush();
                return;
            }
        }

        // Case 4: Create new label in the name of operand
        output.write("(S," + sym_tab_index + ")");
        output.flush();
        sym_tab.add(new String[] {operand, ""});
        sym_tab_index++;
    }

    private void updateOrCreateLabelAddress(String label){
        for (int i=0 ; i<sym_tab_index ; i++){
            if (sym_tab.get(i)[0].equals(label)){
                sym_tab.get(i)[1] = Integer.toString(LC);
                return;
            }
        }
        // Else create a new label if first time creation
        sym_tab.add(new String[] {label, Integer.toString(LC)});
        sym_tab_index++;
    }

    private void handleLiterals(){
        for (int i=0 ; i<back_patch.size() ; i++){
            if (i == (back_patch.size()-1)){
                lit_tab.add(new String[] {back_patch.get(i), Integer.toString(LC)});
                break;
            }

            lit_tab.add(new String[] {back_patch.get(i), Integer.toString(LC++)});
        }

        back_patch = new ArrayList<>();
    }

    private void generateTables() throws IOException {
        BufferedWriter symbol_table = new BufferedWriter(new FileWriter("D:\\Avdhoot\\GitHub\\AG1713\\Practicals\\LP1\\Assembler\\SYMTAB"));
        BufferedWriter literal_table = new BufferedWriter(new FileWriter("D:\\Avdhoot\\GitHub\\AG1713\\Practicals\\LP1\\Assembler\\LITTAB"));

        for (int i=0 ; i<sym_tab_index ; i++){
            symbol_table.write(sym_tab.get(i)[0] + "\t" + sym_tab.get(i)[1] + "\n");
            symbol_table.flush();
        }
        for (int i=0 ; i<lit_tab_index ; i++){
            literal_table.write(lit_tab.get(i)[0] + "\t" + lit_tab.get(i)[1] + "\n");
            literal_table.flush();
        }

    }

}

public class Pass_1 {

    public static void main(String[] args) {
        try {
            Assembler assembler = new Assembler();
            assembler.generateIC();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



}

