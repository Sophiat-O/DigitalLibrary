/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the music catalogue view which
    * displays the screen where logged-in user can see all the music media in the library
    * they can borrow music media on place music media on hold
    ------------------------------------------------------------------------
*/

package digiLib;

import javax.swing.*;

import Medias.Music;
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
 * MusicGUI class
 * Attributes:
 *    musicCatalogue: the display screen
 *    userImage: displays logged-in username and a user icon
 *    borrowButton: the action button to borrow or place music on hold
 *    addWishList: the action button to add music to wishlist
 *    musicLable : displays music image, title and description
 *    musicContainer: hold all music media found in media
 *    otherCategories: a ComboBox that allows subscriber to browse other catalogue
 *    browseMore: action button used for the otherCategories ComboBox
 */


public class MusicGUI {
    private JPanel musicCatalogue;
    private static ArrayList<Media> medias;
    private static Subscriber subscriberDetails;

    public MusicGUI(ArrayList<Media> medias, Subscriber subscriberDetails){

        MusicGUI.medias = medias;
        MusicGUI.subscriberDetails = subscriberDetails;

    }

    public void musicPage() {
        GridLayout catalogueFeel = new GridLayout(0,2);
        JFrame frame = new JFrame("Music Catalogue");
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        JPanel musicCatalogue = new JPanel();
        musicCatalogue.setLayout(catalogueFeel);
        musicCatalogue.setBackground(Color.white);
        JLabel userImage = new JLabel();
        userImage.setVerticalAlignment(JLabel.TOP);
        userImage.setHorizontalAlignment(JLabel.LEFT);
        userImage.setIcon(new ImageIcon(Constants.userIcon));
        userImage.setText("Hi" + " " + subscriberDetails.getFName());
        JButton browseMore = new JButton();
        browseMore.setText("Browse");
        browseMore.setLayout(new BoxLayout(browseMore, BoxLayout.LINE_AXIS));
        //browseMore.setHorizontalAlignment(JLabel.LEFT);
        String categories[] = {"<Browse More>","Videos","Books"};
        JComboBox otherCategories = new JComboBox(categories);
        JPanel topView = new JPanel();
        topView.setBackground(Color.white);
        topView.setLayout(new BoxLayout(topView,BoxLayout.PAGE_AXIS));
        topView.add(userImage);
        topView.add(otherCategories);
        topView.add(browseMore);
        frame.add(musicCatalogue);
        musicCatalogue.add(topView);

        ArrayList<Media> musicWish = subscriberDetails.getWishList();

        int count = 0;
        for(Media m: medias) {
            if (m.getClass() == Music.class) {
                count+=1;
            }
        }
        JPanel musicContainer[] = new JPanel[count];
        int panelCount = 0;

        for(Media m: medias){
            if (m.getClass() == Music.class) {
                JLabel musicLable = new JLabel();
                musicContainer[panelCount] = new JPanel();
                musicContainer[panelCount].setLayout(new BoxLayout(musicContainer[panelCount], BoxLayout.PAGE_AXIS));
                musicContainer[panelCount].setBackground(Color.white);
                JButton borrowButton = new JButton();
                JButton addWishList = new JButton();
                borrowButton.setLayout(new BoxLayout(borrowButton, BoxLayout.LINE_AXIS));
                addWishList.setLayout(new BoxLayout(addWishList, BoxLayout.LINE_AXIS));
                addWishList.setIcon(new ImageIcon(Constants.addWishIcon));
                borrowButton.setPreferredSize(new Dimension(6, 3));
                musicLable.setIcon(new ImageIcon(m.getImgPath()));
                musicLable.setText("<html><p>" + m.getTitle() + "</p><p>" + m.getDescription() + "</p></html>");
                if(Library.getQuantity(m) == 0){
                    borrowButton.setText("Place on Hold");
                }
                else{
                    borrowButton.setText("Borrow");
                }
                addWishList.setText("wishlist");
                musicContainer[panelCount].add(musicLable);
                musicContainer[panelCount].add(borrowButton);
                musicContainer[panelCount].add(addWishList);
                musicCatalogue.add(musicContainer[panelCount]);
                panelCount+=1;
                addWishList.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //for(Media b: subscriberDetails.getWishList())
                        if (!musicWish.contains(m)) {
                            LoginGUI.subscriberDetails.addWish(m);
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
                        if(subscriberDetails.getWishList().contains(m)){
                            subscriberDetails.removeWish(m);
                        }
                        if (Library.getQuantity(m) == 0) {
                            if(!Library.getHold(m).contains(subscriberDetails)) {
                                Library.placeHold(m, subscriberDetails);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Medium already on hold", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            if(subscriberDetails.getBorrowList().containsValue(m)) {
                                for (BorrowDetails details : subscriberDetails.getDetails(m)) {
                                    if(details.getStatus() == 1) {
                                        JOptionPane.showMessageDialog(null, "Medium already exist", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                            else{
                                subscriberDetails.borrow(m);
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
                if(otherCategories.getSelectedIndex() == 2){
                    BooksGUI bookCatalogue = new BooksGUI(DigitalLibGUI.medias,LoginGUI.subscriberDetails);
                    bookCatalogue.bookPage();
                }
                else if(otherCategories.getSelectedIndex() == 1){
                    VideoGUI videoCatalogue = new VideoGUI(DigitalLibGUI.medias,LoginGUI.subscriberDetails);
                    videoCatalogue.videoPage();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Choose a Category","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }
}
