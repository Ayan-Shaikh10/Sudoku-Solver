package com.my.SudokuSolver;


public class SudokuSolver {

    // -----------------------------------------------------
    // Sudoku Solver (Backtracking)
    // -----------------------------------------------------
    public boolean solveSudoku(int[][] board) {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (board[row][col] == 0) {

                    for (int num = 1; num <= 9; num++) {

                        if (isValid(board, row, col, num)) {

                            board[row][col] = num;

                            if (solveSudoku(board))
                                return true;

                            board[row][col] = 0;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    // -----------------------------------------------------
    // Used by Solver
    // -----------------------------------------------------
    private boolean isValid(int[][] board, int row, int col, int num) {

        // Row Check
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num)
                return false;
        }

        // Column Check
        for (int x = 0; x < 9; x++) {
            if (board[x][col] == num)
                return false;
        }

        // 3x3 Box Check
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {

                if (board[r][c] == num)
                    return false;
            }
        }

        return true;
    }

    // -----------------------------------------------------
    // Used for Validation Button
    // -----------------------------------------------------
    public boolean isValidPlacement(int[][] grid,
                                    int row,
                                    int col,
                                    int number) {

        // Row Check
        for (int c = 0; c < 9; c++) {
            if (c != col && grid[row][c] == number)
                return false;
        }

        // Column Check
        for (int r = 0; r < 9; r++) {
            if (r != row && grid[r][col] == number)
                return false;
        }

        // Box Check
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;

        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {

                if (!(r == row && c == col)
                        && grid[r][c] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    public com.my.SudokuSolver.ValidationResult validateBoard(int[][] grid) {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (grid[row][col] != 0) {

                    int value = grid[row][col];

                    if (!isValidPlacement(grid, row, col, value)) {

                        return new com.my.SudokuSolver.ValidationResult(
                                false,
                                "Duplicate value "
                                        + value
                                        + " found at Row "
                                        + (row + 1)
                                        + ", Column "
                                        + (col + 1)
                        );
                    }
                }
            }
        }

        return new com.my.SudokuSolver.ValidationResult(
                true,
                "Sudoku is valid and ready to solve!"
        );
    }


}