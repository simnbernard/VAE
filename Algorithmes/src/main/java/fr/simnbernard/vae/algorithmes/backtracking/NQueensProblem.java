package fr.simnbernard.vae.algorithmes.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Le problème des N reines (https://fr.wikipedia.org/wiki/Problème_des_huit_dames) consiste à
 * déterminer toutes les solutions de placement de N reines sur un échiquier de taille N × N de
 * telle sorte qu’aucune ne soit en prise.
 */
public class NQueensProblem {

  private boolean[][] board;
  private List<boolean[][]> solutions;

  private void init(int queenNb) {
    board = new boolean[queenNb][queenNb];
    solutions = new ArrayList<>();
  }

  private void printBoard(boolean[][] boardToPrint) {
    for (int lineIdx = 0; lineIdx < boardToPrint.length; lineIdx++) {
      for (int colIdx = 0; colIdx < boardToPrint[lineIdx].length; colIdx++) {
        if (boardToPrint[lineIdx][colIdx]) {
          System.out.print("Q ");
        } else {
          System.out.print(". ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  private void addCurrentBoardToSolutions() {
    final var boardClone = new boolean[board.length][board.length];
    for (var lineIdx = 0; lineIdx < board.length; lineIdx++) {
      boardClone[lineIdx] = Arrays.copyOf(board[lineIdx], board[lineIdx].length);
    }

    solutions.add(boardClone);
  }

  private void generateAllPossibleBoardsFromLine(int fromLineIdx) {
    if (fromLineIdx == board.length) {
      addCurrentBoardToSolutions();
      return;
    }

    for (var colIdx = 0; colIdx < board.length; colIdx++) {
      if (isPossibleToPutQueenAtPosition(fromLineIdx, colIdx)) {
        //we put a queen and generate board for next lines
        board[fromLineIdx][colIdx] = true;
        generateAllPossibleBoardsFromLine(fromLineIdx + 1);

        //we reset to try with next colomn (this is backtracking)
        board[fromLineIdx][colIdx] = false;
      }
    }
  }

  private boolean isPossibleToPutQueenAtPosition(int positionLineIdx, int positionColIdx) {
    //check only upper because the algorithm put queens from top to bottom

    //check vertical
    for (int lineIdx = 0; lineIdx < positionLineIdx; lineIdx++) {
      if (board[lineIdx][positionColIdx]) {
        return false;
      }
    }

    //check diagonal left
    for (int lineIdx = positionLineIdx, colIdx = positionColIdx; lineIdx >= 0 && colIdx >= 0;
        --lineIdx, --colIdx) {
      if (board[lineIdx][colIdx]) {
        return false;
      }
    }

    //check diagonal right
    for (int lineIdx = positionLineIdx, colIdx = positionColIdx;
        lineIdx >= 0 && colIdx < board.length; --lineIdx, ++colIdx) {
      if (board[lineIdx][colIdx]) {
        return false;
      }
    }

    return true;
  }

  protected int solve(int queenNb) {
    init(queenNb);
    generateAllPossibleBoardsFromLine(0);

    return solutions.size();
  }

  public void solve() {
    final var scanner = new Scanner(System.in);

    System.out.print("Combien de reines ? : ");
    final var queenNb = scanner.nextInt();

    System.out.println("Nombre de solution(s) pour " + queenNb + " reines : " + solve(queenNb));

    if (solutions.isEmpty()) {
      return;
    }

    System.out.print("Afficher les résultats ? (y) : ");
    if (new Scanner(System.in).nextLine().equals("y")) {
      solutions.forEach(this::printBoard);
    }
  }
}
