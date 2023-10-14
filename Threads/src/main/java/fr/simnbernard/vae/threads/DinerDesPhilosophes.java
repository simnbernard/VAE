package fr.simnbernard.vae.threads;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

public class DinerDesPhilosophes {

  private static final int NB_PHILOSOPHES = 5;

  @RequiredArgsConstructor
  private class Philosophe implements Runnable {
    private static final Random RANDOM = new Random();
    private final int idx;
    private final Semaphore[] fourchettes;

    @Override
    public void run() {
      while (true) {
        think();
        eat();
      }
    }

    private void eat() {
      System.out.println("Philosophe %d veut manger".formatted(idx));

      final var idxFourchetteGauche = idx;
      final var idxFourchetteDroite = (idx + 1) % fourchettes.length;

      final var fourchetteGauche = fourchettes[idxFourchetteGauche];
      final var fourchetteDroite = fourchettes[idxFourchetteDroite];

      try {
        System.out.println(
            "Philosophe %d prends la fourchette de gauche %d".formatted(idx, idxFourchetteGauche));
        fourchetteGauche.acquire();
        System.out.println(
            "Philosophe %d prends la fourchette de droite %d".formatted(idx, idxFourchetteDroite));
        fourchetteDroite.acquire();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      System.out.println("Philosophe %d commence à manger".formatted(idx));
      randomSleep();
      System.out.println("Philosophe %d a terminé à manger".formatted(idx));

      System.out.println("Philosophe %d repose la fourchette de gauche".formatted(idx));
      fourchetteGauche.release();
      System.out.println("Philosophe %d repose la fourchette de droite".formatted(idx));
      fourchetteDroite.release();
    }

    private void think() {
      System.out.println("Philosophe %d commence à penser".formatted(idx));
      randomSleep();
      System.out.println("Philosophe %d a terminé de penser".formatted(idx));
    }

    private void randomSleep() {
      try {
        Thread.sleep(RANDOM.nextInt(50, 1500));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  void startDiner() {
    final var philosophes = new ArrayList<Philosophe>(NB_PHILOSOPHES);
    final var fourchettes = new Semaphore[NB_PHILOSOPHES];

    IntStream.range(0, NB_PHILOSOPHES)
        .forEach(
            idx -> {
              philosophes.add(new Philosophe(idx, fourchettes));
              fourchettes[idx] = (new Semaphore(1));
            });

    final var executor = Executors.newFixedThreadPool(NB_PHILOSOPHES);

    philosophes.stream().forEach(executor::submit);

    executor.shutdown();

    try {
      executor.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdownNow();
    }
  }

  public static void main(String[] args) {
    new DinerDesPhilosophes().startDiner();
  }
}
