package project_16x16.scene;

import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import project_16x16.Constants;
import project_16x16.GameScene;
import project_16x16.ISideScroller;
import project_16x16.ui.Button;

public class MultiplayerMenu extends PScene {

	public Button pressHost;
	public Button pressClient;
	public Button pressMenu;

	private ISideScroller game;

	public MultiplayerMenu(ISideScroller sideScroller) {
		super(sideScroller);
		game = sideScroller;

		pressHost = new Button(sideScroller);
		pressClient = new Button(sideScroller);
		pressMenu = new Button(sideScroller);

		pressHost.setText("Host a game");
		//CTiSE-Larissa: because of use of interface changed access of variable via getter methods
		//original code below (commented out)
//		pressHost.setPosition(applet.width / 2, applet.height / 2 - 240);
		pressHost.setPosition(applet.getWidth() / 2, applet.getHeight() / 2 - 240);
		pressHost.setTextSize(40);
		pressHost.setSize(300, 100);

		pressClient.setText("Connect to a game");
		//CTiSE-Larissa: because of use of interface changed access of variable via getter methods
		//original code below (commented out)
//		pressClient.setPosition(applet.width / 2, applet.height / 2 - 80);
		pressClient.setPosition(applet.getWidth() / 2, applet.getHeight() / 2 - 80);
		pressClient.setTextSize(40);
		pressClient.setSize(400, 100);

		pressMenu.setText("Back to menu");
		//CTiSE-Larissa: because of use of interface changed access of variable via getter methods
		//original code below (commented out)
//		pressMenu.setPosition(applet.width / 2, applet.height / 2 + 240);
		pressMenu.setPosition(applet.getWidth() / 2, applet.getHeight() / 2 + 240);
		pressMenu.setTextSize(40);
		pressMenu.setSize(300, 100);
	}

	@Override
	public void drawUI() {
		background(Constants.Colors.MENU_GREY);
		pressHost.manDisplay();
		pressClient.manDisplay();
		pressMenu.manDisplay();
	}

	public void update() {
		pressHost.update();
		if (pressHost.hover()) {
			game.swapToScene(GameScene.HOST_MENU);
		}

		pressClient.update();
		if (pressClient.hover()) {
			game.swapToScene(GameScene.CLIENT_MENU);
		}

		pressMenu.update();
		if (pressMenu.hover()) {
			game.swapToScene(GameScene.MAIN_MENU);
		}
	}

	@Override
	void mouseReleased(MouseEvent e) {
		update();
	}

	@Override
	void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case PConstants.ESC: // Pause
				game.returnScene();
				break;
			default:
				break;
		}
	}
}
