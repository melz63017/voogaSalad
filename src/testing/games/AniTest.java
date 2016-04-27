package testing.games;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import datamanagement.XMLReader;
import datamanagement.XMLWriter;
import events.Action;
import events.KeyTrigger;
import events.PropertyTrigger;
import events.TimeTrigger;
import model.component.character.Health;
import model.component.character.Score;
import model.component.movement.Position;
import model.component.movement.Velocity;
import model.component.visual.Sprite;
import model.entity.Entity;
import model.entity.Level;
import model.physics.PhysicsEngine;
import api.IEntity;
import api.IEventSystem;
import api.ILevel;
import api.IPhysicsEngine;

public class AniTest{
	public static final String TITLE = "Ani's and Carolyn's game";
    public static final int KEY_INPUT_SPEED = 5;
    private static Group root;
    private Level universe;
    private IEventSystem eventSystem;
    private IPhysicsEngine physics;
    private IEntity character;
    private final String IMAGE_PATH = "resources/images/blastoise.png";
    private final String healthScriptPath = "resources/groovyScripts/ACGameTestScript.groovy";
    private final String moveRightScriptPath = "resources/groovyScripts/keyInputMoveRight.groovy";
    private final String moveLeftScriptPath = "resources/groovyScripts/keyInputMoveLeft.groovy";
    private final String jumpScriptPath = "resources/groovyScripts/keyInputJump.groovy";
    private final String addGravityScriptPath = "resources/groovyScripts/ACAddGravity.groovy";
    private static ImageView charSpr;
    private String aniCarolynLevel = "resources/createdGames/AniGame/levels/Level1.xml";
    private Scene myScene;

    public AniTest() {
    	universe = new XMLReader<Level>().readSingleFromFile(aniCarolynLevel);
    	eventSystem = universe.getEventSystem();
    	physics = universe.getPhysicsEngine();
    }
    
    /**
     * Returns name of the game.
     */
    public String getTitle() {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    public Scene init(int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        myScene.setOnKeyPressed(e -> universe.getEventSystem().takeInput(e));
        initEngine();
        return myScene;
    }

    public void initEngine() {
        
    }

    public void step(double dt) {
    	root.getChildren().clear();
    	physics.update(universe, dt);
        // inputSystem.processInputs();
        eventSystem.updateInputs(dt);
        
        universe.getAllEntities().stream().forEach(e->drawCharacter(e));
        //moveEntity(character, 1);
    }
    
    public ImageView drawCharacter(IEntity character) {
        Sprite imgPath = character.getComponent(Sprite.class);
        ImageView charSprite = imgPath.getImageView();
        charSprite.setFitHeight(100);
        charSprite.setPreserveRatio(true);
        root.getChildren().add(charSprite);
        return charSprite;
    }
}
