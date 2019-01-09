import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

//this is the nemy fountain and it contains most of the enemy ai

public class EnemyFountain extends Enemy
{
    private boolean initializedObject = false;
    //hunters are enemy units that searches for ally miners and kills them
    //the enemy always starts with 2 hunters
    private int numOfHunters = 2;
    /*if
     * attackstate = 0 then the enemy is neutral
     * attackstate = 1 then the enemy is in defence mode
     * attackstate = 2 then the enemy is in attack mode (goes attack the ally base)
     */
    private int attackState = 0;
    //initiate enemy base health
    public EnemyFountain(){
        health = 800;
    }

    public void act() 
    {
        //this method determines what to spend with the gold and where to send the units after
        manageEconomy();
        //this method will only run once (it is used to spawn the starter units for the enemy fountain)
        if(initializedObject == false){
            initializeThisObject();
        }
        //see if the base is still alive
        checkAlive("enemyFountain");
    }
    //the code within this function will run only once (I did this becuase I have to wait until all elements are loaded)

    //this method selects the enemy units that the base wants to tell them to move. we select specific units because the ai might not want al enemy units to move
    private void selectEnemyUnits(ArrayList units){
        for(int i = 0; i < units.size();i++){
            //select the current unit fro mthe arraylist
            ((Enemy)(units.get(i))).selectedMove = true;
        }
    }

    private void initializeThisObject(){
        //save an instance of this object
        initializedObject = true;

        //tells all enemy miners to go to the middleand mine gold
        ArrayList allEnemyMiners = (ArrayList) getWorld().getObjects(EnemyMiner.class);
        selectEnemyUnits(allEnemyMiners);
        moveUnits(allEnemyMiners, 500,300);

        //select all enemy knights and tell them to move to the middle (also sets them to guard the miners)
        ArrayList allEnemyKnights = (ArrayList) getWorld().getObjects(EnemyKnight.class);
        selectEnemyUnits(allEnemyKnights);
        moveUnits(allEnemyKnights, 500,300);
        for(int i = 0; i < allEnemyKnights.size(); i++){
            ((EnemyKnight)allEnemyKnights.get(i)).stayWithEnemyMiners();
        }
    }

    //when a desinated hunter dies the enemy ai will know
    public void minusHunter(){
        numOfHunters --;
    }

    //this is will spawn an enemy knight near the coords of the enemy fountain
    public void spawnEnemyKnight(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new EnemyKnight(), 950 +Greenfoot.getRandomNumber(30)-15, 550 +Greenfoot.getRandomNumber(30)-15);
    }

    //this is will spawn an enemy miner near the coords of the enemy fountain
    public void spawnEnemyMiner(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new EnemyMiner(), 950 +Greenfoot.getRandomNumber(30)-15, 550 +Greenfoot.getRandomNumber(30)-15);
    }

    //this is will spawn an enemy archer near the coords of the enemy fountain
    public void spawnEnemyArcher(){
        MyWorld m = (MyWorld)getWorld();
        m.addObject(new EnemyArcher(), 950 +Greenfoot.getRandomNumber(30)-15, 550 +Greenfoot.getRandomNumber(30)-15);
    }
    //returns the amount of gold the enemy has
    private int numEnemyGold(){
        MyWorld m = (MyWorld)getWorld();
        return m.returnEnemyGoldAmount();
    }

    //the methods below are for the ai to understand the state of hte battle field
    
    //this will return how much gold the ally has
    private int numAllyGold(){
        MyWorld m = (MyWorld)getWorld();
        return m.returnAllyGoldAmount();
    }
    //return num of enemy miners
    private int numEnemyMiners(){
        return getWorld().getObjects(EnemyMiner.class).size();
    }
    //return num of enemy knights
    private int numEnemyKnights(){
        return getWorld().getObjects(EnemyKnight.class).size();
    }
    //return the num of enemy archers
    private int numEnemyArchers(){
        return getWorld().getObjects(EnemyArcher.class).size();
    }
    //return the num of ally miners
    private int numAllyMiners(){
        return getWorld().getObjects(AllyMiner.class).size();
    }
    //return the num of all knights
    private int numAllyKnights(){
        return getWorld().getObjects(AllyKnight.class).size();
    }
    //return the num of ally archers
    private int numAllyArchers(){
        return getWorld().getObjects(AllyArcher.class).size();
    }

    //this method will decide how the ai will spend it's money and where to send the units
    private void manageEconomy(){
        //always tries to purchase at least 3 miners for the enemy
        if(numEnemyMiners() < 3 && numEnemyGold() >99){
            MyWorld m = (MyWorld)getWorld();
            m.buyEnemyMiner();
        }

        //tjos loops through the knight's position and determines where to place them.

        //this firsts determines where our troops are

        //i'm being very specific with which tpyes of troops are where because the value of the troops matter
        int numEneKniOnLeft = 0;
        int numEneKniOnMid = 0;
        int numEneKniOnRight = 0;

        int numEneArcOnLeft = 0;
        int numEneArcOnMid = 0;
        int numEneArcOnRight = 0;

        //loops through each enemy knight and tries to determine which section of hte map they are
        for(int i = 0; i < numEnemyKnights(); i++){
            //get the current enemy knight
            EnemyKnight currentKnight = (EnemyKnight)getWorld().getObjects(EnemyKnight.class).get(i);
            //get its coords
            int currentX = currentKnight.getX();

            //if they are on the left side of the map, add one to the counter that keeps track of how many enemy knights are on that sie of the map
            if(currentX <250){
                numEneKniOnLeft++;
            //if they are on the middle of tte map, add one to the counter that keeps track of how many enemy knights are on that sie of the map
            }else if(currentX < 750){
                numEneKniOnMid++;
            //if they are on the right side of the map, add one to the counter that keeps track of how many enemy knights are on that sie of the map
            }else{
                numEneKniOnRight++;
            }            
        }

        for(int i = 0; i < numEnemyArchers(); i++){
            EnemyArcher currentArcher = (EnemyArcher)getWorld().getObjects(EnemyArcher.class).get(i);
            int currentX = currentArcher.getX();

            if(currentX <250){
                numEneArcOnLeft++;
            }else if(currentX < 750){
                numEneArcOnMid++;
            }else{
                numEneArcOnRight++;
            }          
        }

        //tracking ally troops

        int numAlyKniOnLeft = 0;
        int numAlyKniOnMid = 0;
        int numAlyKniOnRight = 0;

        int numAlyArcOnLeft = 0;
        int numAlyArcOnMid = 0;
        int numAlyArcOnRight = 0;

        //cycles through each ally knight and determiens thier position on the world 
        for(int i = 0; i < numAllyKnights(); i++){
            //get current knight from the arraylist of knights
            AllyKnight currentKnight = (AllyKnight)getWorld().getObjects(AllyKnight.class).get(i);
            //determin it's x and y
            int currentX = currentKnight.getX();

            //if it is on the left column of the page, increase a variable that keeps track of how many allies are on the left side of the map
            if(currentX <250){
                numAlyKniOnLeft++;
                //if it is on the middle column of the page, increase a variable that keeps track of how many allies are on the middle of the map
            }else if(currentX < 750){
                numAlyKniOnMid++;
                //if it is on the right column of the page, increase a variable that keeps track of how many allies are on the right side of the map
            }else{
                numAlyKniOnRight++;
            }         
        }

        for(int i = 0; i < numAllyArchers(); i++){
            AllyArcher currentArcher = (AllyArcher)getWorld().getObjects(AllyArcher.class).get(i);
            int currentX = currentArcher.getX();

            if(currentX <250){
                numAlyArcOnLeft++;
            }else if(currentX < 750){
                numAlyArcOnMid++;
            }else{
                numAlyArcOnRight++;
            }
        }

        //this method will purchase enmey troops and wil lsend them to locations on the map
        if(numEnemyGold() > 200){//if htey have a decet amount of money then purchase something. the enmey doens't save up too much money because it wants to always pump out troops
            MyWorld m = (MyWorld)getWorld();
            if(numEnemyKnights() / 2.0 < numEnemyArchers()){
                m.buyEnemyKnight();
            }else if(numEnemyArchers() < numEnemyKnights() / 2.0 && numEnemyKnights() >7){//must have at least  enemy knights first becuase knights are just so powerful
                m.buyEnemyArcher();
            }else if (numEnemyMiners() < numEnemyKnights() / 2.0 && numEnemyMiners() < 5){
                m.buyEnemyMiner();
            }else{
                m.buyEnemyKnight();
            }

            //after purchasing the unit, go tell the unit where to go and assign it an action (for knight only)
            //ArrayList allEnemies = (ArrayList)getWorld().getObjects(AllyKnight.class);

            //int allEnemySize = allEnemies.size();
            //to locate the new enemy I created, I just look around the fountain for an enemy that's very close by and then assume that that is the one that I generated
            //using this object, I can initialise the unit's info and tell it where to go. (where it's needed)
            ArrayList justSpawnedUnits = (ArrayList)getObjectsInRange(50, Enemy.class);
            //loop through each of the units near the base and give them the same attributes
            for(int a = 0; a < justSpawnedUnits.size();a++){
                //this is the unit we're currently applying the info on
                Enemy currentSpawnedUnit = (Enemy)justSpawnedUnits.get(a);

                //ckeck to see if it exists and if it's a knight
                if(currentSpawnedUnit != null && currentSpawnedUnit instanceof EnemyKnight){//spawned an enemy Knight
                    ((EnemyKnight)currentSpawnedUnit).stayWithEnemyMiners();
                    if(attackState == 2){//it's an attack on the base
                        currentSpawnedUnit.changeMoveCoords(100,130);
                        //if there are more than 5 units, then send the knight to patrol duty
                    }else if(numEnemyArchers() + numEnemyKnights() > 5){
                        ((EnemyKnight)currentSpawnedUnit).goPatrol();
                        //if there are 8 enemies, we send them all to hunt ally miners
                        //I randomise the x and y locations a bit
                        currentSpawnedUnit.changeMoveCoords(500+ Greenfoot.getRandomNumber(50)-25,300+ Greenfoot.getRandomNumber(50)-25);
                    }else if (numEnemyArchers() + numEnemyKnights() > 8){
                        //get an instance of all enemy knights
                        ArrayList allKnights = (ArrayList)getWorld().getObjects(EnemyKnight.class);
                        if(allKnights.size() > 0){
                            for(int b = 0; b < allKnights.size(); b++){
                                //tell each individual one to kill the miners
                                ((EnemyKnight)allKnights.get(b)).goAttackMiners();
                            }
                        }
                        //note we don't give them a coords to go to because they're hunting
                    }else{//if the enemy doens't have enough units to do anything
                        //I randomise the x and y locations a bit
                        currentSpawnedUnit.changeMoveCoords(500+ Greenfoot.getRandomNumber(50)-25,300+ Greenfoot.getRandomNumber(50)-25);
                    }
                }
                else if (currentSpawnedUnit instanceof EnemyArcher){//spawned an enemy archer

                    //if the enemy is currently attacking the base
                    if(attackState == 2){//it's an attack on the base
                        currentSpawnedUnit.changeMoveCoords(140,170);
                    }else{
                        currentSpawnedUnit.changeMoveCoords(580+ Greenfoot.getRandomNumber(50)-25,300+ Greenfoot.getRandomNumber(50)-25);//I place the archers a bit further back so they cna get cover from the knights
                    }
                }else{//it is an enemy miner

                }

            }
        }

        //if there is an invasion to the base, buy up enough knights to match the amount of troops there.
        if(numEnemyArchers() + numEnemyKnights() > 18 && attackState != 2){//if the enemy ai thinks that they have anought troops to push for a win
            //set the current attack state to attack the base
            attackState = 2;

            //purchase troops with its stockpile of money
            MyWorld m = (MyWorld)getWorld();
            m.buyEnemyKnight();
            m.buyEnemyKnight();
            m.buyEnemyArcher();

            //find all enemy units that cna do damage
            ArrayList allEnemyKnights = (ArrayList) getWorld().getObjects(EnemyKnight.class);
            ArrayList allEnemyArchers = (ArrayList) getWorld().getObjects(EnemyArcher.class);

            //tell them to go to the ally base
            selectEnemyUnits(allEnemyKnights);
            moveUnits(allEnemyKnights, 100,130);
            selectEnemyUnits(allEnemyArchers);
            moveUnits(allEnemyArchers, 140, 170);//archers are placed a bit further back to avoid taking damage

        }
        //get all buffs in the world
        ArrayList currentBuff = (ArrayList)getWorld().getObjects(Buff.class);
        if(currentBuff.size() > 0){
            //save the buff's x and y coords into variables
            int buffX = ((Buff)currentBuff.get(0)).getX();
            int buffY =((Buff)currentBuff.get(0)).getY();

            //get an arraylist of all enemies
            ArrayList enemies = (ArrayList)getWorld().getObjects(Enemy.class);
            MyWorld m = (MyWorld)getWorld();
            //find the closest enemy to the buff
            Enemy closestEnemy = (Enemy)m.getClosest((Buff)currentBuff.get(0),enemies);
            //check to see that the enemy is not the fountain (the  base can't move)
            if(!(closestEnemy instanceof EnemyFountain)){
                //tell the nemey to go to the buff
                closestEnemy.changeMoveCoords(buffX,buffY);
            }
        };
    }
}
