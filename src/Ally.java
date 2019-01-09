import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
//this class is the ally class and it determines which common functions make up a ally. They cover 

public abstract class Ally extends SmoothMover
{
    //this is an abstract that states that each subclass is expected to have its own definition of deselectMyself. It is used so that the world can ensure that avery ally has a select function
    //note: you cannot select the base so I have an empty function in that class
    protected abstract void deselectMyself ();
    protected abstract void selectMyself ();
    
    //determines if the unit is selected or not by the user
    protected boolean selected = false;
    
    //the movex and movey are variables that determine where the unit should move to at all times
    //whenever movex == 0, the unit will not move beucase 0 is the defaut value that tells the unit to "rest"
    protected int moveX = 0;
    protected int moveY = 0;   
    //declairs unit health.
    protected int health;
    //if this is set to true, then the unit will disregard its and will folow the user's instructions
    protected boolean selectedMove = false;
    //each ally unit has a different speed for game balance that is why this variable isn't assigned.
    protected double speed;
    
    //when the unit is clicked on, it will tggle the state to tell the world if its selected or not
    protected void clickCheck(){
        if(Greenfoot.mouseClicked(this)){
            if(this.selected == false){
                this.selected = true;
                //calls its own individual selectmyself method (this is because each unit has its own image file that needs to be changed)
                this.selectMyself();
            }else{
            this.selected = false;
            //calls its own individual selectmyself method (this is because each unit has its own image file that needs to be changed)
            this.deselectMyself();
            }
        }
    }
    //this method is used when the user commands it where to move.
    protected void moveTo(){
        //the unit will check to see if it's at its destination and if the user told it to move at that spot (if selected move == true)
        if(moveX !=0 && this.getX() != moveX && this.getY() !=moveY && selectedMove==true){            
           //if the unit is told to move, and it isn't quite there yet, it will turn itself towards where it's supposed to be and move.
           this.turnTowards(this.moveX,this.moveY);
           //each unit's speed is different so I used this.speed
           this.move(this.speed);           
           //if the unit is already at its destination, it change selectedMove to false will will allow its own internal movement ai to take over the unit
        }else{
            // it has reached its destination and its own ai movement can take place
            selectedMove = false;
        }
    }
    
    //this method checks to seei fhte unit is alive
    protected void checkAlive(String type){
        if(!(health > 0)){
            //if it is the ally fountain that has died, I show tell the user that they have lose the game
            if(type == "AllyFountain"){
                //spawn the lose banner
                MyWorld m = (MyWorld)getWorld();
                m.addObject(new YouLost(),500,300);
            }           
            //remove itself from the selected units list
            this.deselectMyself();
            //remove itself from the world
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
    
    //when it recieves damage from an enemy unit
    public void damageMe(int amount){
        
        //deal that amount of damage to the ally unit
        this.health = this.health - amount;
        
        //the health system for the base is different because I have to keep track of it in the world class and I have to update the health bar. THat is why I check to see if the ally is the base
        //if it is the base, then I'll call a method in the world class that will update the healthbar and internal variable
        //this is also important for determining the enemy ai
        if(this instanceof AllyFountain){
            MyWorld m = (MyWorld)getWorld();
            //call method in world
            m.damageAllyBase(this.health);
       }
    }
}

