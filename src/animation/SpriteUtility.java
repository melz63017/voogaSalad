package animation;

import javafx.animation.Animation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import view.Dragger;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import static animation.SaveHandler.saveImage;

class SpriteUtility {
	private static final String SAVE_ANIMATIONS_TO_FILE = "Save Animations to File";
	private static final String NEW_ANIMATION = "New Animation";
	private static final String NEW_SPRITE = "New Sprite";
	private static final String PREVIEW_ANIMATION = "Preview Animation";
	private static final String SAVE_ANIMATION = "Save Animation";
	private static final String ADD_FRAME = "Add Frame";
	private static final String DELETE_FRAME = "Delete Frame";

    private static final double DURATION_MIN = 100;
    private static final double DURATION_MAX = 3000;
    private static final double DURATION_DEFAULT = 1000;
    private static final double KEY_INPUT_SPEED = 5;

    private static final String SELECT_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,50,0.8), 10, 0, 0, 0)";
    private static final String NO_SELECT_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0), 0, 0, 0, 0)";
    
    private BorderPane mainPane;
    private VBox animationPropertiesBox;
    private VBox buttonBox;

    private Map<String, Map> animationList;
    private List<Rectangle> rectangleList;

    private ImageView spriteImageView;
    private ImageView previewImageView;

    private TextField animationName;

    private List<Double> widthList;
    private List<Double> heightList;
    private List<Double> yList;
    private List<Double> xList;
    private DoubleProperty rectinitX;
    private DoubleProperty rectinitY;
    private DoubleProperty rectX;
    private DoubleProperty rectY;

    private Slider durationSlider;

    private Group spriteGroup;
    private Rectangle selectedRectangle;
    private Rectangle rectDrawer;

    private final SimpleObjectProperty<Boolean> changeColorProperty = new SimpleObjectProperty<>(this, "ChangeColor", false);
    private Button activateTransparencyButton;
    private Canvas canvas;
    private Scene scene;
    private Image spriteImage;
	private ScrollPane spriteScroll;

    public void init(Stage stage, Dimension2D dimensions) {
        mainPane = new BorderPane();
        scene = new Scene(mainPane, dimensions.getWidth(), dimensions.getHeight());
        stage.setScene(scene);
        initializeGui();
    }

    private void initializeGui() {
    		selectedRectangle = null;
    	
        rectangleList = new ArrayList<>();
        animationList = new HashMap<>();

        buttonBox = new VBox();
        animationPropertiesBox = new VBox();
        spriteGroup = new Group();
        spriteScroll = new ScrollPane(spriteGroup);

        initNewImage();
        initRectangleDrawer();
        initAnimationProperties();
        initButtons();

        mainPane.setCenter(spriteScroll);
        mainPane.setRight(new ScrollPane(buttonBox));
        mainPane.setLeft(new ScrollPane(animationPropertiesBox));
    }

    private void initButtons() {
    		addButton(UtilityUtilities.makeButton(SAVE_ANIMATIONS_TO_FILE, e -> reInitialize()), buttonBox);
        addButton(UtilityUtilities.makeButton(NEW_ANIMATION, e -> reInitialize()), buttonBox);
        addButton(UtilityUtilities.makeButton(NEW_SPRITE, e -> initializeGui()), buttonBox);
        addButton(UtilityUtilities.makeButton(PREVIEW_ANIMATION, e -> animationPreview()), buttonBox);
        addButton(UtilityUtilities.makeButton(SAVE_ANIMATION, e -> saveAnimation()), buttonBox);
        addButton(UtilityUtilities.makeButton(ADD_FRAME, e -> addFrame()), buttonBox);
        addButton(UtilityUtilities.makeButton(DELETE_FRAME, e -> deleteFrame(selectedRectangle)), buttonBox);
        activateTransparencyButton = UtilityUtilities.makeButton("Activate Transparency", e -> makeTransparent());
        addButton(activateTransparencyButton, buttonBox);
        addButton(UtilityUtilities.makeButton("Save Image", e -> saveImage(spriteImageView.getImage())), buttonBox);
    }
    
    private void addButton(Button button, VBox box) {
        button.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().add(button);
    }

    private void makeTransparent() {
        if (changeColorProperty.get()) {
            activateTransparencyButton.setText("Activate Transparency");
            changeColorProperty.set(false);
            canvas.setCursor(Cursor.DEFAULT);

        } else {
            activateTransparencyButton.setText("Deactivate Transparency");
            changeColorProperty.set(true);
            canvas.setCursor(Cursor.CROSSHAIR);


        }
    }

    private void deleteFrame(Rectangle frameToDelete) {
        if (frameToDelete != null) {
            for (Rectangle existingRect : rectangleList) {
                if (existingRect == frameToDelete) {
                    if (selectedRectangle == frameToDelete) {
                        selectedRectangle = null;
                    }
                    spriteGroup.getChildren().remove(frameToDelete);
                    rectangleList.remove(existingRect);
                    initAnimationProperties();

                    break;
                }
            }
        }
    }

    /*
     * Initialize all animation gui elements (i.e. left side of pane)
     */
    private void initAnimationProperties() {
        buttonBox.getChildren().remove(previewImageView);
        animationPropertiesBox.getChildren().clear();
        initAnimationNameAndDurationFields();
        rectangleList.stream().forEach(this::displayRectangleListProperties);
    }




    /*
     * Initialize sprite sheet gui properties like animation name and duration
     */
    private void initAnimationNameAndDurationFields() {
        animationName = new TextField("Animation Name");
        durationSlider = new Slider(DURATION_MIN, DURATION_MAX, DURATION_DEFAULT);
        Label durationTextLabel = new Label("Duration");
        Label durationValueLabel = new Label(String.format("%.2f", durationSlider.getValue()));
        durationSlider = UtilityUtilities.makeSlider((ov, old_val, new_val) -> {
            durationValueLabel.setText(String.format("%.2f", new_val));
            initAnimationPreview();
        }, DURATION_MIN, DURATION_MAX, DURATION_DEFAULT);
        animationPropertiesBox.getChildren().addAll(animationName, durationTextLabel, durationSlider,
                durationValueLabel);
    }

    private void reInitialize() {
        spriteGroup.getChildren().removeAll(rectangleList);
        rectangleList = new ArrayList<>();
        animationList = new TreeMap<>();
        initAnimationProperties();

    }

    private void initNewImage() {
        spriteImage = initFileChooser();
        spriteImageView = new ImageView(spriteImage);
        spriteGroup.getChildren().add(spriteImageView);
        canvas = new Canvas(spriteImageView.getBoundsInParent().getWidth(), spriteImageView.getBoundsInParent().getHeight());
        canvas.layoutXProperty().set(spriteImageView.getLayoutX());
        canvas.layoutYProperty().set(spriteImageView.getLayoutY());

        initCanvasHandlers(canvas);
        spriteGroup.getChildren().add(canvas);

    }

    private void initCanvasHandlers(Canvas canvas2) {
        canvas2.setOnMouseDragged(this::mouseDragged);
        canvas2.setOnMousePressed(this::mousePressed);
        canvas2.setOnMouseReleased(this::mouseReleased);
        canvas2.setOnMouseClicked(this::mouseClicked);
    }

    private void animationPreview() {
        populateRectanglePropertyLists();
        initAnimationPreview();
    }

    private void initAnimationPreview() {
        buttonBox.getChildren().remove(previewImageView);
        previewImageView = new ImageView(spriteImage);
        Animation animation = new ComplexAnimation(previewImageView, Duration.millis(durationSlider.getValue()),
                rectangleList.size(), xList, yList, widthList, heightList);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        buttonBox.getChildren().add(previewImageView);
    }

    private void saveAnimation() {
        populateRectanglePropertyLists();
        TreeMap<String, String> moveAnimationMap = new TreeMap<>();
        String name = animationName.getText();
        moveAnimationMap.put("Duration", String.format("%.2f", durationSlider.getValue()));
        moveAnimationMap.put("X", convertListToString(xList));
        moveAnimationMap.put("Y", convertListToString(yList));
        moveAnimationMap.put("Width", convertListToString(widthList));
        moveAnimationMap.put("Height", convertListToString(heightList));
        animationList.put(name, moveAnimationMap);
        System.out.println(animationList);
    }

    private String convertListToString(List<Double> list) {
        StringBuilder str = new StringBuilder();
        for (Double value : list) {
            str.append(value.toString()).append(",");
        }
        return str.toString();
    }

    /*
     * 
     */
    private void populateRectanglePropertyLists() {
        xList = new ArrayList<>();
        yList = new ArrayList<>();
        widthList = new ArrayList<>();
        heightList = new ArrayList<>();
        for (Rectangle rect : rectangleList) {
            xList.add(rect.xProperty().get());
            yList.add(rect.yProperty().get());
            widthList.add(rect.widthProperty().get());
            heightList.add(rect.heightProperty().get());
        }
    }

    private void addFrame() {
        Rectangle clone = cloneRect(rectDrawer);
        rectangleList.add(clone);
        addRectangleToDisplay(clone);
    }

    private void addRectangleToDisplay(Rectangle clone) {
        spriteGroup.getChildren().add(clone);
        displayRectangleListProperties(clone);
    }

    /*
     * Puts properties of all frames on GUI i.e. x, y, width, height; Makes them editable
     */
    private void displayRectangleListProperties(Rectangle clone) {
        List<String> propertyList = Arrays.asList("x", "y", "width", "height");

        for (String propertyName : propertyList) {
            Label label = new Label(propertyName);
            TextField field = new TextField();
            field.setMinWidth(50);
            animationPropertiesBox.getChildren().addAll(label, field);
            try {
                Method method = clone.getClass().getMethod(propertyName + "Property");
                StringConverter<Number> converter = new NumberStringConverter();
                DoubleProperty rectProperty;
                rectProperty = (DoubleProperty) method.invoke(clone);
                Bindings.bindBidirectional(field.textProperty(), rectProperty, converter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Rectangle cloneRect(Rectangle rect) {
        Rectangle r = new Rectangle();
        r.setX(rect.getX());
        r.setY(rect.getY());
        r.setWidth(rect.getWidth());
        r.setHeight(rect.getHeight());
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.RED);
        Dragger.makeDraggable(r);
        makeSelectable(r);
        return r;
    }

    private void makeSelectable(Rectangle r) {
        r.setOnMouseClicked(e -> makeSelectable(r));
    }

    private void makeSelected(Rectangle r) {
        addSelectEffect(r);
        selectedRectangle = r;
        if (rectDrawer != selectedRectangle) {
            removeSelectEffect(rectDrawer);
        }
        Predicate<Rectangle> notSelected = (rect) -> (rect != selectedRectangle);
        rectangleList.stream().filter(notSelected).forEach(this::removeSelectEffect);
    }


    private void addSelectEffect(Rectangle img) {
        img.setStyle(SELECT_EFFECT);
    }

    private void removeSelectEffect(Rectangle imageView) {
        imageView.setStyle(NO_SELECT_EFFECT);
    }

    /*
     * Initializes initial rectangle drawer
     */
    private Rectangle initRectangleDrawer() {
        spriteGroup.getChildren().remove(rectDrawer);
        rectDrawer = new Rectangle();
        rectDrawer.widthProperty().unbind();
        rectDrawer.heightProperty().unbind();
        rectinitX = new SimpleDoubleProperty();
        rectinitY = new SimpleDoubleProperty();
        rectX = new SimpleDoubleProperty();
        rectY = new SimpleDoubleProperty();
        rectDrawer.widthProperty().bind(rectX.subtract(rectinitX));
        rectDrawer.heightProperty().bind(rectY.subtract(rectinitY));
        rectDrawer.setFill(Color.TRANSPARENT);
        rectDrawer.setStroke(Color.BLACK);

        spriteScroll.requestFocus(); //ugh someone fix this
        spriteScroll.setOnKeyPressed(this::keyPress); //this line keeps fucking up
        makeSelected(rectDrawer);
        spriteGroup.getChildren().add(rectDrawer);
        

        return rectDrawer;
    }

    private void keyPress(KeyEvent event) {
        KeyCode keycode = event.getCode();
        switch (keycode) {
            case ENTER:
                addFrame();
                break;
            case RIGHT:
                selectedRectangle.setX(selectedRectangle.getX() + KEY_INPUT_SPEED);
                event.consume();
                break;
            case LEFT:
                selectedRectangle.setX(selectedRectangle.getX() - KEY_INPUT_SPEED);
                event.consume();
                break;
            case UP:
                selectedRectangle.setY(selectedRectangle.getY() - KEY_INPUT_SPEED);
                event.consume();
                break;
            case DOWN:
                selectedRectangle.setY(selectedRectangle.getY() + KEY_INPUT_SPEED);
                event.consume();
                break;
            default:
        }
        event.consume();
    }

    private Image initFileChooser() {
        File spriteSheet = UtilityUtilities.promptAndGetFile(new FileChooser.ExtensionFilter("All Images", "*.*"),
                "Choose a spritesheet");
        return new Image(spriteSheet.toURI().toString());
    }


    private void mouseReleased(MouseEvent event) {
        rectDrawer.widthProperty().unbind();
        rectDrawer.heightProperty().unbind();
        Dragger.makeDraggable(rectDrawer);
        spriteImageView.setCursor(Cursor.DEFAULT);
        if (changeColorProperty.get()) {
            canvas.setCursor(Cursor.CROSSHAIR);
        } else {
            canvas.setCursor(Cursor.DEFAULT);
        }


    }

    private void mousePressed(MouseEvent event) {
        if (!changeColorProperty.get()) {
            canvas.setCursor(Cursor.CLOSED_HAND);
            if (event.getButton() == MouseButton.PRIMARY) {
                rectDrawer = initRectangleDrawer();
                rectDrawer.setX(event.getX());
                rectDrawer.setY(event.getY());
                rectinitX.set(event.getX());
                rectinitY.set(event.getY());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                rectDrawer.setX(event.getX());
                rectDrawer.setY(event.getY());
            }
        }
    }

    private void mouseDragged(MouseEvent event) {
        rectX.set(event.getX());
        rectY.set(event.getY());
    }

    private void mouseClicked(MouseEvent event) {
        if (changeColorProperty.get()) {
            double x = event.getX();
            double y = event.getY();
            spriteImage = new ColorChanger(spriteImageView.getImage(), x, y, Color.TRANSPARENT).changeImage();
            spriteImageView.setImage(spriteImage);
        }
    }

}
