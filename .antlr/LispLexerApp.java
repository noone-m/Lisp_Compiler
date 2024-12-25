import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

// AST Node class to represent nodes in the Abstract Syntax Tree
class ASTNode {
    String label;
    List<ASTNode> children = new ArrayList<>();

    public ASTNode(String label) {
        this.label = label;
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return label + (children.isEmpty() ? "" : children.toString());
    }
}

// Custom ASTBuilder class extending LispBaseVisitor
class ASTBuilder extends LispBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(LispParser.ProgramContext ctx) {
        ASTNode node = new ASTNode("Program");
        for (LispParser.ExpressionContext exprCtx : ctx.expression()) {
            ASTNode expressionNode = visit(exprCtx); // Recursively visit expressions
            if(expressionNode != null){
                node.addChild(expressionNode);
            }
        }
        return node;
    }

    @Override
    public ASTNode visitExpression(LispParser.ExpressionContext ctx) {
        if (ctx.NUMBER() != null) {
            return new ASTNode(ctx.NUMBER().getText());
        } else if (ctx.SYMBOL() != null) {
            return new ASTNode(ctx.SYMBOL().getText());
        } else if (ctx.list() != null) {
            return visit(ctx.list());
        } else if (ctx.PLUS() != null || ctx.MINUS() != null || ctx.MULTIPLY() != null || ctx.DIVIDE() != null) {
            String operator = ctx.getChild(0).getText(); // Get the operator symbol
            ASTNode node = new ASTNode(operator);
            for (int i = 1; i < ctx.getChildCount(); i++) { // Skip the operator itself
                node.addChild(visit(ctx.getChild(i)));
            }
            return node;
        }
        return null;
    }

    @Override
    public ASTNode visitList(LispParser.ListContext ctx) {
        ASTNode node = new ASTNode("List");
        for (ParseTree child : ctx.children) {
            ASTNode expressionNode = visit(child); // Recursively visit expressions
            if(expressionNode != null){
                node.addChild(expressionNode);
            }
        }
        return node;
    }

    @Override
    public ASTNode visitBoundedExpression(LispParser.BoundedExpressionContext ctx) {
        ASTNode node = new ASTNode("BoundedExpression");
        for (ParseTree child : ctx.children) {
            node.addChild(visit(child));
        }
        return node;
    }
}
