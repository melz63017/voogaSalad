package view.editor.gameeditor;

import java.util.ResourceBundle;

import api.IEditor;
import api.IEntity;
import api.ILevel;
import api.ISerializable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entity.Entity;
import view.Authoring;
import view.Utilities;
import view.editor.entityeditor.EditorEntity;
import view.editor.eventeditor.EditorEvent;
import view.enums.DefaultStrings;
import view.enums.GUISize;

public class EventDisplay extends ObjectDisplay
{

	private VBox container;
	private Authoring authoringEnvironment;
	private String language;
	private ResourceBundle myResources;
	private ObservableList<IEntity> masterEntityList;
	private ObservableList<ISerializable> masterEnvironmentList;
	private ObservableList<ILevel> levelList;

	public EventDisplay(String language,
			ObservableList<IEntity> masterEntityList, ObservableList<ILevel> levelList, 
			Authoring authoringEnvironment)
	{
		super(authoringEnvironment);
		this.levelList = levelList;
		this.language=language;
		this.masterEntityList = masterEntityList;
		this.authoringEnvironment = authoringEnvironment;
		this.language = language;
		this.myResources = ResourceBundle.getBundle(language);
	}	
	@Override
	protected void addNewObjects(VBox container) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Node makeNewObject() {
		return Utilities.makeButton(myResources.getString(DefaultStrings.EVENT_EDITOR_NAME.getDefault()), 
				e -> createEventEditor());//createEditor(EditorEvent.class, new Entity(), masterEnvironmentList));
		}
	
	private void createEventEditor()
	{
		EditorEvent editor = new EditorEvent(language,  masterEntityList, levelList);
		editor.populateLayout();
		authoringEnvironment.createTab(editor.getPane(), editor.getClass().getSimpleName(), true);
	}
}
