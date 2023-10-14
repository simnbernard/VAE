package fr.simnbernard.vae.algorithmes;

import java.util.Random;

/**
 * https://fr.wikipedia.org/wiki/Tri_rapide
 */
public class TriRapide {

  private static final TriRapide INSTANCE = new TriRapide();

  static TriRapide getInstance() {
    return INSTANCE;
  }

  private static final Random random = new Random();

  private void appliquerTriRapide(final int[] array, final int startIdx, final int endIdx) {
    if (startIdx >= endIdx) {
      return;
    }
    final var pivotIdx = appliquerTriRapideAndGetNewPivotValue(array, startIdx, endIdx);
    appliquerTriRapide(array, startIdx, pivotIdx - 1);
    appliquerTriRapide(array, pivotIdx + 1, endIdx);
  }

  private void echange(int[] array, int fromIdx, int toIdx) {
    if (fromIdx == toIdx) {
      return;
    }
    final var tmp = array[fromIdx];
    array[fromIdx] = array[toIdx];
    array[toIdx] = tmp;
  }

  private int appliquerTriRapideAndGetNewPivotValue(final int[] array, final int startIdx,
      final int endIdx) {
    final var pivotIdx = random.nextInt(startIdx, endIdx + 1);
    final var pivotValue = array[pivotIdx];
    echange(array, pivotIdx, endIdx);

    var lastSmallestValueIdx = startIdx - 1;
    for (var idx = startIdx; idx < endIdx; idx++) {
      if (array[idx] < pivotValue) {
        echange(array, idx, ++lastSmallestValueIdx);
      }
    }

    final var firstBiggestValueThanPivotIdx = lastSmallestValueIdx + 1;
    echange(array, firstBiggestValueThanPivotIdx, endIdx);

    return firstBiggestValueThanPivotIdx;
  }

  int[] sort(final int[] array) {
    appliquerTriRapide(array, 0, array.length - 1);
    return array;
  }

  public static void main(String[] args) {
    final int[] arrayUnsorted = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    System.out.println(TriRapide.getInstance().sort(arrayUnsorted));
  }
}
