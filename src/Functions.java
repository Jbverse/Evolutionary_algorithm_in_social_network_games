import java.util.Random;

public class Functions {

    public static boolean[][] neighborhoodsTable(String game, int size, double percent) {
        System.out.println("PRERER  " + percent  );
        boolean[][] table = new boolean[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i!=j
                    && percent >= ( random.nextInt(100) + 1)){
                    table[i][j] = true;
                    table[j][i] = true;
                }
                if (game.equals("Battle of Sexes") &&
                        AgentsList.getAgent(i).getSex() == AgentsList.getAgent(j).getSex()) {
                    table[i][j] = false;
                    table[j][i] = false;
                }
            }
        }
        return table;
    }

    public static int[][] gainMatrix(int sex) {
        int[][] gainMatrix = new int[2][2];

        switch (sex) {
            case 0: // Female
                gainMatrix[0][0] = 2;
                gainMatrix[0][1] = 0;
                gainMatrix[1][0] = 0;
                gainMatrix[1][1] = 1;
                break;
            case 1: // Male
                gainMatrix[0][0] = 1;
                gainMatrix[0][1] = 0;
                gainMatrix[1][0] = 0;
                gainMatrix[1][1] = 2;
                break;
            case -1: // Prisoner
                gainMatrix[0][0] = 5;
                gainMatrix[0][1] = 10;
                gainMatrix[1][0] = 0;
                gainMatrix[1][1] = 8;
                break;
            default:
                System.out.println("Wrong parameter");
                break;
        }
        return gainMatrix;
    }
}
