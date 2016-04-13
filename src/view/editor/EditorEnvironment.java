package view.editor;

import java.io.File;
import java.util.ResourceBundle;
import api.IEntity;
import api.IEntitySystem;
import api.ISerializable;
import enums.GUISize;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.component.movement.Position;
import model.component.visual.ImagePath;
import view.DragAndResize;
import view.DefaultsMaker;
import view.Utilities;

public class EditorEnvironment extends Editor {

	private BorderPane environmentPane;
	private VBox entityOptions;
	private IEntitySystem entitiesInEnvironment;
	private SubScene gameScene;
	private Group gameRoot;
	private ResourceBundle myResources;
	private ObservableList<ISerializable> entitiesToDisplay;
	private ObservableList<ISerializable> finalEnvironmentList;
	private VBox leftPane;
	private VBox rightPane;
	private VBox entitiesCurrentlyIn;
	private TextField nameField;

	public EditorEnvironment(String language, ISerializable toEdit, ObservableList<ISerializable> masterList,
			ObservableList<ISerializable> addToList) {
		myResources = ResourceBundle.getBundle(language);
		masterList.addListener((ListChangeListener<? super ISerializable>) c -> {
			this.updateDisplay(masterList);
		});
		entitiesToDisplay = masterList;
		entitiesInEnvironment = (IEntitySystem) toEdit;
		finalEnvironmentList = addToList;
		addLayoutComponents();
	}

	private void addLayoutComponents() {
		environmentPane = new BorderPane();
		setLeftPane();
		setRightPane();
		setGameScene();
	}

	private void setLeftPane() {
		leftPane = new VBox();
		leftPane.getChildren().add(setNameDisplay());
		leftPane.getChildren().add(setEntityOptionsDisplay());
	}

	private TextField setNameDisplay() {
		nameField = new TextField();
		if (entitiesInEnvironment.getName().equals("")) {
			nameField.setText(myResources.getString("environmentName"));
		} else {
			nameField.setText(entitiesInEnvironment.getName());
		}
		return nameField;
	}

	private ScrollPane setEntityOptionsDisplay() {
		entityOptions = new VBox();
		loadDefaults();
		populateVbox(entityOptions, entitiesToDisplay);
		return (new ScrollPane(entityOptions));
	}

	private void populateVbox(VBox vbox, ObservableList<ISerializable> entityPopulation) {
		vbox.getChildren().clear();
		for (ISerializable entity : entityPopulation) {
			Button addEntityButton = Utilities.makeButton(((IEntity) entity).getName(),
					e -> addToScene((IEntity) entity));
			(addEntityButton).setMaxWidth(Double.MAX_VALUE);
			vbox.getChildren().add(addEntityButton);
		}
	}

	@Override
	public void loadDefaults() {
		if (Utilities.showAlert(myResources.getString("addDefaults"), myResources.getString("defaultsMessage"),
				myResources.getString("addDefaultsQuestion"), AlertType.CONFIRMATION)) {
			entitiesToDisplay.add(DefaultsMaker.loadBackgroundDefault());
			entitiesToDisplay.add(DefaultsMaker.loadPlatformDefault(entitiesToDisplay));
		}
	}

	private void setRightPane() {
		rightPane = new VBox();
		rightPane.getChildren().add(setSaveButton());
		rightPane.getChildren().add(setEntitiesInEnvironmentDisplay());
	}

	private Button setSaveButton() {
		return Utilities.makeButton(myResources.getString("saveEnvironment"), e -> saveEnvironment());
	}

	private ScrollPane setEntitiesInEnvironmentDisplay() {
		entitiesCurrentlyIn = new VBox();
		return (new ScrollPane(entitiesCurrentlyIn));
	}

	private void setGameScene() {
		gameRoot = new Group();
		gameScene = new SubScene(gameRoot, (GUISize.TWO_THIRDS_OF_SCREEN.getSize()),
				GUISize.HEIGHT_MINUS_TAB.getSize());
		gameScene.setFill(Color.WHITE);
		if (!entitiesInEnvironment.isEmpty()){
			for (IEntity entity : entitiesInEnvironment.getAllEntities()){
				loadScene(entity);
			}
		}
	}

	private void loadScene(IEntity entity) {
			if (!entity.hasComponent(Position.class) || !entity.hasComponent(ImagePath.class)) {
				addComponents(entity);
			}
			ImageView entityView = Utilities.createImage(entity.getComponent(ImagePath.class),
					entity.getComponent(Position.class));
			DragAndResize.makeResizable(entityView, entity.getComponent(ImagePath.class),
					entity.getComponent(Position.class));
				Button entityInButton = new Button(entity.getName());
				entityInButton.setOnAction(e -> removeFromDisplay(entityView, entity, entityInButton));
				entitiesCurrentlyIn.getChildren().add(entityInButton);
				gameRoot.getChildren().add(entityView);		
	}

	private void updateDisplay(ObservableList<ISerializable> masterList) {
		entitiesToDisplay = masterList;
		populateVbox(entityOptions, entitiesToDisplay);
	}

	@Override
	public void updateEditor() {
		populateVbox(entityOptions, entitiesToDisplay);
	}

	@Override
	public void populateLayout() {
		environmentPane.setRight(rightPane);
		environmentPane.setLeft(leftPane);
		environmentPane.setCenter(gameScene);
	}

	private void saveEnvironment() {
		entitiesInEnvironment.removeAllBindingsFromComponents();
		String name = getName();
		entitiesInEnvironment.setName(name);
		//finalEnvironmentList.remove(entitiesInEnvironment);
		finalEnvironmentList.add(entitiesInEnvironment);
		environmentPane.getChildren().clear();
		environmentPane.setCenter(saveMessage(myResources.getString("saveMessage")));
	}

	private String getName() {
		String returnName = null;
		if (nameField.getText().equals(myResources.getString("environmentName"))) {
			returnName = Utilities.userInputBox(myResources.getString("noName"),
					myResources.getString("noNameMessage"));
		} else {
			returnName = nameField.getText();
		}
		return returnName;
	}

	private void addToScene(IEntity entity) {
		try {
			if (!entity.hasComponent(Position.class) || !entity.hasComponent(ImagePath.class)) {
				addComponents(entity);
			}
			IEntity newEntity = Utilities.copyEntity(entity);
			ImageView entityView = Utilities.createImage(newEntity.getComponent(ImagePath.class),
					newEntity.getComponent(Position.class));
			DragAndResize.makeResizable(entityView, newEntity.getComponent(ImagePath.class),
					newEntity.getComponent(Position.class));
			if (!entitiesInEnvironment.containsEntity(newEntity)) {
				entitiesInEnvironment.addEntity(newEntity);
				Button entityInButton = new Button(newEntity.getName());
				entityInButton.setOnAction(e -> removeFromDisplay(entityView, newEntity, entityInButton));
				entitiesCurrentlyIn.getChildren().add(entityInButton);
				gameRoot.getChildren().add(entityView);
			}
		} catch (Exception e) {
			// Utilities.showAlert(myResources.getString("error"), null,
			// myResources.getString("unableToAdd"), AlertType.ERROR);
		}
	}

	private void addComponents(IEntity entity) {
		if (Utilities.showAlert(myResources.getString("confirm"), myResources.getString("componentsRequired"),
				myResources.getString("addComponentQuestion"), AlertType.CONFIRMATION)) {
			addPositionComponent(entity);
			addImagePathComponent(entity);
		}
	}

	private void addPositionComponent(IEntity entity) {
		entity.setSpec(Position.class, 1);
		Position pos = new Position();
		entity.addComponent(pos);
	}

	private void addImagePathComponent(IEntity entity) {
		File file = Utilities.promptAndGetFile(Utilities.getImageFilters(),
				myResources.getString("pickImagePathImage"));
		entity.setSpec(ImagePath.class, 1);
		entity.addComponent(new ImagePath(file.getPath()));
	}

	private void removeFromDisplay(ImageView entityView, IEntity entity, Button entityButton) {
		gameRoot.getChildren().remove(entityView);
		entitiesInEnvironment.removeEntity(entity.getID());
		entitiesCurrentlyIn.getChildren().remove(entityButton);
	}

	public IEntitySystem getEntitySystem() {
		return entitiesInEnvironment;
	}

	@Override
	public Pane getPane() {
		return environmentPane;
	}

	@Override
	public void addSerializable(ISerializable serialize) {
		entitiesInEnvironment.addEntity((IEntity) serialize);
	}

	public boolean displayContains(IEntity checkEntity) {
		return entitiesToDisplay.contains(checkEntity);
	}

	public boolean environmentContains(IEntity checkEntity) {
		return entitiesInEnvironment.containsEntity(checkEntity);
	}

	/*
	 * entityView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	 * 
	 * @Override public void handle(MouseEvent event) { MouseButton button =
	 * event.getButton(); if (button == MouseButton.SECONDARY) {
	 * removeFromDisplay(entityView, newEntity); } } });
	 */

}
