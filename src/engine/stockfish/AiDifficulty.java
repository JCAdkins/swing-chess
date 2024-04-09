package engine.stockfish;

public class AiDifficulty {
    private int timeLimitMillis;
    private int depth;

    public AiDifficulty(int timeLimitMillis, int depth) {
        this.timeLimitMillis = timeLimitMillis;
        this.depth = depth;
    }

    // Getters and setters
    public int getTimeLimitMillis() {
        return timeLimitMillis;
    }

    public void setTimeLimitMillis(int timeLimitMillis) {
        this.timeLimitMillis = timeLimitMillis;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "AiDifficulty{" +
                "timeLimit= " + timeLimitMillis +
                ", depth= " + depth +
                '}';
    }
}
