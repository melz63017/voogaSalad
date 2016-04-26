package animation.model;

import animation.animation.CustomAnimation;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static animation.config.DoubleConstants.KEY_INPUT_SPEED;
import static animation.config.StringConstants.NO_SELECT_EFFECT;
import static animation.config.StringConstants.SELECT_EFFECT;
import static animation.gui.Dragger.makeDraggable;

/**
 * This class holds the rectangles drawn and the logic on them.
 *
 * @author Rhondu Smithwick, Melissa Zhang
 */
public class RectangleLogic {
    private final List<Rectangle> rectangleList = new ArrayList<>();
    private final List<Label> labelList = new ArrayList<>();
    private final ObservableList<Button> buttonList = FXCollections.observableArrayList();
    private final RectangleDrawer rectDrawer;
    private Rectangle selectedRectangle;

    /**
     * Sole constructor.
     *
     * @param rectDrawer a rectangle drawer
     */
    public RectangleLogic(RectangleDrawer rectDrawer) {
        this.rectDrawer = rectDrawer;
    }

    /**
     * Get the animation map describing the rectangles.
     *
     * @return a map describing the rectangles
     */
    public Map<String, String> getAnimationMap() {
        Map<String, String> moveAnimationMap = new HashMap<>();
        moveAnimationMap.put("numFrames", String.valueOf(rectangleList.size()));
        Map<String, List<Double>> propertyMap = getPropertyMap();
        List<String> neededProperties = Arrays.asList("numFrames", "xList", "yList", "widthList", "heightList");
        for (String property : neededProperties) {
            moveAnimationMap.put(property, propertyMap.get(property).toString());
        }
        return moveAnimationMap;
    }

    private Map<String, List<Double>> getPropertyMap() {
        Map<String, List<Double>> propertyMap = new HashMap<>();
        List<Double> xList = getPropertyList(Rectangle::getX);
        propertyMap.put("xList", xList);
        List<Double> yList = getPropertyList(Rectangle::getY);
        propertyMap.put("yList", yList);
        List<Double> widthList = getPropertyList(Rectangle::getWidth);
        propertyMap.put("widthList", widthList);
        List<Double> heightList = getPropertyList(Rectangle::getHeight);
        propertyMap.put("heightList", heightList);
        return propertyMap;
    }

    private List<Double> getPropertyList(Function<Rectangle, Double> func) {
        return rectangleList.stream().map(func).collect(Collectors.toList());
    }

    /**
     * Remove a rectangle.
     *
     * @param rectangle to be removed
     * @return true if a rectangle was removed
     */
    public boolean removeRectangle(Rectangle rectangle) {
        Iterator<Rectangle> iter = rectangleList.iterator();
        while (iter.hasNext()) {
            Rectangle next = iter.next();
            if (next == rectangle) {
                if (selectedRectangle == rectangle) {
                    selectedRectangle = null;
                }
                iter.remove();
                return true;
            }
        }
        return false;
    }


    /**
     * Handle arrow key input
     *
     * @param event the key event
     */
    public void handleKeyInput(KeyEvent event) {
        if (selectedRectangle == null) {
            return;
        }
        switch (event.getCode()) {
            case RIGHT:
                selectedRectangle.setX(selectedRectangle.getX() + KEY_INPUT_SPEED.get());
                break;
            case LEFT:
                selectedRectangle.setX(selectedRectangle.getX() - KEY_INPUT_SPEED.get());
                break;
            case UP:
                selectedRectangle.setY(selectedRectangle.getY() - KEY_INPUT_SPEED.get());
                break;
            case DOWN:
                selectedRectangle.setY(selectedRectangle.getY() + KEY_INPUT_SPEED.get());
                break;
            default:
        }
    }

    /**
     * Make a rectangle selected.
     *
     * @param rectangleToMakeSelected the rectangle to make selected
     */
    public void makeSelected(Rectangle rectangleToMakeSelected) {
        addSelectEffect(rectangleToMakeSelected);
        selectedRectangle = rectangleToMakeSelected;
        if (rectDrawer.getRectangle() != selectedRectangle) {
            removeSelectEffect(rectDrawer.getRectangle());
        }
        Predicate<Rectangle> notSelected = (rect) -> (rect != selectedRectangle);
        rectangleList.stream().filter(notSelected).forEach(this::removeSelectEffect);
    }

    /**
     * Clone a rectangle.
     *
     * @param rectangleToClone the rectangle to be cloned
     * @return the cloned rectangle
     */
    public Rectangle cloneRect(Rectangle rectangleToClone) {
        Rectangle clone = new Rectangle(rectangleToClone.getX(), rectangleToClone.getY(), rectangleToClone.getWidth(), rectangleToClone.getHeight());
        clone.setFill(Color.TRANSPARENT);
        clone.setStroke(Color.SKYBLUE);
        makeDraggable(clone);
        makeSelectable(clone);
        return clone;
    }

    /**
     * Get an animation using this classes lists.
     *
     * @param imageView the imageView to do an animation on
     * @param duration  the duration
     * @return an animation described by the data in this class
     */
    public Animation getAnimation(ImageView imageView, Duration duration) {
        Map<String, List<Double>> propertyMap = getPropertyMap();
        return new CustomAnimation(imageView, duration,
                rectangleList.size(), propertyMap.get("xList"),
                propertyMap.get("yList"), propertyMap.get("widthList"),
                propertyMap.get("heightList"));
    }


    private void makeSelectable(Rectangle r) {
        r.setOnMouseClicked(e -> makeSelected(r));
    }

    private void addSelectEffect(Rectangle rectangle) {
        rectangle.setStyle(SELECT_EFFECT.get());
        rectangle.setStroke(Color.RED);
    }

    private void removeSelectEffect(Rectangle rectangle) {
        rectangle.setStyle(NO_SELECT_EFFECT.get());
        rectangle.setStroke(Color.SKYBLUE);
    }

    public Rectangle getSelectedRectangle() {
        return selectedRectangle;
    }

    public void setSelectedRectangle(Rectangle selectedRectangle) {
        this.selectedRectangle = selectedRectangle;
    }

    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }

    public List<Label> getLabelList() {
        return labelList;
    }

    public ObservableList<Button> getButtonList() {
        return buttonList;
    }

}