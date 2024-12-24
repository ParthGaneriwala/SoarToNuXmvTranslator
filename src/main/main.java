package main;

import parser.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.*;
import java.util.Scanner;

public class main {
    public static String debugPath = "D:\\FIT\\Github Projects -Parth\\ICS_SOAR\\load.soar";

    public static void main(String[] args) {
        try {
            // Read Soar agent into a string
            String inputText = "";
            String inputFilePath = args.length > 0 ? args[0] : debugPath;
            inputText = cleanText(Input.getSoarRules(inputFilePath));

            // Create output file path
            String outputFilePath;
            if (args.length > 1) {
                outputFilePath = args[1];
            } else {
                outputFilePath = inputFilePath.replaceAll("\\.soar$", ".smv");
                System.out.println(outputFilePath);
            }

            // Write cleaned input to a file for debugging purposes
            PrintWriter sw = new PrintWriter(new File(inputFilePath.replaceAll("\\.soar$", "-input.soar")));
            sw.println(inputText);
            sw.flush();
            sw.close();

            // Load Soar File
            ANTLRInputStream input = new ANTLRInputStream(inputText);

            // Create Lexer
            SoarLexer lexer = new SoarLexer(input);

            // Lex Soar file into Tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Create Parser
            SoarParser parser = new SoarParser(tokens);

            Visitor visitor = new Visitor();
            visitor.rules = new SoarRules();
            visitor.visit(parser.soar());

            Output outputFormatter = new Output(visitor.rules);
            String outputText = outputFormatter.generateOutput();

//            System.out.println(outputText);

            // Write output to file
            PrintWriter pw = new PrintWriter(new File(outputFilePath));
            pw.println(outputText);
            pw.flush();
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readInputFile(String path) throws FileNotFoundException {
        File soarFile = new File(path);
        Scanner sc = new Scanner(soarFile);
        String text = "";
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if (s.startsWith("echo")) {
                System.out.println(s.substring(5));
            } else if (s.startsWith("source")) {
                String filePath = soarFile.getParent() + "/" + s.substring(7).replaceAll("\"", "");
                text += readInputFile(filePath);
            } else if (s.contains("(write (crlf)")) {
                // Do nothing for these lines
            } else {
                text += s + "\n";
            }
        }
        return text;
    }

    public static String cleanText(String s) {
        String keywordBlackList[] = {"waitsnc --on", "rl --set learning on", "decide indifferent-selection --softmax"};
        for (int i = 0; i < keywordBlackList.length; i++) {
            s = s.replaceAll(keywordBlackList[i], "");
        }
        // Replace the "." in variable names with "_" but don't replace the "." in floating point numbers
        s = s.replaceAll("([a-z|A-Z])\\.", "$1_");

        return s;
    }
}
