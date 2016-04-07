package model.component.physics;

import com.google.common.base.Preconditions;
import javafx.beans.property.SimpleDoubleProperty;
import api.IComponent;
import utility.Unit;

/**
 * Created by rhondusmithwick on 4/3/16.
 *
 * @author Rhondu Smithwick
 */
public class Mass implements IComponent {

    private final Unit<SimpleDoubleProperty> unit = new Unit<>(new SimpleDoubleProperty(this, "mass", 0.0));

    public Mass() {
    }

    public Mass(double mass) {
        setMass(mass);
    }


    public double getMass() {
        return massProperty().get();
    }

    public void setMass(double mass) {
        boolean valid = mass > 0;
        Preconditions.checkArgument(valid, "Mass not greater than 0");
        massProperty().set(mass);
    }

    public SimpleDoubleProperty massProperty() {
        return unit._1();
    }
}
