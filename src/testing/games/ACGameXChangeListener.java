package testing.games;

import api.IEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.component.movement.Position;

public class ACGameXChangeListener implements ChangeListener {

    private IEntity character;

    public ACGameXChangeListener (IEntity character) {
        this.character = character;
    }

    public void listen (IEntity character) {
        character.getComponent(Position.class).getProperties().get(0).addListener(this);
    }

    @Override
    public void changed (ObservableValue observable, Object oldValue, Object newValue) {
        if ((double) observable.getValue() > 500) {
            System.out.println("out of bounds");

        }
    }

}
