import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;

import org.json.*;

import java.nio.charset.StandardCharsets;
import static java.nio.file.Files.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JSpinner minuteSpinner;
    private JSpinner hourSpinner;
    private JButton Load;
    private JButton refresh;
    private JButton delete;
    private JButton dummyData;

    private static final String DEFAULT_MESSAGE = "Please insert name and time";

    private void createUIComponents() {
        SpinnerNumberModel hourSpinnerModel = new SpinnerNumberModel(0, 0, 23, 1) {
            @Override
            public Object getNextValue() {
                Object value = super.getNextValue();
                if (value == null) {
                    value = getMinimum();
                }
                return value;
            }

            @Override
            public Object getPreviousValue() {
                Object value = super.getPreviousValue();
                if (value == null) {
                    value = getMaximum();
                }
                return value;
            }
        };
        hourSpinner = new JSpinner(hourSpinnerModel);

        SpinnerNumberModel minuteSpinnerModel = new SpinnerNumberModel(0, 0, 59, 1) {
            @Override
            public Object getNextValue() {
                Object value = super.getNextValue();
                if (value == null) {
                    value = getMinimum();
                }
                return value;
            }

            @Override
            public Object getPreviousValue() {
                Object value = super.getPreviousValue();
                if (value == null) {
                    value = getMaximum();
                }
                return value;
            }
        };
        minuteSpinner = new JSpinner(minuteSpinnerModel);
    }



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
        checkIfFile();
        loadApplicantsFromFile();
//---------Also Chats solution
// Add the DocumentFilter to the hourField and minuteField

        //Name field mask

        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new AlphabeticalFilter());



//-------------End of chats solution

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//My own way of making sure the input is correct, if name holds a number, or if the time fields hold anything other than a number,
// a dialogue box will pop up with the error message
                //Read input
                String name = nameField.getText();
                //System.out.print("is this empty?"+ nameField.getText());
                int hour = (int) hourSpinner.getValue();
                int minute = (int) minuteSpinner.getValue();
               // hourField.setText(Integer.toString(hour));
               // minuteField.setText(Integer.toString(minute));
                //System.out.print("Name: "+ name);
                //compare name as string
                if (!name.matches("[a-zA-Z]+")) {
                    //Error popup and break;
                    displayMessage("Invalid name");
                    //if string != contains a name return;
                    return;
                } else {
                    displayMessage("Saving Data");

                    writeFile(nameField.getText(), hourSpinner.getValue().toString(),  minuteSpinner.getValue().toString());
                        //Added a wait time for shits and giggles, to make the app appear to be busy

                    try {
                        Thread.sleep(2000); // Sleep for 4 seconds
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    displayMessage("Data saved!");
                        nameField.setText("");
                        hourSpinner.setValue(0);
                        minuteSpinner.setValue(0);

                    loadApplicantsFromFile();
                }

            }
            // show message

        });
        displayMessage.setText(DEFAULT_MESSAGE);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ask for confirmation

                //Save file in back up file named (BackupCurrentDateTime)
                //Delete content of main file
                deleteFile();
                checkIfFile();
                displayMessage("Data deleted(Not actually gone)");
                loadApplicantsFromFile();
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Reload list
                displayMessage("Refreshing");
                checkIfFile();
                loadApplicantsFromFile();
            }
        });

        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Pop up window asking for which file to revert to
                fileSelection();
                displayMessage("File Loaded");
                loadApplicantsFromFile();
                //Will backup current file unless name exists already.

            }
        });
        dummyData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loadDummyData();
            }
        });
    }
    private void displayMessage(String message) {
        // Set the text of the top text pane to the message
        displayMessage.setText(message);

        // Clear the text after a certain amount of time
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                displayMessage.setText(DEFAULT_MESSAGE);
            }
        });
        timer.setRepeats(false);
        timer.start();

    }

    public static void writeFile(String name, String hour, String minute) {
        //Check if file exists
        checkIfFile();
        //Check input
        try {
            int hours = Integer.parseInt(hour);
            int mins = Integer.parseInt(minute);

            String time = hours + ":" + mins;
            JSONObject values = new JSONObject();
            //Fix structuring, look at php project
            values.put("name ", name);
            values.put("time ", time);

            // Read existing JSON data from file
            JSONArray jsonArray;
            try (FileReader fileReader = new FileReader("data.json")) {
                jsonArray = new JSONArray(new JSONTokener(fileReader));
            } catch (IOException e) {
                jsonArray = new JSONArray();
            }

            // Add new object to JSON array
            jsonArray.put(values);

            // Write updated JSON data back to file
            try (FileWriter fileWriter = new FileWriter("data.json")) {
                fileWriter.write(jsonArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving data.");
                return;
            }

           // JOptionPane.showMessageDialog(null, "Data saved successfully!");
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Invalid input format.");
        }
    }

    public static void deleteFile() {
        //Check input
        try {


            // Read existing JSON data from file
            JSONArray jsonArray;
            try (FileReader fileReader = new FileReader("data.json")) {
                jsonArray = new JSONArray(new JSONTokener(fileReader));
            } catch (IOException e) {
                jsonArray = new JSONArray();
            }

            String currentDateTime = String.valueOf(new java.sql.Timestamp(System.currentTimeMillis()).getTime());;
            //Create backup file
            createFile(Path.of("Backup"+ currentDateTime+ ".JSON"));

            //Write JSON data back to backup file
            try (FileWriter fileWriter = new FileWriter("Backup"+ currentDateTime+ ".JSON")) {
                fileWriter.write(jsonArray.toString());
                //Delete old file
                File oldData = new File(("data.json"));
                oldData.delete();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ow boy, Good luck figuring out what happened there.");
                return;
            }

            // JOptionPane.showMessageDialog(null, "Data saved successfully!");
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Something went wrong try again another day.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ow I hope that data was not valuable");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ApplicantDemo h = new ApplicantDemo();
        h.setContentPane(h.panelMain);
        h.setTitle("ApplicantGUI");
        h.setBounds(500, 200, 800, 600);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadApplicantsFromFile() {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get("Data.JSON")), StandardCharsets.UTF_8);

            // Create a JSONArray from the string
            JSONArray jsonArray = new JSONArray(jsonStr);

            StringBuilder formattedData = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject applicantObj = jsonArray.getJSONObject(i);
                String name = applicantObj.getString("name ");
                String timeString = applicantObj.getString("time ");

                // Formatting time
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date timeDate = dateFormat.parse(timeString);
                String time = dateFormat.format(timeDate);

                // Formatting
                formattedData.append("Applicant name: ").append(name).append(", timeslot: ").append(time).append("\n");
            }

            // Display the formatted data in the JTextPane
            displayApplicantsField.setText(formattedData.toString());

        } catch (IOException | JSONException | ParseException e) {
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
                jo.put("[", "]");
                //System.out.println(jo);
                JSONObject init = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try (FileWriter Data = new FileWriter("Data.JSON")) {

                    Data.write(jsonArray.toString());
                } catch (IOException e1) {
                    System.out.print(" something fucky went with the wucky");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void fileSelection() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON","JSON");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./"));
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            //Now to swap selectedFile with currentFile
            loadFile(selectedFile);
        }

    }
    public static void loadFile(File selectedFile) {
        //Check if file exists
        checkIfFile();
        //Check input
        try {



            JSONObject values = new JSONObject();
            //Fix structuring, look at php project

            // Read existing JSON data from file
            JSONArray jsonArray;
            try (FileReader fileReader = new FileReader(selectedFile)) {
                jsonArray = new JSONArray(new JSONTokener(fileReader));
            } catch (IOException e) {
                jsonArray = new JSONArray();
            }



            // Write Loaded File to Active Data.JSON
            try (FileWriter fileWriter = new FileWriter("data.json")) {
                fileWriter.write(jsonArray.toString());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error loading data.");
                return;
            }

            // JOptionPane.showMessageDialog(null, "Data saved successfully!");
        } catch (NumberFormatException | JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Invalid input format.");
        }

    }
    private void loadDummyData() {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get("dummyData.JSON")), StandardCharsets.UTF_8);

            // Create a JSONArray from the string
            JSONArray jsonArray = new JSONArray(jsonStr);

            StringBuilder formattedData = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject applicantObj = jsonArray.getJSONObject(i);
                String name = applicantObj.getString("name ");
                String timeString = applicantObj.getString("time ");

                // Formatting time
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date timeDate = dateFormat.parse(timeString);
                String time = dateFormat.format(timeDate);

                // Formatting
                formattedData.append("Applicant name: ").append(name).append(", timeslot: ").append(time).append("\n");
            }

            // Display the formatted data in the JTextPane
            displayApplicantsField.setText(formattedData.toString());
            displayMessage("Showing Dummy Data");
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}






