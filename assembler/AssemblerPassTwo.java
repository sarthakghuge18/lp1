
package assembler;

import java.io.*;
import java.util.*;

public class AssemblerPassTwo {
    public static void main(String[] args) throws Exception {
        BufferedReader inputReader = new BufferedReader(new FileReader("intermediate.txt"));
        Map<Integer, Integer> symTab = loadTable("SYMTAB.txt");
        Map<Integer, Integer> litTab = loadTable("LITTAB.txt");
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("machine_code.txt"));

        String inputLine;
        while ((inputLine = inputReader.readLine()) != null) {
            String[] tokens = inputLine.split("\\)\\(");
            StringBuilder outputLine = new StringBuilder();

            for (String token : tokens) {
                token = token.replaceAll("[\\(\\)]", "");

                if (token.startsWith("S,")) {
                    int symIndex = Integer.parseInt(token.split(",")[1]);
                    token = " " + symTab.getOrDefault(symIndex, 0);
                } else if (token.startsWith("L,")) {
                    int litIndex = Integer.parseInt(token.split(",")[1]);
                    token = " " + litTab.getOrDefault(litIndex, 0);
                }

                outputLine.append(token.replace(",", "")).append(" ");
            }

            outputWriter.write(outputLine.toString().trim());
            outputWriter.newLine();
        }

        inputReader.close();
        outputWriter.close();
    }

    private static Map<Integer, Integer> loadTable(String fileName) throws IOException {
        Map<Integer, Integer> table = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length >= 2) {
                table.put(Integer.parseInt(parts[0].replaceAll("[^0-9]", "")),
                        Integer.parseInt(parts[1].replaceAll("[^0-9]", "")));
            }
        }

        reader.close();
        return table;
    }
}

