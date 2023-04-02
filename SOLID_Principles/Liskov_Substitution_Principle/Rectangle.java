package SOLID_Principles.Liskov_Substitution_Principle;

public class Rectangle {
    protected int width, height;

    public Rectangle() {

    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() { return width * height; }

    @Override
    public String toString() {
        return "Rectangle{" + "width=" + width + ", height=" + height + "}";
    }

    public boolean isSquare() {
        return width == height;
    }
}

class Square extends Rectangle {
    public Square() {}

    public Square(int size) {
       width = height = size; // width and height of a square is the same by definition
    }

    /**
     * both method implementations below violate the LSP
     * Reason -> if we define a method that takes a rectangle as input (like our useIt() method), our Square class
     * as an input would lead to an inconsistent result. We must be careful when using inheritance not to violate this LSP.
     *
     * Fix -> We could use a Factory pattern (like below our RectangleFactory class) to generate both Rectangles and Squares and
     * avoid using inheritance all together.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }
    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }
}

class RectangleFactory {
    public static Rectangle newRectangle(int width, int height) {
        return new Rectangle(width, height);
    }

    public static Rectangle newSquare(int side) {
        return new Rectangle(side, side);
    }
}

class LSPDemo {

    static void useIt(Rectangle r) {
        int width = r.getWidth();
        r.setHeight(10);
        // area = width * 10
        System.out.println("Expected area of " + (width * 10) + " got, " + r.getArea());
    }

    public static void main(String[] args) {
        Rectangle rc = new Rectangle(2, 3);
        useIt(rc);

       Rectangle sq = new Square();
       sq.setWidth(5);
       useIt(sq);
    }
}
