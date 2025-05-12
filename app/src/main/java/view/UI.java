package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.CTLFormula;
import model.KripkeStructure;
import model.State;
import utils.Util;

public class UI extends JFrame {
    private final JTextField ctlFormula;
    private final JLabel modelTitle;
    private final JTextArea resultArea;
    private final JTextArea modelText;
    private final JComboBox<String> jComboBox;
    private final JFrame jFrame;

    private static KripkeStructure kripke;

    public UI() {
        jFrame = new JFrame("Enhanced CTL Model Checker");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(900, 750));  // More appropriate dimension
        jFrame.setLayout(new BorderLayout(10, 10));  // Using BorderLayout for better spacing

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton uploadButton = new JButton("Upload File");
        uploadButton.addActionListener(new UploadFileListener());
        filePanel.add(new JLabel("Upload Model Checker File:"));
        filePanel.add(uploadButton);

        JPanel modelPanel = new JPanel(new BorderLayout());
        modelTitle = new JLabel("Model");
        modelText = new JTextArea(10, 70);
        modelText.setEditable(false);
        JScrollPane modelScrollPane = new JScrollPane(modelText);
        modelPanel.add(modelTitle, BorderLayout.NORTH);
        modelPanel.add(modelScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        jComboBox = new JComboBox<>();
        ctlFormula = new JTextField(20);
        inputPanel.add(new JLabel("Select Model State:"));
        inputPanel.add(jComboBox);
        inputPanel.add(new JLabel("Enter CTL Formula:"));
        inputPanel.add(ctlFormula);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkButton = new JButton("Check Result");
        checkButton.addActionListener(new CheckActionListener());
        resultArea = new JTextArea(3, 70);
        resultArea.setEditable(false);
        resultPanel.add(checkButton);
        resultPanel.add(new JScrollPane(resultArea));

        contentPanel.add(filePanel);
        contentPanel.add(modelPanel);
        contentPanel.add(inputPanel);
        contentPanel.add(resultPanel);

        jFrame.setContentPane(contentPanel);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
    }

    private void ClearModel() {
        modelText.setText("");
        modelTitle.setText("Model");
        if(jComboBox.getSelectedIndex() != -1) {
            DefaultComboBoxModel theModel = (DefaultComboBoxModel) jComboBox.getModel();
            theModel.removeAllElements();
        }
    }

    class UploadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClearModel();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int Value  = fileChooser.showOpenDialog(jFrame);
            if(Value == JFileChooser.APPROVE_OPTION) {
                try {
                    new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                    //System.out.println("Selected File"+fileChooser.getSelectedFile());
                    File filePath = fileChooser.getSelectedFile();

                    try {
                        if(filePath == null) {
                            String message  = "Please upload a File!";
                            JOptionPane.showMessageDialog(new JFrame(), message, "Comment",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        assert filePath != null;
                       // System.out.println("PATH: "+ filePath.getAbsolutePath());
                        System.out.println("");
                        String file = Util.readFile((filePath.getAbsolutePath()));
                        kripke = new KripkeStructure(Util.cleanText(file));

                        ClearModel();
                        for(String s: kripke.getStates()) {
                            jComboBox.addItem(s);
                        }
                        String modelName = filePath.getAbsolutePath().substring(filePath.getAbsolutePath().lastIndexOf('M'));
                        modelTitle.setText(modelName);
                        modelText.setText(kripke.toString());
                    }catch(Exception kse) {
                        kse.printStackTrace();
                        JOptionPane.showMessageDialog(new JFrame(), kse.getMessage(), "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch(IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class CheckActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            resultArea.setText("");
            System.out.println("");
            System.out.println("Checking Result For Formula:  "+ctlFormula.getText());
            System.out.println("");
            try {
                if (kripke == null)
                {
                    throw new Exception("Please load Kripke model");
                }
                if(ctlFormula.getText().isEmpty()) {
                    throw new Exception("Please enter CTL formula!");
                }
                String originalExpression = ctlFormula.getText();
                String expression = originalExpression.replaceAll("\\s", "");
                System.out.println("");
                System.out.println("Model.State:  "+ Objects.requireNonNull(jComboBox.getSelectedItem()));
                System.out.println("");
                String checkedStateID = jComboBox.getSelectedItem().toString();

                State checkedState = new State(checkedStateID);

                CTLFormula ctlFormula = new CTLFormula(expression, checkedState, kripke);
                boolean isSatisfy = ctlFormula.IsSatisfy();
                String message = Util.GetMessage(isSatisfy, originalExpression, checkedStateID);
                resultArea.setText("");
                resultArea.append(message);
                System.out.println("Result:");
                System.out.println(message);
                System.out.println();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println();
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Dialog",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }


}