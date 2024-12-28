import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class LispLexerApp {
    public static void main(String[] args) {
        try {
            // Get the file path from the command line argument
            String filePath = args[0];

            // Create a CharStream that reads from the specified file
            CharStream charStream = CharStreams.fromFileName(filePath);

            // Create a lexer and parser for the input
            LispLexer lexer = new LispLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LispParser parser = new LispParser(tokens);

            // Parse the input and generate the parse tree
            ParseTree tree = parser.program();
            
            // Get the vocabulary to convert token types to their names
            Vocabulary vocabulary = lexer.getVocabulary();

            for (Token token : tokens.getTokens()) {
                String tokenInfo = String.format(
                    "[@%d ='%s',<%d:%s>,%d:%d]",
                    token.getTokenIndex(),
                    token.getText(),
                    token.getType(),
                    vocabulary.getSymbolicName(token.getType()),
                    token.getLine(),
                    token.getCharPositionInLine()
                );
                System.out.println(tokenInfo);
            }

            // Print the parse tree (in Lisp format)
            System.out.println(tree.toStringTree(parser));

            // Generate a DOT file for the parse tree
            String parseTreeDotFilePath = "parse_tree.dot";
            generateDotFile(tree, parseTreeDotFilePath);
            System.out.println("DOT file for parse tree created: " + parseTreeDotFilePath);

            // Generate the AST using the custom visitor
            ASTBuilder builder = new ASTBuilder();
            ASTNode ast = builder.visit(tree);

            // Print the AST
            System.out.println("Abstract Syntax Tree (AST):");
            System.out.println(ast);

            // Generate a DOT file for the AST
            String astDotFilePath = "ast.dot";
            generateAstDotFile(ast, astDotFilePath);
            System.out.println("DOT file for AST created: " + astDotFilePath);


            // Evaluator
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(ast);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateDotFile(ParseTree tree, String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println("digraph ParseTree {");
            writeTreeToDot(tree, writer);
            writer.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTreeToDot(ParseTree tree, PrintWriter writer) {
        String nodeId = String.valueOf(tree.hashCode());
        writer.printf("  \"%s\" [label=\"%s\"];\n", nodeId, escapeLabel(tree.getText()));

        // Recursively write the children
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            String childId = String.valueOf(child.hashCode());
            writer.printf("  \"%s\" -> \"%s\";\n", nodeId, childId);
            writeTreeToDot(child, writer);
        }
    }

    private static void generateAstDotFile(ASTNode ast, String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println("digraph AST {");
            writeAstToDot(ast, writer, null);
            writer.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeAstToDot(ASTNode node, PrintWriter writer, String parentId) {
        String nodeId = String.valueOf(node.hashCode());
        writer.printf("  \"%s\" [label=\"%s\"];\n", nodeId, escapeLabel(node.label));

        // Connect the parent node to the current node
        if (parentId != null) {
            writer.printf("  \"%s\" -> \"%s\";\n", parentId, nodeId);
        }

        // Recursively write the children
        for (ASTNode child : node.children) {
            writeAstToDot(child, writer, nodeId);
        }
    }

    private static String escapeLabel(String label) {
        return label.replace("\\", "")  // Escape backslashes
                .replace("\"", "\\\"")  // Escape double quotes
                .replace("\n", "\\n")    // Escape newlines
                .replace("\r", "\\r");   // Escape carriage returns
    }
}






// class Evaluator {
//     private Map<String, Object> variables = new HashMap<>();
//     private Map<String, Function> functionTable = new HashMap<>();

//     public void evaluate(ASTNode node) {
//         switch (node.label) {
//             case "Program":
//             case "List":
//                 for (ASTNode child : node.children) {
//                     evaluate(child);
//                 }
//                 node.value = node.children.get(0).value;
//                 break;
                
//             case "+":
//                 System.out.println("plus");
//                 double result = 0;
//                 for (ASTNode child : node.children) {
//                     result += Double.parseDouble(child.label);
//                 }
//                 node.value = result;
//                 System.out.println("plus");
//                 System.out.println("node.value is " + node.value);
//                 break;                

//             // case "defun":
//             //     defineFunction(node);
//             //     break;
//             // case "call":
//             //     callFunction(node);
//             //     break;
//             case "defparameter":
//                 if (node.children.size() == 2) {
//                     System.out.println("children" + node.children);
//                     String label = node.children.get(0).label;
//                     Object value = node.children.get(1).value;
//                     variables.put(label, value);
//                     System.out.println(label);
//                     System.out.println("variables" + variables);
//                 }
//                 break;
//             case "write":
//                 System.out.println("write");
//                 if (node.children.size() > 0) {
//                     String label = node.children.get(0).label;
//                     System.out.println(label);
//                     System.out.println("variables" + variables);
//                     if(variables.containsKey(label)){
//                         Object output = variables.get(label);
//                         System.out.println(output);
//                     }
//                 }
//                 break;
//             default:
//                 throw new UnsupportedOperationException("Unknown operation: " + node.label);
//         }
//     }

//     // private void defineFunction(ASTNode node) {
//     //     String functionName = node.children.get(0).label;
//     //     List<String> parameters = node.children.get(1).children.stream().map(child -> child.label).collect(Collectors.toList());
//     //     ASTNode body = node.children.get(2);
//     //     functionTable.put(functionName, new Function(parameters, body));
//     // }

//     // private void callFunction(ASTNode node) {
//     //     String functionName = node.children.get(0).label;
//     //     List<ASTNode> arguments = node.children.subList(1, node.children.size());
//     //     Function function = functionTable.get(functionName);
//     //     if (function != null) {
//     //         evaluateFunction(function, arguments);
//     //     } else {
//     //         throw new UnsupportedOperationException("Undefined function: " + functionName);
//     //     }
//     // }

//     // private void evaluateFunction(Function function, List<ASTNode> arguments) {
//     //     Map<String, Object> localVars = new HashMap<>();
//     //     for (int i = 0; i < function.parameters.size(); i++) {
//     //         localVars.put(function.parameters.get(i), arguments.get(i).label); // Simplified for example
//     //     }
//     //     Evaluator localEvaluator = new Evaluator();
//     //     localEvaluator.variables = localVars;
//     //     localEvaluator.functionTable = this.functionTable; // Share the function table
//     //     localEvaluator.evaluate(function.body);
//     // }


// }

// class Function {
//     List<String> parameters;
//     ASTNode body;

//     public Function(List<String> parameters, ASTNode body) {
//         this.parameters = parameters;
//         this.body = body;
//     }
// }
