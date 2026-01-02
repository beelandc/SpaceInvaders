package net.beeland.spaceinvaders.gameobject;

import net.beeland.spaceinvaders.composite.Composite;
import net.beeland.spaceinvaders.sprite.SpriteBatch;

/**
 * AlienFactory is responsible for creating the alien grid structure.
 * It creates an 11x5 formation of aliens with the proper types:
 * - Row 1 (top): 11 Squid aliens (30 points each)
 * - Rows 2-3: 22 Crab aliens (20 points each)
 * - Rows 4-5 (bottom): 22 Jellyfish aliens (10 points each)
 * 
 * Total: 55 aliens in the grid
 * 
 * The factory handles:
 * - Creating the AlienGrid composite
 * - Creating 11 AlienGridColumn composites
 * - Populating each column with 5 aliens
 * - Setting initial positions and spacing
 */
public class AlienFactory {
    
    // Spacing constants
    private static final float COLUMN_SPACING = 60.0f;  // Horizontal spacing between columns
    private static final float ROW_SPACING = -40.0f;    // Vertical spacing between rows (negative = down)
    
    // Initial movement speed
    private static final float INITIAL_X_DELTA = 20.0f;  // Horizontal movement per update
    private static final float INITIAL_Y_DELTA = 0.0f;   // Vertical movement (0 until hitting wall)

    /**
     * Creates a complete alien grid with 11 columns and 5 rows (55 aliens total).
     * 
     * Grid formation:
     * Column:  1    2    3    4    5    6    7    8    9   10   11
     * Row 1:  Squid Squid Squid Squid Squid Squid Squid Squid Squid Squid Squid
     * Row 2:  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab
     * Row 3:  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab  Crab
     * Row 4:  Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly
     * Row 5:  Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly Jelly
     *
     * @param startX The starting X position for the grid (left edge)
     * @param startY The starting Y position for the grid (top row)
     * @return The created AlienGrid composite
     */
    public static AlienGrid createAlienGrid(float startX, float startY) {
        // Create the root AlienGrid composite
        AlienGrid alienGrid = new AlienGrid(
            Composite.CompositeName.ALIEN_GRID,
            INITIAL_X_DELTA,
            INITIAL_Y_DELTA
        );
        
        // Create 11 columns
        for (int col = 0; col < 11; col++) {
            // Calculate column X position
            float columnX = startX + (COLUMN_SPACING * col);
            
            // Create column composite with appropriate name
            Composite.CompositeName columnName = getColumnName(col);
            AlienGridColumn column = new AlienGridColumn(columnName);
            
            // Populate column with 5 aliens
            createAlienColumn(column, columnX, startY);
            
            // Add column to grid
            alienGrid.add(column);
        }
        
        return alienGrid;
    }

    /**
     * Creates a single column of 5 aliens.
     * 
     * @param column The column composite to populate
     * @param x The X position for all aliens in this column
     * @param startY The Y position for the top alien
     */
    private static void createAlienColumn(AlienGridColumn column, float x, float startY) {
        // Row 1: Squid (top)
        SquidAlien squid = new SquidAlien(x, startY);
        column.add(squid);
        
        // Row 2: Crab
        CrabAlien crab1 = new CrabAlien(x, startY + ROW_SPACING);
        column.add(crab1);
        
        // Row 3: Crab
        CrabAlien crab2 = new CrabAlien(x, startY + (ROW_SPACING * 2));
        column.add(crab2);
        
        // Row 4: Jellyfish
        JellyfishAlien jellyfish1 = new JellyfishAlien(x, startY + (ROW_SPACING * 3));
        column.add(jellyfish1);
        
        // Row 5: Jellyfish (bottom)
        JellyfishAlien jellyfish2 = new JellyfishAlien(x, startY + (ROW_SPACING * 4));
        column.add(jellyfish2);
    }

    /**
     * Gets the appropriate column name for the given column index.
     *
     * @param columnIndex The column index (0-10)
     * @return The composite name for this column
     */
    private static Composite.CompositeName getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return Composite.CompositeName.ALIEN_COL_1;
            case 1: return Composite.CompositeName.ALIEN_COL_2;
            case 2: return Composite.CompositeName.ALIEN_COL_3;
            case 3: return Composite.CompositeName.ALIEN_COL_4;
            case 4: return Composite.CompositeName.ALIEN_COL_5;
            case 5: return Composite.CompositeName.ALIEN_COL_6;
            case 6: return Composite.CompositeName.ALIEN_COL_7;
            case 7: return Composite.CompositeName.ALIEN_COL_8;
            case 8: return Composite.CompositeName.ALIEN_COL_9;
            case 9: return Composite.CompositeName.ALIEN_COL_10;
            case 10: return Composite.CompositeName.ALIEN_COL_11;
            default:
                throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        }
    }

    /**
     * Destroys an alien grid by removing all aliens and columns.
     * This is used when resetting the level or game.
     *
     * @param alienGrid The alien grid to destroy
     */
    public static void destroyAlienGrid(AlienGrid alienGrid) {
        // Iterate through all columns
        AlienGridColumn column = (AlienGridColumn) alienGrid.getFirstChild();
        while (column != null) {
            // Get next column before destroying current one
            AlienGridColumn nextColumn = (AlienGridColumn) column.getNext();
            
            // Iterate through all aliens in this column
            GameObject alien = (GameObject) column.getFirstChild();
            while (alien != null) {
                // Get next alien before destroying current one
                GameObject nextAlien = (GameObject) alien.getNext();
                
                // Remove alien from column
                column.remove(alien);
                
                // Mark alien for death
                alien.markForDeath();
                
                alien = nextAlien;
            }
            
            // Remove column from grid
            alienGrid.remove(column);
            
            // Mark column for death
            column.markForDeath();
            
            column = nextColumn;
        }
    }

    /**
     * Counts the number of living aliens in the grid.
     *
     * @param alienGrid The alien grid to count
     * @return The number of living aliens
     */
    public static int countLivingAliens(AlienGrid alienGrid) {
        int count = 0;
        
        // Iterate through all columns
        AlienGridColumn column = (AlienGridColumn) alienGrid.getFirstChild();
        while (column != null) {
            // Iterate through all aliens in this column
            GameObject alien = (GameObject) column.getFirstChild();
            while (alien != null) {
                if (!alien.isMarkedForDeath()) {
                    count++;
                }
                alien = (GameObject) alien.getNext();
            }
            column = (AlienGridColumn) column.getNext();
        }
        
        return count;
    }

    /**
     * Gets the default starting position for the alien grid.
     * This positions the grid near the top-center of the screen.
     *
     * @return Array with [x, y] coordinates
     */
    public static float[] getDefaultStartPosition() {
        // Position grid starting at x=100, y=500 (near top of 800x600 screen)
        return new float[] { 100.0f, 500.0f };
    }
}