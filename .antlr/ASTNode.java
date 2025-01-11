// AST Node class to represent nodes in the Abstract Syntax Tree

import java.util.ArrayList;
import java.util.List;

class ASTNode {
    String label;
    Object value;
    ASTNode parent;
    List<ASTNode> children = new ArrayList<>();

    public ASTNode(String label) {
        this.label = label;
    }

    public void addChild(ASTNode child) {
        child.parent = this;
        children.add(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public void removeChildren(int startIndex, int endIndex) {
        for (int i = endIndex - 1; i >= startIndex; i--){
            removeChild(i);
        }
    }

    public ASTNode copy() {
        ASTNode newNode = new ASTNode(this.label);
        for (ASTNode child : this.children) {
                newNode.children.add(child.copy());
            } 
        return newNode; 
    }


    @Override
    public String toString() {
        return label + (children.isEmpty() ? "" : children.toString());
    }
}