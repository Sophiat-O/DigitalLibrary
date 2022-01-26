/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the welcome page which
    * displays the screen where logged-in user can perform different actions
    * like browse the different catalogue, view wish list , view their borrow list e.t.c
    ------------------------------------------------------------------------
 */

package digiLib;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import LibraryInfo.UsersGraph;
import LibraryInfo.MediaQty;
import LibraryInfo.Constants;

import Medias.Media;

import Persons.BorrowDetails;
import Persons.Subscriber;

/* ------------------------------------------------------------------------- *
 * WelcomeGUI class
 * Attributes:
 *    welcomeScreen: the display screen
 *    userAction: a ComboBox that present different actions the user might want to do in the library
 *    okButton: the action button for the user action ComboBox
 *    selectMedia: for the action browse catalogue, subscriber is present with the different catalogues with this ComboBox
 *    continueButton : action button for the selectMedia ComboBox
 *    userName: a label displaying the logged-in username
 *    recOptions: a ComboBox to present the different recommendation mode available to the subscriber
 *    recButton: action button performed for the recOption ComboBox
 *    fastForward: button to stimulate move the digital library time forward
 *    localTime: displays the local time of the library, it gets updated is fastForward button is used.
 *    userImage: displays logged-in username and a user icon
 */



public class WelcomeGUI {
    private static Subscriber subscriberDetails;
    private static ArrayList<BorrowDetails> borrowDetails;
    private static MediaQty mediaQty;
    private JPanel welcomeScreen;
    private JComboBox userAction;
    private JButton okButton;
    private JComboBox selectMedia;
    private JButton continueButton;
    private JLabel userName;
    private JComboBox recOptions;
    private JButton recButton;
    private JButton fastForward;
    private JLabel localTime;
    private static UsersGraph userRec;


    public WelcomeGUI(Subscriber subscriberDetails) {
        WelcomeGUI.subscriberDetails = subscriberDetails;
        WelcomeGUI.borrowDetails = borrowDetails;

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userAction.getSelectedIndex() == 1) {
                    selectMedia.setVisible(true);
                    continueButton.setVisible(true);
                }
                else if(userAction.getSelectedIndex() == 2){
                    if(subscriberDetails.getWishList().isEmpty()) {
                        JOptionPane.showMessageDialog(null,"You do not have a wishlist, browse catalogue to create wishlist","Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        WishGUI viewWishList = new WishGUI(LoginGUI.subscriberDetails);
                        viewWishList.wishPage();
                    }
                }
                else if(userAction.getSelectedIndex() == 3){
                    recOptions.setVisible(true);
                    recButton.setVisible(true);
                }
                else if(userAction.getSelectedIndex() == 4){
                    if(subscriberDetails.getBorrowList().isEmpty()) {
                        JOptionPane.showMessageDialog(null,"You have not borrowed from our catalogue, browse catalogue to borrow","Message", JOptionPane.INFORMATION_MESSAGE);

                    }
                    else{
                        BorrowGUI borrowView = new BorrowGUI(LoginGUI.subscriberDetails);
                        borrowView.borrowPage();
                    }
                }
                else if(userAction.getSelectedIndex() == 5){
                    if(subscriberDetails.getNewAvailable().isEmpty()){
                        JOptionPane.showMessageDialog(null,"No new available, browse catalogue to borrow","Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        AvaliableGUI availableView = new AvaliableGUI(LoginGUI.subscriberDetails);
                        availableView.availablePage();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Choose an Option","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectMedia.getSelectedIndex()== 1) {
                    BooksGUI viewBooks = new BooksGUI(DigitalLibGUI.medias,LoginGUI.subscriberDetails);
                    viewBooks.bookPage();
                }
                else if(selectMedia.getSelectedIndex()== 2){
                    MusicGUI viewSongs = new MusicGUI(DigitalLibGUI.medias,LoginGUI.subscriberDetails);
                    viewSongs.musicPage();
                }
                else if(selectMedia.getSelectedIndex() == 3){
                    VideoGUI viewvideos = new VideoGUI(DigitalLibGUI.medias, LoginGUI.subscriberDetails);
                    viewvideos.videoPage();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Choose a Category","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        userName.setText("Hi" +" " +subscriberDetails.getFName());
        localTime.setText(Constants.loadDate.toString());
        recButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRec = UsersGraph.getInstance();
                //Random rand = new Random();
                if(recOptions.getSelectedIndex() == 1) {
                    if(userRec.innerRec(LoginGUI.subscriberDetails) != null) {
                        RecInnerGUI viewRec = new RecInnerGUI(LoginGUI.subscriberDetails);
                        viewRec.recPage();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"We could not get you a recommendation, please browse our catalogue","Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if(recOptions.getSelectedIndex() == 2) {
                    //ArrayList<Media> rec = userRec.discovery(DigitalLibGUI.subscribers.get(3));
                    ArrayList<Media> discovered = userRec.discovery(LoginGUI.subscriberDetails);
                    if(discovered.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"We could not get you a recommendation, please browse our catalogue","Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        RecDiscGUI viewRecDisc = new RecDiscGUI(LoginGUI.subscriberDetails);
                        //RecDiscGUI viewRecDisc = new RecDiscGUI(DigitalLibGUI.subscribers.get(rand.nextInt(DigitalLibGUI.subscribers.size())));
                        viewRecDisc.recDiscPage();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Choose recommendation mode","Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        fastForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.forwardWeeks(1);
                Constants.returnOverdue(LoginGUI.borrowDetails);
                localTime.setText(Constants.loadDate.toString());

            }
        });
    }


    public void welcomePage() {

        JFrame frame = new JFrame("Welcome");
        frame.setContentPane(new WelcomeGUI(LoginGUI.subscriberDetails).welcomeScreen);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
