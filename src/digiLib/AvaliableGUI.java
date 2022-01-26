/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------
    * In this file you will find the implementation of the now available  view which
    * displays the screen where logged-in user can see all media that have been restocked
    * they can borrow restocked media here or add to wish list
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
 * AvailableGUI class
 * Attributes:
 *    availableCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow media
 *    bookLable : displays music image, title and description
 *    wishContainer: hold all media that has been restocked
 */


public class AvaliableGUI {
    private JPanel availableCatalogue;
    private static Subscriber subscriberDetails;

    public AvaliableGUI(Subscriber subscriberDetails) {

        AvaliableGUI.subscriberDetails = subscriberDetails;
    }

    public void availablePage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Wish List Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel availableCatalogue = new JPanel();
        availableCatalogue.setLayout(catalogueFeel);
        availableCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        frame.add(availableCatalogue);
        availableCatalogue.add(topView);

        int count = 0;
        ArrayList<Media> availableMedia = subscriberDetails.getNewAvailable();
        for (Media med : availableMedia) {
            count += 1;
        }

        JPanel wishContainer[] = new JPanel[count];
        int panelCount = 0;

        for (Media b : availableMedia) {
            JLabel bookLable = new JLabel();
            wishContainer[panelCount] = new JPanel();
            wishContainer[panelCount].setLayout(new BoxLayout(wishContainer[panelCount], BoxLayout.PAGE_AXIS));
            wishContainer[panelCount].setBackground(Color.white);
            JButton borrowButton = new JButton();
            borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
            borrowButton.setPreferredSize(new Dimension(6, 3));
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
            availableCatalogue.add(wishContainer[panelCount]);
            panelCount += 1;

            borrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    if(subscriberDetails.getWishList().contains(b)){
                        subscriberDetails.removeWish(b);
                    }

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

            });
        }
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);


    }


}
