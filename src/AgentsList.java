import java.util.ArrayList;
import java.util.List;

public class AgentsList {
    private static List<Agent> agents = new ArrayList<>();
    private static List<Integer> lastChoose = new ArrayList<>();

    public static int numAgents() {
        return agents.size();
    }

    public static void addAgent(Agent agent) {
        agents.add(agent);
    }

    public static Agent getAgent(int id) {
        return agents.get(id);
    }

    public static List<Agent> getAgents() {
        return agents;
    }

    public static List<Integer> getLastChoose() {
        return lastChoose;
    }

    public static void setLastChoose(List<Integer> newChoose) {
        lastChoose = newChoose;
    }
}
