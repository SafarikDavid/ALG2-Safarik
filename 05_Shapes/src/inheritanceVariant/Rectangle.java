package inheritanceVariant;

/**
 *
 * @author David Šafařík
 */
public class Rectangle extends Shape {
    //data
    private double a;
    private double b;
    private double area;

    public Rectangle(double a, double b) {
        this.a = a;
        this.b = b;
        this.area = computeArea();
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }
    
    @Override
    public double getArea(){
        return area;
    }

    @Override
    public String toString() {
        //return "Rectangle{" + "a=" + a + ", b=" + b + '}';
        return super.toString() + String.format(" a = %.2f b = %.2f", a, b);
    }

    @Override
    public double computeArea(){
        return a*b;
    }
    
    public static void main(String [] args){
        Rectangle r = new Rectangle(5, 6);
        System.out.println(r.computeArea());
        System.out.println(r.toString());
    }
}
