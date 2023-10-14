package fr.simnbernard.vae.threads;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

public class ParallelFileReader {

  private static final ParallelFileReader INSTANCE = new ParallelFileReader();

  static ParallelFileReader getInstance() {
    return INSTANCE;
  }

  @RequiredArgsConstructor
  private static class LineReaderTask implements Callable<Boolean> {
    private static final AtomicInteger lineInFile = new AtomicInteger(0);

    private final BufferedReader fileReader;
    private int lineIdx;

    @Override
    public Boolean call() {
      try {
        final var line = readLine();
        if (line == null) {
          return true;
        }
        System.out.println("Ligne " + lineIdx + " lue par " + Thread.currentThread().getName());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return false;
    }

    private synchronized String readLine() throws IOException {
      final var line = fileReader.readLine();
      this.lineIdx = lineInFile.incrementAndGet();
      return line;
    }
  }

  void read(final String path) {
    final var start = System.currentTimeMillis();
    var nbCore = Runtime.getRuntime().availableProcessors();
    final var executor = Executors.newFixedThreadPool(nbCore);

    try {
      final var fileReader = new BufferedReader(new FileReader(path));

      var isFinished = false;

      while (!isFinished) {
        final var tasks =
            IntStream.range(0, nbCore).mapToObj(idx -> new LineReaderTask(fileReader)).toList();
        for (var future : executor.invokeAll(tasks)) {
          if (future.get()) {
            isFinished = true;
          }
        }
      }
    } catch (FileNotFoundException | ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      executor.shutdownNow();
    }

    System.out.println("\n> Fichier lu en %d ms".formatted(System.currentTimeMillis() - start));
  }

  public static void main(String[] args) {
    ParallelFileReader.getInstance()
        .read(ParallelFileReader.class.getResource("/file_to_import.txt").getPath());
  }
}
