import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class FifteenPuzzle extends JFrame {
    private JButton[] buttons;
    private final int SIZE = 4;
    private int emptyIndex = SIZE * SIZE - 1;

    public FifteenPuzzle() {
        setTitle("15-spel");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Vi skapar en JPanel och sätter dess layout. Arrayen buttons skapar utrymme för 16 knappar.
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(SIZE, SIZE));
        buttons = new JButton[SIZE * SIZE]; // Initierar arrayen med 16 knappar

        createButtons();
        shuffleButtons();

        // Lägg till varje knapp från arrayen till panelen
        for (JButton button : buttons) {
            panel.add(button);
        }

        JButton newGameButton = new JButton("Nytt spel");
        newGameButton.addActionListener(e -> shuffleButtons());

        add(panel, BorderLayout.CENTER);
        add(newGameButton, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void createButtons() {
        for (int i = 0; i < SIZE * SIZE - 1; i++) { // Loop för att skapa knappar med siffrorna 1-15
            buttons[i] = new JButton(String.valueOf(i + 1));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].addActionListener(new ButtonClickListener());
        }

        // Skapar den tomma knappen
        buttons[emptyIndex] = new JButton(" ");
        buttons[emptyIndex].setFont(new Font("Arial", Font.BOLD, 20));
        buttons[emptyIndex].setEnabled(false);
        buttons[emptyIndex].addActionListener(new ButtonClickListener());
    }


    private void shuffleButtons() {
        //Vi skapar en ArrayList med siffrorna 1 till 15 och en tom plats. Collections.shuffle() blandar listan slumpmässigt.
        ArrayList<String> values = new ArrayList<>();
        for (int i = 1; i < SIZE * SIZE; i++) {
            values.add(String.valueOf(i)); // siffror som strängar
        }
        values.add(" "); // Lägg till tomma knappen
        Collections.shuffle(values);


        //Varje knapp får texten från listan. Vi uppdaterar emptyIndex för att reflektera var den tomma knappen är.
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(values.get(i));
            buttons[i].setEnabled(!values.get(i).equals(" "));
            if (values.get(i).equals(" ")) {
                emptyIndex = i;
            }
        }
    }


    private boolean isSolved() {
        for (int i = 0; i < buttons.length - 1; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        return true;
    }


    //När en knapp trycks, identifierar vi var den är i arrayen.
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedIndex = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == clickedButton) {
                    clickedIndex = i;
                    break;
                }
            }
            //Flytta knappen om den är intill den tomma platsen: byter vi text och aktiverar/avaktiverar knapparna så att tomrummet flyttas.
            if (isClose(clickedIndex, emptyIndex)) {
                buttons[emptyIndex].setText(clickedButton.getText());
                buttons[emptyIndex].setEnabled(true);
                clickedButton.setText(" ");
                clickedButton.setEnabled(false);
                emptyIndex = clickedIndex;

                if (isSolved()) {
                    JOptionPane.showMessageDialog(null, "Grattis, du vann!");
                }
            }
        }
    }

    // Metod för att kontrollera om två index är intill varandra i rutnätet för att se ifall den kan flytta till den tomma rutan
    private boolean isClose(int index1, int index2) {
        int row1 = index1 / SIZE, col1 = index1 % SIZE;
        int row2 = index2 / SIZE, col2 = index2 % SIZE;
        return (Math.abs(row1 - row2) + Math.abs(col1 - col2)) == 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FifteenPuzzle::new);
    }
}