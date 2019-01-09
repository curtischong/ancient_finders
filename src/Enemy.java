import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.ArrayList;
public abstract class Enemy extends SmoothMover
{
    // protected GreenfootImage myImage;
    /**
     * Act - do whatever the Enemy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    //the movex and movey are variables that determine where the unit should move to at all times
    //whenever movex == 0, the unit will not move beucase 0 is the defaut value that tells the unit to "rest"
    protected int moveX = 0;
    protected int moveY = 0;
    //declairs unit health.
    protected int health = 50;
    //if this is set to true, then the unit will disregard its and will folow the user's instructions
    protected boolean selectedMove = false;
    //each ally unit has a different speed for game balance that is why this variable isn't assigned.
    protected double speed;
    
    //this method is used when the user commands it where to move.
    protected void moveTo(){
        if(moveX !=0 && this.getX() != moveX && this.getY() !=moveY && selectedMove==true){
            //if the unit is told to move, and it isn't quite there yet, it will turn itself towards where it's supposed to be and move.
           this.turnTowards(this.moveX,this.moveY);
           //each unit's speed is different so I used this.speed
           this.move(this.speed);           
           //if the unit is already at its destination, it change selectedMove to false will will allow its own internal movement ai to take over the unit
        }else{
            // it has reached its destination and its own ai movement can take place
            this.moveX = 0;
            this.moveY = 0;
            selectedMove = false;
        }
    }
    protected void checkAlive(String type){
        //if the unit has no health
        if(!(health > 0)){
            
            //tell the ai that it has lost a nunter
            if(type == "enemyFountain"){
                MyWorld m = (MyWorld)getWorld();
                m.addObject(new YouWon(),500,300);
            }else if(type == "EnemyKnightHunter"){
                //this tells the ai that it has lost a knight that has been set to hunter
                EnemyFountain enemyFountain = new EnemyFountain();
                enemyFountain.minusHunter();                
            }
            //remove the object
            getWorld().removeObject(this);
        }
    }
    //this method is called when the user wants to assign a location for the unit to move to
    public void changeMoveCoords(int x, int y){
        this.moveX = x;
        this.moveY = y;
        //tells the unit that it is being commanded to move somewhere and should disregard normal movement ai
        this.selectedMove = true;
    }
    
    //all enemy units are succesptable from ally damage and this method allows all enemies to take damage
    public void damageMe(int amount){
        //the enemy's health is recued by the amount of damage taken
        this.health = this.health - amount;
        
        
        //the health system for the base is different because I have to keep track of it in the world class and I have to update the health bar. That is why I check to see if the ally is the base
        //if it is the base, then I'll call a method in the world class that will update the healthbar and internal variable
        //this is also important for determining the enemy ai
        if(this instanceof EnemyFountain){
            MyWorld m = (MyWorld)getWorld();
            //call method in world
            m.damageEnemyBase(this.health);
       }
    }

    //enemy AI
    //this method is called when the enemy ai commands certain units to move to a specific spot
    protected void moveUnits(ArrayList enemyUnits, int locX, int locY){
        //if only one unit is selected to move, then the unit will go to that exact spot
        if(enemyUnits.size() == 1){
            //select the first unit in the array (which is the only unit in the array)
            Enemy currentUnit = (Enemy) enemyUnits.get(0);
            //set it's move values
            currentUnit.moveX = locX;
            currentUnit.moveY = locY;
        }else{
            //if htere is more than one unit told to move, then their destination location is randomised a bit to spread them out.
            //loop through each unit and and assign them a destination to move to.
            for(int i = 0; i < enemyUnits.size();i++){
                //retrieve the current enemy in the arraylist
                Enemy currentUnit = (Enemy) enemyUnits.get(i);                
                //this is to randomise where they will go (to prevent units from stackin)
                currentUnit.moveX = locX + Greenfoot.getRandomNumber(50)-25;
                currentUnit.moveY = locY + Greenfoot.getRandomNumber(50)-25;
            }
        }
    }
}