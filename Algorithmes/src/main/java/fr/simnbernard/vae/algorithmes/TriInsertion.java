package fr.simnbernard.vae.algorithmes;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://fr.wikipedia.org/wiki/Tri_par_insertion
 */
public class TriInsertion {

  public static void main(String[] args) {
    final var list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
    Collections.shuffle(list);
    final var array = list.toArray(new Integer[list.size()]);

    Arrays.stream(array).forEach(System.out::print);
    System.out.println();

    for (var idx = 1; idx < array.length - 1; idx++) {
      final var value = array[idx];
      for (var j = idx; j > 0; j--) {
        if (array[j - 1] > value) {
          final var tmp = array[j - 1];
          array[j - 1] = array[j];
          array[j] = tmp;
        } else {
          break;
        }
      }
    }
    Arrays.stream(array).forEach(System.out::print);
  }
}
