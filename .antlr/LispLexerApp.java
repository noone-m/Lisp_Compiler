import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class LispLexerApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java LispLexerApp <input-file>");
            return;
        }

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
            // System.out.println(tree.toStringTree(parser));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
