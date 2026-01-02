package net.beeland.spaceinvaders.command;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.gameobject.FlyingSaucer;
import net.beeland.spaceinvaders.gameobject.GameObject;
import net.beeland.spaceinvaders.gameobject.GameObjectManager;
import net.beeland.spaceinvaders.image.Image;
import net.beeland.spaceinvaders.sprite.SpriteProxy;
import net.beeland.spaceinvaders.timer.TimeEvent;
import net.beeland.spaceinvaders.timer.TimerManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Random;

/**
 * DeployFlyingSaucerCommand - Command for spawning a flying saucer
 * Randomly spawns from left or right side of screen
 * Schedules movement and bomb dropping
 */
@ApplicationScoped
public class DeployFlyingSaucerCommand extends Command {
    
    @Inject
    GameObjectManager gameObjectManager;
    
    @Inject
    TimerManager timerManager;
    
    @Inject
    FlyingSaucerMovement flyingSaucerMovement;
    
    @Inject
    DropFlyingSaucerBombCommand dropFlyingSaucerBombCommand;
    
    private Random random;
    
    /**
     * Default constructor for CDI
     */
    public DeployFlyingSaucerCommand() {
        this.random = new Random();
    }
    
    @Override
    public void execute(float deltaTime) {
        // Find the flying saucer root composite
        Composite flyingSaucerRoot = (Composite) gameObjectManager.find(GameObject.GameObjectName.FLYING_SAUCER_ROOT);
        
        // Only deploy if there is not already a flying saucer on screen
        if (flyingSaucerRoot != null && flyingSaucerRoot.getFirstChild() == null) {
            // Randomly choose starting location (left or right)
            // 0 == Left, 1 == Right
            int randomStart = random.nextInt(2);
            
            // Calculate starting position and movement direction
            float xPos = 65.0f + (randomStart * 770.0f);
            float xDelta = (randomStart == 0) ? 10.0f : -10.0f;
            float yPos = 735.0f;
            
            // Create the flying saucer with position
            FlyingSaucer flyingSaucer = new FlyingSaucer(GameObject.GameObjectName.FLYING_SAUCER, xPos, yPos);
            
            // TODO: Set sprite when sprite system is integrated
            // For now, the sprite proxy is created in GameObject constructor
            // TODO: Set image when image definitions are created
            // Image flyingSaucerImage = imageManager.find(Image.ImageName.FLYING_SAUCER);
            // flyingSaucer.getSpriteProxy().setImage(flyingSaucerImage);
            
            // Attach to game object manager
            gameObjectManager.attach(flyingSaucer);
            
            // Add to flying saucer root composite
            flyingSaucerRoot.add(flyingSaucer);
            
            // TODO: Activate sprite batch when sprite batch system is integrated
            // flyingSaucer.activateSprite(spriteBatchManager.find(SpriteBatch.Name.FlyingSaucer));
            // flyingSaucer.activateCollisionSprite(spriteBatchManager.find(SpriteBatch.Name.CollisionBox));
            
            // Schedule movement command
            flyingSaucerMovement.set(GameObject.GameObjectName.FLYING_SAUCER, xDelta, 0.0f);
            timerManager.add(TimeEvent.Name.SPRITE_MOVEMENT, flyingSaucerMovement, 0.1f);
            
            // Schedule bomb dropping with random delay
            timerManager.add(TimeEvent.Name.DROP_BOMB, dropFlyingSaucerBombCommand, random.nextFloat());
        }
    }
    
    @Override
    protected void wash() {
        super.wash();
        // Reset state if needed
    }
    
    @Override
    public void dump() {
        System.out.println("DeployFlyingSaucerCommand");
    }
}