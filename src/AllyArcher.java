import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class AllyArcher extends Ally
{
    private GreenfootImage myImage;
    //variable for shoot cooldown
    private int shootCounter;
    public AllyArcher(){
        //health of archer
        health = 20;
        //movement speed
        speed = 0.45;
        //set its attack state (0 = shoot arrow inbetween 1-250 then it's on cooldown and doesn't do anything)
        shootCounter = 0;
    }

    public void act() 
    {
        //if the shoot counter has seen the enemy for 250 acts, then it is ready to shoot
        if(shootCounter == 250){
            shoot();
            shootCounter = 0;
        }
        //check if the user has selected/deselected it
        clickCheck();
        //tell it to move to it's assigned coords
        moveTo();
        //locate nearest emeny to attack (internal ai)
        findNearestEnemy();
        //see if it is alive
        checkAlive("AllyArcher");
    }
    
    //sets its image to the selected image and adds it to the global "selected units" arraylist
    public void selectMyself(){
        myImage = new GreenfootImage ("ally-archer-selected.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.pushSelectedUnits(this);
    }
    
    //sets its image to the default unselected image and removes it from the global "selected units" arraylist
    public void deselectMyself(){
        myImage = new GreenfootImage ("ally-archer.png");
        this.setImage(myImage);
        MyWorld m = (MyWorld)getWorld();
        m.deselectUnits(this);
    }

    private void findNearestEnemy(){        
        //will look for nearby enemies to target
        ArrayList closeEnemy  = (ArrayList)getObjectsInRange(200, Enemy.class);       
        //if at least 1 is found and the user has not told it to move to a location, it will run it's targetting ai
        if(closeEnemy.size() > 0 && this.selectedMove == false){
            //find closest enemy
            MyWorld m = (MyWorld)getWorld();
            Enemy targetEnemy = (Enemy)m.getClosest(this,closeEnemy);

            //tell the miner that it is being attacked (I dod this so it seems like it's noticed that it's being attacked)
            if(targetEnemy instanceof EnemyMiner){
                //I see if the miner is already running away so I can avoid stacking the speeds of the miner (miner will move twice as fast away from enemies if the target method is called twice)
                if(((EnemyMiner)targetEnemy).returnTargeted() == false){
                    ((EnemyMiner)targetEnemy).isTargetted();
                }
            }
            //the Archer moves towards the enemy if the user hasn't commanded it to go somehwere else or it it is currently attacking and enemy.
            this.turnTowards(targetEnemy.getX(),targetEnemy.getY());
            
            //this variable determines how many acts have the archer been in view of its target (I have a delay so it simulates drawing the bow and aiming)
            shootCounter++;
        }else{
            
            //if the archer is told to move by the user, then it'll reset it's shoot counter
            shootCounter = 0;
            
            //the Archer looks for buffs that is within 50 px from it
            ArrayList closeBuff = (ArrayList)getObjectsInRange(50, Buff.class);
            //the Archer looks for boffs that is within 50 px from it
            if(closeBuff.size() > 0 && this.selectedMove == false){
                //turn towards the coord of the the buff
                this.turnTowards(((Buff)closeBuff.get(0)).getX(),((Buff)closeBuff.get(0)).getY());
                //move towards the buff
                this.move(0.4);
            }
        }
    }
    
    //this will create a new arrow object within the world and will give it a rotation that matches the AllyArcher
    private void shoot(){
        AllyArrow a = new AllyArrow(this.getRotation());
        getWorld().addObject(a, this.getX(), this.getY());
    }
}