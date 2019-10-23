package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuRow {

    private List<SudokuElement> row;

    public SudokuRow(List<SudokuElement> row) {
        this.row = row;
    }

    public List<SudokuElement> getRow() {
        return row;
    }

    public SudokuElement getElement(int columnNumber) {
        return row.get(columnNumber);
    }

    public void setElement(int position, int value) {
        row.set(position, new SudokuElement(value, new ArrayList<>()));
    }


    @Override
    public String toString() {

        String rowToDisplay = "";

        for (SudokuElement element : row) {
            rowToDisplay = rowToDisplay + "|" + element.toString();
        }
        rowToDisplay = rowToDisplay + "|";
        return rowToDisplay;

    }
}
