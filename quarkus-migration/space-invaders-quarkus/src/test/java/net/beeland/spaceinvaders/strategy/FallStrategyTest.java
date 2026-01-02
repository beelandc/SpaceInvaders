package net.beeland.spaceinvaders.strategy;

import net.beeland.spaceinvaders.gameobject.Bomb;
import net.beeland.spaceinvaders.gameobject.GameObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FallStrategy implementations
 */
@DisplayName("FallStrategy Tests")
class FallStrategyTest {
    
    @Nested
    @DisplayName("FallStraight Tests")
    class FallStraightTest {
        
        @Test
        @DisplayName("Constructor should initialize oldPosY to 0")
        void testConstructor() {
            FallStraight strategy = new FallStraight();
            assertEquals(0.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Reset should update oldPosY")
        void testReset() {
            FallStraight strategy = new FallStraight();
            strategy.reset(100.0f);
            assertEquals(100.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should not modify bomb position or scale")
        void testFall() {
            FallStraight strategy = new FallStraight();
            Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
            
            float initialX = bomb.getX();
            float initialY = bomb.getY();
            float initialScaleX = bomb.getScaleX();
            float initialScaleY = bomb.getScaleY();
            
            strategy.fall(bomb);
            
            assertEquals(initialX, bomb.getX(), 0.001f);
            assertEquals(initialY, bomb.getY(), 0.001f);
            assertEquals(initialScaleX, bomb.getScaleX(), 0.001f);
            assertEquals(initialScaleY, bomb.getScaleY(), 0.001f);
        }
    }
    
    @Nested
    @DisplayName("FallZigZag Tests")
    class FallZigZagTest {
        
        @Test
        @DisplayName("Constructor should initialize oldPosY to 0")
        void testConstructor() {
            FallZigZag strategy = new FallZigZag();
            assertEquals(0.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Reset should update oldPosY")
        void testReset() {
            FallZigZag strategy = new FallZigZag();
            strategy.reset(100.0f);
            assertEquals(100.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should flip X scale when bomb passes target Y")
        void testFallFlipsXScale() {
            FallZigZag strategy = new FallZigZag();
            strategy.reset(200.0f);
            
            Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
            float initialScaleX = bomb.getScaleX();
            
            // Move bomb below target (200 - 16 = 184)
            bomb.setY(180.0f);
            strategy.fall(bomb);
            
            // X scale should be flipped
            assertEquals(-initialScaleX, bomb.getScaleX(), 0.001f);
            // Y scale should remain unchanged
            assertEquals(1.0f, bomb.getScaleY(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should not flip when bomb is above target")
        void testFallNoFlipAboveTarget() {
            FallZigZag strategy = new FallZigZag();
            strategy.reset(200.0f);
            
            Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
            float initialScaleX = bomb.getScaleX();
            
            // Bomb is still above target
            bomb.setY(190.0f);
            strategy.fall(bomb);
            
            // Scale should not change
            assertEquals(initialScaleX, bomb.getScaleX(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should handle null bomb gracefully")
        void testFallWithNullBomb() {
            FallZigZag strategy = new FallZigZag();
            assertDoesNotThrow(() -> strategy.fall(null));
        }
    }
    
    @Nested
    @DisplayName("FallDagger Tests")
    class FallDaggerTest {
        
        @Test
        @DisplayName("Constructor should initialize oldPosY to 0")
        void testConstructor() {
            FallDagger strategy = new FallDagger();
            assertEquals(0.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Reset should update oldPosY")
        void testReset() {
            FallDagger strategy = new FallDagger();
            strategy.reset(100.0f);
            assertEquals(100.0f, strategy.getOldPosY(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should flip Y scale when bomb passes target Y")
        void testFallFlipsYScale() {
            FallDagger strategy = new FallDagger();
            strategy.reset(200.0f);
            
            Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
            float initialScaleY = bomb.getScaleY();
            
            // Move bomb below target (200 - 16 = 184)
            bomb.setY(180.0f);
            strategy.fall(bomb);
            
            // Y scale should be flipped
            assertEquals(-initialScaleY, bomb.getScaleY(), 0.001f);
            // X scale should remain unchanged
            assertEquals(1.0f, bomb.getScaleX(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should not flip when bomb is above target")
        void testFallNoFlipAboveTarget() {
            FallDagger strategy = new FallDagger();
            strategy.reset(200.0f);
            
            Bomb bomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
            float initialScaleY = bomb.getScaleY();
            
            // Bomb is still above target
            bomb.setY(190.0f);
            strategy.fall(bomb);
            
            // Scale should not change
            assertEquals(initialScaleY, bomb.getScaleY(), 0.001f);
        }
        
        @Test
        @DisplayName("Fall should handle null bomb gracefully")
        void testFallWithNullBomb() {
            FallDagger strategy = new FallDagger();
            assertDoesNotThrow(() -> strategy.fall(null));
        }
    }
    
    @Nested
    @DisplayName("Strategy Comparison Tests")
    class StrategyComparisonTest {
        
        @Test
        @DisplayName("Different strategies should have different behaviors")
        void testDifferentStrategies() {
            FallStraight straight = new FallStraight();
            FallZigZag zigzag = new FallZigZag();
            FallDagger dagger = new FallDagger();
            
            Bomb bomb1 = new Bomb(GameObject.GameObjectName.BOMB, straight, 100.0f, 200.0f);
            Bomb bomb2 = new Bomb(GameObject.GameObjectName.BOMB, zigzag, 100.0f, 200.0f);
            Bomb bomb3 = new Bomb(GameObject.GameObjectName.BOMB, dagger, 100.0f, 200.0f);
            
            // Move all bombs below their targets
            bomb1.setY(180.0f);
            bomb2.setY(180.0f);
            bomb3.setY(180.0f);
            
            straight.fall(bomb1);
            zigzag.fall(bomb2);
            dagger.fall(bomb3);
            
            // Straight: no scale change
            assertEquals(1.0f, bomb1.getScaleX(), 0.001f);
            assertEquals(1.0f, bomb1.getScaleY(), 0.001f);
            
            // ZigZag: X scale flipped
            assertEquals(-1.0f, bomb2.getScaleX(), 0.001f);
            assertEquals(1.0f, bomb2.getScaleY(), 0.001f);
            
            // Dagger: Y scale flipped
            assertEquals(1.0f, bomb3.getScaleX(), 0.001f);
            assertEquals(-1.0f, bomb3.getScaleY(), 0.001f);
        }
    }
}