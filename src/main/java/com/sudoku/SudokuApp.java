package com.sudoku;

import java.util.*;

public class SudokuApp {


    public static void main(String[] args) throws java.lang.Exception {
        String resloveSudokuCommand = "SUDOKU";
        SudokuGame theGame = new SudokuGame(0);

        while (theGame.getBoardSize() == 0) {
            try {
                System.out.println("What size of board do you want to play - 4, 9 or 16?");
                Scanner scannerBoardSize = new Scanner(System.in);
                int scannedBoardSize = scannerBoardSize.nextInt();
                theGame = new SudokuGame(scannedBoardSize);

            } catch (InputMismatchException e) {
                System.out.println("You can enter only integers lover or equal to 16");
            }
        }


        theGame.createEmptyBoard();

        boolean ok = false;

        while (!ok) {
            try {
                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Enter row number, column number and value separated by ',' or enter 'SUDOKU' if you want to see resolved board");

                String scannedString = scanner1.nextLine().replaceAll(",", " ");
                Scanner scanner2 = new Scanner(scannedString);

                if (scannedString.equalsIgnoreCase(resloveSudokuCommand)) {
                    System.out.println("See resolved SUDOKU board");
                    ok = true;
                    System.out.print(theGame.resolveSudoku());
                } else {
                    int rowNumber = scanner2.nextInt();
                    int columnNumber = scanner2.nextInt();
                    int value = scanner2.nextInt();

                    theGame.setBoardElement(rowNumber - 1, columnNumber - 1, value);
                    System.out.print(theGame.getSudokuBoard());
                }


            } catch (InputMismatchException e) {
                System.out.println("Remember that row number, column number and value should be integers separated by ','");

            } catch (NoSuchElementException i) {
                System.out.println("Remember that row number, column number and value should be integers between 1 and " + theGame.getBoardSize());

            }
        }

    }

}
