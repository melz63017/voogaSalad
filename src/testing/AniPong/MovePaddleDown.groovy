package testing.AniPong

import api.IEntity
import api.ILevel
import model.component.character.UserControl
import model.component.movement.Velocity

/**
 * Created by Ani on 4/25/2016.
 */

ILevel level = universe;
Set<IEntity> paddles = level.getEntitiesWithComponents(UserControl.class, Velocity.class);

for (IEntity paddle : paddles) {
    Velocity v = paddle.getComponent(Velocity.class);
    moveDown(v); break;
}

void moveDown(Velocity v) {
    v.setVY(100);
}
