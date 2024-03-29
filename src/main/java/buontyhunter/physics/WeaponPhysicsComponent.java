package buontyhunter.physics;

import buontyhunter.model.CollisionDetector;
import buontyhunter.model.FighterEntity;
import buontyhunter.model.GameObject;
import buontyhunter.model.HidableObject;
import buontyhunter.model.RectBoundingBox;
import buontyhunter.model.World;

public class WeaponPhysicsComponent extends PhysicsComponent {
    private FighterEntity owner;

    public WeaponPhysicsComponent(FighterEntity owner) {
        this.owner = owner;
    }

    /**
     * on attack check if the BoundingBox of the weapon is colliding with the BoundingBox of a fighter entity and if so deal damage to the fighter entity depending on the Type of the FigherEntity
     */
    @Override
    public void update(long td, GameObject obj, World world) {
        if (((HidableObject) obj).isShow()) {
            CollisionDetector detector = new CollisionDetector();

            world.getFighterEntities().forEach(fighter -> {
                if (fighter.getType() != owner.getType()) {
                    RectBoundingBox realBoundingBox = ((RectBoundingBox) fighter.getBBox()).withPoint(fighter.getPos());
                    if (detector.isColliding(((RectBoundingBox) obj.getBBox()), realBoundingBox)) {
                        fighter.takeDamage(owner.getWeapon().getDamage());
                    }
                }
            });
        }
    }
}
