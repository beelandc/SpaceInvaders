package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.strategy.FallDagger;
import net.beeland.spaceinvaders.strategy.FallStraight;
import net.beeland.spaceinvaders.strategy.FallStrategy;
import net.beeland.spaceinvaders.strategy.FallZigZag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Bomb class
 */
@DisplayName("Bomb Tests")
class BombTest {
    
    private FallStrategy straightStrategy;
    private Bomb bomb;
    
    @BeforeEach
    void setUp() {
        straightStrategy = new FallStraight();
        bomb = new Bomb(GameObject.GameObjectName.BOMB, straightStrategy, 100.0f, 200.0f);
    }
    
    @Test
    @DisplayName("Constructor should initialize with correct name")
    void testConstructor() {
        assertEquals(GameObject.GameObjectName.BOMB, bomb.getName());
    }
    
    @Test
    @DisplayName("Constructor should set initial position")
    void testConstructorPosition() {
        assertEquals(100.0f, bomb.getX(), 0.001f);
        assertEquals(200.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor should set default delta to 4.0")
    void testConstructorDelta() {
        assertEquals(4.0f, bomb.getDelta(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor should initialize scale to 1.0")
    void testConstructorScale() {
        assertEquals(1.0f, bomb.getScaleX(), 0.001f);
        assertEquals(1.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("Constructor should throw exception for null strategy")
    void testConstructorNullStrategy() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bomb(GameObject.GameObjectName.BOMB, null, 100.0f, 200.0f);
        });
    }
    
    @Test
    @DisplayName("Constructor should set strategy")
    void testConstructorStrategy() {
        assertSame(straightStrategy, bomb.getStrategy());
    }
    
    @Test
    @DisplayName("Update should move bomb downward")
    void testUpdateMovesDown() {
        float initialY = bomb.getY();
        
        bomb.update();
        
        assertEquals(initialY - 4.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Update should apply strategy")
    void testUpdateAppliesStrategy() {
        FallZigZag zigzag = new FallZigZag();
        zigzag.reset(200.0f);
        Bomb zigzagBomb = new Bomb(GameObject.GameObjectName.BOMB, zigzag, 100.0f, 200.0f);
        
        // Move bomb below target
        zigzagBomb.setY(180.0f);
        zigzagBomb.update();
        
        // Strategy should have flipped X scale
        assertEquals(-1.0f, zigzagBomb.getScaleX(), 0.001f);
    }
    
    @Test
    @DisplayName("Multiple updates should continue moving downward")
    void testMultipleUpdates() {
        float initialY = bomb.getY();
        
        bomb.update();
        bomb.update();
        bomb.update();
        
        assertEquals(initialY - 12.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Reset should move bomb to off-screen position")
    void testReset() {
        bomb.reset();
        assertEquals(700.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Reset should reset strategy")
    void testResetResetsStrategy() {
        FallStraight strategy = new FallStraight();
        strategy.reset(100.0f);
        Bomb testBomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
        
        testBomb.reset();
        
        assertEquals(700.0f, strategy.getOldPosY(), 0.001f);
    }
    
    @Test
    @DisplayName("GetBoundingBoxHeight should return default value")
    void testGetBoundingBoxHeight() {
        assertEquals(16.0f, bomb.getBoundingBoxHeight(), 0.001f);
    }
    
    @Test
    @DisplayName("MultiplyScale should multiply X scale")
    void testMultiplyScaleX() {
        bomb.multiplyScale(2.0f, 1.0f);
        assertEquals(2.0f, bomb.getScaleX(), 0.001f);
        assertEquals(1.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("MultiplyScale should multiply Y scale")
    void testMultiplyScaleY() {
        bomb.multiplyScale(1.0f, 2.0f);
        assertEquals(1.0f, bomb.getScaleX(), 0.001f);
        assertEquals(2.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("MultiplyScale should accumulate")
    void testMultiplyScaleAccumulates() {
        bomb.multiplyScale(2.0f, 3.0f);
        bomb.multiplyScale(2.0f, 2.0f);
        
        assertEquals(4.0f, bomb.getScaleX(), 0.001f);
        assertEquals(6.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("MultiplyScale with negative values should flip")
    void testMultiplyScaleNegative() {
        bomb.multiplyScale(-1.0f, -1.0f);
        assertEquals(-1.0f, bomb.getScaleX(), 0.001f);
        assertEquals(-1.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("SetScaleX should update X scale")
    void testSetScaleX() {
        bomb.setScaleX(5.0f);
        assertEquals(5.0f, bomb.getScaleX(), 0.001f);
    }
    
    @Test
    @DisplayName("SetScaleY should update Y scale")
    void testSetScaleY() {
        bomb.setScaleY(5.0f);
        assertEquals(5.0f, bomb.getScaleY(), 0.001f);
    }
    
    @Test
    @DisplayName("SetDelta should update fall speed")
    void testSetDelta() {
        bomb.setDelta(10.0f);
        assertEquals(10.0f, bomb.getDelta(), 0.001f);
        
        float initialY = bomb.getY();
        bomb.update();
        assertEquals(initialY - 10.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("SetStrategy should update strategy")
    void testSetStrategy() {
        FallZigZag newStrategy = new FallZigZag();
        bomb.setStrategy(newStrategy);
        assertSame(newStrategy, bomb.getStrategy());
    }
    
    @Test
    @DisplayName("SetStrategy should throw exception for null")
    void testSetStrategyNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            bomb.setStrategy(null);
        });
    }
    
    @Test
    @DisplayName("SetStrategy should reset new strategy")
    void testSetStrategyResetsStrategy() {
        FallStraight newStrategy = new FallStraight();
        bomb.setY(300.0f);
        bomb.setStrategy(newStrategy);
        
        assertEquals(300.0f, newStrategy.getOldPosY(), 0.001f);
    }
    
    @Test
    @DisplayName("Move should update position")
    void testMove() {
        bomb.move(10.0f, 20.0f);
        assertEquals(110.0f, bomb.getX(), 0.001f);
        assertEquals(220.0f, bomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Accept should call visitor's visit method")
    void testAccept() {
        TestVisitor visitor = new TestVisitor();
        
        bomb.accept(visitor);
        
        assertTrue(visitor.visitCalled);
        assertSame(bomb, visitor.visitedObject);
    }
    
    @Test
    @DisplayName("Bomb with FallStraight should fall straight")
    void testBombWithFallStraight() {
        FallStraight strategy = new FallStraight();
        Bomb straightBomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
        
        straightBomb.update();
        straightBomb.update();
        
        // Should only move down, no scale changes
        assertEquals(1.0f, straightBomb.getScaleX(), 0.001f);
        assertEquals(1.0f, straightBomb.getScaleY(), 0.001f);
        assertEquals(192.0f, straightBomb.getY(), 0.001f);
    }
    
    @Test
    @DisplayName("Bomb with FallZigZag should zigzag")
    void testBombWithFallZigZag() {
        FallZigZag strategy = new FallZigZag();
        strategy.reset(200.0f);
        Bomb zigzagBomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
        
        // Move below target
        zigzagBomb.setY(180.0f);
        zigzagBomb.update();
        
        // X scale should flip
        assertEquals(-1.0f, zigzagBomb.getScaleX(), 0.001f);
    }
    
    @Test
    @DisplayName("Bomb with FallDagger should flip vertically")
    void testBombWithFallDagger() {
        FallDagger strategy = new FallDagger();
        strategy.reset(200.0f);
        Bomb daggerBomb = new Bomb(GameObject.GameObjectName.BOMB, strategy, 100.0f, 200.0f);
        
        // Move below target
        daggerBomb.setY(180.0f);
        daggerBomb.update();
        
        // Y scale should flip
        assertEquals(-1.0f, daggerBomb.getScaleY(), 0.001f);
    }
    
    /**
     * Test visitor implementation
     */
    private static class TestVisitor implements GameObject.CollisionVisitor {
        boolean visitCalled = false;
        GameObject visitedObject = null;
        
        @Override
        public void visit(GameObject other) {
            visitCalled = true;
            visitedObject = other;
        }
    }
}