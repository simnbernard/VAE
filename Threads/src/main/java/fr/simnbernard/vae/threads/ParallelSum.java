package fr.simnbernard.vae.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

public class ParallelSum {

  private static final ParallelSum INSTANCE = new ParallelSum();

  static ParallelSum getInstance() {
    return INSTANCE;
  }

  @RequiredArgsConstructor
  private static class SumTask implements Callable<Integer> {
    private final int[] array;
    private final int startIdx;
    private final int endIdx;

    @Override
    public Integer call() {
      return IntStream.rangeClosed(startIdx, endIdx).map(idx -> array[idx]).sum();
    }
  }

  public int sum(int[] array) {
    var nbCore = Math.min(Runtime.getRuntime().availableProcessors(), array.length / 2);
    var packetSize = array.length / nbCore;
    var result = 0;

    final var executor = Executors.newFixedThreadPool(nbCore);
    final var sumTasks =
        IntStream.range(0, nbCore)
            .mapToObj(
                taskIdx ->
                    new SumTask(
                        array,
                        taskIdx * packetSize,
                        (taskIdx == nbCore - 1 ? array.length : (taskIdx + 1) * packetSize) - 1))
            .toList();

    try {
      for (final var future : executor.invokeAll(sumTasks)) {
        result += future.get();
      }

    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdownNow();
    }

    return result;
  }

  public static void main(String[] args) {
    System.out.println(ParallelSum.getInstance().sum(new int[] {1, 2, 3, 5, 8, 13, 21}));
  }
}
