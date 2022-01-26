/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------
    * In this file you will find the implementation of the inner circle recommendation mode which
    * displays the screen where logged-in user can see recommended media
    * they can borrow recommended media here or add to wish list
    ---------------------------------------------------------------------------------------------
*/

package digiLib;

import javax.swing.*;

import LibraryInfo.Library;
import LibraryInfo.UsersGraph;
import LibraryInfo.Constants;
import Medias.Media;
import Persons.BorrowDetails;
import Persons.Subscriber;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/* ------------------------------------------------------------------------- *
 * RecInnerGUI class
 * Attributes:
 *    recDiscCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow media
 *    addWishList: the action button to add media to wishlist
 *    bookLable : displays music image, title and description
 *    recContainer: hold all recommended media
 */

public class RecInnerGUI {
    private JPanel recCatalogue;
    //private static ArrayList<Media> medias;
    private static Subscriber subscriberDetails;

    public RecInnerGUI(Subscriber subscriberDetails) {

        //RecInnerGUI.medias = medias;
        RecInnerGUI.subscriberDetails = subscriberDetails;
    }

    public void recPage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Recommendation Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel recCatalogue = new JPanel();
        recCatalogue.setLayout(catalogueFeel);
        recCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        frame.add(recCatalogue);
        recCatalogue.add(topView);

        ArrayList<Media> bookWish = subscriberDetails.getWishList();

        UsersGraph userRec = UsersGraph.getInstance();


        JLabel bookLable = new JLabel();
        JPanel recContainer = new JPanel();
        recContainer.setLayout(new BoxLayout(recContainer, BoxLayout.PAGE_AXIS));
        recContainer.setBackground(Color.white);
        JButton borrowButton = new JButton();
        JButton addWishList = new JButton();
        borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
        borrowButton.setPreferredSize(new Dimension(6, 3));
        addWishList.setLayout(new BoxLayout(addWishList, BoxLayout.LINE_AXIS));
        addWishList.setIcon(new ImageIcon(Constants.addWishIcon));
        bookLable.setIcon(new ImageIcon(userRec.innerRec(subscriberDetails).getImgPath()));
        bookLable.setText("<html><p>" + userRec.innerRec(subscriberDetails).getTitle() + "</p><p>" + userRec.innerRec(subscriberDetails).getDescription() + "</p></html>");
        if (Library.getQuantity(userRec.innerRec(subscriberDetails)) == 0) {
            borrowButton.setText("Place on Hold");
        } else {
            borrowButton.setText("Borrow");
        }
        addWishList.setText("wishlist");
        recContainer.add(bookLable);
        recContainer.add(borrowButton);
        recContainer.add(addWishList);
        recCatalogue.add(recContainer);
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //for(Media b: subscriberDetails.getWishList())
                if(subscriberDetails.getWishList().contains(userRec.innerRec(subscriberDetails))){
                    subscriberDetails.removeWish(userRec.innerRec(subscriberDetails));
                }
                if (Library.getQuantity(userRec.innerRec(subscriberDetails)) == 0) {
                    Library.placeHold(userRec.innerRec(subscriberDetails), subscriberDetails);
                }
                else {
                    if(subscriberDetails.getBorrowList().containsValue(userRec.innerRec(subscriberDetails))) {
                        for (BorrowDetails details : subscriberDetails.getDetails(userRec.innerRec(subscriberDetails))) {
                            if(details.getStatus() == 1) {
                                JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    else{
                        subscriberDetails.borrow(userRec.innerRec(subscriberDetails));
                        BorrowGUI borrowView = new BorrowGUI(LoginGUI.subscriberDetails);
                        borrowView.borrowPage();
                    }
                }

            }

        });
        addWishList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //for(Media b: subscriberDetails.getWishList())
                if (!bookWish.contains(userRec.innerRec(subscriberDetails))) {
                    LoginGUI.subscriberDetails.addWish(userRec.innerRec(subscriberDetails));
                }
                else{
                    JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        });



        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);


    }


}
