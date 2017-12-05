package link.riley.csc254.gameentities;

/**
 * Created by noynaert on 11/28/17.
 * Entity is intended to be an abstract class.
 *
 * The following recommendations are for all entities:
 *   - strength:  values should be 0.00 through 9.99
 *                any value over 2.0 is likely to be an instant kill.
 *                1.0 would be a full strength human with a sword.
 *                Something like a plant would have a strength of 0.0
 *                A bunny might have a strength of .05 unless it is a Monty Python bunny
 *                A T-Rex might have a strength of 5.5
 *    - aggressiveness:  Must be between 0 and 1.0
 *                1.0 would mean attacks at every opportunity.
 *                0.7 would attack 70% of the time and not attack 30%
 *                0 would be for something like a plant.
 *     - health:  Health also represents stamina
 *                Value should be > .10 and < 9.99
 *                Anything below .10 is considered unconscious or dead.
 *                A normal human without a shield would be a 1.0.
 *                A turtle might have a health of 1.5
 *                A daisy might have a health of .101  (slightly better than .10
 *                An oak tree might have a health of 9.99
 *
 *                Symbols are taken from unicode or just txt.
 *                For an alien you could use ðŸ‘½ or just an "A"
 *                A good source is https://emojipedia.org
 *                Using emoji will mess up printing because they are not standard width.
 */
public class Entity {
    static String symbol = "\uD83D\uDC7D";// https://emojipedia.org/white-question-mark-ornament/

    static int nextID = 10001;
    static double strength = 0.0; // 1. is normal human strength
    static double aggressiveness = 0.0;  //1.0 is human in combat situation.  Must not be > 1.0
    static double defaultHealth = .10;  //1.0 is normal human health and stamina.
    static String attackMessage = "swings generically";
    static String passiveMessage = "stares into space";

    //individual characteristics
    int id;
    double health;

    public Entity() {
        id = nextID;
        nextID++;
        health = defaultHealth;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getHealth() {
        return health;
    }

    public void subtractHealth(double delta) {
        health -= delta;
    }

    public String getAttackMessage() {
        return attackMessage;
    }

    public String getPassiveMessage() {
        return passiveMessage;
    }

    public double getAggressiveness() {
        return aggressiveness;
    }

    public String toString() {
        return String.format("%s%6d (%2d%%)", getSymbol(), id, (int) Math.round(getHealth() * 100));
    }

    public double getStrength() {
        return strength;
    }

    //equals and hasCode were generated automatically, using only the id field.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity that = (Entity) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

