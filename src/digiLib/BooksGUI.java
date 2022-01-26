/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the book catalogue view which
    * displays the screen where logged-in user can see all the books in the library
    * they can borrow books on place books on hold
    ------------------------------------------------------------------------
 */

package digiLib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Medias.Book;
import Medias.Media;

import Persons.BorrowDetails;
import Persons.Subscriber;

import LibraryInfo.Library;
import LibraryInfo.Constants;

/* ------------------------------------------------------------------------- *
 * BookGUI class
 * Attributes:
 *    bookCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow or place a book on hold
 *    addWishList: the action button to add book to wishlist
 *    bookLable : displays book image, title and description
 *    bookContainer: hold all books found in media
 *    otherCategories: a ComboBox that allows subscriber to browse other catalogue
 *    browseMore: action button used for the otherCategories ComboBox
 */


public class BooksGUI {
    private JPanel bookCatalogue;
    private static ArrayList<Media> medias;
    private static Subscriber subscriberDetails;


    public BooksGUI(ArrayList<Media> medias, Subscriber subscriberDetails) {

        BooksGUI.medias = medias;
        BooksGUI.subscriberDetails = subscriberDetails;
    }

    public void bookPage() {
        GridLayout catalogueFeel = new GridLayout(0, 2);
        JFrame frame = new JFrame("Books Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel bookCatalogue = new JPanel();
        bookCatalogue.setLayout(catalogueFeel);
        bookCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JButton browseMore = new JButton();
        browseMore.setText("Browse");
        browseMore.setLayout(new BoxLayout(browseMore, BoxLayout.LINE_AXIS));
        String categories[] = {"<Browse More>", "Videos", "Music"};
        JComboBox otherCategories = new JComboBox(categories);
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView, BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        topView.add(otherCategories);
        topView.add(browseMore);
        frame.add(bookCatalogue);
        bookCatalogue.add(topView);



        int count = 0;
        //ArrayList<Music> music = new ArrayList();
        for (Media med : medias) {
            if (med.getClass() == Book.class) {
                //System.out.println(med);
                count += 1;
            }
        }

        ArrayList<Media> bookWish = subscriberDetails.getWishList();


        JPanel bookContainer[] = new JPanel[count];
        int panelCount = 0;

        for (Media b : medias) {
            if (b.getClass() == Book.class) {
                //int mediaId = b.getId();
                JLabel bookLable = new JLabel();
                bookContainer[panelCount] = new JPanel();
                bookContainer[panelCount].setLayout(new BoxLayout(bookContainer[panelCount], BoxLayout.PAGE_AXIS));
                bookContainer[panelCount].setBackground(Color.white);
                JButton borrowButton = new JButton();
                JButton addWishList = new JButton();
                borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
                addWishList.setLayout(new BoxLayout(addWishList, BoxLayout.LINE_AXIS));
                addWishList.setIcon(new ImageIcon(Constants.addWishIcon));
                borrowButton.setPreferredSize(new Dimension(6, 3));
                bookLable.setIcon(new ImageIcon(b.getImgPath()));
                bookLable.setText("<html><p>" + b.getTitle() + "</p><p>" + b.getDescription() + "</p></html>");
                if(Library.getQuantity(b) == 0){
                    borrowButton.setText("Place on Hold");
                }
                else{
                    borrowButton.setText("Borrow");
                }
                addWishList.setText("wishlist");
                bookContainer[panelCount].add(bookLable);
                bookContainer[panelCount].add(borrowButton);
                bookContainer[panelCount].add(addWishList);
                bookCatalogue.add(bookContainer[panelCount]);
                panelCount += 1;
                addWishList.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //for(Media b: subscriberDetails.getWishList())
                        if (!bookWish.contains(b)) {
                            LoginGUI.subscriberDetails.addWish(b);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                });
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

            }

        }

        browseMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (otherCategories.getSelectedIndex() == 2) {
                    MusicGUI musicCatalogue = new MusicGUI(DigitalLibGUI.medias, LoginGUI.subscriberDetails);
                    musicCatalogue.musicPage();
                } else if (otherCategories.getSelectedIndex() == 1) {
                    VideoGUI videoCatalogue = new VideoGUI(DigitalLibGUI.medias, LoginGUI.subscriberDetails);
                    videoCatalogue.videoPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Choose a Category", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}

