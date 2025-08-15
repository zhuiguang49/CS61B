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

### Project1:Data Structures

#### checkPoint

需要做两件事情，基于链表和数组分别实现双向队列。其中基于链表实现双向队列是比较常规的，我们只需要给链表节点配备两个指针 `prev` 和 `next`，然后注意一下 Deque 的 API 部分处理一下边界情况即可。

着重说明一下基于数组实现双向队列，这点可以查看笔记网站里给出的一点[知识补充](https://note.zhuiguang.tech/CS/CS61B/Chapter2%20Lists/)。对于这样 `ArrayDeque`，我们需要提供这些变量：首先是底层的数组 `Item`，然后是当前已存储的 `size`，以及数组的容量 `capacity`，数组使用率 `ratio`，以及头索引 `first`，尾索引 `rear`。

我们首先说明一下 `resize` 的逻辑，有两种情况需要进行 `resize`，首先是 `ratio == 1`，也即数组已满的情况下，这时候我们将 `capacity` 变为原来的两倍，然后通过 `System.arraycopy` 进行数组复制，但是需要注意的是，拷贝的逻辑我们需要对 `first`、`rear` 的位置进行分类，当 `first` 在 `rear` 之后时，我们首先拷贝 `first` 到 `capacity` 的部分；然后再拷贝 `0` 到 `rear` 的部分；而当 `first` 在 `rear` 之前时，常规拷贝即可。另一种情况是 `size >= 16` 且 `ratio < 0.25` 的时候，这时候将 `capacity` 变为原来的 $\frac{1}{2}$，拷贝的时候仍然对 `first` 和 `rear` 的位置关系分两类。

接下来我们说明一下常规API，`printDeque`，和 `resize` 一样，对 `first`、`rear`分两类；而 `addFirst`、`addLast`、`removeFirst`、`removeLast` 的时候就需要注意一下循环数组的 `first`、`rear` 的变更逻辑。

然后对于 `equals`、`iterator()` 以及 `MaxArrayDeque` 等的实现需要对 `Iterable`、`Iterator`、`Comparable`、`Comparator` 等比较熟悉，我做的时候就有点懵懵懂懂，花了好久才大致搞明白，可以参考下我的笔记。


#### Guitar String

比较简单，不多说了

#### Extra Credit Autograder


## Lab

### lab 1

没啥好说的

### lab 2

>环境配置有点麻烦

#### Debugging

`DebugExercis2` 文件里的 `arrayMax`、`arraySum` 两个函数的实现有些问题，Lab2 应该是希望我们不去深究其具体实现，而是发现有问题后完全替代其实现。可供参考的是，直接运行得到的结果应该是 17，修改完 `arrayMax` 后得到的结果应该是 71，完全修改正确后运行结果应是 15

#### IntList 修复

**`addConstant`方法：** 这个方法希望给链表的每个结点都加上常数 c，我们很容易发现实现的错误在于没给尾结点加上这个常数，这是因为 `while` 循环的判定条件 `head.rest != null` 在 `head` 为尾结点的时候结果为 `false`，无法进入循环，导致尾结点没有加常数，我们只需要改成 `while(head != null)` 即可

**`setToZeroIfMaxFEL`方法:** 这里的错误实际上出在了 `firstDigitEqualsLastDigit` 函数中，具体在于
```java
while(x>10){
    x=x/10;
}
```

当 `x` 刚好为 `10` 时，不会执行 `x=x/10`，所以求出来的 `firstDigit` 为 0 而非正确的 1，最终发生了错误。

**`squarePrimes`方法：**

应该是最后 `return` 的递归调用有些问题，因为 `||` 的逻辑是短路的，我们 `return currElemIsPrime || SquarePrimes(lst.rest)`，如果 `currElemIsPrime` 为 `true`，由于短路逻辑就不会有后续的递归调用，导致并非所有素数都进行了平方操作。

我个人不太喜欢递归地实现，就直接改成了迭代。

### lab 3

#### Timing Tests for List61B

##### Timing the construction of an AList with a bad resize strategy

主要是想让我们测试数据规模 `N` 的增长对 `addLast` 耗时的影响，`AList` 的实现中，`addLast` 的 `resize` 操作是在数组满了的时候创建一个比原数组大1的新数组，构建这样的大小为 `N` 的列表的时间复杂度为 $O(N^2)$，我们需要计时来验证这个性能问题。

观察给出的 `printTimingTable` 函数我们可以知道，其接受三个参数，分别为 `AList<Integer>`、`AList<Double>`、`AList<Integer>`类型，分别对应数据规模 `N`，总耗时，存储操作次数。

我们要做的就是针对不同的 `N`(1000,2000,...,128000) 来计算其总耗时、操作数等等，然后构建出 `printTimingTable` 函数参数所需要的三个 `AList`，难度不大；计时问题实验文档也给出了说明，可以参考 `StopwatchDemo` 中的实现。

>需要说明的是，skeleton-sp21 仓库中并没有提供 `edu.princeton.cs.algs4.Stopwatch` 包，可以自行前往我的仓库的 `/skeleton-sp21/lab3` 目录下下载 `algs4.jar`，然后在 Intellij IDEA 中导入为库即可。

##### Timing the construction of an AList with a good resize strategy

更改一下 `resize` 策略，然后修改一下存放数据规模 `N` 的链表即可

##### Timing the getLast method of SLList

逻辑和 Timing Tests for List61B 差不多，只不过这时候 `N` 与 `ops` 不再相同，`ops` 代表的是我们进行 `getLast()` 的次数，这里我们固定为了 10000，而 `N` 仍然代表数据规模。

#### Randomized Comparison Tests

##### Simple Comparison Test

有一说一我不太会写 JUnit test，最开始都没有用上 `@Test` 等注解，也没有用 `assertEquals` 这种断言的形式，而是自己写了几个不是 JUnit test 的函数用于判断，还好后面问 AI 确认了一下。这题逻辑是不难的，可能目的是想让我们熟悉一下 JUnit 的书写规范和格式。

##### Adding Randomized Comparisons

创建 `RandomizedTest.java` 文件，在给定的代码的基础上进行修改，给出的 `operationNumber` 为 2 的时候，调用 `getLast()` 方法，但是需要注意在 `size` 为 0 的时候要 `continue`；在给出的 `operationNumber` 为 3 的时候，调用 `removeLast()` 方法，但是需要注意在 `size` 为 0 的时候需要 `continue`，然后给 `BuggyAList` 也调用相同的方法，对有返回值的函数使用 `assertEquals` 进行测试即可

##### BuggyAList 修复

`BuggyAList` 的问题出在了 `removeLast` 方法中，因为数组需要缩小容量的时候，它调用了 `resize(size / 4)` ，所以最终导致 `resize` 方法拷贝元素的时候数组越界。修改一下将 `removeLast` 方法中 `resize` 的实际参数改为 `items.length / 4` 即可





