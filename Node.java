public class Node {
    private int f,g,h,x,y;
    private String orientation;
    private Node parent;
    private String action;

    public Node(int x, int y, String orientation) {
        this.x = x;
        this.y = y;
        
        this.orientation = orientation;
        this.f = 0;
        this.h = 0;
        this.g = 0;
        this.action = "";
        
        parent = null;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int calculaH(int x, int y)
    {
        return Math.abs(this.y - y) + Math.abs(this.x - x);
    }

    public String getOrientation() {
        return orientation;
    }

    public Node getParent() {
        return parent;
    }    
    
    @Override
    public boolean equals(Object n)
    {
        Node a = (Node) n;
        return (this.y == a.getY() && this.x == a.getX() && this.f < a.getF());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }
}