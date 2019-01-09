import greenfoot.*;

//parent class for all of the buffs in the game

public class MinerBuff extends Buff
{
    //administrates the buff
    public void giveBuff(boolean team){
        MyWorld m = (MyWorld)getWorld();
        if(team){//ally team
            //add 200 gold to the Ally team so they have enough money to buy 2 knights
            m.addAllyGold(200);
            m.buyAllyMiner();
            m.buyAllyMiner();
        }else{//enemy team
            //add 200 gold to the Enemy team so they have enough money to buy 2 knights
            m.addEnemyGold(200);
            m.buyEnemyMiner();
            m.buyEnemyMiner();
        }
    }
}