package api;

import model.physics.Vector;

/**
 * Interface for the physics engine
 *
 * @author Tom Wu
 */
public interface IPhysicsEngine {
    /**
     * Update Positions of IEntities in universe based on dt and their Velocities
     *
     * @param universe IEntitySystem containing IEntities with both Positions and Velocities
     * @param dt       duration of update in seconds
     */
    void update (ILevel universe, double dt);

    /**
     * Marks Collision IComponents of applicable IEntities in universe with appropriate collidingIDs
     *
     * @param universe   IEntitySystem containing IEntities with both Positions and Collisions
     * @param dynamicsOn if true, then impulses are subsequently applied to applicable IEntities
     *                   (with Mass, Position, Velocity, and of course, Collision)
     */
    void applyCollisions (ILevel universe, boolean dynamicsOn);

    /**
     * Useful for debugging but will be deprecated!
     * Applies impulse J to IEntity body
     *
     * @param body IEntity with Mass, Position, and Velocity
     * @param J    Impulse vector used to calculate the updated Velocity
     * @return whether J is successfully applied to body
     * (false if body does not have Mass, Position, and Velocity)
     */
    boolean applyImpulse (IEntity body, Vector J);
}