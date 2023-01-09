package project_16x16.entities;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import project_16x16.SideScroller;
import project_16x16.Tileset;
import project_16x16.scene.GameplayScene;

import java.util.ArrayList;

public class CleanerRobotTest {

    @Mock
    private SideScroller sideScroller;
    @Mock
    private GameplayScene scene;
    @Mock
    private PImage image;

    private CleanerRobot robot;

    private final float distance = 50f;

    @BeforeEach
    public void before(){
        scene = Mockito.mock(GameplayScene.class);
        sideScroller = Mockito.mock(SideScroller.class);
        scene.objects = new ArrayList<>();
        try(MockedStatic<Tileset> tileset_mocked = Mockito.mockStatic(Tileset.class)){
            tileset_mocked.when(() -> Tileset.getTile(0, 258, 14, 14, 4))
                    .thenReturn(image);
            robot = new CleanerRobot(sideScroller,scene,new PVector(distance,0),new PVector(-distance,0));
        }
    }

    @Test
    public void constructorTest(){
        Assert.assertNotNull(robot);
    }

    @Test
    public void updateTestVelocity(){
        robot.update();
        Assert.assertEquals((float)robot.speedWalk, robot.velocity.x);
    }

    @Test
    public void updateTestReachTarget(){
        robot.velocity.set(0,0);
        robot.position.x = distance;
        robot.update(); // Change target
        robot.update(); // change direction
        Assert.assertEquals(PConstants.LEFT,robot.enemyState.facingDir);
    }

    @Test
    public void getDistanceTestFirstDimension(){
        PVector v1 = new PVector(1,0);
        PVector v2 = new PVector(5,0);
        Assert.assertEquals(4d, robot.getDistance(v1,v2));
    }

    @Test
    public void getDistanceTestSecondDimension(){
        PVector v1 = new PVector(0,1);
        PVector v2 = new PVector(0,5);
        Assert.assertEquals(4d, robot.getDistance(v1,v2));
    }

    @Test
    public void getDistanceTestBothDimensions(){
        PVector v1 = new PVector(1,1);
        PVector v2 = new PVector(5,5);
        Assert.assertEquals(4*Math.sqrt(2), robot.getDistance(v1,v2));
    }

    @Test
    public void getDistanceTest2DimensionsOnly(){
        PVector v1 = new PVector(1,1,3);
        PVector v2 = new PVector(5,5,3);
        PVector v3 = new PVector(1,1,2);
        PVector v4 = new PVector(5,5,3);
        Assert.assertEquals(robot.getDistance(v1,v2), robot.getDistance(v3,v4));
    }

    @Test
    public void getDistanceTestWithNegative(){
        PVector v1 = new PVector(-1,-2);
        PVector v2 = new PVector(3,2);
        Assert.assertEquals(4*Math.sqrt(2),robot.getDistance(v1,v2));
    }

    @Test
    public void getDistanceTestWith0(){
        PVector v1 = new PVector(4,4);
        PVector v2 = new PVector(0,0);
        Assert.assertEquals(4*Math.sqrt(2),robot.getDistance(v1,v2));
    }

    @Test
    public void getDistanceTestNull(){
        PVector v1 = new PVector(4,4);
        Assertions.assertThrows(NullPointerException.class,() -> robot.getDistance(v1,null));
    }

}
