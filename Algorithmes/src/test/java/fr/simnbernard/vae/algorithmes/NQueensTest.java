package fr.simnbernard.vae.algorithmes;

import org.junit.jupiter.api.Test;

class NQueensTest {

  @Test
  void givenNQueens_whenSolve_thenPrintExecutionTime() {
    for (var queenNb = 0; queenNb < 15; queenNb++) {
      final var startTimeIsMs = System.currentTimeMillis();
      NQueens.getInstance().solve(queenNb);
      System.out.println(
          "Le traitement pour %d reines a pris %d ms".formatted(queenNb,
              System.currentTimeMillis() - startTimeIsMs));
    }
  }
}
