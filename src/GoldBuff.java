import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//parent class for all of the buffs in the game

public class GoldBuff extends Buff
{
    //administrates the buff
    public void giveBuff(boolean team){
        MyWorld m = (MyWorld)getWorld();
        if(team){//ally team
            //add 200 gold to the Ally team
            m.addAllyGold(200);
        }else{//enemy team
            //add 200 gold to the Enemy team
            m.addEnemyGold(200);
        }
    }
}
