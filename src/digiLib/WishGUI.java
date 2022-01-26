/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------
    * In this file you will find the implementation of the wish list view which
    * displays the screen where logged-in user can see all  media in their wishlist
    * they can borrow music media, place music media on hold or remove media from their wishlist
    ---------------------------------------------------------------------------------------------
*/

package digiLib;

import javax.swing.*;

import Medias.Media;
import Persons.BorrowDetails;
import Persons.Subscriber;
import LibraryInfo.Library;
import LibraryInfo.Constants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/* ------------------------------------------------------------------------- *
 * WishGUI class
 * Attributes:
 *    wishCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow or place media on hold
 *    removeButton: the action button to remove media from wishlist
 *    bookLable : displays media image, title and description
 *    wishContainer: display all media found in subscriber wishlist
 */

public class WishGUI {
    private JPanel wishCatalogue;
    private static Subscriber subscriberDetails;

    public WishGUI(Subscriber subscriberDetails) {

        WishGUI.subscriberDetails = subscriberDetails;
    }

    public void wishPage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Wish List Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel wishCatalogue = new JPanel();
        wishCatalogue.setLayout(catalogueFeel);
        wishCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        frame.add(wishCatalogue);
        wishCatalogue.add(topView);

        int count = 0;
        ArrayList<Media> bookWish = subscriberDetails.getWishList();
        for (Media med : bookWish) {
                count += 1;
        }

        JPanel wishContainer[] = new JPanel[count];
        int panelCount = 0;

        for (Media b : bookWish) {
            JLabel bookLable = new JLabel();
            wishContainer[panelCount] = new JPanel();
            wishContainer[panelCount].setLayout(new BoxLayout(wishContainer[panelCount], BoxLayout.PAGE_AXIS));
            wishContainer[panelCount].setBackground(Color.white);
            JButton borrowButton = new JButton();
            JButton removeButton = new JButton();
            borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
            borrowButton.setPreferredSize(new Dimension(6, 3));
            removeButton.setLayout(new BoxLayout(removeButton, BoxLayout.LINE_AXIS));
            removeButton.setPreferredSize(new Dimension(6, 3));
            removeButton.setIcon(new ImageIcon(Constants.removeWishIcon));
            removeButton.setText("Wishlist");
            bookLable.setIcon(new ImageIcon(b.getImgPath()));
            bookLable.setText("<html><p>" + b.getTitle() + "</p><p>" + b.getDescription() + "</p></html>");
            if(Library.getQuantity(b) == 0){
                borrowButton.setText("Place on Hold");
            }
            else{
                borrowButton.setText("Borrow");
            }
            wishContainer[panelCount].add(bookLable);
            wishContainer[panelCount].add(borrowButton);
            wishContainer[panelCount].add(removeButton);
            wishCatalogue.add(wishContainer[panelCount]);
            panelCount += 1;

            borrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    if(subscriberDetails.getWishList().contains(b)){
                        subscriberDetails.removeWish(b);
                    }
                    if (Library.getQuantity(b) == 0) {
                        if(!Library.getHold(b).contains(subscriberDetails)) {
                            Library.placeHold(b, subscriberDetails);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Medium already on hold", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        if(subscriberDetails.getBorrowList().containsValue(b)) {
                            for (BorrowDetails details : subscriberDetails.getDetails(b)) {
                                if(details.getStatus() == 1) {
                                    JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else{
                            subscriberDetails.borrow(b);
                            BorrowGUI borrowView = new BorrowGUI(LoginGUI.subscriberDetails);
                            borrowView.borrowPage();
                        }
                    }

                }

            });
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    subscriberDetails.removeWish(b);
                    removeButton.setVisible(false);
                    borrowButton.setVisible(false);
                    bookLable.setVisible(false);

                }

            });
        }
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);


    }


}
