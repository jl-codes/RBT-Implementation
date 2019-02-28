import java.util.*;

public class RedBlackTree<T extends Comparable<T>>{

    private enum Color {RED, BLACK}

    private class Node{
        int data;
        Node parent;
        Node left;
        Node right;
        Color color;

        public Node(int data){
            this.data = data;
            this.color = Color.RED;
            this.left = new Node(this);
            this.right = new Node(this);
        }

        public Node(Node node){
            this.data = 0;
            this.parent = node;
            this.color = Color.BLACK;
        }
    }

    Node root;

    public int heightOfTree(Node root) {
        if (root == null) {
            return -1;
        }
        else {
            return 1 + Math.max(heightOfTree(root.left), heightOfTree(root.right));
        }
    }

    public boolean delete(int data){
        Node deleteNode = searchRec(root,data);
        if (deleteNode == null){
            return false;
        }
        else{
            delete(deleteNode);
            return true;
        }
    }

    public void delete(Node node){
        if ((node.left.data == 0) && (node.right.data == 0)){
            Node replaceNode = searchMax(node.left);
            node.data = replaceNode.data;
            delete(replaceNode);
        }
        else{
            deleteOneChild(node);
        }
    }

    private void deleteOneChild(Node node){
        Node child;
        if (node.right.data == 0){
            child = node.left;
        }
        else {
            child = node.right;
        }
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = child;
            } else {
                node.parent.right = child;
            }
        }
        else {
            root = child;
        }
        child.parent = node.parent;
        if (node.color == Color.BLACK){
            if (child.color == Color.RED){
                child.color = Color.BLACK;
            }
            else{
                deleteCase1(child);
            }
        }
    }

    private void deleteCase1(Node node){
        if (node.parent != null){
            deleteCase2(node);
        }
    }

    private void deleteCase2(Node node){
        Node sibling = getSibling(node);
        if (sibling.color == Color.RED){
            node.parent.color = Color.RED;
            sibling.color = Color.BLACK;
            if (node == node.parent.left){
                rotateLeft(node.parent);
            }
            else{
                rotateRight(node.parent);
            }
        }
        deleteCase3(node);
    }

    private void deleteCase3(Node node){
        Node sibling = getSibling(node);
        if ((node.parent.color == Color.BLACK)
                && (sibling.color == Color.BLACK)
                && (sibling.left.color == Color.BLACK)
                && (sibling.right.color == Color.BLACK)){
            sibling.color = Color.RED;
            deleteCase1(node.parent);
        }
        else{
            deleteCase4(node);
        }
    }

    private void deleteCase4(Node node){
        Node sibling = getSibling(node);
        if ((node.parent.color == Color.RED)
                && (sibling.color == Color.BLACK)
                && (sibling.left.color == Color.BLACK)
                && (sibling.right.color == Color.BLACK)){
            sibling.color = Color.RED;
            node.parent.color = Color.BLACK;
        }
        else{
            deleteCase5(node);
        }
    }

    private void deleteCase5(Node node){
        Node sibling = getSibling(node);
        if (sibling.color == Color.BLACK){
            if ((node == node.parent.left)
                    && (sibling.right.color == Color.BLACK)
                    && (sibling.left.color == Color.RED)){
                sibling.color = Color.RED;
                sibling.left.color = Color.BLACK;
                rotateRight(sibling);
            }
            else if ((node == node.parent.right)
                    && (sibling.left.color == Color.BLACK)
                    && (sibling.right.color == Color.RED)){
                sibling.color = Color.RED;
                sibling.right.color = Color.BLACK;
                rotateLeft(sibling);
            }
        }
        deleteCase6(node);
    }

    private void deleteCase6(Node node){
        Node sibling = getSibling(node);
        sibling.color = node.parent.color;
        node.parent.color = Color.BLACK;
        if (node == node.parent.left){
            sibling.right.color = Color.BLACK;
            rotateLeft(node.parent);
        }
        else {
            sibling.left.color = Color.BLACK;
            rotateRight(node.parent);
        }
    }

    public boolean add(int data){
        Node newNode = new Node(data);
        boolean addSuccess = false;
        if (root == null) {
            root = newNode;
        }
        else {
            Node tmpNode = root;
            while (!addSuccess) {
                if (data <= 0) {
                    if (tmpNode.left.data == 0) {
                        tmpNode.left = newNode;
                        newNode.parent = tmpNode;
                        addSuccess = true;
                    } else {
                        tmpNode = tmpNode.left;
                    }
                }
                else if (data > 0){
                    if (tmpNode.right.data == 0) {
                        tmpNode.right = newNode;
                        newNode.parent = tmpNode;
                        addSuccess = true;
                    } else {
                        tmpNode = tmpNode.right;
                    }
                }
                else {
                    return false;
                }
            }
        }
        addCase1(newNode);
        return true;
    }

    private void addCase1(Node node){
        if (node.parent == null){
            node.color = Color.BLACK;
        }
        else {
            addCase2(node);
        }
    }

    private void addCase2(Node node){
        if (node.parent.color == Color.BLACK){
            return;
        }
        else{
            addCase3(node);
        }
    }

    private void addCase3(Node node){
        Node uncle = getUncle(node);
        if ((uncle != null) && (uncle.color == Color.RED) && (node.parent.color == Color.RED)){
            node.parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            Node grandPa = getGrandparent(node);
            grandPa.color = Color.RED;
            addCase1(grandPa);
        }
        else{
            addCase4(node);
        }
    }

    private void addCase4(Node node){
        Node grandPa = getGrandparent(node);
        if ((node == node.parent.right) && (node.parent == grandPa.left)){
            rotateLeft(node.parent);
            node = node.left;
        }
        else if ((node == node.parent.left) && (node.parent == grandPa.right)){
            rotateRight(node.parent);
            node = node.right;
        }
        addCase5(node);
    }

    private void addCase5(Node node) {
        Node grandPa = getGrandparent(node);
        node.parent.color = Color.BLACK;
        grandPa.color = Color.RED;
        if ((node == node.parent.left) && (node.parent == grandPa.left)){
            rotateRight(grandPa);
        }
        else{
            rotateLeft(grandPa);
        }
    }

    private Node getGrandparent(Node node){
        if ((node != null) && (node.parent != null)){
            return node.parent.parent;
        }
        else {
            return null;
        }
    }

    private Node getUncle(Node node){
        Node grandPa = getGrandparent(node);
        if (grandPa == null){
            return null;
        }
        if (node.parent == grandPa.left){
            return grandPa.right;
        }
        else {
            return grandPa.left;
        }
    }

    private Node getSibling(Node node){
        if (node == node.parent.left){
            return node.parent.right;
        }
        else{
            return node.parent.left;
        }
    }

    private void rotateLeft(Node node){
        Node pivot = node.right;
        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = pivot;
            } else {
                node.parent.right = pivot;
            }
        }
        else{
            root = pivot;
        }
        node.right = pivot.left;
        if (pivot.left != null){
            pivot.left.parent = node;
        }
        node.parent = pivot;
        pivot.left = node;
    }

    private void rotateRight(Node node){
        Node pivot = node.left;
        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = pivot;
            } else {
                node.parent.right = pivot;
            }
        }
        else{
            root = pivot;
        }
        node.left = pivot.right;
        if (pivot.right != null){
            pivot.right.parent = node;
        }
        node.parent = pivot;
        pivot.right = node;
    }

    private Node searchRec(Node node, int data){
        if (node.data != 0) {
            if (node.data > 0) {
                return node;
            } else if (data <= 0) {
                return searchRec(node.left, data);
            } else {
                return searchRec(node.right, data);
            }
        }
        else{
            return null;
        }
    }

    private Node searchMax(Node node){
        while ((node.right.data > 0 ) && (node.right.data > 0)){
            node = node.right;
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root,sb);
        return sb.toString();
    }

    private void toStringRec(Node node, StringBuilder sb){
        if (node.data != 0){
            toStringRec(node.left,sb);
            sb
                    .append(node.data)
                    /*.append("(")
                    .append(node.parent != null ? node.parent.data : "null")
                    .append(")")
                    .append("(")
                    .append(node.color)
                    .append(")")*/
                    .append(" ");
            toStringRec(node.right,sb);
        }
    }
    void run(){
        /*rbt.add(6);
        rbt.add(5);
        rbt.add(4);
        rbt.add(3);
        rbt.add(2);
        rbt.add(1);*/

        for (int i = 0; i < 100; i++) {
            Random ree = new Random();
            int z = ree.nextInt(90); // generates num between 0 and 89
            z += 10; // generates num between 10 and 99
            add(z);
            System.out.println("Height:" + heightOfTree(root));
        }

        System.out.println(this.toString());
        //rbt.delete(5);
        //System.out.println(rbt.toString());
    }


    public static void main(String[] args) {
        RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();
        rbt.run();
        
    }
}