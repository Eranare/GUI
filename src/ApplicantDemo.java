import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            //Check input

            //Show message if failed (wrong input, other errors)

            //Else Submit data to json?
            // show message of success
        }
    });
}

    public static void main(String[] args) {
    ApplicantDemo h=new ApplicantDemo();
    h.setContentPane(h.panelMain);
    h.setTitle("ApplicantGUI");
    h.setBounds(800,800 , 500, 800);
    //h.setSize(400, 600);
    h.setVisible(true);
    h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

