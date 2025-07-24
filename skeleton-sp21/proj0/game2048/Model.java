package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author zhuiguang
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        // 如果视角不是朝北，就设置视角朝北以简化实现
        if(side == Side.WEST){
            board.setViewingPerspective(Side.WEST);
        }else if(side == Side.EAST){
            board.setViewingPerspective(Side.EAST);
        }else if(side == Side.SOUTH){
            board.setViewingPerspective(Side.SOUTH);
        }

        boolean[][] mergedArrays= new boolean[board.size()][board.size()];

        for(int col = 0; col < board.size(); col++){
            for(int row = board.size()-2; row >= 0; row--){
                if(tile(col,row)==null){
                    continue;
                }
                // 这里我大致把 board 上面的每个格子分成了三类情况
                // 第一种，上方所有格子内没有 tile
                // 第二种，上方格子有 tile，但是值不相等
                // 第三种，上方格子有 tile，且值相等，这时候要依靠 mergedArrays 来区分是否合并（也即移动到当前格子还是移动到下面的格子）

                boolean allNull = true; // 判断是否均为 null

                // 循环遍历 (row,col) 位置元素上方的所有 tile，判断是否均为 null
                for(int it = board.size()-1; it > row; it--){

                    // 如果有非 null 的
                    if(tile(col,it) != null){
                        allNull = false;
                        break;
                    }
                }
                if(allNull){ // 均为 null，直接将当前 tile 移动到 该列的第一行，并且将 changed 标记为 true
                    board.move(col, board.size()-1,tile(col,row));
                    changed = true;
                }else{ // 上方有 tile 非 null 的情形
                    int destination= board.size()-1;
                    // 遍历上方所有行，找出离 (col,row) 最近的非 null 的 tile
                    for(int it = board.size()-1; it > row; it--){
                        if(tile(col,it) != null){
                            destination = it;
                        }
                    }
                    if(tile(col,destination).value() != tile(col,row).value()){ // 非 null 但是值不相等，移动到其下方
                        board.move(col,destination-1,tile(col,row));
                        changed = true;
                    }else{ // 非 null 且值相等，分两类情况考虑，当前区域是否进行过合并，也即 mergedArrays 的值是 true 还是 false
                        if(mergedArrays[col][destination] == false){ // 当前区域未合并
                            changed = true;
                            score = score + 2 * tile(col,row).value();
                            board.move(col,destination,tile(col,row));
                            mergedArrays[col][destination] = true;
                        }else{ // 当前区域已经合并
                            changed = true;
                            board.move(col,destination-1,tile(col,row));
                        }
                    }
                }
            }
        }





        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0; i < b.size(); i++){
            for(int j = 0; j < b.size(); j++){
                if(b.tile(j,i) == null){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for(int row = 0; row < b.size(); row++){
            for(int col = 0; col < b.size(); col++){
                if(b.tile(col,row) != null && (b.tile(col,row)).value() == MAX_PIECE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean checkvalue(int col, int row, Board b) {
        if (col == 0) {
            if (row == 0) {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value()) {
                    return true;
                }
            } else if (row == b.size() - 1) {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            } else {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            }
        } else if (col == b.size() - 1) {
            if (row == 0) {
                if (b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value()) {
                    return true;
                }
            } else if (row == b.size() - 1) {
                if (b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            } else {
                if (b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            }
        } else {
            if (row == 0) {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value()) {
                    return true;
                }
            } else if (row == b.size() - 1) {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            } else {
                if (b.tile(col, row).value() == b.tile(col + 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col - 1, row).value() ||
                        b.tile(col, row).value() == b.tile(col, row + 1).value() ||
                        b.tile(col, row).value() == b.tile(col, row - 1).value()) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if(emptySpaceExists(b)){
            return true;
        }else{
            for(int row = 0; row < b.size(); row++){
                for(int col = 0; col < b.size(); col++){
                    if(checkvalue(col,row,b)){
                        return true;
                    }
                }
            }
            return false;

        }
    }
    /*
     这里我的实现写的比较繁琐，我是自己定义了一个 checkvalue 函数 `public static boolean checkvalue(int row,int col,Board b)`
     （不过这个实现实在是太臭了），用于检查第二条要求 'There are two adjacent tiles with the same value'，
     然后 `atLeastOneMoveExists` 函数使用 `checkvalue`函数实现，实现逻辑看起来就没那么繁琐
     */

    // 下面是 AI 提供的一个更简便的思路，其大概的思路是由于我们迭代是从左上方开始迭代的，所以我们检查的时候其实只需要检查右下就可以了，事实上，后面的
    // 方块的左上其实在前面就已经检查过了，所以逻辑就没有我们想象中的那么繁琐。
    /*
    public static boolean atLeastOneMoveExists(Board b) {
        int size = b.size();

        // 如果存在空位，直接返回 true
        if (emptySpaceExists(b)) {
            return true;
        }

        // 检查是否有相邻且值相同的 tile
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile current = b.tile(row, col);

                // 向右和向下检查，避免重复检查
                if (col + 1 < size && b.tile(col + 1, row) != null &&
                        current.value() == b.tile(col + 1, row).value()) {
                    return true;
                }

                if (row + 1 < size && b.tile(col, row + 1) != null &&
                        current.value() == b.tile(col, row + 1).value()) {
                    return true;
                }
            }
        }

        return false;
    }

*/

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
