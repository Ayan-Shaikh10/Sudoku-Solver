package com.example.sudokusolver;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class ResultActivity extends AppCompatActivity {

    private TextView[][] resultCells = new TextView[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 1. Map result grid
        mapResultGrid();

        // 2. Get solved board
        int[] flat = getIntent().getIntArrayExtra("solvedBoard");
        int[] originalFlat = getIntent().getIntArrayExtra("originalBoard");

        int[][] board = new int[9][9];
        int[][] originalBoard = new int[9][9];

        int index = 0;

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                board[i][j] = flat[index];

                originalBoard[i][j] = originalFlat[index];

                index++;
            }
        }

        // 3. Fill 81 cells
        fillResultGrid(board,originalBoard);

        // 4. CLEAR BUTTON SETUP
        Button clearBtn = findViewById(R.id.clearBtn);

        clearBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);

            // Clear activity stack and start Main fresh
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });
    }

    private void mapResultGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String idName = "result_cell_" + row + col;
                int resID = getResources().getIdentifier(idName, "id", getPackageName());
                resultCells[row][col] = findViewById(resID);
            }
        }
    }

    private void fillResultGrid(int[][] board , int[][] originalBoard) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                int value = board[i][j];
                resultCells[i][j].setText(String.valueOf(value));

                // highlight with light-green for solved cells
                if (originalBoard[i][j] == 0) {

                    resultCells[i][j].setBackgroundColor(Color.GREEN);

                } else {


                            resultCells[i][j].setTextColor(Color.RED);
                }
                // faint green with transparency

                resultCells[i][j].setTextColor(Color.WHITE); // match your dark theme UI
            }
        }
    }
}