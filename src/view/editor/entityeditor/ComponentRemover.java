package view.editor.entityeditor;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import api.IComponent;
import api.IEntity;
import view.Utilities;
import view.enums.DefaultStrings;

public class ComponentRemover extends ComponentModifier {
	private ResourceBundle myResources, myLocs, myComponentNames;
	private IEntity entity;
	
	public ComponentRemover(String language, IEntity entity){
		super(language);
		myResources = ResourceBundle.getBundle(language);
		myLocs = ResourceBundle.getBundle(DefaultStrings.COMPONENT_LOC.getDefault());
		myComponentNames = ResourceBundle.getBundle(language + DefaultStrings.COMPONENTS.getDefault());
		this.entity=entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void modifyComponentList(){
		List<IComponent> components = (List<IComponent>) entity.getAllComponents();
		List<String> componentNames = new ArrayList<>();
		components.forEach(component->componentNames.add(myComponentNames.getString(component.getClass().getSimpleName())));
		String componentName = super.makeAndShowChooser("chooseToRemove", componentNames);
		if(componentName == null){
			return;
		}
		try {
			entity.removeComponent((Class<? extends IComponent>) Class.forName(myLocs.getString(componentName)));
		} catch (ClassNotFoundException e) {
			Utilities.showError(myResources.getString("error"), myResources.getString("removeComponentError"));
		}

		
	}



}
