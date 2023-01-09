package project_16x16.entities;

import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import processing.core.PImage;
import processing.core.PVector;
import project_16x16.SideScroller;
import project_16x16.Tileset;
import project_16x16.objects.CollidableObject;
import project_16x16.objects.EditableObject;
import project_16x16.scene.GameplayScene;

import java.util.ArrayList;

public class EnemyTest {

    @Mock
    private SideScroller sideScroller;
    @Mock
    private GameplayScene scene;
    @Mock
    private PImage image;

    private Enemy enemy;
    private ArrayList<EditableObject> objectList;

    @BeforeEach
    public void before(){
        scene = Mockito.mock(GameplayScene.class);
        sideScroller = Mockito.mock(SideScroller.class);
        objectList = new ArrayList<>();
        scene.objects = objectList;
        try(MockedStatic<Tileset> tileset_mocked = Mockito.mockStatic(Tileset.class)){
            tileset_mocked.when(() -> Tileset.getTile(0, 258, 14, 14, 4))
                    .thenReturn(image);
            enemy = new Enemy(sideScroller,scene,2,1,7,18,56,40,145);
        }
    }

    @Test
    public void constructorTest(){
        Assert.assertNotNull(enemy);
    }

    @Test
    public void displayTest(){
        enemy.display();
    }

    @Test
    public void updateTestNoStateChange(){
        enemy.update();
        Assert.assertFalse(enemy.enemyState.flying);
    }

    @Test
    public void updateTestStateChangeFlying(){
        enemy.velocity.y = 1;
        enemy.update();
        Assert.assertTrue(enemy.enemyState.flying);
    }

    @Test
    public void getVelocityTest(){
        PVector vel = enemy.getVelocity();
        Assert.assertEquals(vel.x,enemy.velocity.x);
        Assert.assertEquals(vel.y,enemy.velocity.y);
        Assert.assertEquals(vel.z,enemy.velocity.z);
        Assert.assertNotSame(vel,enemy.velocity);
    }

    @Test
    public void getStateTest(){
        Assert.assertEquals(enemy.enemyState,enemy.getState());
    }

    // Note: Enemy Collision Variables
    //    width = (56)
    //    height = (40)
    //    collisionRange = 145

    @Test
    public void checkEnemyCollisionTestIgnoresSelf(){
        enemy.velocity.set(5f,5f);
        objectList.add(enemy);
        enemy.checkEnemyCollision();
        Assert.assertEquals(5f, enemy.velocity.x);
        Assert.assertEquals(5f, enemy.velocity.y);
    }

    @Test
    public void checkEnemyCollisionTestDashingXCollision(){
        enemy.position.set(0,0);
        enemy.velocity.set(50f,0f);
        enemy.enemyState.dashing = true;
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,160,100,100,0);
        objectList.add(collisionObject);
        enemy.checkEnemyCollision();
        Assert.assertEquals(enemy.velocity.x,0f);
        Assert.assertFalse(enemy.enemyState.dashing);
        Assert.assertEquals(collisionObject.position.x-collisionObject.width/2-enemy.width/2,enemy.position.x);
    }

    @Test
    public void checkEnemyCollisionTestFlyingYCollision(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,50f);
        enemy.enemyState.flying = true;
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,160,100,0,100);
        objectList.add(collisionObject);
        enemy.checkEnemyCollision();
        Assert.assertEquals(enemy.velocity.y,0f);
        Assert.assertFalse(enemy.enemyState.flying);
        Assert.assertTrue(enemy.enemyState.landing);
        Assert.assertEquals(collisionObject.position.y-collisionObject.height/2-enemy.height/2,enemy.position.y);
    }

    @Test
    public void checkEnemyCollisionTestJumpingYCollision(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,-50f);
        enemy.enemyState.jumping = true;
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,160,100,0,-100);
        objectList.add(collisionObject);
        enemy.checkEnemyCollision();
        Assert.assertEquals(enemy.velocity.y,0f);
        Assert.assertFalse(enemy.enemyState.jumping);
        Assert.assertEquals(collisionObject.position.y+collisionObject.height/2+enemy.height/2,enemy.position.y);
    }

    @Test
    public void collidesFuturXNoCollision(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,10,40,33,0);
        Assert.assertFalse(enemy.collidesFuturX(collisionObject));
    }

    @Test
    public void collidesFuturXCollisionThroughPositiveVelocity(){
        enemy.position.set(0,0);
        enemy.velocity.set(5f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,10,40,33,0);
        Assert.assertTrue(enemy.collidesFuturX(collisionObject));
    }

    @Test
    public void collidesFuturXCollisionThroughNegativeVelocity(){
        enemy.position.set(0,0);
        enemy.velocity.set(-5f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,10,40,-33,0);
        Assert.assertTrue(enemy.collidesFuturX(collisionObject));
    }

    @Test
    public void collidesFuturXCollisionThroughPosition(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,10,40,32,0);
        Assert.assertTrue(enemy.collidesFuturX(collisionObject));
    }
    @Test
    public void collidesFuturYNoCollision(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,56,10,0,25);
        Assert.assertFalse(enemy.collidesFuturY(collisionObject));
    }

    @Test
    public void collidesFuturYCollisionThroughPositiveVelocity(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,5f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,56,10,0,25);
        Assert.assertTrue(enemy.collidesFuturY(collisionObject));
    }

    @Test
    public void collidesFuturYCollisionThroughNegativeVelocity(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,-5f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,56,10,0,-25);
        Assert.assertTrue(enemy.collidesFuturY(collisionObject));
    }

    @Test
    public void collidesFuturYCollisionThroughPosition(){
        enemy.position.set(0,0);
        enemy.velocity.set(0f,0f);
        CollidableObject collisionObject = new CollidableObject(sideScroller,scene,56,10,0,24);
        Assert.assertTrue(enemy.collidesFuturY(collisionObject));
    }

}
