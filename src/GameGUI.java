import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame {
    private JTextField agentsField;
    private JTextField networkDensityField;
    private JComboBox<String> gameComboBox;
    private JTextField roundsField;
    private JButton startButton;
    private JTextArea resultArea; // Text area to display results

    public GameGUI() {
        setTitle("Graphical Interface for Game");
        setSize(700, 500); // Increased size to accommodate results area
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLookAndFeel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2));
        mainPanel.setBackground(Color.WHITE);

        JLabel gameLabel = new JLabel("Choose Game:");
        gameLabel.setForeground(Color.BLUE);
        String[] games = {"Prisoner's Dilemma", "Battle of the Sexes"};
        gameComboBox = new JComboBox<>(games);

        JLabel agentsLabel = new JLabel("Number of Agents:");
        agentsField = new JTextField(10);

        JLabel networkDensityLabel = new JLabel("Network Density (between 0 and 1):");
        networkDensityField = new JTextField(10);

        JLabel roundsLabel = new JLabel("Number of Rounds:");
        roundsField = new JTextField(10);

        startButton = new JButton("START");
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        mainPanel.add(gameLabel);
        mainPanel.add(gameComboBox);
        mainPanel.add(agentsLabel);
        mainPanel.add(agentsField);
        mainPanel.add(networkDensityLabel);
        mainPanel.add(networkDensityField);
        mainPanel.add(roundsLabel);
        mainPanel.add(roundsField);
        mainPanel.add(startButton);

        add(mainPanel, BorderLayout.NORTH);

        // Add space between components
        add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);

        // Add description panel on the right side
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(Color.LIGHT_GRAY);
        descriptionPanel.setPreferredSize(new Dimension(200, getHeight()));
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        descriptionPanel.add(new JLabel("Game Description:"));
        descriptionPanel.add(new JLabel("Detailed explanation about the selected game will be here."));
        add(descriptionPanel, BorderLayout.EAST);

        // Add result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Start button action listener
        startButton.addActionListener(e -> {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            int numAgents = Integer.parseInt(agentsField.getText());
            double density = Double.parseDouble(networkDensityField.getText());
            int numRounds = Integer.parseInt(roundsField.getText());

            // Create and run the game
            Game game = new Game(selectedGame, (int) (density * 100), numAgents, numRounds, 1, 50);
            String gameResult = game.run();

            // Display the results in the result area
            resultArea.setText(gameResult);
        });

        setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GameGUI();
    }
}
