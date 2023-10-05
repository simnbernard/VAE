package fr.simnbernard.vae.algorithmes.backtracking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NQueensProblemTest {

  static final NQueensProblem nQueensProblem = new NQueensProblem();

  @Test
  void given8Queens_whenSolve_thenResultIs92() {
    assertEquals(nQueensProblem.solve(8), 92);
  }

  @Test
  void given3Queens_whenSolve_thenResultIs0() {
    assertEquals(nQueensProblem.solve(3), 0);
  }

  @Test
  void given7Queens_whenSolve_thenResultIs40() {
    assertEquals(nQueensProblem.solve(7), 40);
  }

  @Test
  void givenNQueens_whenSolve_thenPrintExecutionTime() {
    for (var queenNb = 0; queenNb < 15; queenNb++) {
      final var startTimeIsMs = System.currentTimeMillis();
      nQueensProblem.solve(queenNb);
      System.out.println(
          "Le traitement pour " + queenNb + " reines a pris " + (System.currentTimeMillis()
              - startTimeIsMs) + " ms");
    }
  }
}
