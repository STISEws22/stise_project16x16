package project_16x16.scene.gameplaymodes;

import project_16x16.scene.GameplayScene;
import project_16x16.scene.GameplayScene.GameModes;

public class TestGameMode extends GameMode {

	public TestGameMode(GameplayScene gameplayScene) {
		super(gameplayScene);
	}

	@Override
	public GameModes getModeType() {
		return GameModes.TEST;
	}

}
