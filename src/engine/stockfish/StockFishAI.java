package engine.stockfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class StockFishAI {
    private Process stockfishProcess;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public StockFishAI() {
        try {
            // Start Stock fish process
            stockfishProcess = new ProcessBuilder("stockfish").start();

            // Get input and output streams
            reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            writer = new OutputStreamWriter(stockfishProcess.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public <T> T sendCommand(String cmd, Function<List<String>, T> commandProcessor, Predicate<String> breakCondition, long timeout)
            throws InterruptedException, ExecutionException, TimeoutException {

        // This completable future will send a command to the process
        // And gather all the output of the engine in the List<String>
        // At the end, the List<String> is translated to T through the
        // commandProcessor Function
        CompletableFuture<T> command = supplyAsync(() -> {
            final List<String> output = new ArrayList<>();
            try {
                writer.flush();
                writer.write(cmd + "\n");
                writer.write("isready\n");
                writer.flush();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.contains("Unknown command")) {
                        throw new RuntimeException(line);
                    }
                    if (line.contains("Unexpected token")) {
                        throw new RuntimeException("Unexpected token: " + line);
                    }
                    output.add(line);
                    if (breakCondition.test(line)) {
                        // At this point we are no longer interested to read any more
                        // output from the engine, we consider that the engine responded
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return commandProcessor.apply(output);
        });

        return command.get(timeout, TimeUnit.MILLISECONDS);
    }

    public void close() {
        try {
            // Close input and output streams
            reader.close();
            writer.close();

            // Destroy Stock fish process
            stockfishProcess.destroy();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

