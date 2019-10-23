package com.sudoku;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static java.lang.Math.sqrt;

public class SudokuGame {

    private int boardSize;
    private SudokuBoard sudokuBoard = new SudokuBoard();
    private Deque<Backtrack> backtrackQueue = new ArrayDeque<>();

    public SudokuGame(int boardSize) {
        if ((double) sqrt(boardSize) == (int) sqrt(boardSize) && boardSize <= 16) {
            this.boardSize = boardSize;

        } else {
            System.out.println("The number cannot be sudoku board size. Try again.");
        }

    }

    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean ifPossibleToBeSudokuBoardSize(int size) {

        if ((double) sqrt(size) == (int) sqrt(size) && size <= 36) {
            return true;
        } else {
            return false;
        }
    }

    public SudokuBoard createEmptyBoard() {

        List<SudokuRow> newSudokuRows = new ArrayList<>();
        List<Integer> newFulfillmentOptions = new ArrayList<>();
        for (int k = 0; k < boardSize; k++) {
            newFulfillmentOptions.add(k + 1);
        }

        for (int i = 0; i < boardSize; i++) {
            List<SudokuElement> newSudokuElements = new ArrayList<>();
            for (int j = 0; j < boardSize; j++) {
                newSudokuElements.add(new SudokuElement(SudokuElement.EMPTY, new ArrayList<Integer>(newFulfillmentOptions)));
            }
            newSudokuRows.add(new SudokuRow(newSudokuElements));
        }

        for (SudokuRow sudokuRow : newSudokuRows) {
            sudokuBoard.addRow(sudokuRow);
        }

        return sudokuBoard;
    }


    public void setBoardElement(int rowNumber, int columnNumber, int value) {

        if (rowNumber >= 0 && columnNumber >= 0 && value >= 1 && rowNumber < boardSize && columnNumber < boardSize && value <= boardSize) {
            sudokuBoard.getRow(rowNumber).setElement(columnNumber, value);
        } else {
            System.out.println("Value, row number and column number should be between 1 and " + sudokuBoard.getBoard().size() + ". Check given numbers and try again.");
        }
    }


    public SudokuBoard resolveSudoku() {

        basicResolveSudoku();

        boolean isEmpty = getEmptyElementPosition().getRowPosition() > -1;

        while (isEmpty) {
            if (getEmptyElementPosition().getRowPosition() > -1) {
                int i = getEmptyElementPosition().getRowPosition();
                int j = getEmptyElementPosition().getColumnPosition();
                guessValue(i, j);
                basicResolveSudoku();
            } else {
                isEmpty = false;
            }
        }
        return sudokuBoard;
    }

    public Position getEmptyElementPosition() {

        Position emptyElementPosition = new Position(-1, -1);

        for (int i = 0; i < sudokuBoard.getBoard().size(); i++) {
            SudokuRow theRow = sudokuBoard.getRow(i);
            for (int j = 0; j < theRow.getRow().size(); j++) {
                SudokuElement sudokuElement = theRow.getElement(j);
                int value = sudokuElement.getValue();
                if (value == -1) {
                    emptyElementPosition.setRowPosition(i);
                    emptyElementPosition.setColumnPosition(j);
                    return emptyElementPosition;
                }

            }

        }
        return emptyElementPosition;

    }

    public void basicResolveSudoku() {
        boolean ok = true;
        try {
            while (ok) {
                removeEnteredValuesFromOptions();
                boolean setResult1 = isOptionValueOrOptionInOtherFields();
                boolean setResult2 = setElementWhereSingleOptionExists();
                if (!(setResult1 || setResult2)) {
                    ok = false;

                }
            }

        } catch (GuessSolutionException e) {

            if (backtrackQueue.size() == 0) {
                System.out.println("This SUDOKU do not have solution");
            } else {
                int value = backtrackQueue.peekFirst().getOption();
                Position position = backtrackQueue.peekFirst().getPosition();
                sudokuBoard = backtrackQueue.peekFirst().getSudokuBoard();
                backtrackQueue.pop().getSudokuBoard();

                List<Integer> fulfillmentOptions = sudokuBoard.getRow(position.getRowPosition())
                        .getElement(position.getColumnPosition()).getFulfillmentOptions();
                fulfillmentOptions.removeIf(o -> o == value);
            }
        }

    }


    public void removeEnteredValuesFromOptions() {
        for (int i = 0; i < sudokuBoard.getBoard().size(); i++) {
            SudokuRow theRow = sudokuBoard.getRow(i);
            for (int j = 0; j < theRow.getRow().size(); j++) {
                SudokuElement theElement = theRow.getElement(j);
                int value = theElement.getValue();
                if (value > 0) {
                    sudokuBoard.removeValueFromOptionsInRow(i, value);
                    sudokuBoard.removeValueFromOptionsInColumn(j, value);
                    sudokuBoard.removeValueFromOptionsInBox(i, j, value);
                }
            }
        }
    }

    public boolean setElementWhereSingleOptionExists() throws GuessSolutionException {

        boolean ifAnySetDone = false;

        for (int i = 0; i < sudokuBoard.getBoard().size(); i++) {
            SudokuRow theRow = sudokuBoard.getRow(i);
            for (int j = 0; j < theRow.getRow().size(); j++) {
                SudokuElement theElement = theRow.getElement(j);
                int value = theElement.getValue();
                List<Integer> fulfillmentOptions = theElement.getFulfillmentOptions();
                if (fulfillmentOptions.size() == 1 && value < 0) {
                    for (Integer o : fulfillmentOptions) {
                        if (!(sudokuBoard.isInRow(i, o) || sudokuBoard.isInColumn(j, o) || sudokuBoard.isInBlock(i, j, o))) {
                            setBoardElement(i, j, theElement.getFulfillmentOptions().get(0));
                            ifAnySetDone = true;
                        } else {
                            throw new GuessSolutionException();
                        }
                    }
                }
                if (fulfillmentOptions.size() < 1 && value < 0) {
                    throw new GuessSolutionException();
                }
            }
        }
        return ifAnySetDone;
    }

    public boolean isOptionValueOrOptionInOtherFields() {

        boolean ifAnySetDone = false;

        for (int i = 0; i < sudokuBoard.getBoard().size(); i++) {
            SudokuRow theRow = sudokuBoard.getRow(i);
            for (int j = 0; j < theRow.getRow().size(); j++) {
                List<Integer> fulfillmentOptions = theRow.getElement(j).getFulfillmentOptions();
                for (Integer o : fulfillmentOptions) {
                    if (!(sudokuBoard.isInRow(i, o) || sudokuBoard.isInColumn(j, o) || sudokuBoard.isInBlock(i, j, o)
                            || sudokuBoard.isInRowOptions(i, o) || sudokuBoard.isInColumnOptions(j, o)
                            || sudokuBoard.isInBlockOptions(i, j, o))) {
                        setBoardElement(i, j, o);
                        ifAnySetDone = true;
                    }
                }
            }
        }
        return ifAnySetDone;
    }

    public void guessValue(int row, int column) {
        try {
            SudokuElement sudokuElement = sudokuBoard.getRow(row).getElement(column);
            Position position = new Position(row, column);
            if (sudokuElement.getFulfillmentOptions().size() > 0) {
                int option = sudokuElement.getFulfillmentOptions().get(0);
                backtrackQueue.push(new Backtrack(sudokuBoard.deepCopy(), position, option));
                setBoardElement(row, column, option);
            } else {
                throw new GuessSolutionException();
            }

        } catch (Exception e) {

        }
    }

}
