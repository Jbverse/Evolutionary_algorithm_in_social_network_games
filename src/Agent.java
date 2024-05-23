import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private int id;
    private String game;
    private int state;
    private int strategy;
    private int sex; // 0=female   1=male   -1=prisoner
    private int payoff = 0;

    private Strategies strategies = new Strategies();
    private ArrayList<Integer> opponents = new ArrayList<>();

    private Random rand = new Random();

    public Agent(int id, String game, int strategy) {
        this.id = id;
        this.game = game;
        if (strategy == -1) this.strategy = rand.nextInt(5);
        else this.strategy = strategy;
        if (game.equals("Prisoner's Dilemma")) sex = -1;
        else if (game.equals("Battle of Sexes")) sex = rand.nextInt(2);
        else System.out.println("DBUG( wrong Game )DBUG");
        setFirstChoose();
    }

    public ArrayList<Integer> getOpponents() {
        return opponents;
    }

    public int getState() {
        return state;
    }

    public String getStateString() {
        if (game.equals("Prisoner's Dilemma")) {
            if (state == 0) {
                return "betray";
            } else {
                return "coop";
            }
        } else if (game.equals("Battle of Sexes")) {
            if (state == 0) {
                return "ballet";
            } else {
                return "football";
            }
        } else {
            return "";
        }
    }

    public int getId() {
        return id;
    }

    public int getPayoff() {
        return payoff;
    }

    public int getSex() {
        return sex;
    }

    public String getSexString() {
        if (sex == 0) {
            return "girl";
        } else {
            return "guy";
        }
    }

    public void setOpponents(ArrayList<Integer> opponents) {
        this.opponents = opponents;
    }

    public void updatePayoff(int a) {
        payoff += a;
    }

    public void initAgent(int id) {
        this.id = id;
        payoff = 0;
    }

    public void useStrategy() {
        int choose;
        switch (strategy) {
            case 0 -> choose = strategies.titForTat(opponents, sex);
            case 1 ->
                // 70 אחוז מגודל השכנים (מעוגל למספר שלם)
                    choose = strategies.thresholdStrategy((int) Math.round(opponents.size() * 0.7), opponents, sex);
            case 2 -> choose = strategies.bestResStrategy(opponents, sex);
            case 3 -> choose = strategies.honestStrategy(opponents, sex);
            case 4 -> choose = strategies.evilStrategy(sex);
            default -> {
                System.out.println("Invalid type of strategy!");
                choose = 0;
            }
        }
        if (choose > 1) System.out.println("ERRROR -> inCase: " + strategy + "  == " + choose);
        state = choose;
    }

    public void setFirstChoose() {
        int choose;
        switch (strategy) {
            // מידה כנגד מידה, אם אסיר - תשתף פעולה. אם מינים - תגריל
            case 0 -> choose = sex == -1 ? 1 : rand.nextInt(2);
            // טרשהולד, אם אסיר - תשתף פעולה. אם מינים - תגריל
            case 1 -> choose = sex == -1 ? 1 : rand.nextInt(2);
            // בסט רספונז, תגריל
            case 2 -> choose = rand.nextInt(2);
            // צדיק, אם אסיר - תשתף פעולה. אם מינים - תבחר במין השני
            case 3 -> choose = sex == -1 ? 1 : 1 - sex;
            // רשע, אם אסיר - תדפוק. אם מינים - תבחר במין שלך
            case 4 -> choose = sex == -1 ? 0 : sex;
            default -> {
                System.out.println("Invalid type of strategy!");
                choose = 0;
            }
        }
        if (choose > 1) System.out.println("ERRROR -> inCase: " + strategy + "  == " + choose);
        state = choose;
    }

    public int getStrategy() {
        return strategy;
    }

    public String getStrategyName() {
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

