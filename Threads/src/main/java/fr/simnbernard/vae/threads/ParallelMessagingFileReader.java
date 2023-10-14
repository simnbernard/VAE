package fr.simnbernard.vae.threads;

import static fr.simnbernard.vae.threads.ParallelMessagingFileReader.LineConsumerTask.receivedLineCpt;
import static fr.simnbernard.vae.threads.ParallelMessagingFileReader.LineProducerTask.readLineCpt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

public class ParallelMessagingFileReader {

  private static final ParallelMessagingFileReader INSTANCE = new ParallelMessagingFileReader();

  static ParallelMessagingFileReader getInstance() {
    return INSTANCE;
  }

  @RequiredArgsConstructor
  static class LineProducerTask implements Runnable {

    static final AtomicInteger readLineCpt = new AtomicInteger(0);
    private final BufferedReader reader;
    private final BlockingQueue<String> queue;

    @Override
    public void run() {
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          readLineCpt.incrementAndGet();
          System.out.println("Ligne " + line + " envoyée par " + Thread.currentThread().getName());
          queue.put(line);
        }
      } catch (IOException | InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  @RequiredArgsConstructor
  static class LineConsumerTask implements Runnable {
    static final AtomicInteger receivedLineCpt = new AtomicInteger(0);

    private final BlockingQueue<String> queue;

    @Override
    public void run() {
      try {
        while (true) {
          System.out.println(
              "Ligne %s lue par %s".formatted(queue.take(), Thread.currentThread().getName()));
          receivedLineCpt.incrementAndGet();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  void read(final String path) {
    final var start = System.currentTimeMillis();

    final var nbProducer = 2;
    final var nbConsumer = 4;
    final BlockingQueue<String> queue = new ArrayBlockingQueue<>(nbConsumer + 1);

    final var nbThreads = nbProducer + nbConsumer;
    final var executor = Executors.newFixedThreadPool(nbThreads);

    try {
      final var fileReader = new BufferedReader(new FileReader(path));
      IntStream.range(0, nbConsumer).forEach(idx -> executor.submit(new LineConsumerTask(queue)));
      final var producersFutures =
          IntStream.range(0, nbProducer)
              .mapToObj(idx -> new LineProducerTask(fileReader, queue))
              .map(executor::submit)
              .toList();

      for (var future : producersFutures) {
        future.get();
      }

      while (receivedLineCpt.get() < readLineCpt.get()) {}

      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdownNow();
    }

    System.out.println("\n> Fichier lu en %d ms".formatted(System.currentTimeMillis() - start));
  }

  public static void main(String[] args) {
    ParallelMessagingFileReader.getInstance()
        .read(ParallelMessagingFileReader.class.getResource("/file_to_import.txt").getPath());
  }
}
