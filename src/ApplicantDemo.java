import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import java.nio.charset.StandardCharsets;
import static java.nio.file.Files.*;

import java.io.FileWriter;
import java.io.IOException;

public class ApplicantDemo extends JFrame {

    private JPanel panelMain;
    private JLabel name;
    private JFormattedTextField nameField;
    private JLabel timeSlot;
    private JLabel h;
    private JLabel m;
    private JFormattedTextField hourField;
    private JFormattedTextField minuteField;
    private JButton btn;
    private JTextPane displayMessage;
    private JTextPane displayApplicantsField;

    //--ChatGPT solution for limiting the input fields, took me several hours on my own untill i asked him and even that took another 3 hours to get it working
    // Define a DocumentFilter to only allow alphabets
    private class AlphabeticalFilter extends DocumentFilter {
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("^[a-zA-Z]*$")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("^[a-zA-Z]*$")) {
                super.replace(fb, offset, length, string, attr);
            }
        }
    }

    // Define a DocumentFilter to only allow digits
    private class NumericFilter extends DocumentFilter {
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) {
                super.replace(fb, offset, length, string, attr);
            }
        }
    }


    public ApplicantDemo() {
        super("Applicant Demo");
//---------Also Chats solution
// Add the DocumentFilter to the hourField and minuteField
        ((AbstractDocument) hourField.getDocument()).setDocumentFilter(new NumericFilter());
        ((AbstractDocument) minuteField.getDocument()).setDocumentFilter(new NumericFilter());

        //Name field mask

        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new AlphabeticalFilter());

        // Create the hour formatter
        NumberFormat hourFormat = NumberFormat.getInstance();
        NumberFormatter hourFormatter = new NumberFormatter(hourFormat);
        hourFormatter.setValueClass(Integer.class);
        hourFormatter.setMinimum(0);
        hourFormatter.setMaximum(23);
        // Set the hourField's formatter factory to use the hour formatter
        hourField.setFormatterFactory(new DefaultFormatterFactory(hourFormatter));
        // Create the minute field with a NumberFormatter
        NumberFormat minuteFormat = NumberFormat.getInstance();
        NumberFormatter minuteFormatter = new NumberFormatter(minuteFormat);
        minuteFormatter.setValueClass(Integer.class);
        minuteFormatter.setMinimum(0);
        minuteFormatter.setMaximum(59);
        // Set the minuteField's formatter factory to use the minute formatter
        minuteField.setFormatterFactory(new DefaultFormatterFactory(minuteFormatter));

//-------------End of chats solution

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//My own way of making sure the input is correct, if name holds a number, or if the time fields hold anything other than a number,
// a dialogue box will pop up with the error message
                //Read input
                String name = nameField.getText();
                //System.out.print("is this empty?"+ nameField.getText());
                String hour = hourField.getText();
                String min =  minuteField.getText();
                //System.out.print("Name: "+ name);
                //compare name as string
                if (!name.matches("[a-zA-Z]+")) {
                    //Error popup and break;
                    JOptionPane.showMessageDialog(btn, "Invalid name ");
                    //if string != contains a name return;
                    return;
                } else if (!hour.matches("\\d+")) {
                    //Error popup
                    JOptionPane.showMessageDialog(btn, "invalid hours ");
                    //if string != contains just numbers return;
                    return;
                } else if (!min.matches("\\d+")) {
                    JOptionPane.showMessageDialog(btn, "invalid minutes ");
                    //if string != contains just 2 numbers return;
                    return;
                } else {
                    JOptionPane.showMessageDialog(btn, "HELLO " + nameField.getText());
                    writeFile(nameField.getText(), hourField.getText(), minuteField.getText());
                    loadApplicantsFromFile();
                }

            }
            // show message

        });
    }

    public static void writeFile(String name, String hour, String minute) {
        //Check input
        try {
            int hours = Integer.parseInt(hour);
            int mins = Integer.parseInt(minute);

            String time = hours + ":" + mins;
            JSONObject values = new JSONObject();
            //Fix structuring, look at php project
            values.put("name ", name);
            values.put("time ", time);

            File file = new File("Data.JSON");
            boolean fileExists = file.exists();

            try (FileWriter data = new FileWriter(file, true)) {
                if (fileExists) {
                    data.write(",\n");
                }
                data.write(values.toString(4));
                data.flush();

                JOptionPane.showMessageDialog(null, "Data saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving data.");
            }
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Invalid input format.");
        }
    }

    public static void main(String[] args) {
        ApplicantDemo h = new ApplicantDemo();
        h.setContentPane(h.panelMain);
        h.setTitle("ApplicantGUI");
        h.setBounds(500, 200, 700, 800);
        //h.setSize(400, 600);
        h.setVisible(true);
        checkIfFile();

        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadApplicantsFromFile() {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get("Data.JSON")), StandardCharsets.UTF_8);

            // Create a JSONArray from the string
            JSONArray jsonArray = new JSONArray(jsonStr);

            // Create a new JSONObject with pretty-printing enabled
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("applicants", jsonArray);

            String prettyJsonStr = jsonObject.toString(4); // The number 4 is the indentation level

            // Display the pretty-printed JSON in the JTextPane
            displayApplicantsField.setText(prettyJsonStr);

        } catch (IOException | JSONException e) {
            e.printStackTrace();

        }
    }
    public static void checkIfFile() {
        if (exists(Path.of("Data.JSON"))) {
            System.out.println("Data.JSON exists");

        } else {
            try {
                createFile(Path.of("Data.JSON"));
                JSONObject jo = new JSONObject();
                jo.put("start of", "new Json");
                System.out.println(jo);
                JSONObject init = new JSONObject();
                try (FileWriter Data = new FileWriter("Data.JSON")) {
                    Data.write(jo.toString(4));
                } catch (IOException e1) {
                    System.out.print(" something fucky went with the wucky");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}






