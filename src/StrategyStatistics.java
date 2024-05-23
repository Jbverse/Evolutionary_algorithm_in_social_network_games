import java.util.HashMap;
import java.util.List;

public class StrategyStatistics {
    private HashMap<Integer, Integer> maxPayoffPerStrategy;
    private HashMap<Integer, Integer> totalPayoffPerStrategy;
    private HashMap<Integer, Integer> strategyCount;

    public StrategyStatistics() {
        maxPayoffPerStrategy = new HashMap<>();
        totalPayoffPerStrategy = new HashMap<>();
        strategyCount = new HashMap<>();
    }

    public void updateStatistics(List<Agent> agents) {
        for (Agent agent : agents) {
            int strategy = agent.getStrategy();
            int payoff = agent.getPayoff();

            maxPayoffPerStrategy.merge(strategy, payoff, Math::max);
            totalPayoffPerStrategy.merge(strategy, payoff, Integer::sum);
            strategyCount.merge(strategy, 1, Integer::sum);
        }
    }


    public String displayStatistics() {
        StringBuilder statsHtml = new StringBuilder();

        statsHtml.append("<h3>Strategy Statistics:</h3>");

        statsHtml.append("<h4>Max Payoffs per Strategy:</h4><ul>");
        maxPayoffPerStrategy.forEach((strategy, payoff) ->
                statsHtml.append("<li>Strategy ").append(strategy).append(": ").append(payoff).append("</li>"));
        statsHtml.append("</ul>");

        statsHtml.append("<h4>Total Payoffs per Strategy:</h4><ul>");
        totalPayoffPerStrategy.forEach((strategy, payoff) ->
                statsHtml.append("<li>Strategy ").append(strategy).append(": ").append(payoff).append("</li>"));
        statsHtml.append("</ul>");

        statsHtml.append("<h4>Participation Percentages per Strategy:</h4><ul>");
        int totalAgents = strategyCount.values().stream().mapToInt(Integer::intValue).sum();
        strategyCount.forEach((strategy, count) -> {
            double percentage = 100.0 * count / totalAgents;
            statsHtml.append(String.format("<li>Strategy %d: %.2f%%</li>", strategy, percentage));
        });
        statsHtml.append("</ul>");

        return statsHtml.toString();
    }
}
