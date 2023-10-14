package fr.simnbernard.vae.algorithmes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * https://fr.wikipedia.org/wiki/Problème_des_huit_dames
 */
public class NQueens {

  private static final NQueens INSTANCE = new NQueens();
  private static final Scanner scanner = new Scanner(System.in);


  static NQueens getInstance() {
    return INSTANCE;
  }

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

  private boolean isPossibleToPutQueenAtPosition(final int positionLineIdx,
      final int positionColIdx) {
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

  int solve(final int queenNb) {
    init(queenNb);
    generateAllPossibleBoardsFromLine(0);

    return solutions.size();
  }

  void solve() {
    System.out.print("Combien de reines ? : ");
    final var queenNb = scanner.nextInt();

    System.out.println(
        "Nombre de solution(s) pour %d reines : ".formatted(queenNb, solve(queenNb)));

    if (solutions.isEmpty()) {
      return;
    }

    System.out.print("Afficher les résultats ? (y) : ");
    if (scanner.nextLine().equals("y")) {
      solutions.forEach(this::printBoard);
    }
  }

  //Voir NQueensProblemTest pour durées
  public static void main(String[] args) {
    final var message = "Nombre de possibilité pour %d reines : %d";
    System.out.println(message.formatted(8, NQueens.getInstance().solve(8)));
    System.out.println(message.formatted(3, NQueens.getInstance().solve(8)));
    System.out.println(message.formatted(7, NQueens.getInstance().solve(8)));

    NQueens.getInstance().solve();
  }
}
