package fr.simnbernard.vae.algorithmes.graphes;

import fr.simnbernard.vae.algorithmes.graphes.Graph.Chemin;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {

  private static final Dijkstra INSTANCE = new Dijkstra();

  static Dijkstra getInstance() {
    return INSTANCE;
  }

  private List<Character> getPlusCourtCheminGraphNonPondere(final Graph graph,
      final Character sommetSrc, final Character sommetDest) {
    return getPlusCourtCheminGraphNonPondere(graph, sommetSrc, sommetDest, new HashSet<>());
  }

  List<Character> getPlusCourtCheminGraphNonPondere(final Graph graph,
      final Character sommetSrc, final Character sommetDest, final Set<Character> sommetsTraite) {
    final var sommetsAdjacents = graph.getSommetsAdjacents(sommetSrc);
    if (sommetsAdjacents.contains(sommetDest)) {
      return List.of(sommetSrc, sommetDest);
    }

    sommetsTraite.add(sommetSrc);
    final var plusCourtCheminTrouve = new ArrayList<Character>();
    for (final var sommetAdjacent : sommetsAdjacents) {
      if (!sommetsTraite.contains(sommetAdjacent)) {
        final var nouveauChemin = getPlusCourtCheminGraphNonPondere(graph, sommetAdjacent,
            sommetDest,
            sommetsTraite);
        if (plusCourtCheminTrouve.isEmpty()
            || nouveauChemin.size() < plusCourtCheminTrouve.size()) {
          plusCourtCheminTrouve.clear();
          plusCourtCheminTrouve.addAll(nouveauChemin);
        }
      }
    }
    if (!plusCourtCheminTrouve.isEmpty()) {
      plusCourtCheminTrouve.add(0, sommetSrc);
    }
    return plusCourtCheminTrouve;
  }

  List<Character> getPlusCourtCheminGraphPondere(final Graph graph,
      final Character sommetSrc, final Character sommetDest) {
    final var plusCourtCheminBySommetTrouve = new HashMap<Character, Chemin>();
    final var chemins = new PriorityQueue<>(Comparator.comparingInt(Chemin::getPoids));
    chemins.add(new Chemin(sommetSrc, sommetSrc, 0));

    while (!chemins.isEmpty()) {
      final var chemin = chemins.poll();
      plusCourtCheminBySommetTrouve.put(chemin.getSommet(), chemin);
      if (chemin.getSommet().equals(sommetDest)) {
        break;
      }
      final var aretes = graph.getAretes(chemin.getSommet());

      for (final var arete : aretes.entrySet()) {
        if (plusCourtCheminBySommetTrouve.keySet().contains(arete)) {
          break;
        }
        chemins.add(new Chemin(chemin.getSommet(), arete.getKey(),
            chemin.getPoids() + arete.getValue()));
      }
    }

    final var result = new ArrayList<Character>();
    var sommet = sommetDest;
    while (plusCourtCheminBySommetTrouve.containsKey(sommet)) {
      result.add(0, sommet);
      if (sommet.equals(sommetSrc)) {
        return result;
      }
      sommet = plusCourtCheminBySommetTrouve.get(sommet).getOrigine();
    }

    return List.of();
  }

  public static void main(String[] args) {
    final var graphNonPondere = new Graph(false);
    graphNonPondere.addArete('A', 'B');
    graphNonPondere.addArete('B', 'C');
    graphNonPondere.addArete('A', 'E');
    graphNonPondere.addArete('B', 'D');
    graphNonPondere.addArete('D', 'E');
    graphNonPondere.addSommet('F');

    final var plusCourtCheminNonPondere = Dijkstra.getInstance()
        .getPlusCourtCheminGraphNonPondere(graphNonPondere, 'A', 'C');

    System.out.println("Plus court chemin non pondéré : %d".formatted(plusCourtCheminNonPondere));

    final var graphPondere = new Graph(false);
    graphPondere.addArete('A', 'B', 1);
    graphPondere.addArete('B', 'C', 3);
    graphPondere.addArete('A', 'C', 4);
    graphPondere.addSommet('D');

    final var plusCourtCheminPondere = Dijkstra.getInstance()
        .getPlusCourtCheminGraphPondere(graphPondere, 'A', 'D');

    System.out.println("Plus court chemin pondéré : %d".formatted(plusCourtCheminPondere));
  }
}
