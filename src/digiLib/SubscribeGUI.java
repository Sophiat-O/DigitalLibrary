/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the subscribe page which
    * displays the form new subscribers will fill to register to the digital library
    * subscribers can register to our program here
    ------------------------------------------------------------------------
 */

package digiLib;


import Persons.Subscriber;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/* ------------------------------------------------------------------------- *
 * SubscribeGUI class
 * Attributes:
 *    emailField: subscriber enters email here
 *    passwordField: subscriber enters password here
 *    subscribeScreen: the display screen
 *    subscribeButton: the subscribe button that triggers subscribe action
 *    email : a label displaying the text Email:
 *    password: a label displaying the text Password:
 */

public class SubscribeGUI {
    private JPanel subscribeScreen;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField email;
    private JTextField password;
    private JButton subscribeButton;
    private JPasswordField passwordField;
    private JTextField emailField;

    public SubscribeGUI() {
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String myPass = String.valueOf(passwordField.getPassword());
                myPass = myPass.trim();
                if(firstName.getText() != null && lastName.getText() != null && myPass.length() > 0 && emailField.getText() != null) {
                    Subscriber sub = new Subscriber(firstName.getText().trim(), lastName.getText().trim(), emailField.getText().trim(), myPass, "src/data/subscriber.csv");
                    LoginGUI welcomeSub = new LoginGUI(DigitalLibGUI.subscribers);
                    welcomeSub.loginPage();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Complete the form","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void subscribePage() {

        JFrame frame = new JFrame("Subscribe");
        frame.setContentPane(new SubscribeGUI().subscribeScreen);
        frame.pack();
        frame.setVisible(true);
    }
}
