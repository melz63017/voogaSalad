package events;

import api.ILevel;
import api.IEventSystem;
import api.ILevel;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import datamanagement.XMLReader;
import datamanagement.XMLWriter;
import utility.Pair;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/***
 * Created by ajonnav 04/12/16
 *
 * @author Anirudh Jonnavithula, Carolyn Yao For non-key events, we want to
 *         create a string "entityid:componentName:index". Register string to an
 *         action in the map. A Trigger Factory can interpret the string to
 *         create the right kind of Trigger Using this string, we generate the
 *         triggers in some sort of factory fashion
 */
public class EventSystem implements Observer, IEventSystem {

    private transient ILevel universe;
    private InputSystem inputSystem;
    private ListMultimap<Trigger, Action> actionMap = ArrayListMultimap.create();

    public EventSystem(ILevel universe, InputSystem inputSystem) {
        this.universe = universe;
        this.inputSystem = inputSystem;
    }

    @Override
    public void registerEvent(Trigger trigger, Action action) {
        actionMap.put(trigger, action);
        if (!actionMap.containsKey(trigger)) {
            trigger.addObserver(this);
            trigger.addHandler(universe, inputSystem);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        List<Action> actions = actionMap.get((Trigger) o);
        actions.stream().forEach(e -> e.activate(universe));
    }


    @Override
    public void setUniverse(ILevel universe) {
        this.universe = universe;
        addHandlers();
    }

    @Override
    public File saveEventsToFile(String filepath) {
        stopObservingTriggers(actionMap);
        new XMLWriter<Pair<Trigger, List<Action>>>().writeToFile(filepath, convertMapToList(actionMap));
        watchTriggers(actionMap);
        addHandlers();
        return new File(filepath);
    }

    @Override
    public void readEventsFromFilePath(String filepath) {
        List<Pair<Trigger, List<Action>>> eventList = new XMLReader<Pair<Trigger, List<Action>>>().readFromFile(filepath);
        actionMap = convertListToMap(eventList);
        watchTriggers(actionMap);
        addHandlers();
    }

    @Override
    public void readEventsFromFile(File file) {
        List<Pair<Trigger, List<Action>>> eventList = new XMLReader<Pair<Trigger, List<Action>>>().readFromFile(file.getPath());
        actionMap = convertListToMap(eventList);
        watchTriggers(actionMap);
        addHandlers();
    }

    @Override
    public String returnEventsAsString() {
        return new XMLWriter<Pair<Trigger, List<Action>>>().writeToString(convertMapToList(actionMap));
    }

    private ListMultimap<Trigger, Action> convertListToMap(List<Pair<Trigger, List<Action>>> eventList) {
        ListMultimap<Trigger, Action> returnMap = ArrayListMultimap.create();
        for (Pair<Trigger, List<Action>> event : eventList) {
            Trigger trigger = event._1();
            List<Action> actionList = event._2();
            actionList.stream().forEach(action -> returnMap.put(trigger, action));
        }
        return returnMap;
    }

    private List<Pair<Trigger, List<Action>>> convertMapToList(ListMultimap<Trigger, Action> map) {
        return map.keySet().stream().map(trigger -> new Pair<>(trigger, map.get(trigger))).collect(Collectors.toList());
    }

    private void stopObservingTriggers(ListMultimap<Trigger, Action> map) {
        for (Trigger trigger : map.keySet()) {
            trigger.deleteObserver(this);
        }
    }

    private void watchTriggers(ListMultimap<Trigger, Action> map) {
        for (Trigger trigger : map.keySet()) {
            trigger.addObserver(this);
        }
    }

    private void addHandlers() {
        actionMap.keySet().stream().forEach(trigger -> trigger.addHandler(universe, inputSystem));
    }

    private void clearListeners() {
        actionMap.keySet().stream().forEach(trigger -> trigger.clearListener(universe, inputSystem));
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        clearListeners();
        stopObservingTriggers(actionMap);
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        watchTriggers(actionMap);
    }

}