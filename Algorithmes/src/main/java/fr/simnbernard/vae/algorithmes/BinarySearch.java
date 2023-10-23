package fr.simnbernard.vae.algorithmes;

import java.util.stream.IntStream;

/**
 * https://fr.wikipedia.org/wiki/Recherche_dichotomique
 */
public class BinarySearch {

  private static final BinarySearch INSTANCE = new BinarySearch();

  static BinarySearch getInstance() {
    return INSTANCE;
  }

  private static int fineElementIndexUsingbinarySearch(final int[] array, final int toFind,
      final int startIdx, final int endIdx) {
    if (startIdx > endIdx) {
      return -1;
    }

    final var center = (endIdx + startIdx) / 2;

    if (array[center] == toFind) {
      return center;
    } else if (array[center] > toFind) {
      return fineElementIndexUsingbinarySearch(array, toFind, startIdx, center - 1);
    } else {
      return fineElementIndexUsingbinarySearch(array, toFind, center + 1, endIdx);
    }
  }

  int fineElementIndexUsingbinarySearch(final int[] array, final int toFind) {
    return fineElementIndexUsingbinarySearch(array, toFind, 0, array.length - 1);
  }

  public static void main(String[] args) {
    final var array = IntStream.range(0, 20).toArray();
    final var toFind = 5;

    final var result = BinarySearch.getInstance().fineElementIndexUsingbinarySearch(array, toFind);

    if (result > -1) {
      System.out.println("L'élément %d trouvé à l'index %d".formatted(toFind, result));
    } else {
      System.out.println("L'élément %d n'a pas été trouvé".formatted(toFind));
    }
  }
}
