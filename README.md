# sudoku-solver

:::info
Visit [this README on HackMD](https://hackmd.io/@cherylfong/r1Ayp-MEw) for full LaTeX Math rendering.
:::

Solves a given 9x9 sudoku grid.

<!-- $\sqrt{N}$ x  $\sqrt{N}$ -->

## Backtracking Algorithm

### Decision Space:

The numbers 1 to 9.
<!-- 0 to N<sup>th</sup> numbers, where N is the height or width of the board; assuming a perfect square grid e.g.
2<sup>2</sup> = 4x4, 4<sup>2</sup> = 16x16, etc. -->

### Constraints:

Classic sudoku requires each cell space to contain a number between 1 to 9 inclusive on a 9x9 grid where, the
number in the cell is unique across all columns, rows and within the cell's 3x3 subgrid.

### Base case:

Also known as the end goal of the solution. It is to fill the entire sudoku grid up till the n<sup>th</sup>x n<sup>th</sup>
cell, i.e. the last bottom right cell. Get to a point where there are no more empty cells.

### Runtime Complexity:

The following is the method that solves the sudoku grid.
```java
    public boolean fillBoard(int[][] array){

        // a nested for loops of size n
        // O(N^2)
        int[] cell = getEmptyCell(array);

        // constant time O(1)
        // base case
        if (! isCellEmpty(cell)){
            return true;
        }

        // size of decision space
        // O(N)
        for(int v = 1; v < array.length+1; v++){

            // checking against the a column and row is only O(N), however,
            // checking within a subgrid requires a nested for loop. Thus,
            // O(N^2) -- ignoring lower order term
            if(checkConstraints(array, cell, v)){

                // if constraints are met then set value in empty cell!
                array[cell[1]][cell[0]] = v;

                // call function recursively with the newly added cell value
                if (fillBoard(array)){
                    return true;
                }

                // *
                // else (meaning board still contains empty cell or cells)
                // unset value because previous choices did not work
                // hence, next loop will use the previous value's + 1 and so on
                // this goes on but is bounded by outer for-loop constraint
                array[cell[1]][cell[0]] = 0;
            }
        }

        // for backtracking
        // to go where * is
        return false;
    }
```

### Using The Master Theorem

$$ T(n) = a\space T(\frac{n}{b}) + f(n) \space ; \space a \geq 1, \space b > 1$$

> $n$ : problem size
>
> $a$ : subproblems or the number of recursive calls
>
> $\frac{n}{b}$ : subproblem size or size of the recursive call

The $a$ subproblems are solved recursively, each in time $T(\frac{n}{b})$.

> $f(n)$ = encompasses the cost of dividing the problem and combining the results of the subproblems.

Given a recurrence relation of the form $T(n) = a\space T(\frac{n}{b}) + f(n)$, $T(n)$ has the following asymptotic
bounds or cases:

1. If $f(n) \space = \space O(n^{log_b a - \epsilon})$ for some constant $\epsilon > 0$, then $T(n) = \Theta(n^{log_b a})$

2. If $f(n) \space = \space \Theta(n^{log_b a})$, then $T(n) = \theta(n^{log_b a} \cdot lg n)$

3. If $f(n) \space = \space \Omega(n^{log_b a + \epsilon})$ for some constant $\epsilon > 0$, and if $a \space f \space (\frac{n}{b}) \space \leq \space c \space f(n)$ for some constant $c < 1$ and all large $n$, then $T(n) = \Theta(f(n))$

### Analysis
Upon inspecting the `fillBoard()` function above, the recursive call occurs within the for-loop from 1 to 9 e.g. when
solving a 9x9 sudoku grid. Thus, the number of recursive calls or subproblems is 9 i.e. $a=9$ because we are trying 1
to 9 for each empty cell.

While the size of the subproblem is the input/argument of the recursive call i.e. on the line `if (fillBoard(array)){`,
the input is `array`. `array` is a actually a 2D array, and if the grid is of size 9x9, then there are 81 cells. Hence,
$b=81$.

The work done outside the recursion or the work done to approach the base case i.e. $f(n)$ is $n^2$ when finding an
empty cell on line `int[] cell = getEmptyCell(array);`.

In sum,

$a = 9$

$b = 81$

$f(n) = n^2$

$\therefore$

$T(n) = 9 \space T(\frac{n}{81}) + n^2$

$log_b a = log_{81} 9 = \frac{1}{2}$

$n^{log_b a} = n^\frac{1}{2}$

$\because f(n) = n^2 = \Omega(n^{\frac{1}{2} + \epsilon})$
and, $9 \space \cdot \space (\frac{n}{81}) \space \leq \space c \space \cdot \space n^2$

The third case or bound of the master method holds, therefore $T(n) = \Theta(f(n)) = \Theta(n^2)$

$\square$ The runtime complexity is $\Theta(n^2)$

[Reference](https://medium.com/@bostjan_cigan/understanding-time-complexity-of-recursive-algorithms-13b3efa3a322)

### How to know when a solution requires backtracking:
> When it is necessary to go through (or exhaust) the entire decision space (i.e. the list of all possible decisions or
> choices as defined by the constrains) sequentially, and where for each decision made, the decision space diminishes.


## Java Environment

```bash
$ java --version
openjdk 11.0.8 2020-07-14
OpenJDK Runtime Environment 18.9 (build 11.0.8+10)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.8+10, mixed mode, sharing)

$ javac -version
javac 11.0.8
```