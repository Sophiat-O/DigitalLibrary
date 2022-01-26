/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------
    * In this file you will find the implementation of the borrow list view which
    * displays the screen where logged-in user can see all media they have borrowed
    * they can extend return date, return  media and rate media that has been returned
    ---------------------------------------------------------------------------------------------
*/

package digiLib;

import javax.swing.*;
import Medias.Media;
import Persons.BorrowDetails;
import Persons.Subscriber;
import LibraryInfo.Constants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/* ------------------------------------------------------------------------- *
 * BorrowGUI class
 * Attributes:
 *    borrowCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    extendButton: the action button to extend borrowed media return date
 *    returnButton: the action button to return borrowed media
 *    rateButton: the action button to rate returned media
 *    borrowLable : displays media image, title and description
 *    borrowContainer: display all media found in subscriber borrow list
 */


public class BorrowGUI {
    private JPanel borrowCatalogue;
    private static Subscriber subscriberDetails;

    public BorrowGUI(Subscriber subscriberDetails) {
        BorrowGUI.subscriberDetails = subscriberDetails;
    }

    public void borrowPage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Borrow Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel borrowCatalogue = new JPanel();
        borrowCatalogue.setLayout(catalogueFeel);
        borrowCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        frame.add(borrowCatalogue);
        borrowCatalogue.add(topView);

        int count = 0;
        Map<BorrowDetails, Media> borrowScreen = subscriberDetails.getBorrowList();
        for (Map.Entry<BorrowDetails, Media> entry : borrowScreen.entrySet()){
            BorrowDetails details = entry.getKey();
            Media media = entry.getValue();
            count += 1;

        }

        JPanel borrowContainer[] = new JPanel[count];
        int panelCount = 0;
        String s = "";

        for (Map.Entry<BorrowDetails, Media> entry : borrowScreen.entrySet()){
            BorrowDetails details = entry.getKey();
            Media media = entry.getValue();
            if(details.getStatus() == 0){
                s = "Status: Returned";
            }
            else{
                s = "<html><p>Status: In use</p><p>Return Date: " + details.getEndDate() + "</p></html";
            }


            borrowContainer[panelCount] = new JPanel();
            borrowContainer[panelCount].setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            JLabel borrowLable = new JLabel();
            c.gridx = 0;
            c.gridy = 0;
            borrowContainer[panelCount].add(borrowLable, c);
            borrowContainer[panelCount].setBackground(Color.white);
            JButton extendButton = new JButton();
            JButton returnButton = new JButton();
            JButton rateButton = new JButton();
            extendButton.setText("Extend");
            extendButton.setVisible(false);
            c.gridx = 0;
            c.gridy = 1;
            c.ipady = 0;
            borrowContainer[panelCount].add(extendButton, c);
            returnButton.setText("Return");
            returnButton.setVisible(false);
            c.gridx = 0;
            c.gridy = 2;
            c.ipady = 0;
            borrowContainer[panelCount].add(returnButton, c);
            rateButton.setText("Rate");
            c.gridx = 0;
            c.gridy = 3;
            c.ipady = 0;
            rateButton.setVisible(false);
            borrowContainer[panelCount].add(rateButton, c);

            borrowLable.setIcon(new ImageIcon(media.getImgPath()));
            borrowLable.setText("<html><p>" + media.getTitle() + "</p><p>" + media.getDescription() + "</p><p>" + s + "</p></html>");
            Double scores[] = {1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0};
            JComboBox borrowMark = new JComboBox(scores);

            borrowMark.setVisible(false);
            c.gridx = 0;
            c.gridy = 4;
            c.ipady = 0;
            borrowContainer[panelCount].add(borrowMark, c);
            if(details.getStatus() == 0 && details.getMarked() == 0){
                rateButton.setVisible(true);
                borrowMark.setVisible(true);
            }
            if(details.getStatus() == 1){
                extendButton.setVisible(true);
                returnButton.setVisible(true);
            }
            borrowCatalogue.add(borrowContainer[panelCount]);
            panelCount += 1;

            rateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    String selectMark = borrowMark.getSelectedItem().toString();
                    Double doubleMark = Double.parseDouble(selectMark);
                    subscriberDetails.mark(details, doubleMark);
                    rateButton.setVisible(false);
                    borrowMark.setVisible(false);

                }

            });
            extendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    subscriberDetails.extend(details);

                }

            });
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //for(Media b: subscriberDetails.getWishList())
                    subscriberDetails.returnMedia(details);
                    rateButton.setVisible(true);
                    borrowMark.setVisible(true);
                    extendButton.setVisible(false);
                    returnButton.setVisible(false);

                }

            });

        }
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);


    }


}
