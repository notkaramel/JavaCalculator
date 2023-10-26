import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * The Calculator Graphic User Interface (GUI)
 * An interactive application for user to use the calculator app.
 * <p>
 * The implementation of this file is influenced by multiple sources,
 * noticeably [the Standard Java
 * API](https://docs.oracle.com/javase/7/docs/api/),
 * forums such as Stack Overflow, Stack Exchange, Reddit, etc.,
 * online teaching websites, and YouTube tutorials.
 */

public class JavaCalGUI extends Frame implements ActionListener, KeyListener {
    // Declaring global variables/Objects
    JFrame frame;
    JTextField inLine;
    JTextField outLine;
    postFix currentCal;
    JSlider decimalSlider;
    JSlider sigfigSlider;
    int decimalPoint = 2; // initial value for decimalPoint
    int sigFig = 3; // initial value for sigFig
    boolean inDecMode = true; // this will be used for a toggle button

    // Main method
    public static void main(String[] args) {
        new JavaCalGUI();
    }

    // Constructor for the calculator
    public JavaCalGUI() {
        frame = new JFrame();
        inLine = new JTextField();
        outLine = new JTextField();
        currentCal = new postFix();

        /*---------------------Top Panel Tools---------------------*/
        // Make output line read-only.
        outLine.setEditable(false);
        outLine.setBackground(Color.white);

        // GridLayout(rows, cols, hgap, vgap). Note that I will use this format
        // throughout the entire program without commenting

        // Text Panel, where input is typed in to a text field
        JPanel TextPanel = new JPanel(new GridLayout(4, 1, 0, 6));
        TextPanel.setBorder(BorderFactory.createEmptyBorder(12, 36, 10, 36));

        // adding labels before each text field
        TextPanel.add(new JLabel("Input: "));
        TextPanel.add(inLine);
        TextPanel.add(new JLabel("Output: "));
        TextPanel.add(outLine);
        inLine.addKeyListener(this); // allow user to press RETURN/ENTER key to get the answer

        JPanel middlePanel = middlePanel();
        JPanel NumberPanel = NumberPanel();

        // FEATURE: Trigonometry tools
        JPanel TrigPanel = TrigPanel();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(TrigPanel);
        bottomPanel.add(NumberPanel);
        /*---------------------Assembling All Panels---------------------*/

        // Add panels (Text, Util, Number) to the frame
        frame.add(TextPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setTitle("JCalcGUI"); // title of the calculator
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit program as user close it.
        frame.pack(); // Resize the frame to fit every panel it contains on the GUI
        frame.setLocationRelativeTo(null); // set frame to initialize at the center of the screen
        frame.setVisible(true); // MAKE THE MAGIC APPEAR!!
    }
    
    // Number Panel for the Calculator
    private JPanel NumberPanel() {
        JPanel NumberPanel = new JPanel();
        NumberPanel.setLayout(new GridLayout(6, 4, 10, 10));
        NumberPanel.setBorder(BorderFactory.createEmptyBorder(10, 36, 20, 36));

        // Create all buttons and add them to the panel
        String[] signs = {  "7", "8", "9",  "+",
                            "4", "5", "6",  "-",
                            "1", "2", "3",  "x", 
                            "0", ".", "^",  "/", 
                            "(", ")", "Ans", "="};
        
        Color button_Color = new Color(185, 185, 185); // set the color of the buttons
        for(String sign : signs) {
            JButton button = new JButton(sign);
            button.setBorderPainted(false);
            button.setBackground(button_Color);
            button.addActionListener(this);
            NumberPanel.add(button);
        }

        return NumberPanel;
    }

    // Feature: Trigonometry Panel
    private JPanel TrigPanel() {
        JPanel TrigPanel = new JPanel();
        TrigPanel.setLayout(new GridLayout(2, 3, 10, 10));
        TrigPanel.setBorder(BorderFactory.createEmptyBorder(16, 36, 0, 36));

        // Create trigonometry buttons and add them to the panel
        String[] trigonometry = { "sin", "cos", "tan",
                                "sec", "csc", "cot" };
        Color button_Color = new Color(185, 185, 185); // set the color of the buttons
        for(String sign : trigonometry) {
            JButton button = new JButton(sign);
            button.setBorderPainted(false);
            button.setBackground(button_Color);
            button.addActionListener(this);
            TrigPanel.add(button);
        }
        return TrigPanel;
    }

    private JPanel middlePanel() {
        /*---------------------Middle Panel Tools---------------------*/
        /*
         * middlePanel is going to contain C button, a toggle button between
         * decimal point mode & sig-figs mode, and a SliderPanel that switches
         * between the modes respectively to the toggle mode.
         *
         * - UtilPanel: GridLayout: "C" button & the toggle button
         * - SliderPanel: GridLayout, hold a slot for the two panels to replace each
         * other
         * depends on the mode of the toggle button
         * + sigfigPanel: Sig-figs label & sliders
         * + decimalPanel: Decimal-point label & sliders
         */

        JPanel middlePanel = new JPanel(); // the middlePanel, as described above
        middlePanel.setBorder(BorderFactory.createEmptyBorder(6, 36, 0, 36));
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));

        // decimalPanel, with a label and a precision slider
        JPanel decimalPanel = new JPanel(new GridLayout(2, 1));
        decimalPanel.add(new JLabel("Decimal Point(s) Slider"));
        decimalPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 36, 0));

        decimalSlider = new JSlider(0, 10, decimalPoint); // Decimal-point slider, from 0 to 10
        decimalSlider.setMajorTickSpacing(1);
        decimalSlider.setPaintTicks(true); // show ticks on the slider
        decimalSlider.setPaintLabels(true); // show numbers alongside the ticks
        // addChangeListener to Slider to update the current output line
        decimalSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ed) {
                decimalPoint = ((JSlider) ed.getSource()).getValue();
                if (!inLine.getText().isEmpty())
                    calculatePostEval();
            }
        });

        decimalPanel.add(decimalSlider);

        // sigfigPanel, with a label and a precision slider
        JPanel sigfigPanel = new JPanel(new GridLayout(2, 1));
        sigfigPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 36, 0));

        sigfigSlider = new JSlider(1, 7, sigFig); // Significant figures slider, from 1 to 7
        sigfigSlider.setMajorTickSpacing(1);
        sigfigSlider.setPaintTicks(true); // show ticks on the slider
        sigfigSlider.setPaintLabels(true); // show numbers alongside the ticks
        
        // addChangeListener to Slider to update the current output line
        sigfigSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent es) {
                sigFig = ((JSlider) es.getSource()).getValue();
                if (!inLine.getText().isEmpty())
                    calculatePostEval();
            }
        });

        sigfigPanel.add(new JLabel("Significant Figures Slider"));
        sigfigPanel.add(sigfigSlider);

        JPanel SliderPanel = new JPanel();
        SliderPanel.add(decimalPanel); // default initial panel

        /*--- UtilPanel with the two buttons ---*/
        JPanel UtilPanel = new JPanel(new GridLayout(2, 1, 0, 10));

        JButton C = new JButton("C"); // The C button
        C.setBorderPainted(false);
        C.setBackground(new Color(185, 185, 185));
        C.addActionListener(this);

        JToggleButton DecOrSigfig = new JToggleButton("Decimal Mode");
        DecOrSigfig.setBorderPainted(false);
        DecOrSigfig.setBackground(new Color(185, 185, 185));
        DecOrSigfig.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (DecOrSigfig.isSelected()) {
                    inDecMode = false;
                    DecOrSigfig.setText("Sig-figs Mode");
                    SliderPanel.removeAll();
                    SliderPanel.add(sigfigPanel);
                    calculatePostEval();
                } else {
                    inDecMode = true;
                    DecOrSigfig.setText("Decimal Mode");
                    SliderPanel.removeAll();
                    SliderPanel.add(decimalPanel);
                    calculatePostEval();
                }
            }
        });

        UtilPanel.add(C); // Add C button to UtilPanel
        UtilPanel.add(DecOrSigfig); // add DecOrSigfig button to UtilPanel

        middlePanel.add(UtilPanel);
        middlePanel.add(SliderPanel);
        return middlePanel;
    }

    

    /**
     * Helper method that takes in the input line
     * and prints out on the output line the answer
     * to that equation. It will try to catch any errors
     * which the user made on the input line.
     */
    private void calculatePostEval() {
        try // This is to prevent cases where there is an exception or the input is invalid
        {
            if (inLine.getText().equals("")) {
                return;
            }
            String input = inLine.getText();
            double result = currentCal.PostEval(currentCal.In2Post(currentCal.parse(input)));
            if (inDecMode)
                outLine.setText(decimalRound(result));
            else
                outLine.setText(sigfigRound(result));

        } catch (Exception ex) {
            outLine.setText("Invalid input!");
        }
    }

    /**
     * The method indicates actions to be performed
     * when a button is clicked.
     * 
     * @param e the ActionEvent that when being called getActionCommand,
     *          it returns a string
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "C":
                inLine.setText("");
                outLine.setText("");
                break;
            case "=":
                calculatePostEval();
                break;
            case "sin", "cos", "tan", "cot", "sec", "csc":
                String currentLine = inLine.getText();
                currentLine += cmd + "(";
                inLine.setText(currentLine);
                break;
            case "Ans": // put the previous answer from the output line to the input line
                try {
                    inLine.setText(outLine.getText());
                    outLine.setText("");
                    // The edge case is that, when the number is in sig-fig form (e.g., ##E+#),
                    // the parse input does not recognize it and throw an exception
                    break;
                } catch (Exception ex) {
                    break;
                }
            default:
                inLine.setText(inLine.getText() + cmd);
                break;
        }
    }

    /**
     * Calculate the current input if the Return key is hit
     * 
     * @param e the KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) // if RETURN key is hit
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inLine.getText().equals(""))
                outLine.setText("Input empty!");
            else
                calculatePostEval();
        }
    }

    /**
     * Doesn't have any events assign to it.
     * 
     * @param e the KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * It doesn't have any events assign to it.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Round the double value to the decimal place indicates on the slider.
     * The implementation of this method
     * is taken from this question on
     * [StackOverflow](https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places)
     *
     * @param value the value that needs to be rounded
     * @return the rounded value
     */
    private String decimalRound(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPoint, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**
     * Round the number to the equivalent significant figure settings
     * The implementation of this method is taken from
     * [StackOverflow](https://stackoverflow.com/questions/7548841/round-a-double-to-3-significant-figures)
     * 
     * @param value the value that needs to be rounded
     * @return the rounded value
     */
    private String sigfigRound(double value) {
        BigDecimal bd = new BigDecimal(value, new MathContext(sigFig));
        return bd.toString();
    }

    
}
