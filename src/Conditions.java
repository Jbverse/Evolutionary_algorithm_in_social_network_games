import java.util.ArrayList;

public class Conditions {

    private int type;
    private int players;
    private int numOfIterations;
    private boolean synchronous;

    public Conditions() {

    }

    public Conditions(Conditions conditions) {
        type = conditions.type;
        players = conditions.players;
        numOfIterations = conditions.numOfIterations;
        synchronous = conditions.synchronous;
    }

    public Conditions(int type, int players, int numOfIterations, boolean synchronous) {
        this.type = type;
        this.players = players;
        this.numOfIterations = numOfIterations;
        this.synchronous = synchronous;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getNumOfIterations() {
        return numOfIterations;
    }

    public void setNumOfIterations(int numOfIterations) {
        this.numOfIterations = numOfIterations;
    }

    public boolean isSynchronous() {
        return synchronous;
    }

    public void setSynchronous(boolean synchronous) {
        this.synchronous = synchronous;
    }
}
