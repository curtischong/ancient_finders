import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class EnemyArcher extends Enemy
{
    private GreenfootImage myImage;
    //variable for shoot cooldown
    private int shootCounter;
    public EnemyArcher(){
        //health of archer
        health = 20;
        //movement speed
        speed = 0.45;
        //set its attack state (0 = shoot arrow inbetween 1-250 then it's on cooldown and doesn't do anything)
        shootCounter = 0;
    }

    public void act() 
    {
        //tell it to move to it's assigned coords
        moveTo();
        //locate nearest emeny to attack (internal ai)
        findNearestAlly();
        //see if it is alive and tell the method what type of object it is
        checkAlive("EnemyArcher");

        //if the shoot counter has seen the enemy for 250 acts, then it is ready to shoot
        if(shootCounter == 250){
            shoot();
            shootCounter = 0;
        }
    }

    private void findNearestAlly(){
        //will look for nearby allies to target
        ArrayList closeAlly  = (ArrayList)getObjectsInRange(200, Ally.class);
        //if at least 1 is found and the user has not told it to move to a location, it will run it's targetting ai 
        if(closeAlly.size() > 0 && this.selectedMove == false){
            //find closest Ally
            MyWorld m = (MyWorld)getWorld();
            Ally targetAlly = (Ally)m.getClosest(this,closeAlly);

            //the archer will look at the ally
            this.turnTowards(targetAlly.getX(),targetAlly.getY());
            shootCounter++;
        }else{
            shootCounter = 0;
        }
    }

    //this will create a new arrow object within the world and will give it a rotation that matches the enemyArcher
    private void shoot(){
        EnemyArrow a = new EnemyArrow(this.getRotation());
        getWorld().addObject(a, this.getX(), this.getY());
    }
}