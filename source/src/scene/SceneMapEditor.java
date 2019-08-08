package scene;

import java.util.ArrayList;

import objects.EditorItem;
import objects.Collision;

import processing.core.*;
import processing.event.MouseEvent;
import scene.components.WorldViewportEditor;
import sidescroller.GameGraphics.Graphic;
import sidescroller.SideScroller;
import ui.Anchor;
import ui.ScrollBarVertical;
import windows.SaveLevelWindow;

public class SceneMapEditor extends PScene {

	// Graphics Slots
	PImage slot;
	PImage slotEditor;

	// Graphics Icon
	PImage icon_eye;
	PImage icon_arrow;
	PImage icon_inventory;
	PImage icon_play;
	PImage icon_save;
	PImage icon_eyeActive;
	PImage icon_arrowActive;
	PImage icon_inventoryActive;
	PImage icon_playActive;
	PImage icon_saveActive;

	// Windows
	public SaveLevelWindow window_saveLevel;

	// Editor Item
	public EditorItem editorItem;

	// Editor Viewport
	public WorldViewportEditor worldViewportEditor;
	
	// Scroll Bar
	public ScrollBarVertical scrollBar;

	public enum Tools {
		MOVE, MODIFY, INVENTORY, PLAY, SAVE,
	}

	public Tools tool;

	public ArrayList<String> inventory;

	public boolean focusedOnObject;

	public boolean edit;

	int scroll_inventory;

	public SceneMapEditor(SideScroller a) {
		super(a);
	}

	@Override
	public void setup() {

		// Create Inventory
		inventory = new ArrayList<String>();
		inventory.add("WEED_WALK_MIDDLE:0");
		inventory.add("WEED_WALK_MIDDLE:1");
		inventory.add("WEED_WALK_MIDDLE:2");
		inventory.add("WEED_WALK_MIDDLE:3");
		inventory.add("WEED_WALK_MIDDLE:4");
		inventory.add("WEED_WALK_MIDDLE:5");

		// Init Editor Components
		editorItem = new EditorItem(applet);
		worldViewportEditor = new WorldViewportEditor(applet);
		
		// Get Slots Graphics
		slot = util.pg(applet.graphicsSheet.get(289, 256, 20, 21), 4);
		slotEditor = util.pg(applet.graphicsSheet.get(310, 256, 20, 21), 4);

		// Get Icon Graphics
		icon_eye = util.pg(applet.graphicsSheet.get(267, 302, 11, 8), 4);
		icon_arrow = util.pg(applet.graphicsSheet.get(279, 301, 9, 9), 4);
		icon_inventory = util.pg(applet.graphicsSheet.get(289, 301, 9, 9), 4);
		icon_play = util.pg(applet.graphicsSheet.get(298, 301, 9, 9), 4);
		icon_save = util.pg(applet.graphicsSheet.get(307, 301, 9, 9), 4);

		icon_eyeActive = util.pg(applet.graphicsSheet.get(267, 292, 11, 8), 4);
		icon_arrowActive = util.pg(applet.graphicsSheet.get(279, 291, 9, 9), 4);
		icon_inventoryActive = util.pg(applet.graphicsSheet.get(289, 291, 9, 9), 4);
		icon_playActive = util.pg(applet.graphicsSheet.get(298, 291, 9, 9), 4);
		icon_saveActive = util.pg(applet.graphicsSheet.get(307, 291, 9, 9), 4);

		// Init Window
		window_saveLevel = new SaveLevelWindow(applet);
		
		// Init ScollBar
		Anchor scrollBarAnchor = new Anchor(applet, -20, 150, 20, 50);
		scrollBarAnchor.anchorOrigin = Anchor.AnchorOrigin.TopRight;
		scrollBarAnchor.stretch = Anchor.Stretch.Vertical;
		scrollBar = new ScrollBarVertical(applet, scrollBarAnchor);
		
		// Default Scene
		applet.collisions.add(new Collision(applet, "METAL_WALK_MIDDLE:0", 0, 0));

		// Default Tool
		tool = Tools.MODIFY;

		util.loadLevel(SideScroller.LEVEL); // TODO change level
	}

	@Override
	public void draw() {
		background(29, 33, 45);

		applet.noStroke();
		applet.fill(29, 33, 45);
//		applet.rect(applet.worldPosition.x, applet.worldPosition.y, applet.worldWidth,
//				applet.worldHeight); // todo

		displayGrid();

		boolean objectFocus = false;

		// View Background Objects
		for (int i = 0; i < applet.backgroundObjects.size(); i++) {
			if (tool == Tools.MODIFY) {
				applet.backgroundObjects.get(i).updateEdit();
			}

			applet.backgroundObjects.get(i).display();

			if (applet.backgroundObjects.get(i).focus && applet.keyPress(8) && applet.keyPressEvent) {
				applet.backgroundObjects.remove(i);
				applet.keyPressEvent = false;
			}
		}

		// View Collisions
		for (int i = 0; i < applet.collisions.size(); i++) {
			if (tool == Tools.MODIFY) {
				applet.collisions.get(i).updateEdit();
				if (applet.collisions.get(i).focus) {
					objectFocus = true;
				}
			}

			applet.collisions.get(i).display();

			if (applet.collisions.get(i).focus && applet.keyPress(8) && applet.keyPressEvent) {
				applet.collisions.remove(i);
				applet.keyPressEvent = false;
			}
		}
		if (tool == Tools.MODIFY) {
			if (!objectFocus) {
				focusedOnObject = false;

				// Loop for new Selection
				for (int i = 0; i < applet.collisions.size(); i++) {
					applet.collisions.get(i).updateEdit();
				}
			}
		}

		// View Game Objects
		for (int i = 0; i < applet.gameObjects.size(); i++) {
			if (tool == Tools.MODIFY) {
				applet.gameObjects.get(i).updateEdit();
				if (applet.gameObjects.get(i).focus) {
					objectFocus = true;
				}
			}

			if (tool == Tools.PLAY) {
				applet.gameObjects.get(i).update();
			}

			applet.gameObjects.get(i).display();

			// Delete
			if (applet.gameObjects.get(i).focus && applet.keyPress(8) && applet.keyPressEvent) {
				applet.gameObjects.remove(i);
				applet.keyPressEvent = false;
			}
		}
		
		// Editor View
		if (tool == Tools.MODIFY) {
			for (int i = 0; i < applet.collisions.size(); i++) {
				applet.collisions.get(i).displayEdit();
			}
			for (int i = 0; i < applet.backgroundObjects.size(); i++) {
				applet.backgroundObjects.get(i).displayEdit();
			}
			for (int i = 0; i < applet.gameObjects.size(); i++) {
				applet.gameObjects.get(i).displayEdit();
			}
		}

		// Editor Object Destination
		if (tool == Tools.MODIFY) {
			editorItem.displayDestination();
		}

		// View Projectiles
		for (int i = 0; i < applet.projectileObjects.size(); i++) {
			applet.projectileObjects.get(i).update();
			applet.projectileObjects.get(i).display();
		}

		// View Player
		if (tool == Tools.PLAY) {
			applet.player.update();
			applet.player.display();
		} else {
			applet.player.display();
		}
		if (tool == Tools.PLAY) {
			applet.player.updateEdit();
			applet.player.displayEdit();
		}
		
		// View Viewport Editor
		worldViewportEditor.updateEditor();
		worldViewportEditor.displayEditor();
	}

	public void drawUI() {

		// GUI Slots
		if (tool != Tools.INVENTORY) {
			for (int i = 0; i < 6; i++) {
				// Display Slot
				image(slot, 20 * 4 / 2 + 10 + i * (20 * 4 + 10), 20 * 4 / 2 + 10);

				// Display Item
				PImage img = applet.gameGraphics.get(inventory.get(i));
				applet.image(img, 20 * 4 / 2 + 10 + i * (20 * 4 + 10), 20 * 4 / 2 + 10, img.width * (float) 0.5,
						img.height * (float) 0.5);

				// Focus Event
				if (applet.mousePressEvent) {
					float x = 20 * 4 / 2 + 10 + i * (20 * 4 + 10);
					float y = 20 * 4 / 2 + 10;
					if (applet.getMouseX() > x - (20 * 4) / 2 && applet.getMouseX() < x + (20 * 4) / 2
							&& applet.getMouseY() > y - (20 * 4) / 2 && applet.getMouseY() < y + (20 * 4) / 2) {
						editorItem.focus = true;
						editorItem.setTile(inventory.get(i));
						editorItem.type = applet.gameGraphics.getType(inventory.get(i));
					}
				}
			}
		}
		
		// GUI Icons
		if (tool == Tools.MOVE || (util.hover(40, 120, 36, 36) && tool != Tools.SAVE && tool != Tools.INVENTORY)) {
			if (util.hover(40, 120, 36, 36) && applet.mousePressEvent) {
				tool = Tools.MOVE;
			}
			image(icon_eyeActive, 40, 120);
		} else {
			image(icon_eye, 40, 120);
		}
		if (tool == Tools.MODIFY || (util.hover(90, 120, 36, 36) && tool != Tools.SAVE && tool != Tools.INVENTORY)) {
			if (util.hover(90, 120, 36, 36) && applet.mousePressEvent) {
				tool = Tools.MODIFY;
			}
			image(icon_arrowActive, 90, 120);
		} else {
			image(icon_arrow, 90, 120);
		}
		if (tool == Tools.INVENTORY
				|| (util.hover(90 + 48, 120, 36, 36) && tool != Tools.SAVE && tool != Tools.INVENTORY)) {
			if (util.hover(90 + 48, 120, 36, 36) && applet.mousePressEvent) {
				tool = Tools.INVENTORY;
			}
			image(icon_inventoryActive, 90 + 48, 120);
		} else {
			image(icon_inventory, 90 + 48, 120);
		}
		if (tool == Tools.PLAY
				|| (util.hover(90 + 48 * 2, 120, 36, 36) && tool != Tools.SAVE && tool != Tools.INVENTORY)) {
			if (util.hover(90 + 48 * 2, 120, 36, 36) && applet.mousePressEvent) {
				tool = Tools.PLAY;
			}
			image(icon_playActive, 90 + 48 * 2, 120);
		} else {
			image(icon_play, 90 + 48 * 2, 120);
		}
		if (tool == Tools.SAVE
				|| (util.hover(90 + 48 * 3, 120, 36, 36) && tool != Tools.SAVE && tool != Tools.INVENTORY)) {
			if (util.hover(90 + 48 * 3, 120, 36, 36) && applet.mousePressEvent) {
				tool = Tools.SAVE;
			}
			image(icon_saveActive, 90 + 48 * 3, 120);
		} else {
			image(icon_save, 90 + 48 * 3, 120);
		}

		// GUI Editor Object
		if (tool == Tools.MODIFY) {
			editorItem.update();
			editorItem.display();
		}

		// Display Inventory
		if (tool == Tools.INVENTORY) {
			displayCreativeInventory();
		}

		// Windows
		if (tool == Tools.SAVE) {
			window_saveLevel.update();
			window_saveLevel.display();
		}

		// Move Tool
		if (tool == Tools.MOVE) {
			if (applet.mousePressed) {
//				applet.originTargetX += applet.pmouseX - applet.getMouseX();
//				applet.originTargetY += applet.pmouseY - applet.getMouseY();
//				applet.originX = applet.originTargetX;
//				applet.originY = applet.originTargetY;
			}
		}

		// Change tool;
		if (tool != Tools.SAVE) {
			if (applet.keyPressEvent) {
				if (applet.keyPress(49)) {
					tool = Tools.MOVE;
					editorItem.setMode("CREATE");
					editorItem.focus = false;
				}
				if (applet.keyPress(50)) {
					tool = Tools.MODIFY;
					editorItem.setMode("CREATE");
					editorItem.focus = false;
				}
				if (applet.keyPress(51)) {
					tool = Tools.INVENTORY;
					editorItem.setMode("ITEM");
					editorItem.focus = false;
					scroll_inventory = 0;
				}
				if (applet.keyPress(52)) {
					tool = Tools.PLAY;
					editorItem.setMode("CREATE");
					editorItem.focus = false;
				}
				if (applet.keyPress(53)) {
					tool = Tools.SAVE;
					editorItem.setMode("CREATE");
					editorItem.focus = false;
				}
				if (applet.keyPress(69)) {
					if (tool == Tools.INVENTORY) {
						tool = Tools.MOVE;
						editorItem.setMode("CREATE");
						editorItem.focus = false;
					} else {
						tool = Tools.INVENTORY;
						editorItem.setMode("ITEM");
						editorItem.focus = false;
						scroll_inventory = 0;
					}
				}
			}
		}
	}

	private void displayCreativeInventory() {// complete creative inventory

		// Display Background
		applet.stroke(50);
		applet.fill(0, 100);
		applet.rect(applet.width / 2, applet.height / 2, applet.width, applet.height);

		// Display Editor Mode Items
		int x = 0;
		int y = 1;
		int index = 0;
		for (Graphic g : applet.gameGraphics.graphics.values()) {
			if (index % 6 == 0) { // show 6 items per row
				x = 0;
				y++;
			} else {
				x++;
			}
			applet.image(slotEditor, 20 * 4 / 2 + 10 + x * (20 * 4 + 10), y * (20 * 4 + 10) + scroll_inventory);
			PImage img = g.image;
			if (img.width > 20 * 4 || img.height > 20 * 4) {
				applet.image(img, 20 * 4 / 2 + 10 + x * (20 * 4 + 10), y * (20 * 4 + 10) + scroll_inventory,
						img.width / 4, img.height / 4);
			} else {
				applet.image(img, 20 * 4 / 2 + 10 + x * (20 * 4 + 10), y * (20 * 4 + 10) + scroll_inventory,
						img.width / 2, img.height / 2);
			}

			// Grab Item
			if (applet.mousePressEvent) {
				float xx = 20 * 4 / 2 + 10 + x * (20 * 4 + 10);
				float yy = y * (20 * 4 + 10) + scroll_inventory;
				if (applet.getMouseY() > 100) {
					if (applet.getMouseX() > xx - (20 * 4) / 2 && applet.getMouseX() < xx + (20 * 4) / 2
							&& applet.getMouseY() > yy - (20 * 4) / 2 && applet.getMouseY() < yy + (20 * 4) / 2) {
						editorItem.focus = true;
						editorItem.setTile(g.name);
					}
				}
			}
			index++;
		}

		
		// Display ScrollBar
		scrollBar.display();
		scrollBar.update();
		scroll_inventory = (int) PApplet.map(scrollBar.barLocation, 1, 0,  -getInventorySize() + applet.height - 8, 0);
		
		// Display Top Bar
		applet.noStroke();
		applet.fill(29, 33, 45);
		applet.rect(applet.width / 2, 50, applet.width, 100);

		// Display Line Separator
		applet.strokeWeight(4);
		applet.stroke(74, 81, 99);
		applet.line(0, 100, applet.width, 100);

		// Display Inventory Slots
		for (int i = 0; i < 6; i++) {
			// Display Slot
			image(slot, 20 * 4 / 2 + 10 + i * (20 * 4 + 10), 20 * 4 / 2 + 10);

			// Display Item
			PImage img = applet.gameGraphics.get(inventory.get(i));
			applet.image(img, 20 * 4 / 2 + 10 + i * (20 * 4 + 10), 20 * 4 / 2 + 10, img.width * (float) 0.5,
					img.height * (float) 0.5);

			// Focus Event
			if (applet.mouseReleaseEvent) {
				float xx = 20 * 4 / 2 + 10 + i * (20 * 4 + 10);
				float yy = 20 * 4 / 2 + 10;
				if (editorItem.focus && applet.getMouseX() > xx - (20 * 4) / 2 && applet.getMouseX() < xx + (20 * 4) / 2
						&& applet.getMouseY() > yy - (20 * 4) / 2 && applet.getMouseY() < yy + (20 * 4) / 2) {
					editorItem.focus = false;
					inventory.set(i, editorItem.id);
				}
			}
		}

		// Display Editor Object
		editorItem.update();
		editorItem.display();
	}

	private void displayGrid() {// world grid
		applet.strokeWeight(1);
		applet.stroke(50);
		int x = 0;
		int y = 0;
		int l = applet.width / (16 * 4) * applet.height / (16 * 4);
		for (int i = 0; i < l; i++) {

			x++;
			if (i % applet.height / (16 * 4) == 0) {
				y++;
				x = 0;
			}
//			applet.line(x * (4 * 16) - (applet.originX % (16 * 4)) - ((4 * 16) / 2), 0,
//					x * (4 * 16) - (applet.originX % (16 * 4)) - ((4 * 16) / 2), applet.height);
//			applet.line(0, y * (4 * 16) - (applet.originY % (16 * 4)) - ((4 * 16) / 2), applet.width,
//					y * (4 * 16) - (applet.originY % (16 * 4)) - ((4 * 16) / 2)); // todo
			
			applet.line(x * (4 * 16) - (0 % (16 * 4)) - ((4 * 16) / 2), 0,
					x * (4 * 16) - (0 % (16 * 4)) - ((4 * 16) / 2), applet.height);
			applet.line(0, y * (4 * 16) - (0 % (16 * 4)) - ((4 * 16) / 2), applet.width,
					y * (4 * 16) - (0 % (16 * 4)) - ((4 * 16) / 2)); // todo
		}
	}

	private float getInventorySize() {
		int y = 1;

		for (int i = 0; i < applet.gameGraphics.graphics.size(); i++) {
			if (i % 6 == 0) {
				y++;
			} else {
			}
		}
		return 20 * 4 + 10 + y * (20 * 4 + 10);
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if (event.isShiftDown()) {
		} else {
			if (tool == Tools.INVENTORY) {
				scrollBar.mouseWheel(event);
				scroll_inventory = (int) PApplet.map(scrollBar.barLocation, 1, 0, -getInventorySize() + applet.height - 8, 0);
			}
		}
	}
}
