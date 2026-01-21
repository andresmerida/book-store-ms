package dev.am.bookstore.web.practice.solid.lsp.before;

public class Square extends Rectangle {
    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        super.setWidth(height);
    }

    public void setWidth(double width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    /*
     * The Square class does not behave like a Rectangle when setting the witht and height
     * which violate the Liskov Substitution Principle.
     */
    static void main() {
        Rectangle rectangle = new Square();
        rectangle.setHeight(5);
        rectangle.setWidth(10);
        IO.println("Rectangle area: " + rectangle.getArea());

        Square square = new Square();
        square.setHeight(5);
        IO.println("Square area: " + square.getArea());
    }
}
