package buontyhunter.model;

import buontyhunter.common.Point2d;
import buontyhunter.common.Vector2d;
import buontyhunter.graphics.GraphicsComponent;
import buontyhunter.input.InputComponent;
import buontyhunter.physics.PhysicsComponent;

public class FighterEntity extends GameObject {

    private int health;
    private final int maxHealth;

    /**
     * Create a new fighter entity which is a game object with health
     * @param type this entity type serve to identify the entity (it can be player, enemy, etc)
     * @param pos initial position of the entity
     * @param vel initial velocity of the entity
     * @param box TODO: what is this?
     * @param input InputComponent that will be used to control the entity while playing
     * @param graph GraphicsComponent that will be used to draw the entity
     * @param phys PhysicsComponent that will be used to calculate the entity physics when an event occurs (Example: collision)
     * @param health initial health of the entity
     * @param maxHealth maximum health that the entity can have
     */
    public FighterEntity(GameObjectType type, Point2d pos, Vector2d vel, BoundingBox box, InputComponent input,
            GraphicsComponent graph, PhysicsComponent phys, int health,final int maxHealth) {
        super(type, pos, vel, box, input, graph, phys);
        setHealth(health);
        if(maxHealth >= health) {
            this.maxHealth = maxHealth;
        }else {
            throw new IllegalArgumentException("Max health must be greater than health");
        }
    }

    /**
     * Get the current health of the entity
     * @return the current health of the entity
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the maximum health of the entity
     * @return the maximum health of the entity
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set the health of the entity
     * @param healt the new health of the entity (must be positive & > 0)
     */
    public void setHealth(int healt) {
        if (healt <= 0) {
            throw new IllegalArgumentException("Healt must be positive");
        }
        this.health = healt;
    }
}