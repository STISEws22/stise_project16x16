package project_16x16.entities;

import java.awt.event.KeyEvent;

import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;
import project_16x16.SideScroller;
import project_16x16.SideScroller.DebugType;
import project_16x16.Tileset;
import project_16x16.Utility;
import project_16x16.objects.CollidableObject;
import project_16x16.objects.EditableObject;
import project_16x16.scene.GameplayScene;

/**
 * <h1>Enemy Class</h1>
 * <p>
 * This class handles the enemy parent behavior.
 * </p>
 */
public class Enemy extends CollidableObject {

	private static final int OUT_OF_BOUNDS_DISTANCE = 2000;

	private PImage image;
	final PVector velocity = new PVector(0, 0);

	public int health;
	float gravity;

	final int speedWalk;
	final int speedJump;

	private final int collisionRange;

	EntityState enemyState;

	/**
	 * Constructor
	 * 
	 * @param sideScroller SideScroller game controller.
	 */
	public Enemy(SideScroller sideScroller, GameplayScene gameplayScene, int health, float gravity, int speedWalk, int speedJump,
				 int width, int height, int collisionRange) {
		super(sideScroller, gameplayScene);
		image = Tileset.getTile(0, 258, 14, 14, 4);
		this.health = health; //2
		this.gravity = gravity; //1
		this.speedWalk = speedWalk; //7
		this.speedJump = speedJump; //18
		this.width = width;
		this.height = height;
		this.collisionRange = collisionRange;
		enemyState = new EntityState();
	}

	/**
	 * The display method controls how to display the character to the screen with
	 * what animation.
	 */
	public void display() {
		applet.pushMatrix();
		applet.translate(position.x, position.y);
		if (enemyState.facingDir == LEFT) {
			applet.scale(-1, 1);
		}
		applet.image(image, 0, 0);
		applet.noTint();
		applet.popMatrix();
		if (applet.debug == DebugType.ALL) {
			applet.strokeWeight(1);
			applet.stroke(0, 255, 200);
			applet.noFill();
			applet.rect(position.x, position.y, width, height); // display player bounding box
		}
	}

	/**
	 * The update method handles updating the character.
	 */
	public void update() {
		// velocity.set(0, velocity.y + gravity);

		checkEnemyCollision();
		if (velocity.y != 0) {
			enemyState.flying = true;
		}
		position.add(velocity);
		if (position.y > OUT_OF_BOUNDS_DISTANCE) { // out of bounds check
			// Destroy(gameObject);
		}
		if (applet.debug == DebugType.ALL) {
			applet.noFill();
			applet.stroke(255, 0, 0);
			applet.strokeWeight(1);
			applet.ellipse(position.x, position.y, collisionRange * 2, collisionRange * 2);
		}
	}

	public PVector getVelocity() {
		return velocity.copy();
	}

	public EntityState getState() {
		return enemyState;
	}

	protected void checkEnemyCollision() {
		for (EditableObject o : gameplayScene.objects) {
			if (o.equals(this)) {
				continue;
			}
			if (o instanceof CollidableObject) {
				CollidableObject collision = (CollidableObject) o;
				if (Utility.fastInRange(position, collision.position, collisionRange)) { // In Player Range
					if (applet.debug == DebugType.ALL) {
						applet.strokeWeight(2);
						applet.rect(collision.position.x, collision.position.y, collision.width, collision.height);
						applet.fill(255, 0, 0);
						applet.ellipse(collision.position.x, collision.position.y, 5, 5);
						applet.noFill();
					}
					if (collidesFuturX(collision)) {
						// enemy left of collision
						if (position.x < collision.position.x) {
							position.x = collision.position.x - collision.width / 2 - width / 2;
							// enemy right of collision
						}
						else {
							position.x = collision.position.x + collision.width / 2 + width / 2;
						}
						velocity.x = 0;
						enemyState.dashing = false;
					}
					if (collidesFuturY(collision)) {
						// enemy above collision
						if (position.y < collision.position.y) {
							if (enemyState.flying) {
								enemyState.landing = true;
							}
							position.y = collision.position.y - collision.height / 2 - height / 2;
							enemyState.flying = false;
							// enemy below collision
						}
						else {
							position.y = collision.position.y + collision.height / 2 + height / 2;
							enemyState.jumping = false;
						}
						velocity.y = 0;
					}
				}
			}
		}
	}

	/**
	 * 
	 * Determines is the character has collided with an object of type Collision.
	 * 
	 * @param collision The other object
	 * @return boolean if it has or has not collided with the object.
	 */

	protected boolean collidesFuturX(CollidableObject collision) {
		return (position.x + velocity.x + width / 2 > collision.position.x - collision.width / 2
				&& position.x + velocity.x - width / 2 < collision.position.x + collision.width / 2)
				&& (position.y + 0 + height / 2 > collision.position.y - collision.height / 2
						&& position.y + 0 - height / 2 < collision.position.y + collision.height / 2);
	}

	protected boolean collidesFuturY(CollidableObject collision) {
		return (position.x + 0 + width / 2 > collision.position.x - collision.width / 2
				&& position.x + 0 - width / 2 < collision.position.x + collision.width / 2)
				&& (position.y + velocity.y + height / 2 > collision.position.y - collision.height / 2
						&& position.y + velocity.y - height / 2 < collision.position.y + collision.height / 2);
	}

	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}

	@Override
	public JSONObject exportToJSON() {
		// TODO Auto-generated method stub
		return null;
	}
}
