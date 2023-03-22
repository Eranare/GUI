import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;

public class ApplicantDemo extends JFrame{

    private JPanel panelMain;
    private JLabel name;
    private JTextField nameField;
    private JLabel timeSlot;
    private JLabel h;
    private JLabel m;
    private JFormattedTextField hourField;
    private JFormattedTextField minuteField;
    private JButton btn;
    private JTextPane displayMessage;
    private JTextPane displayApplicantsField;

    public ApplicantDemo() {
    btn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JOptionPane.showMessageDialog(btn,"HELLO");

            writeFile();




            // show message
        }
    });
}
    public static void writeFile(){
        //Check input
        JSONObject obj = new JSONObject();
        //Show message if failed (wrong input, other errors)
        try(FileWriter Data = new FileWriter("Data.JSON")) {
            Data.write(obj.toString(4));
            //Else Submit data to json?
            //clear input field


        } catch( IOException e1) {
            e1.printStackTrace();
            System.out.println("uhoh Something fucky went in the wucky");
        }
    }
    public static void main(String[] args) {

    ApplicantDemo h=new ApplicantDemo();
    h.setContentPane(h.panelMain);
    h.setTitle("ApplicantGUI");
    h.setBounds(500,200 , 700, 800);
    //h.setSize(400, 600);
    h.setVisible(true);
    checkIfFile();
    h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void checkIfFile(){
        if(exists(Path.of("Data.JSON"))){
            System.out.println("Data.JSON exists");
        }
        else {

            try {
                createFile(Path.of("Data.JSON"));
                JSONObject jo = new JSONObject();
                jo.put("start of","new Json");
                System.out.println(jo);
                JSONObject init = new JSONObject();
                try(FileWriter Data = new FileWriter("Data.JSON")) {
                    Data.write(jo.toString(4));}
                catch(IOException e1){
                    System.out.print(" something fucky went with the wucky");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class Applicants {


    }
}

