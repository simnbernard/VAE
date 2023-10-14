package fr.simnbernard.vae.algorithmes.graphes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Graph {

  private final boolean oriented;

  private Map<Character, Map<Character, Integer>> graph = new HashMap<>();

  public Map<Character, Map<Character, Integer>> get() {
    return graph;
  }

  public Set<Character> getSommets() {
    return graph.keySet();
  }

  public Set<Character> getSommetsAdjacents(final Character sommet) {
    return graph.get(sommet).keySet();
  }

  public Map<Character, Integer> getAretes(final Character sommet) {
    return graph.get(sommet);
  }

  public void addSommet(Character sommet) {
    graph.putIfAbsent(sommet, new HashMap<>());
  }

  public void addArete(Character sommetSrc, Character sommetDest, Integer poids) {
    graph.putIfAbsent(sommetSrc, new HashMap<>());
    graph.putIfAbsent(sommetDest, new HashMap<>());

    graph.get(sommetSrc).put(sommetDest, poids);
    if (!oriented) {
      graph.get(sommetSrc).put(sommetSrc, poids);
    }
  }

  public void addArete(Character sommetSrc, Character sommetDest) {
    addArete(sommetSrc, sommetDest, 1);
  }

  protected record Chemin(@Getter Character origine, @Getter Character sommet,
                          @Getter Integer poids) {

  }
}
