package fr.simnbernard.vae.algorithmes;

import java.util.HashMap;

public class AccordByzantin {

  private static final AccordByzantin INSTANCE = new AccordByzantin();

  static AccordByzantin getInstance() {
    return INSTANCE;
  }

  int getRightValueBasedOnThereIsMoreGoodNodeThanBadNode(final int[] values,
      final int seuilMalveillant) {
    final var valueCounts = new HashMap<Integer, Integer>();
    var bestValue = values[0];
    for (final var value : values) {
      final var count = valueCounts.containsKey(value) ? valueCounts.get(value) + 1 : 1;
      if (count > seuilMalveillant) {
        return value;
      }
      valueCounts.put(value, count);

      if (count > valueCounts.get(bestValue)) {
        bestValue = value;
      }
    }
    throw new RuntimeException("Impossible d'être certain de la meilleure valeure");
  }

  int getRightValueBasedOnThereIsMoreGoodNodeThanBadNode(final int[] values) {
    final var valueCounts = new HashMap<Integer, Integer>();
    var bestValue = values[0];
    for (final var value : values) {
      final var count = valueCounts.containsKey(value) ? valueCounts.get(value) + 1 : 1;
      valueCounts.put(value, count);

      if (count > valueCounts.get(bestValue)) {
        bestValue = value;
      }
    }
    return bestValue;
  }

  public static void main(String[] args) {
    final var values = new int[]{0, 1, 2, 1, 3};

    System.out.println("Meilleure valeur : %s".formatted(
        AccordByzantin.getInstance().getRightValueBasedOnThereIsMoreGoodNodeThanBadNode(values)));

    System.out.println("Meilleure valeur : %s".formatted(
        AccordByzantin.getInstance()
            .getRightValueBasedOnThereIsMoreGoodNodeThanBadNode(values, 2)));
  }
}
