package com.example.sudokusolver;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.app.ProgressDialog;

import com.my.SudokuSolver.SudokuSolver;
import com.my.SudokuSolver.ValidationResult;

public class MainActivity extends AppCompatActivity {

    private SudokuSolver solver;

    // 9x9 grid reference array
    private TextView[][] cells = new TextView[9][9];
    private TextView txtCellCounter;

    // -----------------------------------------------------
    // onCreate() START
    // -----------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solver=new SudokuSolver();
        txtCellCounter=findViewById(R.id.txtCellCounter);

        // Map all 81 cells into "cells[][]"
        mapAllCells();

        updateCellCounter();

        // Buttons
        Button btnValidate = findViewById(R.id.btnValidate);
        Button btnSolve = findViewById(R.id.btnSolve);

        btnValidate.setOnClickListener(v -> validateSudoku());
        btnSolve.setOnClickListener(v -> onSolvePressed());
    }
    // -----------------------------------------------------
    // onCreate() END
    // -----------------------------------------------------

    // -----------------------------------------------------
    // mapAllCells() START - stores 81 TextViews into cells[][]
    // -----------------------------------------------------
    private void mapAllCells() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                String idName = "cell_" + row + col;
                int resID = getResources().getIdentifier(idName, "id", getPackageName());

                cells[row][col] = findViewById(resID);

                TextView cell = cells[row][col];

                cell.setOnClickListener(v -> showNumberDialog(cell));
            }
        }
    }
    // -----------------------------------------------------
    // mapAllCells() END
    // -----------------------------------------------------

    // -----------------------------------------------------
    // showNumberDialog() START
    // -----------------------------------------------------
    private void showNumberDialog(TextView cell) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Number");

        String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        builder.setItems(numbers, (dialog, which) -> {
            cell.setText(numbers[which]);
            updateCellCounter();
        });

        builder.setNegativeButton("Clear", (dialog, which) -> {
            cell.setText("");
            updateCellCounter();
        });

        builder.show();
    }
    // -----------------------------------------------------
    // showNumberDialog() END
    // -----------------------------------------------------

    // -----------------------------------------------------
    // validateSudoku() START
    // -----------------------------------------------------
    private void validateSudoku() {

        int[][] grid = readBoardValues();

        ValidationResult result =
                solver.validateBoard(grid);

        Toast.makeText(
                this,
                result.getMessage(),
                Toast.LENGTH_LONG
        ).show();
    }
    // -----------------------------------------------------
    // validateSudoku() END
    // -----------------------------------------------------


    // -----------------------------------------------------
    // readBoardValues() START
    // -----------------------------------------------------
    private int[][] readBoardValues() {
        int[][] board = new int[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String value = cells[row][col].getText().toString().trim();
                board[row][col] = value.isEmpty() ? 0 : Integer.parseInt(value);
            }
        }

        return board;
    }
    // -----------------------------------------------------
    // readBoardValues() END
    // -----------------------------------------------------

    private void updateCellCounter() {

        int count = 0;

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                String value =
                        cells[row][col]
                                .getText()
                                .toString()
                                .trim();

                if (!value.isEmpty()) {
                    count++;
                }
            }
        }

        txtCellCounter.setText(
                "Filled Cells: "
                        + count
                        + " / 81"
        );
    }

    // -----------------------------------------------------
    // onSolvePressed() START (Button event)
    // -----------------------------------------------------
    private void onSolvePressed() {

        int[][] board = readBoardValues();

        int[][] originalBoard = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                originalBoard[i][j] = board[i][j];
            }
        }

        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_solver);

        dialog.setCancelable(false);

        TextView txtSolving =
                dialog.findViewById(R.id.txtSolving);

        dialog.show();

        Handler animationHandler = new Handler();

        Runnable animationRunnable = new Runnable() {

            int state = 0;

            @Override
            public void run() {

                switch (state) {

                    case 0:
                        txtSolving.setText("Solving..");
                        break;

                    case 1:
                        txtSolving.setText("Solving...");
                        break;

                    case 2:
                        txtSolving.setText("Solving....");
                        break;
                }

                state++;

                if (state > 2)
                    state = 0;

                animationHandler.postDelayed(this, 250);
            }
        };

        animationHandler.post(animationRunnable);

        new Handler().postDelayed(() -> {

            if (solver.solveSudoku(board)) {

                int[] flat = new int[81];
                int[] originalFlat = new int[81];
                int index = 0;

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        flat[index++] = board[i][j];
                    }
                }

                int originalIndex =0;

                for (int i =0; i<9; i++){
                    for (int j =0; j<9; j++){

                        originalFlat[originalIndex++] = originalBoard[i][j];
                    }
                }

                dialog.dismiss();

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                ResultActivity.class
                        );

                intent.putExtra("solvedBoard", flat);
                intent.putExtra("originalBoard", originalFlat);

                animationHandler.removeCallbacksAndMessages(null);
                dialog.dismiss();

                startActivity(intent);

            } else {

                dialog.dismiss();

                Toast.makeText(
                        MainActivity.this,
                        "Sudoku cannot be solved!",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }, 1000);
    }


}