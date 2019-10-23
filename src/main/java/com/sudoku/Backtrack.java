package com.sudoku;

public class Backtrack {

    private SudokuBoard sudokuBoard;
    private Position position;
    private int option;

    public Backtrack(SudokuBoard sudokuBoard, Position position, int option) {
        this.sudokuBoard = sudokuBoard;
        this.position = position;
        this.option = option;
    }

    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public Position getPosition() {
        return position;
    }

    public int getOption() {
        return option;
    }


}
