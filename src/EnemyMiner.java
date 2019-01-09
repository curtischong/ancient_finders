import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
public class EnemyMiner extends Enemy
{
    private GreenfootImage myImage;
    //variable for mining cooldown
    private int miningState;
    //variable for overwriting command ai
    private boolean survivalInstinct = false;

    public EnemyMiner(){
        //health of miner
        health = 40;
        //set its mining state (0 = attack on once it colloids if inbetween 1-10 then it's on cooldown and doesn't do anything)
        miningState = 0;
        //movement speed
        speed = 0.4;
    }

    public void act() 
    {
        //if the miner is set to flee an ally, then it will take his surival instinct as priority over mining or going to a location.
        if(survivalInstinct == false){
            //tell it to move to it's assigned coords
            moveTo();
            //locate nearest emeny to attack (internal ai)
            findNearestGold();
            //asks for the closest knight that 
            
        }
        //sets survival instinct to false because every act the ally knight/archer will call it so it doesn't need to be cached
        survivalInstinct = false;
        //see if it is alive and tell the method what type of enemy it is.
        checkAlive("EnemyMiner");
    }

    public void isTargetted(){
        //finds the nearest ally units
        ArrayList allNearbyAllies = (ArrayList)getObjectsInRange(180, Ally.class);
        for(int i = 0; i < allNearbyAllies.size();i++){

            //loops through each of them and sees if they are a unit that can damage it
            if( (allNearbyAllies.get(i) instanceof AllyKnight || allNearbyAllies.get(i) instanceof AllyArcher)){
                //if it is a unit that can do harm to hte miner, it will find its rotation
                int allyDirection = ((Ally)allNearbyAllies.get(i)).getRotation();

                //using this rotation, the miner will rotate the other way and "run away" this is basically the ai to avoid being attacked
                this.setRotation(allyDirection);
                this.move(0.4);
                survivalInstinct = true;

                //ask for nearest knight within a reasonable range for help
                ArrayList allNearbyKnights = (ArrayList)getObjectsInRange(250, EnemyKnight.class);
                
                MyWorld m = (MyWorld)getWorld();
                if(allNearbyKnights.size() > 0){
                    Enemy closestEnemy = (Enemy)m.getClosest(this,allNearbyKnights);
                    closestEnemy.changeMoveCoords(this.getX(),this.getY());
                }
                //breaks out of the for loop once it finds an enemy that is close enough
                break;
            }else{
                //if there is no ally unit that is targetting the miner, it doesn't need to be in a survival mindset
                survivalInstinct = false;
            }

        }
    }

    //tells enemies if it is currently running away so the method doesn't get called twice
    public boolean returnTargeted(){
        return survivalInstinct;
    }

    private void findNearestGold(){
        //checks to see if it is touching goldore
        GoldOre currentGold = (GoldOre)getOneIntersectingObject (GoldOre.class);
        //if it is touching it
        if (currentGold != null){
            //if it is mining hte gold
            if(miningState == 0){
                //call the method in the gold ore to tell it to remove one health and give one gold to the enemy side
                currentGold.mineMe(false);
                //increase the mining state to start to cooldown variable
                miningState++;
            }else{
                //increase the cooldown variable once every act
                if(miningState < 10){
                    miningState++;
                    //if the cooldown variable is at 10, then set it to zero where it will mine next act
                }else{
                    miningState = 0;
                }
            }
            
            //ask the world to send a knight to guard it if there are no knights guarding it
            ArrayList allNearbyKnights = (ArrayList)getObjectsInRange(100, EnemyKnight.class);
            ArrayList allEnemyKnights = (ArrayList)getWorld().getObjects(EnemyKnight.class);
            if(allNearbyKnights.size() == 0){
                MyWorld m = (MyWorld)getWorld();
                //Knight m.getClosest(this,allNearbyKnights);
                for(int i = 0; i < allEnemyKnights.size();i++){
                    EnemyKnight currentKnight = ((EnemyKnight)allEnemyKnights.get(i));
                    //if the knight is set to go protect miners and if it isn't guarding anything
                    //if the knight is not guarding and is supposed to be guarding
                    if(currentKnight.getState() == 3 && currentKnight.returnIsGuarding() == false){
                        currentKnight.isGuarding();
                        currentKnight.changeMoveCoords(this.getX(),this.getY());
                    }
                    //since the miner has recieved it's guard we can stop searching for availible knights
                    break;
                }
            }
            
        }else{
            //the miner looks for gold ore that is within 50 px from it
            ArrayList closeGoldOre = (ArrayList)getWorld().getObjects(GoldOre.class);
            if(closeGoldOre.size() > 0 && this.selectedMove == false){

                //find closest gold ore
                MyWorld m = (MyWorld)getWorld();
                GoldOre targetGold = (GoldOre)m.getClosest(this,closeGoldOre);
                //the miner moves towards the gold ore if the user hasn't commanded it to go somehwere else or it it is currently mining the gold.
                this.turnTowards(targetGold.getX(),targetGold.getY());
                this.move(0.4);
            }
        }
    }
}