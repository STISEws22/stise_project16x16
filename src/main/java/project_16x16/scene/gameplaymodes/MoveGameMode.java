package project_16x16.scene.gameplaymodes;

import project_16x16.scene.GameplayScene;
import project_16x16.scene.GameplayScene.GameModes;

public class MoveGameMode extends GameMode {

	public MoveGameMode(GameplayScene gameplayScene) {
		super(gameplayScene);
	}

	@Override
	public GameModes getModeType() {
		return GameModes.MOVE;
	}

}
