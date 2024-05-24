import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private String selectedGame;
    private int density;
    private int numAgents;
    private int numRounds;
    private int numGames;
    private int strongPop;
    private StringBuilder htmlOutput;
    private Map<String, Integer> strategyPayoffs;
    private Map<Integer, Integer> agentSurvival;
    private List<Integer> gameWinners;
    private int maxTotalProfit;
    private int maxTotalProfitGame;
    private Map<String, Integer> strongPopulationStrategies;
    private Map<String, Integer> strongPopulationProfits;
    private Agent overallTopAgent;
    private Map<Integer, Integer> agentTotalPayoffs;

    public Game(String selectedGame, int density, int numAgents, int numRounds, int numGames, int strongPop) {
        this.selectedGame = selectedGame;
        this.density = density;
        this.numAgents = numAgents;
        this.numRounds = numRounds;
        this.numGames = numGames;
        this.strongPop = strongPop;
        this.htmlOutput = new StringBuilder();
        this.strategyPayoffs = new HashMap<>();
        this.agentSurvival = new HashMap<>();
        this.gameWinners = new ArrayList<>();
        this.maxTotalProfit = Integer.MIN_VALUE;
        this.maxTotalProfitGame = -1;
        this.strongPopulationStrategies = new HashMap<>();
        this.strongPopulationProfits = new HashMap<>();
        this.overallTopAgent = null;
        this.agentTotalPayoffs = new HashMap<>();
    }

    public String run() {
        htmlOutput.append("<html><head><style>");
        htmlOutput.append("body { font-family: 'Cormorant Garamond', serif; background-color: #fdf6e3; color: #073642; line-height: 1.8; }");
        htmlOutput.append("h2, h3 { font-family: 'Montserrat', sans-serif; color: #2c3e50; margin-bottom: 0.5em; text-transform: uppercase; letter-spacing: 1px; }");
        htmlOutput.append("h2 { font-size: 24px; }");
        htmlOutput.append("h3 { font-size: 20px; margin-top: 20px; }");
        htmlOutput.append("strong { font-weight: bold; }");
        htmlOutput.append("p, th, td { font-family: 'Cormorant Garamond', serif; }");
        htmlOutput.append("table { border-collapse: collapse; margin: 25px auto; width: 95%; background: #eee8d5; }");
        htmlOutput.append("th, td { padding: 8px 14px; font-size: 14px; }");
        htmlOutput.append("th { background-color: #fdf6e3; color: #657b83; font-size: 16px; }");
        htmlOutput.append("td { background-color: transparent; color: #586e75; }");
        htmlOutput.append(".neighbor { color: #859900; font-weight: bold; }");
        htmlOutput.append(".agent { background-color: #eee8d5; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); margin-bottom: 25px; transition: transform .2s; }");
        htmlOutput.append(".agent:hover { transform: scale(1.05); }");
        htmlOutput.append("</style></head><body>");

        htmlOutput.append("<h2>").append(selectedGame).append("</h2>");
        htmlOutput.append("<p><strong>Density:</strong> ").append(density).append("%<br/><strong>Number of Agents:</strong> ").append(numAgents);
        htmlOutput.append("<br/><strong>Number of Rounds:</strong> ").append(numRounds).append("<br/><strong>Number of Games:</strong> ").append(numGames);
        htmlOutput.append("<br/><strong>Strong Population:</strong> ").append(strongPop).append("%</p>");

        initAgents();

        for (int currentGame = 0; currentGame < numGames; currentGame++) {
            if (currentGame > 0) {
                removeWeakAgents();
            }
            gameInit();

            htmlOutput.append("<table><tr>");
            for (Agent agent : AgentsList.getAgents()) {
                htmlOutput.append("<th><div class='agent'>");
                htmlOutput.append("<h2>").append(getStrategyName(agent.getStrategy()));
                if (selectedGame.equals("Battle of Sexes")) {
                    htmlOutput.append(": ").append(agent.getSexString());
                }
                htmlOutput.append("</h2>");
                htmlOutput.append("<img src='").append(getImagePath(agent.getStrategy()))
                        .append("' alt='").append(agent.getStrategy())
                        .append("' width='120' height='150'>");
                htmlOutput.append("<p>ID: ").append(agent.getId()).append("</p>");
                htmlOutput.append("</div></th>");
            }
            htmlOutput.append("</tr>");

            int currentRounds = numRounds;
            while (currentRounds > 0) {
                htmlOutput.append("<tr>");
                playRound();
                htmlOutput.append("</tr>");
                currentRounds--;
            }
            htmlOutput.append("</table>");

            int totalPayoff = calculateTotalPayoff();
            if (totalPayoff > maxTotalProfit) {
                maxTotalProfit = totalPayoff;
                maxTotalProfitGame = currentGame + 1;
            }
            Agent winner = getWinner();
            if (winner != null) {
                gameWinners.add(winner.getId());
            }
            if (overallTopAgent == null || (winner != null && agentTotalPayoffs.get(winner.getId()) > agentTotalPayoffs.get(overallTopAgent.getId()))) {
                overallTopAgent = winner;
            }
            htmlOutput.append("<p>Total Payoff for Game ").append(currentGame + 1).append(": ").append(totalPayoff).append("</p>");
            updateStatistics(); // Update statistics after each game
            updateSurvivalStats(); // Update survival stats after each game
        }

        removeWeakAgents();

        htmlOutput.append("<h2>Endgame BI Statistics</h2>");
        displayStatistics(); // Display statistics at the end of all games

        htmlOutput.append("</body></html>");

        return htmlOutput.toString();
    }

    private int calculateTotalPayoff() {
        int total = 0;
        for (Agent agent : AgentsList.getAgents()) {
            total += agent.getPayoff();
        }
        return total;
    }

    private void playRound() {
        List<Agent> allAgents = AgentsList.getAgents();
        for (Agent agent : allAgents) {
            htmlOutput.append("<td style='font-size: 7px;'>");
            ArrayList<Integer> opponents = agent.getOpponents();
            htmlOutput.append("<p>(").append(agent.getStateString()).append(") Payoff [").append(agent.getPayoff()).append("]</p>");

            for (Integer nextOpponent : opponents) {
                Agent opponent = allAgents.get(nextOpponent);
                int payoff = Functions.gainMatrix(agent.getSex())[agent.getState()][opponent.getState()];
                agent.updatePayoff(payoff);
                htmlOutput.append("opponent").append(nextOpponent).append(" (").append(opponent.getStateString());

                if (selectedGame.equals("Battle of Sexes")) {
                    htmlOutput.append(": ").append(opponent.getSexString());
                }

                htmlOutput.append(") ----> [")
                        .append(agent.getPayoff()).append("]<br/>");
            }
            htmlOutput.append("</td>");
        }

        for (Agent agent : allAgents) {
            agent.useStrategy();
        }
    }

    private void initAgents() {
        AgentsList.getAgents().clear();
        for (int i = 0; i < numAgents; i++) {
            Agent agent = new Agent(i, selectedGame, -1);
            AgentsList.addAgent(agent);
            agentSurvival.put(agent.getId(), 0);
            agentTotalPayoffs.put(agent.getId(), 0);
        }
    }

    private String getImagePath(int strategy) {
        String imagePath = getClass().getResource("/images/").toString();
        switch (strategy) {
            case 2: return imagePath + "bestRes.jpeg";
            case 4: return imagePath + "Evil.jpeg";
            case 3: return imagePath + "Honest.jpeg";
            case 1: return imagePath + "Threshold.jpeg";
            case 0: return imagePath + "TitForTat.jpeg";
            default: return imagePath + "defaultImage.jpeg";
        }
    }

    private void gameInit() {
        boolean[][] neighborhoodsTable = Functions.neighborhoodsTable(selectedGame, AgentsList.numAgents(), density);

        htmlOutput.append("<h3>Neighbors Matrix</h3>");
        htmlOutput.append("<table>");
        htmlOutput.append("<tr><th></th>");

        for (int i = 0; i < AgentsList.numAgents(); i++) {
            htmlOutput.append("<th>").append(i).append("</th>");
        }
        htmlOutput.append("</tr>");

        for (int i = 0; i < AgentsList.numAgents(); i++) {
            htmlOutput.append("<tr><th>").append(i).append("</th>");
            for (int j = 0; j < AgentsList.numAgents(); j++) {
                if (neighborhoodsTable[i][j]) {
                    htmlOutput.append("<td><span class='neighbor'>&#10004;</span></td>");
                } else {
                    htmlOutput.append("<td></td>");
                }
            }
            htmlOutput.append("</tr>");
        }
        htmlOutput.append("</table><br/>");

        for (int i = 0; i < AgentsList.numAgents(); i++) {
            ArrayList<Integer> opponents = new ArrayList<>();
            for (int j = 0; j < AgentsList.numAgents(); j++) {
                if (neighborhoodsTable[i][j] && i != j) {
                    opponents.add(j);
                }
            }
            AgentsList.getAgent(i).setOpponents(opponents);
        }
    }

    private void removeWeakAgents() {
        List<Agent> allAgents = AgentsList.getAgents();

        int strongCount = (int) Math.round(allAgents.size() * (strongPop / 100.0));

        allAgents.sort((a, b) -> Integer.compare(b.getPayoff(), a.getPayoff()));

        List<Agent> retainedAgents = new ArrayList<>(allAgents.subList(0, strongCount));
        if (retainedAgents.size() > 0) {
            htmlOutput.append("<h3>Retained Agents: ").append(retainedAgents.size()).append("</h3>");
            htmlOutput.append("<table border='1'>");
            htmlOutput.append("<tr><th>Strategy</th><th>Payoff</th></tr>");

            for (Agent agent : retainedAgents) {
                htmlOutput.append("<tr>")
                        .append("<td>").append(getStrategyName(agent.getStrategy())).append("</td>")
                        .append("<td>").append(agent.getPayoff()).append("</td>")
                        .append("</tr>");

                String strategyName = agent.getStrategyName();
                strongPopulationStrategies.put(strategyName, strongPopulationStrategies.getOrDefault(strategyName, 0) + 1);
                strongPopulationProfits.put(strategyName, strongPopulationProfits.getOrDefault(strategyName, 0) + agent.getPayoff());
                agentTotalPayoffs.put(agent.getId(), agentTotalPayoffs.get(agent.getId()) + agent.getPayoff());
            }
            htmlOutput.append("</table>");
        }

        for (int i = 0; i < retainedAgents.size(); i++) {
            retainedAgents.get(i).initAgent(i);
        }

        AgentsList.getAgents().clear();
        AgentsList.getAgents().addAll(retainedAgents);

        int numNewAgents = numAgents - retainedAgents.size();

        for (int i = 0; i < numNewAgents; i++) {
            Agent agent = new Agent(retainedAgents.size() + i, selectedGame, -1);
            AgentsList.addAgent(agent);
            agentSurvival.put(agent.getId(), agentSurvival.getOrDefault(agent.getId(), 0) + 1);
            agentTotalPayoffs.put(agent.getId(), 0);
        }
    }

    private void updateStatistics() {
        for (Agent agent : AgentsList.getAgents()) {
            String strategyName = agent.getStrategyName();
            strategyPayoffs.put(strategyName, strategyPayoffs.getOrDefault(strategyName, 0) + agent.getPayoff());
        }
    }

    private void updateSurvivalStats() {
        for (Agent agent : AgentsList.getAgents()) {
            agentSurvival.put(agent.getId(), agentSurvival.getOrDefault(agent.getId(), 0) + 1);
        }
    }

    private Agent getWinner() {
        return AgentsList.getAgents().stream().max(Comparator.comparingInt(Agent::getPayoff)).orElse(null);
    }

    private void displayStatistics() {
        // Best Strategy
        String bestStrategy = null;
        int maxPayoff = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : strategyPayoffs.entrySet()) {
            if (entry.getValue() > maxPayoff) {
                maxPayoff = entry.getValue();
                bestStrategy = entry.getKey();
            }
        }
        htmlOutput.append("<h3>Best Strategy: ").append(bestStrategy).append(" with ").append(maxPayoff).append(" points</h3>");

        // Top 3 Agents
        htmlOutput.append("<h3>Top 3 Agents:</h3>");
        List<Agent> topAgents = agentTotalPayoffs.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(entry -> AgentsList.getAgent(entry.getKey()))
                .collect(Collectors.toList());
        htmlOutput.append("<table border='1'><tr><th>Strategy</th><th>Payoff</th><th>Sex</th></tr>");
        for (Agent agent : topAgents) {
            htmlOutput.append("<tr>")
                    .append("<td>").append(getStrategyName(agent.getStrategy())).append("</td>")
                    .append("<td>").append(agentTotalPayoffs.get(agent.getId())).append("</td>");
            if (selectedGame.equals("Battle of Sexes")) {
                htmlOutput.append("<td>").append(agent.getSexString()).append("</td>");
            } else {
                htmlOutput.append("<td>N/A</td>");
            }
            htmlOutput.append("</tr>");
        }
        htmlOutput.append("</table>");

        // Agents who survived the most games
        htmlOutput.append("<h3>Agents who survived the most games:</h3>");
        List<Map.Entry<Integer, Integer>> topSurvivors = agentSurvival.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .collect(Collectors.toList());
        htmlOutput.append("<table border='1'><tr><th>Strategy</th><th>Games Survived</th><th>Sex</th></tr>");
        for (Map.Entry<Integer, Integer> entry : topSurvivors) {
            Agent agent = AgentsList.getAgent(entry.getKey());
            htmlOutput.append("<tr>")
                    .append("<td>").append(getStrategyName(agent.getStrategy())).append("</td>")
                    .append("<td>").append(entry.getValue()).append("</td>");
            if (selectedGame.equals("Battle of Sexes")) {
                htmlOutput.append("<td>").append(agent.getSexString()).append("</td>");
            } else {
                htmlOutput.append("<td>N/A</td>");
            }
            htmlOutput.append("</tr>");
        }
        htmlOutput.append("</table>");

        // The biggest total profit
        htmlOutput.append("<h3>Biggest Total Profit:</h3>");
        htmlOutput.append("<p>The biggest total profit: Game number ").append(maxTotalProfitGame).append(" with ").append(maxTotalProfit).append(" points</p>");

        // Percentage of each strategy in the strong population
        htmlOutput.append("<h3>Percentage of Each Strategy in the Strong Population:</h3>");
        int totalStrongPopulation = strongPopulationStrategies.values().stream().mapToInt(Integer::intValue).sum();
        htmlOutput.append("<table border='1'><tr><th>Strategy</th><th>Percentage</th></tr>");
        for (Map.Entry<String, Integer> entry : strongPopulationStrategies.entrySet()) {
            double percentage = (entry.getValue() / (double) totalStrongPopulation) * 100;
            htmlOutput.append("<tr><td>").append(entry.getKey()).append("</td><td>").append(String.format("%.2f", percentage)).append("%</td></tr>");
        }
        htmlOutput.append("</table>");

        // Total profit of representatives who were a strong population from each strategy
        htmlOutput.append("<h3>Total Profit of Representatives in the Strong Population from Each Strategy:</h3>");
        htmlOutput.append("<table border='1'><tr><th>Strategy</th><th>Total Profit</th></tr>");
        for (Map.Entry<String, Integer> entry : strongPopulationProfits.entrySet()) {
            htmlOutput.append("<tr><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
        }
        htmlOutput.append("</table>");

        // Winners of each game
        htmlOutput.append("<h3>Winners of Each Game:</h3>");
        htmlOutput.append("<table border='1'><tr><th>Game</th><th>Strategy</th></tr>");
        for (int i = 0; i < gameWinners.size(); i++) {
            Agent winner = AgentsList.getAgent(gameWinners.get(i));
            htmlOutput.append("<tr><td>Game ").append(i + 1).append("</td><td>").append(getStrategyName(winner.getStrategy())).append("</td></tr>");
        }
        htmlOutput.append("</table>");

        // Agent with the most points across all games
        if (overallTopAgent != null) {
            htmlOutput.append("<h3>Agent with the Most Points Across All Games:</h3>");
            htmlOutput.append("<table border='1'><tr><th>Strategy</th><th>Payoff</th><th>ID</th></tr>");
            htmlOutput.append("<tr><td>").append(getStrategyName(overallTopAgent.getStrategy()))
                    .append("</td><td>").append(agentTotalPayoffs.get(overallTopAgent.getId())).append("</td><td>").append(overallTopAgent.getId()).append("</td></tr>");
            htmlOutput.append("</table>");
        }
    }

    private String getStrategyName(int strategy) {
        switch (strategy) {
            case 0:
                return "Tit For Tat";
            case 1:
                return "Threshold";
            case 2:
                return "Best Response";
            case 3:
                return "Honest";
            case 4:
                return "Evil";
            default:
                return "Unknown Strategy";
        }
    }
}
