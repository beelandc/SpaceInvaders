package net.beeland.spaceinvaders.collision;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.*;

/**
 * CollisionVisitor - Visitor pattern interface for collision detection
 * This is a placeholder that will be fully implemented in Phase 4 (Collision System)
 * 
 * The Visitor pattern allows double dispatch for collision detection between
 * different game object types without instanceof checks.
 */
public interface CollisionVisitor {
    
    // Shield-related visits
    default void visitShieldBrick(ShieldBrick shieldBrick) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitShieldColumn(Composite shieldColumn) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitShieldGroup(Composite shieldGroup) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitShieldRoot(Composite shieldRoot) {
        // Placeholder - will be implemented in collision system
    }
    
    // Wall-related visits
    default void visitWall(Wall wall) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitLeftWall(Wall leftWall) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitRightWall(Wall rightWall) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitTopWall(Wall topWall) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitBottomWall(Wall bottomWall) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitLeftBumper(Wall leftBumper) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitRightBumper(Wall rightBumper) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitWallGroup(Composite wallGroup) {
        // Placeholder - will be implemented in collision system
    }
    
    // Game entity visits (already exist, adding for completeness)
    default void visitMissile(Missile missile) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitCoreCannon(CoreCannon coreCannon) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitAlien(Alien alien) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitBomb(Bomb bomb) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitBombRoot(Composite bombRoot) {
        // Placeholder - will be implemented in collision system
    }
    
    // Flying Saucer visits
    default void visitFlyingSaucer(FlyingSaucer flyingSaucer) {
        // Placeholder - will be implemented in collision system
    }
    
    default void visitFlyingSaucerRoot(Composite flyingSaucerRoot) {
        // Placeholder - will be implemented in collision system
    }
}