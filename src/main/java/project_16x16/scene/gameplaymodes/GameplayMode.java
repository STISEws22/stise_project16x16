package project_16x16.scene.gameplaymodes;

import project_16x16.entities.Player;
import project_16x16.objects.EditableObject;
import project_16x16.objects.GameObject;
import project_16x16.scene.GameplayScene;
import project_16x16.scene.GameplayScene.GameModes;

public class GameplayMode extends GameMode {

	private Player localPlayer;

	public GameplayMode(GameplayScene gameplayScene, Player localPlayer) {
		super(gameplayScene);
		this.localPlayer = localPlayer;
	}
	
	@Override
	public void enter() {
		scene.setZoomable(true);
	}

	@Override
	public GameModes getModeType() {
		return GameModes.PLAY;
	}

	@Override
	public void updateEditableObject(EditableObject object) {
/* Merge conflict, due to renaming -> belongs to GameMode
	}

	public void displayDestination() {
	}

	public void updateLocalPlayer(Player localPlayer) {
	}

	public void displayGUISlots() {
		scene.displayGUISlots();
	}

	public void updateGUIButton(int xAnchor, PImage activeIcon, PImage inactiveIcon, GameModes mode, boolean isHighlighted) {
		if (getModeType().equals(mode)) {
			drawGUIButton(activeIcon, xAnchor, 120);
		}
		else if (isNotInvalidGUIButtonMode() && isHighlighted) {
			if (scene.applet.isMousePressEvent()) {
				scene.changeMode(mode);
			}
			drawGUIButton(activeIcon, xAnchor, 120);
		}
		else {
			drawGUIButton(inactiveIcon, xAnchor, 120);
/*
		if (object instanceof GameObject) {
			((GameObject) object).update();
		}
	}
	
	@Override
	public void updateLocalPlayer(Player localPlayer) {
		localPlayer.update();
	}

	@Override
	public void updateGUI() {
		localPlayer.displayLife();
	}
}
