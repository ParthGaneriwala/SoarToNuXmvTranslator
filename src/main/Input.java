package main;

import org.jsoar.kernel.Agent;
import org.jsoar.kernel.Production;
import org.jsoar.kernel.SoarException;
import org.jsoar.kernel.tracing.Printer;
import org.jsoar.util.commands.SoarCommands;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {

    // Function to load soar agent into JSoar and return all productions
    public static String getSoarRules(String path) throws SoarException {
        // Magic to enable TCL
        System.setProperty("jsoar.agent.interpreter","tcl");
        // Create agent
        Agent agent = new Agent();
        agent.getPrinter().pushWriter(new OutputStreamWriter(System.out));
        agent.initialize();
        SoarCommands.source(agent.getInterpreter(), path);

        StringWriter sw = new StringWriter();
        Printer pw = new Printer(new PrintWriter(sw));
        String productions = "";

        for(Production p: agent.getProductions().getProductions(null)){
            p.print(pw, true);
            pw.flush();
            productions += cleanProduction(sw.toString());
            // clear printWriter's buffer
            sw = new StringWriter();
            pw = new Printer(new PrintWriter(sw));
        }
        return productions;
    }

    private static String cleanProduction(String input){
        Pattern attributePattern = Pattern.compile("<[a-zA-Z0-9]+[*][a-zA-Z0-9]+>");
        ArrayList<String> attributes = new ArrayList<String>();
        ArrayList<String> attributesToRemove = new ArrayList<String>();
        Matcher attributeMatcher = attributePattern.matcher(input);
        // Find all placeholder attributes
        while(attributeMatcher.find()){
            attributes.add(attributeMatcher.group());
        }
        // Find which attributes to remove based on number of occurrences
        for(String s: attributes){
            int occurrences = 0;
            for(String s1: attributes){
                if(s.equals(s1)){
                    occurrences++;
                }
            }
            if(occurrences == 1){
                attributesToRemove.add(s);
            }
        }

        for(String s: attributesToRemove){
            input = input.replace(s,"");
        }
        return input;
    }

}
