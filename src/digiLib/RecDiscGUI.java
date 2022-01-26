/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------
    * In this file you will find the implementation of the discovery recommendation mode which
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
 * RecDiscGUI class
 * Attributes:
 *    recDiscCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow media
 *    addWishList: the action button to add media to wishlist
 *    bookLable : displays music image, title and description
 *    recDiscContainer: hold all recommended media
 */

public class RecDiscGUI {
    private JPanel recDiscCatalogue;
    private static Subscriber subscriberDetails;
    //private static Subscriber subscriberDetails2;

    public RecDiscGUI(Subscriber subscriberDetails) {

        //RecDiscGUI.subscriberDetails2 = subscriberDetails2;
        RecDiscGUI.subscriberDetails = subscriberDetails;
    }

    public void recDiscPage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Recommendation Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel recDiscCatalogue = new JPanel();
        recDiscCatalogue.setLayout(catalogueFeel);
        recDiscCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + LoginGUI.subscriberDetails.getFName());
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        frame.add(recDiscCatalogue);
        recDiscCatalogue.add(topView);

        ArrayList<Media> bookWish = subscriberDetails.getWishList();

        UsersGraph userRec = UsersGraph.getInstance();

        int count = 0;

        for (Media med : userRec.discovery(subscriberDetails)) {
            count += 1;
        }


        JPanel recDiscContainer[] = new JPanel[count];
        int panelCount = 0;

        for (Media r : userRec.discovery(subscriberDetails)) {
            //int mediaId = b.getId();
            JLabel bookLable = new JLabel();
            recDiscContainer[panelCount] = new JPanel();
            recDiscContainer[panelCount].setLayout(new BoxLayout(recDiscContainer[panelCount], BoxLayout.PAGE_AXIS));
            recDiscContainer[panelCount].setBackground(Color.white);
            JButton borrowButton = new JButton();
            JButton addWishList = new JButton();
            addWishList.setLayout(new BoxLayout(addWishList, BoxLayout.LINE_AXIS));
            addWishList.setIcon(new ImageIcon(Constants.addWishIcon));
            borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
            borrowButton.setPreferredSize(new Dimension(6, 3));
            bookLable.setIcon(new ImageIcon(r.getImgPath()));
            bookLable.setText("<html><p>" + r.getTitle() + "</p><p>" + r.getDescription() + "</p></html>");
            if (Library.getQuantity(r) == 0) {
                borrowButton.setText("Place on Hold");
            } else {
                borrowButton.setText("Borrow");
            }
            addWishList.setText("wishlist");
            recDiscContainer[panelCount].add(bookLable);
            recDiscContainer[panelCount].add(borrowButton);
            recDiscContainer[panelCount].add(addWishList);
            recDiscCatalogue.add(recDiscContainer[panelCount]);
            panelCount += 1;
            //recDiscCatalogue.add(recContainer[panelCount]);
            borrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    if(subscriberDetails.getWishList().contains(r)){
                        subscriberDetails.removeWish(r);
                    }
                    if (Library.getQuantity(r) == 0) {
                        if(!Library.getHold(r).contains(subscriberDetails)) {
                            Library.placeHold(r, subscriberDetails);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Medium already on hold", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        if(subscriberDetails.getBorrowList().containsValue(r)) {
                            for (BorrowDetails details : subscriberDetails.getDetails(r)) {
                                if(details.getStatus() == 1) {
                                    JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else{
                            subscriberDetails.borrow(r);
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
                    if (!bookWish.contains(r)) {
                        LoginGUI.subscriberDetails.addWish(r);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }

            });
        }

        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);


    }


}
