package Spells;

import Effects.Effect;

public interface Spell {

    public int getId();
    public int getDamage();
    public int getCooldown();
    public Effect getEffect();

}
