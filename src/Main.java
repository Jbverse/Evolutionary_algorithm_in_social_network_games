import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Configuration");
            frame.setSize(1500, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(230, 240, 255)); // Soft blue background
            frame.setLayout(new GridLayout(12, 2, 10, 10)); // Adjust grid layout to fit new components

            Font labelFont = new Font("Arial", Font.BOLD, 14);
            Color buttonColor = new Color(70, 130, 180); // Steel blue

            JLabel gameLabel = new JLabel("Select Game:");
            gameLabel.setFont(labelFont);
            JRadioButton prisonersDilemmaButton = new JRadioButton("Prisoner's Dilemma");
            JRadioButton battleOfSexesButton = new JRadioButton("Battle of Sexes");
            prisonersDilemmaButton.setSelected(true);
            ButtonGroup gameGroup = new ButtonGroup();
            gameGroup.add(prisonersDilemmaButton);
            gameGroup.add(battleOfSexesButton);
            frame.add(gameLabel);
            frame.add(prisonersDilemmaButton);
            frame.add(new JLabel());
            frame.add(battleOfSexesButton);

            JLabel densityLabel = new JLabel("Density (50%):");
            densityLabel.setFont(labelFont);
            frame.add(densityLabel);
            JSlider densitySlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
            densitySlider.setMajorTickSpacing(10);
            densitySlider.setPaintTicks(true);
            densitySlider.setPaintLabels(true);
            frame.add(densitySlider);
            densitySlider.addChangeListener(e -> densityLabel.setText("Density (" + densitySlider.getValue() + "%)"));

            JLabel agentsLabel = new JLabel("Number of Agents:");
            agentsLabel.setFont(labelFont);
            JTextField agentsField = new JTextField("4");
            frame.add(agentsLabel);
            frame.add(agentsField);

            JLabel roundsLabel = new JLabel("Number of Rounds:");
            roundsLabel.setFont(labelFont);
            JTextField roundsField = new JTextField("10");
            frame.add(roundsLabel);
            frame.add(roundsField);

            JLabel gamesLabel = new JLabel("Number of Games:");
            gamesLabel.setFont(labelFont);
            JTextField gamesField = new JTextField("1");
            frame.add(gamesLabel);
            frame.add(gamesField);

            JLabel strongPopLabel = new JLabel("Strong Population (50%):");
            strongPopLabel.setFont(labelFont);
            frame.add(strongPopLabel);
            JSlider strongPopSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            strongPopSlider.setMajorTickSpacing(10);
            strongPopSlider.setPaintTicks(true);
            strongPopSlider.setPaintLabels(true);
            frame.add(strongPopSlider);
            strongPopSlider.addChangeListener(e -> strongPopLabel.setText("Strong Population (" + strongPopSlider.getValue() + "%)"));

            JButton startButton = new JButton("Start Game");
            startButton.setBackground(buttonColor);
            startButton.setForeground(Color.WHITE);
            startButton.setFont(new Font("Arial", Font.BOLD, 16));
            frame.add(startButton);

            startButton.addActionListener(e -> {
                String selectedGame = prisonersDilemmaButton.isSelected() ? "Prisoner's Dilemma" : "Battle of Sexes";
                int density = densitySlider.getValue();
                int numAgents = Integer.parseInt(agentsField.getText());
                int numRounds = Integer.parseInt(roundsField.getText());
                int numGames = Integer.parseInt(gamesField.getText());
                int strongPop = strongPopSlider.getValue();

                Game game = new Game(selectedGame, density, numAgents, numRounds, numGames, strongPop);
                String gameResult = game.run();

                showResultsDialog(frame, gameResult);
            });

            frame.setVisible(true);
        });
    }

    private static void showResultsDialog(JFrame parent, String results) {
        JDialog resultsDialog = new JDialog(parent, "Game Results", true);
        resultsDialog.setSize(1500, 1000);
        resultsDialog.setLayout(new BorderLayout());

        JTextPane resultsPane = new JTextPane();
        resultsPane.setContentType("text/html");
        resultsPane.setText(results);
        resultsPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsPane);
        resultsDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            resultsDialog.dispose();
            System.exit(0); // Close the program
        });
        resultsDialog.add(closeButton, BorderLayout.SOUTH);

        resultsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // Close the program when the window is closed
            }
        });

        resultsDialog.setLocationRelativeTo(parent);
        resultsDialog.setVisible(true);
    }
}
