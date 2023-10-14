package fr.simnbernard.vae.algorithmes.graphes;

import java.util.HashMap;

public class BellmanFord {

  private static final BellmanFord INSTANCE = new BellmanFord();

  static BellmanFord getInstance() {
    return INSTANCE;
  }

  int getPlusCourtChemin(final Graph graph, final Character sommetSrc,
      final Character sommetDest) {
    final var nbrSommet = graph.getSommets().size();
    final var distances = new HashMap<Character, Integer>(nbrSommet);
    distances.put(sommetSrc, 0);

    for (var itIdx = 1; itIdx < nbrSommet; itIdx++) {
      for (final var sommet : graph.getSommets()) {
        if (!distances.containsKey(sommet)) {
          continue;
        }
        final var currentDistanceSommet = distances.get(sommet);
        for (var arrete : graph.getAretes(sommet).entrySet()) {
          final var sommetDestArete = arrete.getKey();
          final int newDistance = currentDistanceSommet + arrete.getValue();
          if (!distances.containsKey(sommetDestArete) || newDistance < distances.get(
              sommetDestArete)) {
            distances.put(arrete.getKey(), newDistance);
          }
        }
      }
    }

    //On refait une itération pour voir si il y a une valeur plus petite encore, si c'est le cas,
    //alors il y a un cycle négatif, car nous sommes censés avoir trouvé les chemins les plus cours
    //avec nbSommet - 1 itérations
    for (final var sommet : graph.getSommets()) {
      final var distanceSommet = distances.get(sommet);
      for (final var currentArrete : graph.getAretes(sommet).entrySet()) {
        final var sommetDestArete = currentArrete.getKey();
        final int newDistance = distanceSommet + currentArrete.getValue();
        if (newDistance < distances.get(sommetDestArete)) {
          throw new RuntimeException("Cycle négatif trouvé");
        }
      }
    }

    System.out.println(
        "Plus petites distances pour chaque sommet depuis %s : %s".formatted(sommetSrc, distances));
    return distances.get(sommetDest);
  }

  public static void main(String[] args) {
    final var graph = new Graph(true);
    graph.addArete('S', 'B', 6);
    graph.addArete('S', 'C', 3);
    graph.addArete('C', 'B', 2);
    graph.addArete('B', 'D', 8);
    graph.addArete('D', 'B', 1);
    graph.addArete('B', 'C', 1);
    graph.addArete('D', 'E', -5);
    graph.addArete('E', 'C', 2);
    graph.addSommet('Y');

    System.out.println(
        "Plus court chemin : %s".formatted(BellmanFord.getInstance().getPlusCourtChemin(graph, 'S',
            'B')));

    graph.addArete('B', 'D', -9);
    //Doit throw car cycle négatif
    BellmanFord.getInstance().getPlusCourtChemin(graph, 'S', 'E');
  }
}
