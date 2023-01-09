package project_16x16.entities;

import processing.core.PConstants;

public class EntityState {

    public boolean flying;
    public boolean attacking;
    public boolean dashing;
    public int facingDir;
    public boolean landing;
    public boolean jumping;

    public EntityState(){
        flying = false;
        attacking = false;
        dashing = false;
        facingDir = PConstants.RIGHT;
        jumping = false;
        landing = false;
    }
}
