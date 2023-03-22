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
public ApplicantDemo() {
    btn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Submit data
            //Show message if failed (wrong input, other errors)
            //Else show message of success
        }
    });
}

    public static void main(String[] args) {
    ApplicantDemo h=new ApplicantDemo();
    h.setContentPane(h.panelMain);
    h.setTitle("ApplicantGUI");
    h.setBounds(600,200 , 400, 600);
    //h.setSize(400, 600);
    h.setVisible(true);
    h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
