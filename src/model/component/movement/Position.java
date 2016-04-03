package model.component.movement;

import api.IComponent;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * Created by rhondusmithwick on 4/1/16.
 *
 * @author Rhondu Smithwick
 */
public class Position implements IComponent {

    private final SimpleDoubleProperty x = new SimpleDoubleProperty(this, "x", 0);
    private final SimpleDoubleProperty y = new SimpleDoubleProperty(this, "y", 0);
    private final SimpleDoubleProperty orientation = new SimpleDoubleProperty(this, "orientation", 0);


    public Position (Double x, Double y) {
        setXY(x, y);
    }

    public Position(Double x, Double y, Double orientation) {
        this(x, y);
        setOrientation(orientation);
    }


    public double getX () {
        return x.get();
    }

    public SimpleDoubleProperty xProperty () {
        return x;
    }

    public double getY () {
        return y.get();
    }

    public SimpleDoubleProperty yProperty () {
        return y;
    }

    public void setXY (double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }


    public double getOrientation() {
        return orientation.get();
    }

    public SimpleDoubleProperty orientationProperty() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation.set(orientation);
    }


    public void add (double dx, double dy) {
        this.setXY(getX() + dx, getY() + dy);
    }

    @Override
    public String toString () {
        return String.format("Position: [X: %s, Y: %s]", getX(), getY());
    }

    @Override
    public boolean unique () {
        return true;
    }

}
