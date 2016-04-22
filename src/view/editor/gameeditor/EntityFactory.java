package view.editor.gameeditor;

import java.util.Map;
import java.util.ResourceBundle;

import api.IComponent;
import api.IEntity;
import model.entity.Entity;
import view.Utilities;
import view.editor.ComponentFactory;
import view.enums.DefaultStrings;

public class EntityFactory {
	
	private final ComponentFactory componentFactory = new ComponentFactory();
	public IEntity createEntity(String template, String language){
		
		IEntity entity = new Entity();
		entity.loadSpecsFromPropertiesFile(DefaultStrings.TEMPLATE_BUNDLE_LOC.getDefault() + template);
		Map<Class<? extends IComponent>, Integer> numComponents = entity.getSpecs();
		numComponents.keySet().stream().forEach(e->addComponent(e, entity, numComponents, language));
		return entity;
	}


	private void addComponent(Class<? extends IComponent> componentName, IEntity entity, Map<Class<? extends IComponent>, Integer> numComponents, String language) {
		int numToAdd = numComponents.get(componentName);
		for(int i=0; i<numToAdd; i++){
			try {
					entity.addComponent(componentFactory.getComponent(componentName, entity));

			
				
			} catch (Exception e) {
				
				ResourceBundle resources = ResourceBundle.getBundle(language);
				Utilities.showError(resources.getString("error"), resources.getString("createComp"));
			}
		}
	}
}
