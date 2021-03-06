package view.utilities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ContextMenuFactory {
    private ContextMenuFactory () {

    }

    public static ContextMenu createContextMenu (Map<String, EventHandler<ActionEvent>> menuMap) {
        ContextMenu context = new ContextMenu();
        List<MenuItem> menuItems = new ArrayList<>();
        for (Entry<String, EventHandler<ActionEvent>> entry : menuMap.entrySet()) {
            MenuItem item = new MenuItem(entry.getKey());
            item.setOnAction(entry.getValue());
            menuItems.add(item);
        }
        context.getItems().addAll(menuItems);
        return context;
    }
}
