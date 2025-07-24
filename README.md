# CS61B
> My note of CS61B is available at the website https://note.zhuiguang.tech

## Project

### Project0:2048

Project0 以 2048 游戏为背景，希望我们完成 `emptySpaceExists`、`makeTileExists`、`atLeastOneMoveExists` 和 `tilt` 四个方法

#### `emptySpaceExists` 方法

该方法是要检查棋盘中是否存在为 `null` 的格子，只要注意一下 `value` 是 `private` 的，不能直接访问，需要用 `tile` 函数间接访问即可，实现用一个很简单的两层循环就行

#### `maxTileExists` 方法

`maxTileExists` 方法是要检查棋盘上是否有值等于 `MAX_PIECE` 的方块，检查的循环逻辑和 `emptySpaceExists` 方法类似，但是要注意调用 `b.tile(row,col).value()` 的时候需要先检查 `b.tile(row,col)` 是否为 `null`，如果是 `null`，就会抛出 `NullPointerException` 的异常，导致 `TestMaxTileExists.java` 测试不通过

#### `atLeastOneMoveExist` 方法

`atLeastOneMoveExist` 的判定比较容易理解，两个条件：

1. 棋盘上还有空格
2. 相邻方块有相同值

棋盘上还有空格通过 `emptySpaceExists` 方法就能很容易地判定，但是第二个条件看起来似乎没有那么好实现，我自己的做法是定义了一个 `checkvalue` 函数，其函数原型如下：

```java
public static boolean checkvalue(int row,int col,Board b)
```

用于检查相邻的方块是否有相同值，从而简化 `atLeastOneMoveExist` 函数的实现，但实际上 `checkvalue` 函数的实现仍然是非常繁琐的。在 `proj0/game2048/Model.java` 的注释里我贴了一个 AI 实现的 `atLeastOneMoveExist` 函数，实现非常的简洁，主要逻辑是：
- 从左上角开始遍历棋盘(`row` 从 0 到 `size-1`，`col` 从 0 到 `size -1`)
- 对于每一个非空的 `Tile`，只检查它右边和下边的相邻格子

这样检查的逻辑比我们想象的简单很多，这是因为当我们检查当前格子的右边格子时，右边格子的左边就是当前格子，所以在后面循环到“右边格子”时我们不再需要检查它的左边格子；下边格子也是同理，因此我们只需要检查右边和下面的格子就可以，这样的逻辑是简单的。

#### `tilt`方法

>这里 Tips 中有这么一段话 "In a given column, the piece on the top row (row 3) stays put. The piece on row 2 can move up if the space above it is empty, or it can move up one if the space above it has the same value as itself. In other words, when iterating over rows, it is safe to iterate starting from row 3 down, since there’s no way a tile will have to move again after moving once."。需要非常注意的是，top row 被标记为了 row 3 (也即 board.size()-1)，这和我们平常的规定是不同的，如果没注意的话后面的理解、实现会有比较大的偏差，可以自行对照 `TestUpOnly` 查看预期结果和实际结果 debug。

**视角朝北的实现：**

我的实现是这样的，CS61B 的文档给出了部分提示，我们可以从第二行（实际的行坐标是 `board.size()-2`）向下进行遍历，然后对每一个 tile 考虑它的 `move`。对于当前的 tile `tile(col,row)` 大致可以分为以下三种情况：
- 该列上方所有行都为 null，只需直接移动到该列的第一行即可，也即 `board.move(col,board.size()-1,tile(col,row))`
- 该列上方有不为 null 的 tile（我们需要找到离当前 tile 最近的一个，我们记其横坐标为 `destination`），两者的 value 并不相等，这时候需要移动到它的下方，也即 `board.move(col,destination-1,tile(col,row))`
- 该列上方有不为 null 的 tile（也要找到离当前 tile 最近的一个，我们同样记其横坐标为 `destination`），且其 value 与
`tile(col,row).value` 相同，**这时候我们就需要考虑 `(col,destination)` 这个位置是否合并过，因为题目明确指出只能合并一次**。我的实现是采用了一个 boolean 类型的二维数组 mergedArrays，默认值为 false，用于标记当前位置是否合并。如果值为 false，那就移动到 `(col,destination)` 的位置，并且更新分数；如果值为 true，那就移动到 `(col,destination-1)` 的位置。

实现完视角朝北，对于其他值的 `side`，我们只需要仿照项目文档给出的，在最开始的时候设置 `board.setViewingPerspective(Side.blabla)`，然后在最后设置 `board.setViewingPerspective(Side.NORTH)` 即可

## Lab

