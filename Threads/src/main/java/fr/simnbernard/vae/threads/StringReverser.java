package fr.simnbernard.vae.threads;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import lombok.RequiredArgsConstructor;

public class StringReverser {

  private static final StringReverser INSTANCE = new StringReverser();

  static StringReverser getInstance() {
    return INSTANCE;
  }

  @RequiredArgsConstructor
  private static class StringReverserTask extends RecursiveTask<String> {
    private final String stringToReverse;

    @Override
    protected String compute() {
      if (stringToReverse.length() == 1) {
        return stringToReverse;
      }
      final var lastIdx = stringToReverse.length() - 1;
      final var forkTask = new StringReverserTask(stringToReverse.substring(0, lastIdx));
      forkTask.fork();
      return stringToReverse.charAt(lastIdx) + forkTask.join();
    }
  }

  String reverse(final String stringToReverse) {
    return ForkJoinPool.commonPool().invoke(new StringReverserTask(stringToReverse));
  }

  public static void main(String[] args) {
    System.out.println(StringReverser.getInstance().reverse("toto"));
  }
}
