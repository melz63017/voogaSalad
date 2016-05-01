package view.editor.eventeditor.tables;

import api.IComponent;
import api.IEntity;
import api.ILevel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import model.entity.Entity;
import view.editor.eventeditor.tabs.PropertyEventEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * Table manager that will control everyTable in the PropertyEventEditor Tab.
 *
 * @author Alankmc
 */
public class PropertyTableManager extends TableManager {
    private final HBox container;
    private EntityTable entityTable;
    private ComponentTable componentTable;
    private PropertyTable propertyTable;

    private Entity entity;
    private IComponent component;
    private final PropertyEventEditor editor;

    private final ObservableList<IEntity> selectedEntities;
    private final List<IEntity> chosenEntities = new ArrayList<>();

    public PropertyTableManager (String language, PropertyEventEditor editor) {
        container = new HBox();
        String language1 = language;
        selectedEntities = FXCollections.observableArrayList();

        try {
            entityTable = new EntityTable(selectedEntities, this, language);
            componentTable = new ComponentTable(this, language);
            propertyTable = new PropertyTable(this, language);

        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        this.editor = editor;

        editor.resetTrigger();
        fillLayout();
    }

    /**
     * When Entity is clicked, update the subsequent Tables. 
     * 
     * @param Entity entity
     */
    public void entityWasClicked (Entity entity) {
        editor.setEntityForAnimation(entity);
        if (chosenEntities.contains(entity)) {
            chosenEntities.remove(entity);
        } else {
            editor.resetTrigger();
            chosenEntities.add(entity);
            componentTable.refreshTable();
            propertyTable.refreshTable();
            componentTable.fillEntries(entity);
            this.entity = entity;
            editor.choseEntity(chosenEntities);
        }
//		editor.resetTrigger();
//		editor.setEntityForAnimation(entity);
//
//		componentTable.refreshTable();
//		propertyTable.refreshTable();
//		componentTable.fillEntries(entity);
//		this.entity = entity;
    }

    /**
     * When a component is clicked, update the Property table. 
     *   
     * @param IComponent component
     */
    public void componentWasClicked (IComponent component) {
        editor.resetTrigger();
        propertyTable.refreshTable();
        propertyTable.fillEntries(component);
        this.component = component;
    }

    /**
     * Then a property is clicked, a Property Trigger is ready to be made.
     * 
     * @param SimpleObjectProperty<?> property
     */
    public void propertyWasClicked (SimpleObjectProperty<?> property) {
        editor.triggerSet(entity.getName(), component, property);
    }

    /**
     * When level is picked, change the Entities that are being displayed, 
     * and that will be affected by the Event.
     * 
     * @param List<ILevel> levels
     */
    public void levelWasPicked (List<ILevel> levels) {
        selectedEntities.clear();
        componentTable.refreshTable();
        propertyTable.refreshTable();

        for (ILevel level : levels) {
            selectedEntities.addAll(level.getAllEntities());
        }

        entityTable.levelWasPicked(selectedEntities);
    }

    public void allBoxChanged (String event) {
        System.out.println(event);
    }

    private void fillLayout () {
        container.getChildren().addAll(entityTable.getTable(), componentTable.getTable(), propertyTable.getTable());
    }

    public HBox getContainer () {
        return container;
    }

}
