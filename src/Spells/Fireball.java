package Spells;

import Effects.Effect;

public class Fireball implements Spell {
    private int damage;
    private int cooldown;
    private Effect effect;
    private int id;

    public Fireball(){
        damage = 20;
        cooldown = 3000;
        effect = null;
        id = 0;
    }

    public int getDamage(){
        return damage;
    }
    public int getCooldown(){
        return cooldown;
    }
    public Effect getEffect(){
        return effect;
    }

    public int getId(){
        return id;
    }
}
