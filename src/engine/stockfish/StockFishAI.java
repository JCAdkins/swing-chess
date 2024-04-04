package engine.stockfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StockFishAI {
    private Process stockfishProcess;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    public StockFishAI() {
        try {
            // Start Stock fish process
            stockfishProcess = new ProcessBuilder("/usr/local/Cellar/stockfish/16.1/bin/stockfish").start();

            // Get input and output streams
            reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            writer = new OutputStreamWriter(stockfishProcess.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        try {
            // Send command to Stock fish
            writer.write(command + "\n");
            writer.flush();

            // Read Stock fish output
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
            return null;
        }
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
//            e.printStackTrace();
        }
    }
}

