import java.util.HashMap;
import java.util.Map;


public class Evaluator {
    private Map<String, Object> variables = new HashMap<>();

    public void evaluate(ASTNode node) {
        if (isNumber(node.label)){
            node.value = Double.parseDouble(node.label);
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

                default:
                    if (variables.containsKey(node.label)) {
                        node.value = variables.get(node.label); // Fetch variable value if defined
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
}