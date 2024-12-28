import java.util.HashMap;
import java.util.Map;


public class Evaluator {
    private Map<String, Object> variables = new HashMap<>();

    public void evaluate(ASTNode node) {
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
                        System.out.println(variables.get(label));
                    }else if (isNumber(node.children.get(0).label) || isStringLiteral(node.children.get(0).label)) {
                        System.out.println(node.children.get(0).label);
                    }else {
                        System.out.println("Undefined variable: " + label);
                    }
                }
                break;

            default:
                if (variables.containsKey(node.label)) {
                    node.value = variables.get(node.label); // Fetch variable value if defined
                } else {
                    node.value = node.label; // Assume node is a literal (number or symbol)
                }
                break;
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