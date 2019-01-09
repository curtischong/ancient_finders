import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class AllyFountain extends Ally
{
    public AllyFountain(){
        //set the health of the base to 800
        health = 800;
    }

    //these methods are empty becuase it must be here for the ally class to compile
    //the ally fountain doesn't use these methods because it isn't selectable
    public void deselectMyself(){

    }

    public void selectMyself(){

    }
    //whenever this method is called, it spawn an ally knight near the base with randomised coords to spread out each knight
    public void spawnAllyKnight(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new AllyKnight(), 50 +Greenfoot.getRandomNumber(30)-15, 80 +Greenfoot.getRandomNumber(30)-15);
    }
    //whenever this method is called, it spawn an ally knight near the base with randomised coords to spread out each knight
    public void spawnAllyMiner(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new AllyMiner(), 50 +Greenfoot.getRandomNumber(30)-15, 80 +Greenfoot.getRandomNumber(30)-15);
    }
    //whenever this method is called, it spawn an ally knight near the base with randomised coords to spread out each knight
    public void spawnAllyArcher(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new AllyArcher(), 50 +Greenfoot.getRandomNumber(30)-15, 80 +Greenfoot.getRandomNumber(30)-15);
    }
    //this method is here so the world can keep trakc of its current hp
    public int getHealth(){
        return health;
    }

    public void act(){
        checkAlive("AllyFountain");
    }
}
