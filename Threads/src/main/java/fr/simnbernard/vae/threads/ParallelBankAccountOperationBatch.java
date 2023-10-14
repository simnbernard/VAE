package fr.simnbernard.vae.threads;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ParallelBankAccountOperationBatch {
  @Getter
  private final AtomicInteger bankAccountBalance = new AtomicInteger(new Random().nextInt(15));

  @RequiredArgsConstructor
  private static class OperationMakerTask implements Runnable {

    private final AtomicInteger bankBalance;
    private final int operationValue;

    @Override
    public void run() {
      if (operationValue < 0) {
        retirer(operationValue);
      } else {
        deposer(operationValue);
      }
    }

    private synchronized void retirer(final int value) {
      if (bankBalance.get() >= value) {
        bankBalance.addAndGet(value);
      }
    }

    private void deposer(final int value) {
      bankBalance.addAndGet(value);
    }
  }

  @Override
  public String toString() {
    return "Montant actuel : %d".formatted(bankAccountBalance.get());
  }

  public static void main(String[] args) {
    final var operations = new int[] {-2, 1, 9, -6, 12};
    final var batch = new ParallelBankAccountOperationBatch();

    System.out.println(batch);

    final var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    Arrays.stream(operations)
        .mapToObj(operation -> new OperationMakerTask(batch.getBankAccountBalance(), operation))
        .forEach(executor::submit);

    executor.shutdown();
    try {
      executor.awaitTermination(15, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdownNow();
    }

    System.out.println(batch);
  }
}
