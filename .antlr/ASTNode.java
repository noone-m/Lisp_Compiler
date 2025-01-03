// AST Node class to represent nodes in the Abstract Syntax Tree

import java.util.ArrayList;
import java.util.List;

class ASTNode {
    String label;
    Object value;
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