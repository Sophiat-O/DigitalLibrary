/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the login page which
    * displays the username and password field
    * subscribers can login our program here
    ------------------------------------------------------------------------
 */

package digiLib;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import LibraryInfo.Constants;
import LibraryInfo.Library;
import Medias.Media;
import Persons.Actor;
import Persons.BorrowDetails;
import Persons.Creators.Author;
import Persons.Creators.Director;
import Persons.Creators.Singer;
import Persons.Subscriber;

/* ------------------------------------------------------------------------- *
 * LoginGUI class
 * Attributes:
 *    emailField: subscriber enters email here
 *    passwordField: subscriber enters password here
 *    loginScreen: the display screen
 *    loginButton: the login button that triggers login action
 *    subscribers: the subscribe loaded from the subscriber file
 *    subscriberDetails: the logged in subscriber details
 *    borrowDetails : the data in borrow details
 *    email : a label displaying the text Email:
 *    password: a label displaying the text Password:
 */

public class LoginGUI {
    public static Subscriber subscriberDetails;
    private JTextField emailField;
    private JButton loginButton;
    private JPanel loginScreen;
    private JPasswordField passwordField;
    private JLabel email;
    private JLabel password;
    private static ArrayList<Subscriber> subscribers;
    public static ArrayList<BorrowDetails> borrowDetails;

    public LoginGUI(ArrayList<Subscriber> subscribers) {
        LoginGUI.subscribers = subscribers;


        //Login button action is performed here and data is validated

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String myPassword = String.valueOf(passwordField.getPassword());
                Subscriber results = Constants.correctPassword(emailField.getText().trim(),myPassword.trim(), LoginGUI.subscribers);
                if(results != null) {
                    subscriberDetails = results;
                    WelcomeGUI welcome = new WelcomeGUI(LoginGUI.subscriberDetails);
                    welcome.welcomePage();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Invalid Username/Password","Error", JOptionPane.ERROR_MESSAGE);
                }
                }
        });

    }


    public void loginPage() {

        JFrame frame = new JFrame("Login");
        frame.setContentPane(new LoginGUI(subscribers).loginScreen);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<Actor> actors = Actor.load(Constants.actorsFile);
        ArrayList<Director> directors = Director.load(Constants.directorsFile);
        ArrayList<Author> authors = Author.load(Constants.authorsFile);
        ArrayList<Singer> singers = Singer.load(Constants.singersFile);
        ArrayList<Media> medias = Media.load(Constants.mediasFile, singers, directors, authors, actors);
        borrowDetails = BorrowDetails.load(Constants.borrowFile, medias);
        subscribers = Subscriber.load(Constants.subscribersFile, borrowDetails, medias);
        Subscriber.loadNewAvailable(Constants.newAvailableFile, subscribers, medias);
        Library mylibrary = Library.getInstance();
        Library.load(Constants.qtyFile, subscribers, medias);
        frame.pack();
        frame.setVisible(true);
    }
}
