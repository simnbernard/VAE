package fr.simnbernard.vae.algorithmes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * https://fr.wikipedia.org/wiki/Probl%C3%A8me_du_sac_%C3%A0_dos
 */
public class Knapsack {

  private static final Knapsack INSTANCE = new Knapsack();

  static Knapsack getInstance() {
    return INSTANCE;
  }

  //Programmation Dynamique : on test toutes les solutions possibles et on sélectionne la meilleure
  List<Integer> fillWithBestSolution(final int bagCapacity, final int[]... items) {
    var bestValueFound = 0;
    var bestBagFound = new ArrayList<Integer>();

    final var nbPossibilite = (int) Math.pow(2, items.length);

    for (var possibilityIdx = 1; possibilityIdx < nbPossibilite; possibilityIdx++) {
      var tryCapacityRemain = bagCapacity;
      var tryValue = 0;
      var tryItems = new ArrayList<Integer>();
      for (var itemIdx = 0; itemIdx < items.length; itemIdx++) {
        //Ici vu que l'on test toutes les valeurs possible, à savoir si on prends on nous chaque objet
        //je suis parti sur un remplissage binaire du tableau avec 1 je prends l'item, 0 je prends pas
        final var bitBool = ((possibilityIdx >> itemIdx) & 1);
        if (bitBool == 1) {
          final var item = items[itemIdx];
          final var itemPoids = item[1];
          if (itemPoids <= tryCapacityRemain) {
            tryCapacityRemain -= itemPoids;
            final var itemValue = item[0];
            tryItems.add(itemValue);
            tryValue += itemValue;
          }
        }

        //Optimisations
        if (tryValue > bestValueFound) {
          bestValueFound = tryValue;
          bestBagFound = tryItems;
        }
        if (tryCapacityRemain == 0) {
          break;
        }
      }
    }
    return bestBagFound;
  }

  //Solution gloutonne
  List<Integer> fill(final int bagCapacity, final int[]... items) {
    final var bag = new ArrayList<Integer>();
    var bagCapacityRemain = bagCapacity;

    for (final var item : Arrays.stream(items)
        .sorted(Comparator.comparingInt(item -> (item[0] / item[1]))).toList()) {
      final var poids = item[1];
      if (poids <= bagCapacityRemain) {
        final var value = item[0];
        bag.add(value);
        bagCapacityRemain -= poids;
      }
      if (bagCapacityRemain == 0) {
        break;
      }
    }
    return bag;
  }

  public static void main(String[] args) {
    System.out.println(
        Knapsack.getInstance().fill(5, new int[]{6, 2}, new int[]{5, 3}, new int[]{10, 5},
            new int[]{7, 5}));

    System.out.println(
        Knapsack.getInstance().fillWithBestSolution(10, new int[]{6, 8}, new int[]{5, 3},
            new int[]{2, 5}));
  }
}
