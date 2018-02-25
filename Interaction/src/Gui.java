import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This is the main class being used for the Calculator assignment inherits from the JFrame class
 */
public class Gui extends JFrame {

    /**
     * This is the panel being used for the JFrame GUI
     */
    private JPanel picPanel = new JPanel();

    private JPanel entering = new JPanel();

    private JLabel enterHere = new JLabel("Please enter the phrase you would like converted.");

    public String phrase;

    public Speak speak = new Speak();

    /**
     * This is the text field at the top of the calculator and this is used for the append method.
     */
    private JTextField text = new JTextField();


    public Gui() throws IOException {
        super("The Simple Doctor");

        text.setEditable(true);

        BufferedImage doctor = ImageIO.read(new File("../TheSimpleDoctor/theDoctor.png"));

        JLabel picture = new JLabel(new ImageIcon(doctor));

//        phrase = text.getText();
        text.addActionListener(new ButtonListener());

        picPanel.setLayout(new FlowLayout());
        entering.setLayout(new GridLayout(2,1));

        picPanel.add(picture);

        entering.add(enterHere, BorderLayout.EAST);
        entering.add(text,BorderLayout.SOUTH);

        this.add(entering, BorderLayout.SOUTH);
        this.add(picPanel,BorderLayout.CENTER);
        setSize(500,500);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            phrase = text.getText().toLowerCase();
            System.out.println("The phrase is " + phrase);
            speak.setSayThis(phrase);
            speak.test();
        }

    }

}
