package view.utilities;

import api.IEntity;
import javafx.scene.image.ImageView;
import model.component.visual.AnimatedSprite;
import model.component.visual.Sprite;

/**
 * Used for handling inheritance in the Sprites.
 *
 * @author Rhondu Smithwick
 */
public class SpriteUtilities {
    private SpriteUtilities () {
    }

    public static Sprite getSpriteComponent (IEntity entity) {
        if (entity.hasComponent(AnimatedSprite.class)) {
            return entity.getComponent(AnimatedSprite.class);
        }
        return entity.getComponent(Sprite.class);
    }

    public static <T extends Sprite> T getSpriteComponent (IEntity entity, Class<T> spriteClass) {
        if (entity.hasComponent(AnimatedSprite.class)) {
            return spriteClass.cast(entity.getComponent(AnimatedSprite.class));
        }
        return entity.getComponent(spriteClass);
    }

    public static boolean isSprite(IEntity entity) {
        return entity.hasComponent(AnimatedSprite.class) || entity.hasComponent(Sprite.class);
    }

    public static ImageView getImageView (IEntity entity) {
        return getSpriteComponent(entity).getImageView();
    }
}
