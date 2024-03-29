package project_16x16.objects;

import java.lang.reflect.Constructor;

import processing.core.PVector;
import processing.data.JSONObject;
import project_16x16.*;
import project_16x16.scene.GameplayScene;

/**
 * Extends {@link PClass}.
 */
public abstract class EditableObject extends PClass {

    // Base Data
    public PVector position;
    public int width;
    public int height;

    // Image data
    public String id;

    protected ObjectType type;

    // Focus
    private boolean focus;

    /**
     * Child of gameObject.
     */
    public boolean child;

    // Map Editor Scene
    public GameplayScene gameplayScene;

    protected PVector editOffset;

    public EditableObject(ISideScroller sideScroller, GameplayScene gameplayScene) {
        super(sideScroller);

        position = new PVector(0, 0);
        editOffset = new PVector(0, 0);
        this.gameplayScene = gameplayScene;
    }

    public abstract void display();

    public abstract void debug();

    public JSONObject exportToJSON(){
        JSONObject item = new JSONObject();
        item.setString("id", id);
        item.setString("type", type.toString());
        item.setInt("x", (int) position.x);
        item.setInt("y", (int) position.y);
        return item;
    }

    /**
     * Draws position edit arrows and bounding box if the object is selected
     * (focused) in MODIFY mode.
     */
    public void displayEdit() {
        if (focus) { // focus = held by player
            // draw border around object
            applet.strokeWeight(10);
            applet.noFill();
            applet.stroke(0, 255, 200);
            applet.rect(position.x, position.y, width, height);
            applet.strokeWeight(4); // reset style
        }
    }

    /**
     * Called during modify mode when mouse pressed
     */
    public void updateEdit() {
        if (child) {
            return;
        }
        if (applet.isMouseReleaseEvent() && applet.getMouseButton() == LEFT) {
            focus = false;
            return;
        }
        // Focus Event
        focusEvent();
    }

    private void focusEvent() {
        focusOnMouseIfLeftClick();
        if (focus) { // When Focused
            if (applet.isMousePressEvent() && mouseHover()) {
                focus = true;
                editOffset = PVector.sub(position, applet.getMouseCoordGame());
            }
            // Duplicate Object Shift
            duplicateObjectShift();

            if (focus && applet.getMousePressed() && applet.getMouseButton() == LEFT) {
                position = new PVector(Utility.roundToNearest(applet.getMouseCoordGame().x + editOffset.x, SideScroller.snapSize), Utility.roundToNearest(applet.getMouseCoordGame().y + editOffset.y, SideScroller.snapSize));
            }
        }
    }

    private void duplicateObjectShift() {
        if (applet.isKeyPressEvent() && applet.isKeyDown(SideScroller.SHIFT)) {
            switch (type) {
                case COLLISION:
                    handleCollision();
                    break;
                case OBJECT:
                    handleObject();
                    break;
                default:
                    break;
            }
            applet.setKeyPressEvent(false);
            focus = false;
        }
    }

    private void handleObject() {
        EditableObject copy;
        try {
            Class<? extends AnimatedObject> gameObjectClass = Tileset.getObjectClass(id);
            Constructor<?> ctor = gameObjectClass.getDeclaredConstructors()[0];
            copy = (AnimatedObject) ctor.newInstance(new Object[]{applet, this});
            copy.focus = true;
            copy.position = position.copy();
            copy.editOffset = editOffset.copy();
            gameplayScene.objects.add(copy);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("MIRROR_BOX".equals(id)) {
            ((MirrorBoxObject) gameplayScene.objects.get(gameplayScene.objects.size() - 1)).direction = ((MirrorBoxObject) this).direction;
        }
    }

    private void handleCollision() {
        EditableObject copy;
        copy = new GameObject(applet, gameplayScene, id, 0, 0);
        copy.focus = true;
        copy.position = position.copy();
        copy.editOffset = editOffset.copy();
        gameplayScene.objects.add(copy);
    }

    private void focusOnMouseIfLeftClick() {
        if (applet.isMousePressEvent() && applet.getMouseButton() == LEFT && !focus && mouseHover()) {
            // Focus Enable
            if (gameplayScene.focusedObject == null) {
                focus = true;
                gameplayScene.focusedObject = this;

            }
        }
    }

    public void focus() {
        editOffset = PVector.sub(position, applet.getMouseCoordGame());
        focus = true;
    }

    public void unFocus() {
        focus = false;
    }

    public boolean isFocused() {
        return focus;
    }

    public boolean mouseHover() {
        if (applet.getMouseX() < 400 && applet.getMouseY() < 100) { // Over Inventory Bar -- rough approximation
            return false;
        }
        return Utility.hoverGame(position.x, position.y, width, height);
    }
}
