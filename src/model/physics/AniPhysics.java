//package model.physics;
//
//import api.IEntity;
//import api.ILevel;
//import api.IPhysicsEngine;
//import javafx.geometry.Point2D;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class AniPhysics implements IPhysicsEngine {
//
//    private Map<String, Vector> globalForces;
//
//    public AniPhysics () {
//        globalForces = new HashMap<>();
//    }
//
//    public static void main (String[] args) {
//        AniPhysics ani = new AniPhysics();
//        ani.addGlobalForce("f1", new Vector(1, 2));
//        ani.addGlobalForce("f2", new Vector(3, 4));
//        System.out.println(ani.netGlobalForces().getXComponent());
//        System.out.println(ani.netGlobalForces().getYComponent());
//    }
//
//    @Override
//    public void update (ILevel level, double dt) {
//        level.getEntitySystem().getAllEntities().stream().forEach(this::calculateForces);
//
//
//    }
//
//    @Override
//    public void applyCollisions (ILevel universe) {
//        // TODO Auto-generated method stub
//
//    }
//
//    public void addGlobalForce (String forceName, Vector force) {
//        globalForces.put(forceName, force);
//    }
//
//    public Vector getGlobalForce (String forceName) {
//        return globalForces.get(forceName);
//    }
//
//    public void removeGlobalForce (String forceName) {
//        globalForces.remove(forceName);
//    }
//
//    private void calculateForces (IEntity entity) {
//
//    }
//
//    private Vector netGlobalForces () {
//        Vector netVector = globalForces.values().stream()
//                .reduce(Vector::add)
//                .get();
//        return netVector;
//    }
//
//    public void update () {
//
//    }
//
//    @Override
//    public boolean applyImpulse (IEntity body, Point2D impulse) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//}
