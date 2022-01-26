/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Digital Library home page which
    * displays a subscribe and login action in our system
    * subscribers can login or subscribe to our program here
    ------------------------------------------------------------------------
 */

package digiLib;

import LibraryInfo.Library;
import Medias.Media;
import Persons.Actor;
import Persons.BorrowDetails;
import Persons.Creators.Author;
import Persons.Creators.Director;
import Persons.Creators.Singer;
import Persons.Subscriber;

import LibraryInfo.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/* ------------------------------------------------------------------------- *
 * DigitalLibGUI class
 * Attributes:
 *    medias: data in the media file
 *    subscribers: data in subscriber file
 *    homeScreen: the display screen
 *    loginButton: the login button that triggers login action
 *    subscribeButton: the subscribe button that triggers subscribe action
 */

public class DigitalLibGUI extends javax.swing.JFrame {
    public static ArrayList<Media> medias;
    public static ArrayList<Subscriber> subscribers;
    private JPanel homeScreen;
    private JButton loginButton;
    private JButton subscribeButton;



    public DigitalLibGUI() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI login = new LoginGUI(DigitalLibGUI.subscribers);
                login.loginPage();
            }
        });
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubscribeGUI subscribe = new SubscribeGUI();
                subscribe.subscribePage();
            }
        });
    }



    public static void main(String args[]) {

        ArrayList<Actor> actors = Actor.load(Constants.actorsFile);
        ArrayList<Director> directors = Director.load(Constants.directorsFile);
        ArrayList<Author> authors = Author.load(Constants.authorsFile);
        ArrayList<Singer> singers = Singer.load(Constants.singersFile);
        medias = Media.load(Constants.mediasFile, singers, directors, authors, actors);
        ArrayList<BorrowDetails> borrowDetails = BorrowDetails.load(Constants.borrowFile, medias);
        subscribers = Subscriber.load(Constants.subscribersFile, borrowDetails, medias);
        Library mylibrary = Library.getInstance();
        Library.load(Constants.qtyFile, subscribers, medias);

        JFrame frame = new JFrame("Digital Library");
        frame.setContentPane(new DigitalLibGUI().homeScreen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    }



