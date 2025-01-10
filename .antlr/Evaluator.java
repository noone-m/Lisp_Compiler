import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {
    private Map<String, Object> variables = new HashMap<>();
    private Map<String, Function> functions = new HashMap<>(); // Store user-defined functions


    public void evaluate(ASTNode node) {
        if (isNumber(node.label)){
            node.value = Double.parseDouble(node.label);
        }
        else if (isStringLiteral(node.label)) {
            node.value = node.label;
        }

        else{
            switch (node.label) {
                case "Program":
                case "List":
                    for (ASTNode child : node.children) {
                        evaluate(child);
                    }
                    if (!node.children.isEmpty()) {
                        node.value = node.children.get(0).value;
                    }
                    break;

    

                case "nil":
                    node.value = false;
                    break;

                case "t":
                    node.value = true;
                    break;

                case "+":
                    double sum = 0;
                    for (ASTNode child : node.children) {
                        evaluate(child); // Ensure child nodes are evaluated
                        sum += Double.parseDouble(child.value.toString());
                    }
                    node.value = sum;
                    break;

                case "-":
                    // System.out.println(node.children);
                    evaluate(node.children.get(0)); // get first number
                    double result = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        result -= Double.parseDouble(node.children.get(i).value.toString());
                    }
                    node.value = result;
                    break;


                case "setq":
                case "defparameter":
                    if (node.children.size() == 2) {
                        String label = node.children.get(0).label;
                        ASTNode valueNode = node.children.get(1);
                        evaluate(valueNode); // Compute the value of the expression
                        variables.put(label, valueNode.value);
                    }
                    break;

                case "write":
                    if (!node.children.isEmpty()) {
                        String label = node.children.get(0).label;
                        if (variables.containsKey(label)) {
                            Object value = variables.get(label);
                            if(value instanceof Boolean){
                                if ((Boolean) value){
                                    System.out.println("T");    
                                }
                                else{
                                    System.out.println("NIL");
                                }
                            }else{
                                System.out.println(value);
                            }

                        }else if (isNumber(label) || isStringLiteral(label) || label.equals("nil") || label.equals("t")) {
                            System.out.println(label);
                        }
                        else if (label.equals("List")) {
                            evaluate(node.children.get(0));
                            System.out.println(node.children.get(0).value);
                        }
                        else {
                            System.out.println("Undefined variable: " + label);
                        }
                    }
                    break;

                case "write-line":
                    if (!node.children.isEmpty()) {
                        String label = node.children.get(0).label;
                        if (isStringLiteral(label)) {
                            System.out.println(label);
                        }
                        else{
                            System.out.println("ERROR: write-line expect string");
                        }
                    }
                    break;

                case "and":
                    boolean andResult = true;
                    for (ASTNode child : node.children) {
                        evaluate(child);
                        if (child.value instanceof Boolean){
                            andResult = andResult && Boolean.parseBoolean(child.value.toString());
                        }
                        else{
                            throw new Error("\"and\"  got non boolean value");
                        }
                    }
                    node.value = andResult;
                    break;

                case "or":
                    boolean orResult = false;
                    for (ASTNode child : node.children) {
                        evaluate(child);
                        if (child.value instanceof Boolean){
                            orResult = orResult || Boolean.parseBoolean(child.value.toString());
                        }
                        else{
                            throw new Error("\"or\"  got non boolean value");
                        }
                    }
                    node.value = orResult;
                    break;            

                case "not": 
                    if (!node.children.isEmpty()) {
                        evaluate(node.children.get(0));
                        try {
                            node.value = ! (Boolean)node.children.get(0).value;
                        } catch (Exception e) {
                            throw new Error("\"not\"  got non boolean value");
                        }
                    } 
                    break;
                

                case "<":
                    if (node.children.isEmpty()) {
                        throw new Error("\"<\" operator expects at least one argument");
                    }
                    evaluate(node.children.get(0));
                    node.value = true; // Assume true until proven otherwise
                    double lastValue = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        Object childValue = node.children.get(i).value;
                        if (childValue == null || !(childValue instanceof Number)) {
                            throw new Error("\"<\" operator got non-numeric value");
                        }
                        double currentValue = Double.parseDouble(childValue.toString());
                        if (currentValue <= lastValue) {
                            node.value = false;
                            break;
                        }
                        lastValue = currentValue;
                    }
                    break;


                case ">":
                    if (node.children.isEmpty()) {
                        throw new Error("\">\" operator expects at least one argument");
                    }
                    evaluate(node.children.get(0));
                    node.value = true; // Assume true until proven otherwise
                    double lastValue2 = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        Object childValue = node.children.get(i).value;
                        if (childValue == null || !(childValue instanceof Number)) {
                            throw new Error("\">\" operator got non-numeric value");
                        }
                        double currentValue = Double.parseDouble(childValue.toString());
                        if (currentValue >= lastValue2) {
                            node.value = false;
                            break;
                        }
                        lastValue2 = currentValue;
                    }
                    break;

                case ">=":
                    if (node.children.isEmpty()) {
                        throw new Error("\">=\" operator expects at least one argument");
                    }
                    evaluate(node.children.get(0));
                    node.value = true; // Assume true until proven otherwise
                    double lastValue3 = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        Object childValue = node.children.get(i).value;
                        if (childValue == null || !(childValue instanceof Number)) {
                            throw new Error("\">=\" operator got non-numeric value");
                        }
                        double currentValue = Double.parseDouble(childValue.toString());
                        if (currentValue > lastValue3) {
                            node.value = false;
                            break;
                        }
                        lastValue3 = currentValue;
                    }
                    break;

                case "<=":
                    if (node.children.isEmpty()) {
                        throw new Error("\"<=\" operator expects at least one argument");
                    }
                    evaluate(node.children.get(0));
                    node.value = true; // Assume true until proven otherwise
                    double lastValue4 = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        Object childValue = node.children.get(i).value;
                        if (childValue == null || !(childValue instanceof Number)) {
                            throw new Error("\"<=\" operator got non-numeric value");
                        }
                        double currentValue = Double.parseDouble(childValue.toString());
                        if (currentValue < lastValue4) {
                            node.value = false;
                            break;
                        }
                        lastValue4 = currentValue;
                    }
                    break;
                    
                case "=":
                    if (node.children.isEmpty()) {
                        throw new Error("\"=\" operator expects at least one argument");
                    }
                    evaluate(node.children.get(0));
                    node.value = true; // Assume true until proven otherwise
                    double firstValue = Double.parseDouble(node.children.get(0).value.toString());
                    for (int i = 1; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        Object childValue = node.children.get(i).value;
                        if (childValue == null || !(childValue instanceof Number)) {
                            throw new Error("\"=\" operator got non-numeric value");
                        }
                        double currentValue = Double.parseDouble(childValue.toString());
                        if (currentValue != firstValue) {
                            node.value = false;
                            break;
                        }
                    }
                    break;

                case "if":
                    if (node.children.size() < 2 || node.children.size() > 3) {
                        throw new Error("Invalid number of arguments for 'if'. Expected 2 or 3.");
                    }
                    // Evaluate the condition (first child)
                    evaluate(node.children.get(0));
                    Object condition = node.children.get(0).value;
                    
                    if (!(condition instanceof Boolean)) {
                        throw new Error("'if' condition must evaluate to a boolean value.");
                    }

                    if ((Boolean) condition) {
                        // Evaluate the "then" branch (second child)
                        evaluate(node.children.get(1));
                        node.value = node.children.get(1).value;
                    } else if (node.children.size() == 3) {
                        // Evaluate the "else" branch (third child), if provided
                        evaluate(node.children.get(2));
                        node.value = node.children.get(2).value;
                    }
                    break;

                case "cond":
                    for (ASTNode branch : node.children) {
                        // Evaluate the cond_branch
                        if (branch.label.equals("cond_branch")) {
                            evaluate(branch);
                            // if one brach got executed stop don't execute other braches 
                            if ((boolean)branch.value){
                                break;
                            }
                            // If a branch is evaluated and matches, assign its value to the cond node
                        } else {
                            throw new Error("cond: encountered non-cond_branch node.");
                        }
                    }
                    break;


                case "cond_branch":
                    // number 2 here has no meaning just because condition is aready reserved
                    ASTNode condition2 = node.children.get(0);
                    evaluate(condition2);
                    if((boolean)condition2.value){
                        node.value = true;
                        for(ASTNode child : node.children){
                            evaluate(child);
                        }
                    }
                    else{
                        node.value = false;
                    }
                    break;


                case "defun":
                    // Define a new function
                    String functionName = node.children.get(0).label;
                    ASTNode parametersNode = node.children.get(1); // Parameters
                    ASTNode bodyNode = node.children.get(2); // Function body
                    functions.put(functionName, new Function(parametersNode, bodyNode));
                    break;


                case "format":
                    if (node.children.size() < 2) {
                        throw new Error("FORMAT requires at least two arguments: destination and format-string");
                    }

                    // Evaluate the destination
                    evaluate(node.children.get(0));
                    Object destination = node.children.get(0).value;

                    // Ensure the second argument is a string (format string)
                    ASTNode formatNode = node.children.get(1);
                    evaluate(formatNode);
                    if (!(isStringLiteral(formatNode.label))) {
                        throw new Error("FORMAT's second argument must be a string");
                    }
                    String formatString = formatNode.label;

                    // Evaluate the rest of the arguments
                    List<Object> args = new ArrayList<>();
                    for (int i = 2; i < node.children.size(); i++) {
                        evaluate(node.children.get(i));
                        args.add(node.children.get(i).value);
                    }
                    // Process the format string
                    StringBuilder stringBuilder = new StringBuilder();
                    int argIndex = 0;

                    for (int i = 0; i < formatString.length(); i++) {
                        char ch = formatString.charAt(i);

                        if (ch == '~') {
                            // Handle format specifiers
                            if (i + 1 < formatString.length()) {
                                char nextCh = formatString.charAt(i + 1);

                                switch (nextCh) {
                                    case '%': // Newline
                                        stringBuilder.append("\n");
                                        i++; // Skip the 't'
                                        break;

                                    case 'a': // Argument substitution
                                        if (argIndex >= args.size()) {
                                            throw new Error("Not enough arguments provided for format string");
                                        }
                                        Object arg = args.get(argIndex);
                                        try{
                                            if(isStringLiteral((String)arg)){
                                                arg = ((String)arg).substring(1, ((String)arg).length() - 1);
                                            }
                                        }catch (Exception e){

                                        }
                                        stringBuilder.append(arg);
                                        argIndex++;
                                        i++; // Skip the 'a'
                                        break;

                                    default:
                                        throw new Error("Unsupported format specifier: ~" + nextCh);
                                }
                            } else {
                                throw new Error("Unexpected end of format string after '~'");
                            }
                        } else {
                            stringBuilder.append(ch);
                        }
                    }

                    if ((boolean)destination) { // T
                        // Print to standard output
                        System.out.print(stringBuilder.deleteCharAt(stringBuilder.length() - 1).deleteCharAt(0));
                    } else{ // nil
                        // Return the formatted result
                        node.value = stringBuilder.toString();
                    } 
                    break;

                case "equal":
                    // Evaluate both child nodes
                    evaluate(node.children.get(0));
                    evaluate(node.children.get(1));
                    Object leftValue = node.children.get(0).value;
                    Object rightValue = node.children.get(1).value;
                    // Perform the comparison
                    if (isStringLiteral((String)leftValue)&& isStringLiteral((String)rightValue)) {
                        node.value =  leftValue.equals(rightValue);
                    } else if (leftValue instanceof List && rightValue instanceof List) {
                        node.value =  leftValue.equals(rightValue); // Structural equality
                    }
                    else{
                        throw new RuntimeException("Unsupported types for 'equal'");
                    }
                    break;
               
                default:
                    // Handle function calls
                    if (functions.containsKey(node.label)) {
                        Function function = functions.get(node.label);
                        List<ASTNode> arguments = new ArrayList();
                        ASTNode parent = node.parent;
                        for (int i=1;i<parent.children.size();i++){
                            arguments.add(parent.children.get(i));
                        }                      
                        if (arguments.size() != function.parameters.children.size()) {
                            throw new RuntimeException(
                                "Function " + node.label + " expects " +
                                function.parameters.children.size() + " arguments but got " + arguments.size()
                            );
                        }

                        // Save current variable state
                        Map<String, Object> previousVariables = new HashMap<>(variables);

                        // Assign arguments to parameters
                        for (int i = 0; i < arguments.size(); i++) {
                            ASTNode parameter = function.parameters.children.get(i);
                            ASTNode argument = arguments.get(i);
                            evaluate(argument);
                            variables.put(parameter.label, argument.value);
                        }

                        // Evaluate function body
                        evaluate(function.body);
                        node.value = function.body.value;

                        // Restore previous variable state
                        variables = previousVariables;
                    } else if (variables.containsKey(node.label)) {
                        node.value = variables.get(node.label);
                    }
                    break;
            }
        }
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isStringLiteral(String value) {
        return value.startsWith("\"") && value.endsWith("\"");
    }


    private class Function {
        ASTNode parameters;
        ASTNode body;

        Function(ASTNode parameters, ASTNode body) {
            this.parameters = parameters;
            this.body = body;
        }
    }

}