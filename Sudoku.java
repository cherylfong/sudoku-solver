import java.lang.Math;
import java.util.Scanner;

class Sudoku{

    // queries the user for an input matching the inputCondition otherwise,
    // shows an errorMessage and repeat query.
    public String prompt(String query, String inputCondition, String errorMessage){

        Scanner s = new Scanner(System.in);
        String userInput = "";

        while(true){
            try{

                System.out.println(query);
                userInput = s.nextLine();

                if(userInput.length() != 0)
                {
                    // Only one letter
                    if(!userInput.matches(inputCondition)){
                        // \u26D4 No Entry Symbol
                        throw new IllegalArgumentException(errorMessage);
                    }
                    break;
                }

            }catch(IllegalArgumentException e){

                System.out.println(e.getMessage());
            }
        }

        return userInput;

    }

    // fill up a 2D array using input from the user
    public int[][] populate2DArray(int row, int col){

        int[][] array = new int[row][col];
        String s = "";

        for(int r=0; r<row; r++){
            for(int c=0; c<col; c++){

                s = prompt("Input value for position (" + r + "," + c + "):" , "[0-9]" , "\nError: No other characters allowed, please input a single digit value between 0 and 9!\n");
                array[r][c] = Integer.parseInt(s);
            }
        }
        return array;
    }

    //helper method
    public static void printDashes(int[][] array){

        int offsetDashes =  (int)Math.floor(Math.sqrt((double)(array.length))) + 1;

        // prints the top line of dashes
        System.out.println();
        for(int l=0; l < array[0].length + offsetDashes; l++){
            System.out.print("- ");
        }
    }

    // to print the entire 2D array
    public void printGrid(int[][] array){

        // row = y
        // column = x

        double subGridHeight = Math.floor(Math.sqrt((double)(array.length)));
        double subGridWidth = Math.ceil(Math.sqrt((double)(array[0].length)));
        int countH = 0;
        int countW = 0;

        printDashes(array);

        for(int y=0; y < array.length; y++){

            countW = 0;
            if(countH == subGridHeight){

                printDashes(array);
                countH = 0;
                System.out.println();

            }else{
                System.out.println();
            }

            for(int x=0; x<array[y].length; x++){

                if(x==0){
                    System.out.print("| ");
                }
                if(countW == subGridWidth){
                    System.out.print("| ");
                    countW = 0;
                }
                System.out.print(array[y][x] + " ");
                countW++;
            }
            countH++;
            System.out.print("| ");
        }
        printDashes(array);
        System.out.println();
    }

    // get the cell coordinates containing a zero i.e. an empty cell
    // returns the coordinates as an int array where index=0 is x and index=1 is y
    // coordinates = [x,y]
    // if none found both x and y coordinates returned are MAX values
    public int[] getEmptyCell(int[][] array){

        int[] coordinates = {Integer.MAX_VALUE, Integer.MAX_VALUE};

        //row
        for(int y=0; y < array.length; y++){
            //column
            for(int x=0; x<array[y].length; x++){
                if(array[y][x] == 0){
                    coordinates[0] = x;
                    coordinates[1] = y;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }

    // check the inserted value against the sudoku constraints/requirements
    // i.e. no matching "value" in row, column and subgrid
    // return false if value fails a constraint
    // emptyCell = [x,y]
    public boolean checkConstraints(int[][] array, int[] emptyCell, int value){

        // check each cells along the x-axis
        for(int x =0; x < array[0].length; x++){


            if(array[ emptyCell[1] ][ x ] == value && x != emptyCell[0]) return false;
        }

        // check each cell on the y-axis
        for(int y =0; y < array.length; y++){

            if(array[ y ][ emptyCell[0] ] == value && y != emptyCell[1]) return false;
        }

        // check the cells in the subgrid

        // get the root of a perfect square
        int root = (int)Math.floor(Math.sqrt((double)(array.length)));

        // 9x9 grid will have the following subgrids:
        //
        // 0,0 | 1,0 | 2,0
        // 1,0 | 1,1 | 1,2
        // 2,0 | 2,1 | 2,2
        int subgridX = emptyCell[0] / 3;
        int subgridY = emptyCell[1] / 3;

        for(int y = subgridY * 3; y < ((subgridY * 3) + 3); y++ ){
            for(int x = subgridX * 3; x < ((subgridX * 3) + 3); x++ ){
            if(array[y][x] == value && y != emptyCell[1] && x != emptyCell[0])
                return false;
            }
        }

        return true;
    }

    public boolean isCellEmpty(int[] cell){

        if(cell[1] == Integer.MAX_VALUE){return false;}
        return true;
    }

    public boolean fillBoard(int[][] array){

        int[] cell = getEmptyCell(array);
        if (! isCellEmpty(cell)){
            return true;
        }

        for(int v = 1; v < array.length+1; v++){

            if(checkConstraints(array, cell, v)){

                // if constraints are met then set value in empty cell!
                array[cell[1]][cell[0]] = v;

                // call function recursively with newly added value
                if (fillBoard(array)){
                    return true;
                }

                // else
                // unset value and next loop will use the next value incremented
                array[cell[1]][cell[0]] = 0;
            }
        }

        return false;
    }

    public void isSolvable(boolean solution){
        if(solution){
            System.out.println("Board Solved!");
        }else{
            System.out.println("Board unsolvable :'(");
        }
    }

    public static void main(String[] args) {

        int[][] test1 =  {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };

        int[][] test2 =  {
            {9,1,0,0,8,0,0,0,0},
            {3,0,0,9,1,6,0,0,0},
            {0,7,8,2,4,0,0,9,0},
            {8,0,0,0,6,0,0,0,5},
            {4,0,0,5,0,7,0,0,3},
            {5,0,0,0,9,0,0,0,7},
            {0,6,0,0,0,0,2,5,0},
            {0,0,0,4,7,2,0,0,8},
            {0,0,0,0,5,0,0,7,4}
        };

        int[][] test11 =  {
            { 3, 0, 1, 9, 0, 5, 0, 8, 0 },
            { 0, 4, 0, 2, 7, 0, 5, 0, 0 },
            { 0, 0, 7, 1, 0, 0, 0, 6, 4 },
            { 6, 2, 4, 0, 0, 3, 0, 0, 5 },
            { 8, 0, 5, 0, 0, 4, 0, 9, 2 },
            { 1, 0, 0, 6, 0, 0, 0, 0, 3 },
            { 5, 0, 2, 0, 0, 7, 3, 1, 9 },
            { 0, 1, 6, 0, 2, 0, 4, 0, 0 },
            { 0, 9, 0, 5, 8, 0, 7, 0, 0 }
            };

        // unsolvable
        int[][] test22 = {

        {7, 8, 1, 5, 4, 3, 9, 2, 6 },
        {0, 0, 6, 1, 7, 9, 5, 0, 0 },
        {9, 5, 4, 6, 2, 8, 7, 3, 1 },
        {6, 9, 5, 8, 3, 7, 2, 1, 4 },
        {1, 4, 8, 2, 6, 5, 3, 7, 9 },
        {3, 2, 7, 9, 1, 4, 8, 0, 0 },
        {4, 1, 3, 7, 5, 2, 6, 9, 8 },
        {0, 0, 2, 0, 0, 0, 4, 0, 0 },
        {5, 7, 9, 4, 8, 6, 1, 0, 3 },

        };

        int[][] test3 = new int[1][2];
        int[][] test6 = new int[25][25];

        int[][] test7 = {
            {1,2,0},
            {4,5,7},
            {3,6,8},
    };

        Sudoku s = new Sudoku();

        int row = Integer.parseInt(

            s.prompt(
            "> Input row size: ",
            "[0-9]",
            "\nError: No other characters allowed, please input a single digit value between 0 and 9!\n"
            )
        );

        int column = Integer.parseInt(

            s.prompt(
            "> Input column size: ",
            "[0-9]",
            "\nError: No other characters allowed, please input a single digit value between 0 and 9!\n"
            )
        );

        int[][] a = s.populate2DArray(row, column);

        System.out.println("\n Solving...");
        s.printGrid(a);

        System.out.println();
        s.isSolvable(s.fillBoard(a));
        s.printGrid(a);

        System.out.println("\nFIN\n");

    }

}