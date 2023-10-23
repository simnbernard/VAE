package fr.simnbernard.vae.algorithmes;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://fr.wikipedia.org/wiki/Tri_%C3%A0_bulles
 */
public class TriBulles {

  public static void main(String[] args) {
    final var list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
    Collections.shuffle(list);
    final var array = list.toArray(new Integer[list.size()]);

    Arrays.stream(array).forEach(System.out::print);
    System.out.println();

    for (var lastIdx = array.length - 1; lastIdx > 0; lastIdx--) {
      var sorted = true;
      for (var idx = 0; idx < lastIdx; idx++) {
        if (array[idx + 1] < array[idx]) {
          sorted = false;
          final var tmp = array[idx + 1];
          array[idx + 1] = array[idx];
          array[idx] = tmp;
        }
      }
      if (sorted) {
        break;
      }
    }
    Arrays.stream(array).forEach(System.out::print);
  }
}
