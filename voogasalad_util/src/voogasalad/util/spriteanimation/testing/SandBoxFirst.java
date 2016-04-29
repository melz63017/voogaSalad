package voogasalad.util.spriteanimation.testing;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import voogasalad.util.spriteanimation.animation.AnimationContainer;

public class SandBoxFirst extends Application implements SandBox {

    private static final String SPRITE_PATH = "voogasalad/util/spriteanimation/spriteSheets/sonic.png";
    private static final String RESOURCE_BUNDLE = "voogasalad/util/spriteanimation/spriteProperties/sonic";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageView imageView = new ImageView(SPRITE_PATH);
        AnimationContainer container = new AnimationContainer(RESOURCE_BUNDLE);
        Animation animation = container.createAnimation(imageView, "ball");
        animation.play();
        init(primaryStage, imageView, animation);
        primaryStage.show();
    }


}