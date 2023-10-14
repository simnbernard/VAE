package fr.simnbernard.vae.algorithmes;

/**
 * https://fr.wikipedia.org/wiki/Tri_fusion
 */
public class TriFusion {

  private static final TriFusion INSTANCE = new TriFusion();

  public static TriFusion getInstance() {
    return INSTANCE;
  }

  private void appliquerTriFusion(int[] array, int startIdx, int endIdx) {
    if (startIdx >= endIdx) {
      return;
    }

    final var middle = (endIdx + startIdx) / 2;
    appliquerTriFusion(array, startIdx, middle);
    appliquerTriFusion(array, middle + 1, endIdx);
    fusionner(array, startIdx, middle, endIdx);
  }

  private void fusionner(int[] array, int leftStartIdx, int middle, int rightEndIndex) {
    int leftIdx = leftStartIdx, rightIdx = middle + 1, sortedIdx = 0;

    final var sorted = new int[rightEndIndex - leftStartIdx + 1];

    while (leftIdx <= middle && rightIdx <= rightEndIndex) {
      if (array[rightIdx] < array[leftIdx]) {
        sorted[sortedIdx++] = array[rightIdx++];
      } else {
        sorted[sortedIdx++] = array[leftIdx++];
      }
    }

    while (leftIdx <= middle) {
      sorted[sortedIdx++] = array[leftIdx++];
    }

    while (rightIdx <= rightEndIndex) {
      sorted[sortedIdx++] = array[rightIdx++];
    }

    sortedIdx = 0;
    for (int arrIdx = leftStartIdx; arrIdx <= rightEndIndex; arrIdx++) {
      array[arrIdx] = sorted[sortedIdx++];
    }
  }

  int[] sort(int[] array) {
    appliquerTriFusion(array, 0, array.length - 1);
    return array;
  }

  public static void main(String[] args) {
    final int[] arrayUnsorted = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    System.out.println(TriFusion.getInstance().sort(arrayUnsorted));
  }
}
