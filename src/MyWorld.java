/**
Curtis Chong

--------features---------

--Ally units--

**Knights**
The tiny rectangular brown objects. They are a melee unit that have pretty good stats.
if within 50 px from an enemy it will chase it down

cost: 100 gold
health: 60
speed: 0.45
damage: 1/10 acts

**Miners**
This unit is your tool to gain a supply of gold.
If it is near gold ore (50 px) it'll uatomatically go towards it and mine it.

cost: 100 gold
health: 40
speed: 0.4
damage: 0

**Archers**
This unit shoots arrows at enemies to do damage. It is weak in terms of health  but it can deal damage without getting hurt.
It will shoot the closest target to it within 200 px

cost: 150 gold
health: 20
speed: 0.45
damage: 25/250 acts (fire rate)




--controls--

**Selection**
you can select ally units by clicking on them

note: you can ONLY select your own troops

You can click and drag (only on the black backgrouund) a rectangle. anything within that rectangle will be selected

you can use KEYBOARD SHORTCUTS
"q" will select all miners
"w" will select all knights
"e" will select all archers
"r" will deselect all units

**deselection**
you can click on the world to deselect all units
you can click on a specific unit to deselect that unit 

**movement**
right click with your mouse to tell all selected units to go there

--fountains--
if the enemy destroys your fountain then you'll lose and vice versa
they have 800 health.
they are where all of you units spawn (x and y coords are randomised a bit but it's still very close to spawning on your fountain)



--gold--

Used to purchase more troops
gold is spawned randomly and in clumps all over the world once it starts (ever 3000 acts = 10 gold ore = 300 gold)
gold spawned periodically in the middle throughout the duration of the game (every 55 acts = 1 gold)
both teams get a slow trickle of gold every few seconds (gives players hope if they lose all of their miners)

--buffs--
there are alters on the top right and bottom left corner of the screen that will periodically spawn buffs 
the first team that sends a unit to touch the buff first will recieve the buff

there are 3 types of buffs:
Gold Buff: give 100 gold
Knight Buff: give 2 knights
Miner Buff: gives 2 Miners

buffs will spawn ever 2000 acts and will dissapear after 1700 acts (every 2000 acts it could be spawned on either one of the Alters but never both)


--Enemy Units--
for all enemy units, they have the same stats/costs. this section will focus more on their behaviour

**knights**
the enemy knights at any time can be in 1 of 4 modes:
1: stay in 1 spot
2: go and find the nearest AllyMiner and try to hunt it down
3: travel upwards until it nearly reached the top of the map. then it will move downwards until it reaches a specific point. then it'll reach up again (this is called patrolling)
4: will look for any EnemyMiner that is undefended by a nearby knight and will go towards them to defend it

if the enemy has 6 units then it'll turn all new knights into patrol mode
if the enemy has 9 units it will send all knights to hunt down ally miners

Knight attacking ai behaves the smae as ally ones where they will go towards an Ally unit if they are within 50 px from it

**Miners**
Miners are very special since they cannot defend themselves. As a result they wl aks for help from ther Enemy units to come and shake off the Ally unit chasing it

if an ally unit is targetting the Miner, it will face the opposite directon and run.
duing this phase it will ask for help from other EnemyKnights to come to its location to distract the ally chasing it


--the enemy ai--
(long term goals)

1. get 3 miners minimun
2. get 2 knights consecutively (sets it to defend miners mode)
3. get 1 archer
4. get 1 miner (stops getting them at 5)

upon unit spawn, it tells all new units to go directly to the middle of the map at first

if the enemy has 19 units that can do damage then send them all to attack the ally base






--reactionary ai--

if the enemy base senses a lot of ally troops near is base (in the form of an attack) it will use up all of its gold and spawn troops at the base to defend it

if a buff has spawned, then it will get the closest Enemy to go directly to it nomatter what



----tips----
fullscreen to get rid of annoying scroll bars
use a mouse (trackpad is hard to right and left click)
keyboard shortcuts are a godsend
if you have trouble selecting units then just click on the world to deselect them all and then go back and reselect them
you cannot out run the ai to get the buffs. because they just react so quickly (the moment it spawns) if you want to get buffs, you have to camp the buff location and wait for one to spawn

knights are very powerful early game units
get some archers later on
place archers behind a row of knights (since it has low health)

if the enemy has launched an attack on your base then go to the middle of the map and farm as much gold as possible to spanic spawn knights to desperately save your fountain
gaining control of the middle of the map is important as a vast amount of gold is stored there

--known bugs--

the units can sometimes go to the top of the world onto the hotbar (there is no collision that prevents them from going up there)
sometimes when a selected unit dies, it doesn't deselect itself form the global selected arraylist and you get a null pointer everytime you try and deslect all units because it cannot find that actor
sometimes units that have a green outline that says selected arn't internally selected (they arn't added to the array list)
you cannot start or end a drag selection on any other object other than the world (black background). If you start or end a drag selection on that part, then you won't select anyhting
 */
import greenfoot.*;
import java.util.ArrayList;
public class MyWorld extends World
{

    private int allyGold = 100;
    //this is zero becuase the enemy always uses it to buy a miner
    private int enemyGold = 0;

    //setting up the bases and alter
    private AllyFountain allyFountain;
    private EnemyFountain enemyFountain;
    private BaseHealthBar allyBaseHealthBar;
    private BaseHealthBar enemyBaseHealthBar;
    private Alter rightAlter;
    private Alter leftAlter;
    //gold ore declaration
    private GoldOre goldOre;

    //declaring mouse information
    private MouseInfo m;
    private int rightMouseX = 0;
    private int rightMouseY = 0;
    private boolean drag = false;
    private int startDragX = 0;
    private int startDragY = 0;
    private int endDragX = 0;
    private int endDragY = 0;
    //this variable is required help determine when did the user stop dragging
    private boolean wasDragBefore = false;

    //arraylist that will take note of the user's current selected units
    private ArrayList selectedUnits = new ArrayList();
    private int currentAct = 0;

    //the hotbar
    private ScoreKeeper scoreKeeper;
    private BuyKnightBtn buyKnightBtn;
    private BuyMinerBtn buyMinerBtn;
    private BuyArcherBtn buyArcherBtn;

    //selected is to tell the word if the user has selected a ubnit or not before telling it where to go
    public MyWorld()
    {    
        //setup world
        super(1000, 600, 1,true);

        //hotbar setup. it creates the buttons
        scoreKeeper = new ScoreKeeper();
        addObject(scoreKeeper,500,15);
        scoreKeeper.setScore(allyGold,enemyGold);
        buyKnightBtn = new BuyKnightBtn();
        addObject(buyKnightBtn,300,15);
        buyMinerBtn = new BuyMinerBtn();
        addObject(buyMinerBtn,370,15);
        buyArcherBtn = new BuyArcherBtn();
        addObject(buyArcherBtn,440,15);

        //create general game objects
        allyFountain = new AllyFountain();
        enemyFountain = new EnemyFountain();
        allyBaseHealthBar = new BaseHealthBar();
        enemyBaseHealthBar = new BaseHealthBar();
        rightAlter = new Alter();
        leftAlter = new Alter();
        //the fountain is 50 px away from the left and 50 px away from the hotbar
        //place standard objects into the world
        addObject(allyFountain,50,80);
        addObject(allyBaseHealthBar,55,45);
        allyBaseHealthBar.setHealth(800);
        addObject(enemyBaseHealthBar,945,585);
        enemyBaseHealthBar.setHealth(800);
        addObject(enemyFountain,950, 550);

        //place the alters
        addObject(rightAlter, 950,80);
        addObject(leftAlter, 50,550);
        //spawn gold ore in the middle line
        for(int i = 0; i < 33; i++){
            goldOre = new GoldOre();
            addObject(goldOre,500+Greenfoot.getRandomNumber(50)-25,600-Greenfoot.getRandomNumber(560));
        }
        //upon game generation, I spawn gold in the middle twice
        spawnGoldInMiddle();
        spawnGoldInMiddle();
        //gold ore spawns near ally and enemy base
        for(int i = 0; i < 10;i++){
            goldOre = new GoldOre();
            //ally base coords
            addObject(goldOre,90+Greenfoot.getRandomNumber(50),120+Greenfoot.getRandomNumber(50));
        }
        for(int i = 0; i< 10; i++){
            goldOre = new GoldOre();
            //enemy base coords
            addObject(goldOre, 860 +Greenfoot.getRandomNumber(50),460+Greenfoot.getRandomNumber(50));
        }
        //randomly sprinkle gold ore everywhere
        for(int i = 0 ; i < 80; i++){
            goldOre = new GoldOre();
            addObject(goldOre, Greenfoot.getRandomNumber(1000),35+Greenfoot.getRandomNumber(565));
        }

        //spawn starting troops
        for(int i = 0; i<1;i++){
            //the enemy gets two troops becuase it starts with 0 gold (it "spends" all of its gold right away for a miner)
            enemyFountain.spawnEnemyMiner();
            enemyFountain.spawnEnemyMiner();
            allyFountain.spawnAllyMiner();
        }
        //both sides get 3 knights
        for(int i = 0; i<3;i++){
            enemyFountain.spawnEnemyKnight();
            allyFountain.spawnAllyKnight();           
        }
    }

    private void spawnGoldInMiddle(){
        //spawn gold ore in middle clump
        for(int i = 0; i < 10; i++){
            goldOre = new GoldOre();
            //the gold ore spawns within a radius of 100 px from the middle
            addObject(goldOre,500+Greenfoot.getRandomNumber(100)-50,365-Greenfoot.getRandomNumber(100));
        }
    }

    public void act(){
        //see if the user used any hotkeys
        checkKeyboard();
        //this method keeps track of what itteration of act the game is in.
        //this is important as part of the enemy ai and the gold respawning mechanics depends on the time passed in the game
        actEvents();
        //get global info for the mouse
        m = Greenfoot.getMouseInfo();
        //check to see if the user has clicked on the world (which wil ldeselct all units)
        checkMouse();
        //check to see if the user has dragged and selected units in the world. it will calculate and select all of the units within 2 coords that form a rectangle
        checkDrag();
        //a constant method that moves ally units to thier commanded location of they have been selected to move
        moveAllyUnits();
    }

    private void actEvents(){
        currentAct++;
        //every 3,000 acts soemthig will happen
        if(currentAct%3000 == 0){
            spawnGoldInMiddle();   
        }
        //every 2,000 acts, I will randomly spawn a buff on one of the alters
        if(currentAct%2000 == 0){
            spawnBuff();
        }
        //this function increases the gold for both teams over time (so one team doesn't lose if all of their miners are gone)
        if(currentAct%55 == 0){
            addAllyGold(1);
            addEnemyGold(1);
        }
    }

    //this method is used to find the closest object in an arraylist in relation to another object
    public Actor getClosest(Actor theObject, ArrayList actors){

        //start off by selecting the first object as a base object to compare with
        Actor targetActor = (Actor)actors.get(0);
        float closestTargetDistance = getDistance(theObject, targetActor);
        //a varialbe that remembers the distance between the other actors
        float distanceToActor;
        //Loop through the objects in the ArrayList to find the closest target
        for (int i = 0; i < actors.size();i++)
        {
            // Cast for use in generic method
            Actor currentActor = (Actor)actors.get(i);
            // Measure distance from me
            distanceToActor = getDistance(theObject, currentActor);
            // If I find a Flower closer than my current target, I will change
            // targets
            if (distanceToActor < closestTargetDistance)
            {
                //if there is a closer actor, then set it as the closes actor (all future comparisons will be compared to this new actor)
                targetActor = currentActor;
                //update the variable that remembers the closests distance between the two actors
                closestTargetDistance = distanceToActor;
            }
        }

        return targetActor;
    }

    //sets the health of the enemybase after it has been damaged
    public void damageEnemyBase(int health){
        enemyBaseHealthBar.setHealth(health);
    }
    //sets the health of the allybase after it has been damaged
    public void damageAllyBase(int health){
        allyBaseHealthBar.setHealth(health);
    }
    //increase the ally gold count and then update the scoreboard
    public void addAllyGold(int amount){
        allyGold = allyGold+amount;
        scoreKeeper.setScore(allyGold,enemyGold);
    }
    //increase the enemy gold count and then update the scoreboard
    public void addEnemyGold(int amount){
        enemyGold = enemyGold+amount;
        scoreKeeper.setScore(allyGold,enemyGold);
    }

    //returns the amount of gold that the enemy base has this is used for ai
    public int returnEnemyGoldAmount(){
        return enemyGold;
    }
    //returns the amount of gold that the ally base has this is used for ai
    public int returnAllyGoldAmount(){
        return allyGold;
    }

    //put the selected ally units in the selected units arraylist
    public void pushSelectedUnits(Ally pushUnit){
        selectedUnits.add(pushUnit);
    }
    //remove the selected ally units from the selected units arraylist
    public void deselectUnits(Ally removeUnit){
        selectedUnits.remove(removeUnit);
    }

    //handles the mouse events (check to see if users have clicked on world and if they have right clicked)
    private void checkMouse()
    {
        //check to see if the mouse is in the scene
        if(m!=null){
            //check to see that the user has clicked
            if(m.getButton() != 0){
                //I check to see if the user is currently dragging or not. if they are then we don't count the mouse click as a "clear selection" function
                if(m.getButton() ==1 && drag == false){
                    //see what actor did it clicked on
                    Actor actorClickedOn = m.getActor();
                    //we don't want to deselect all units if hte user clicked on an ally unit (becuase it will run a method on the unit itself that will onyl deselect that one unit)
                    if(!(actorClickedOn instanceof AllyKnight) || !(actorClickedOn instanceof AllyMiner) || !(actorClickedOn instanceof AllyArcher)){
                        if(selectedUnits.size() > 0){
                            //I use a while loop instead of a for loop because when I remove the index, the entire array lists shifts indexes and I will not clear the entire arraylist
                            while(selectedUnits.size()>0){
                                ((Ally)selectedUnits.get(0)).deselectMyself(); 
                            }
                        }
                    }
                }
                //check to see if hte user has right clicked
                if (m.getButton() == 3){
                    //set the right click coords to that pos
                    rightMouseX = m.getX();
                    rightMouseY = m.getY();
                }
            }
        }
    }

    private void moveAllyUnits(){
        if(rightMouseX!=0){
            /*for(int i = 0; i < selectedUnits.size();i++){
            ((Ally)selectedUnits.get(i)).changeMoveCoords(rightMouseX,rightMouseY);
            ((Ally)selectedUnits.get(i)).deselectMyself(); 
            }*/
            while(selectedUnits.size()>0){
                //I randomise the spot where they will go to by several pixels so they all don't stack on the same place
                if(selectedUnits.size() >1){
                    ((Ally)selectedUnits.get(0)).changeMoveCoords(rightMouseX +Greenfoot.getRandomNumber(50)-25,rightMouseY +Greenfoot.getRandomNumber(50)-25);
                }else{//else put that simgle unit exacally where you want them to be
                    ((Ally)selectedUnits.get(0)).changeMoveCoords(rightMouseX,rightMouseY);
                }//after moving the units, deselect all of the units
                ((Ally)selectedUnits.get(0)).deselectMyself(); 
            }
            //reset the coords of the right mouse click X and Y
            rightMouseX = 0;
            rightMouseY = 0;
        }
    }

    //this method is used to determine if the user has dragged, if so then select all units within the two coords
    private void checkDrag(){
        //see if the mouse is durrently dragging
        if(Greenfoot.mouseDragged(this)){
            //if it is currently dragging, then change the drag value
            drag = true;
        }

        //see id the user clicked the left mouse button, and if they were previously dragging
        if((Greenfoot.mouseClicked(null) || Greenfoot.mouseClicked(this))  && wasDragBefore){
            //retrive mouse state info (for clicks)
            int button = m.getButton();           
            //see if the user left clicked (it means that the user is dragging with the left mouse btn)
            if(button == 1){
                //when this method runs, we know that the user has let go of the left mouse button. Since we also know that they were previously dragging,
                //we know that this location of the mouse is the second coord of the selection
                drag = false;
                //therefore we set the drag coords for hte second x and y
                endDragX = m.getX();
                endDragY = m.getY();

                //since a drag can start from the top left corner or from the bottom right ocrner, we have to have some way to diffrientiate between the two types
                //of drags. This will change how the if statements determine if the unit is in the selection
                boolean typeDrag = false;

                if(endDragX > startDragX){//user dragged from top left to bottom right
                    typeDrag = true;
                }//else user dragged from bottom to top

                //find all ally units
                ArrayList allAllyUnits = (ArrayList) getObjects(Ally.class);
                for(int i = 0; i < allAllyUnits.size(); i++){   
                    //select the current unit
                    Ally currentAlly = (Ally)allAllyUnits.get(i);

                    //if the object is within the range of the drag area
                    //there's two if statements becuase if the x value of the first point was larger than the x value of the second point, the different ways of detecting if it was
                    //inside the drag area is differemt
                    //note I don't check for if startx == endx because that would mean the player has clicked on the same spot and that isn't a selection
                    if(typeDrag && currentAlly.getX() > startDragX && currentAlly.getX() < endDragX && currentAlly.getY() > startDragY && currentAlly.getY() < endDragY){
                        //selects the current unit
                        currentAlly.selectMyself();
                    }else if(currentAlly.getX() < startDragX && currentAlly.getX() > endDragX && currentAlly.getY() < startDragY && currentAlly.getY() > endDragY){
                        //selects the current unit
                        currentAlly.selectMyself();
                    }
                }
                //reset the drag coords for the first drag coords
                startDragX = 0;
                startDragY = 0;
            }
        }
        //if the start drag coord was set to its defaut value, then we know that the user hasn't dragged before
        if(startDragX == 0){
            //we know that the user didn't start dragging yet and therefore we do not have the first coords for the drag
            wasDragBefore = false;
        }

        if(drag && !wasDragBefore) {
            //to find the coords of where the drag initially was
            startDragX = m.getX();
            startDragY = m.getY();
            //since the start coords was laready set, we don't have to set it again so we turn we dragbefore to true
            wasDragBefore = true;
        }

    }

    //this method checks the keyboard for shortcuts the user may use in game
    private void checkKeyboard(){
        //if the user presses q then the game will select all miners
        if(Greenfoot.isKeyDown("q")){//select all miners
            //find all ally miners
            ArrayList allyMiners = (ArrayList) getObjects(AllyMiner.class);
            //loop through each ally miner
            for(int i = 0; i < allyMiners.size(); i++){
                //tell it to select itself
                Ally currentMiner = (Ally)allyMiners.get(i);
                currentMiner.selectMyself();
            }
        }
        if(Greenfoot.isKeyDown("w")){//select all knights
            //find all ally knights
            ArrayList allyKnights = (ArrayList) getObjects(AllyKnight.class);
            //loop through each ally knight
            for(int i = 0; i < allyKnights.size(); i++){
                Ally currentKnight = (Ally)allyKnights.get(i);
                currentKnight.selectMyself();
            }
        }
        if(Greenfoot.isKeyDown("e")){//select all archers
            //find all ally archers
            ArrayList allyArchers = (ArrayList) getObjects(AllyArcher.class);
            //loop through each ally archer
            for(int i = 0; i < allyArchers.size(); i++){
                Ally currentArcher = (Ally)allyArchers.get(i);
                currentArcher.selectMyself();
            }
        }
        if(Greenfoot.isKeyDown("r")){//deselect all
            //check to see if there are any units selected
            if(selectedUnits.size() > 0){
                //I use a while loop instead of a for loop because when I remove the index, the entire array lists shifts indexes and I will not clear the entire arraylist
                while(selectedUnits.size()>0){
                    ((Ally)selectedUnits.get(0)).deselectMyself(); 
                }
            }
        }
    }

    //a method used to find distance between two different actors
    public float getDistance (Actor a, Actor b)
    {
        double distance;
        //find the difference in x and y values
        double xLength = a.getX() - b.getX();
        double yLength = a.getY() - b.getY();
        //I then square both differences and add them up. when I'm done I square root of it to find the distance between the two points
        //basically pythagorean's theorem
        distance = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
        //return the value
        return (float)distance;
    }

    //purchasing ally units
    public void buyAllyMiner(){
        //check to see if the user has enough gold
        if(allyGold >99){
            //if so then minus that amount of gold from the global variables
            allyGold = allyGold-100;
            //call the method within the fountain to spawn the unit
            allyFountain.spawnAllyMiner();
            //update the score to reflect the amount of money that each player has
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    public void buyAllyKnight(){
        if(allyGold >99){
            allyGold = allyGold-100;
            allyFountain.spawnAllyKnight();
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    public void buyAllyArcher(){
        if(allyGold >149){
            allyGold = allyGold-150;
            allyFountain.spawnAllyArcher();
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    //purchasing enemy units
    public void buyEnemyMiner(){
        //check to see if the user has enough gold
        if(enemyGold > 99){
            //if so then minus that amount of gold from the global variables
            enemyGold = enemyGold-100;
            enemyFountain.spawnEnemyMiner();
            //update the score to reflect the amount of money that each player has
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    public void buyEnemyKnight(){
        if(enemyGold > 99){
            enemyGold = enemyGold-100;
            enemyFountain.spawnEnemyKnight();
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    public void buyEnemyArcher(){
        if(enemyGold >149){
            enemyGold = enemyGold-150;
            enemyFountain.spawnEnemyArcher();
            scoreKeeper.setScore(allyGold,enemyGold);
        }
    }

    //this method is called everytime the world wants to spawn in a buff
    private void spawnBuff(){

        //this will decide on a random buff that the alters will spawn
        int buffType = Greenfoot.getRandomNumber(3);
        //0 = miner
        //1 = knight
        //2 = gold
        //this will decide on which alter to place the buff on
        int whichAlter = Greenfoot.getRandomNumber(2);
        //0 = leftAlter
        //1 = rightAlter
        //declare the chosen alter's coords for placement
        int chosenAlterX;
        int chosenAlterY;
        if(whichAlter == 1){//leftAlter
            //set the coords that the buff will be placed on (which Alter location)
            chosenAlterX = 50;
            chosenAlterY = 550;
        }else{//rightAlter
            //set the coords that the buff will be placed on (which Alter location)
            chosenAlterX = 950;
            chosenAlterY = 80;
        }

        if(buffType == 0){//miner
            MinerBuff minerBuff = new MinerBuff();
            addObject(minerBuff,chosenAlterX,chosenAlterY);
        }else if (buffType == 1){//knight
            KnightBuff knightBuff = new KnightBuff();
            addObject(knightBuff,chosenAlterX,chosenAlterY);
        }else{//gold
            GoldBuff goldBuff = new GoldBuff();
            addObject(goldBuff,chosenAlterX,chosenAlterY);
        }
    }
}
