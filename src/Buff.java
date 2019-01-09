import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public abstract class Buff extends Actor
{
    //every buff must do something when it is touched by a unit. that is why each buff has a method called giveBuff()
    //the boolean in the parameter is to tell the buff which team to give the buff to. true mean ally team, false means enemy team
    protected abstract void giveBuff(boolean team);

    //set the lifespan of a buff before it dissapears
    private int lifespan = 1700;
    public void act() 
    {

        //test to see if the buff is touching a unit
        //I first retrieve all of the nearby actors and select hte first one. becuase getoneintersction actors was buggy
        ArrayList allNearbyActors = (ArrayList)getObjectsInRange(20, Actor.class);

        for(int i = 0 ; i < allNearbyActors.size(); i++){
            Actor currentActor = (Actor)allNearbyActors.get(i);
            if(currentActor!=null){
                //check to see that it's not an arrow hitting the buff
                if(currentActor instanceof AllyKnight || currentActor instanceof AllyArcher || currentActor instanceof AllyMiner){
                    //I call the buff's give buff function to tell it to administrate the buff
                    giveBuff(true);
                    //I set the buff's lifespan to zero to remove it from the world
                    lifespan = 0;

                    //I use else if to filter out any arrows touching the alter and to filter out the alter itself
                }else if(currentActor instanceof EnemyKnight || currentActor instanceof EnemyArcher || currentActor instanceof EnemyMiner){
                    //I call the buff's give buff function to tell it to administrate the buff
                    giveBuff(false);
                    //I set the buff's lifespan to zero to remove it from the world
                    lifespan = 0;
                }
            }
        }

        //this code checks to see if the buff has reached the end of its lifespan
        if(lifespan == 0){
            //remove itself from the world
            getWorld().removeObject(this);
        }else{
            //decrease the lifespan timer by 1 act
            lifespan--;
        }
    }    
}
