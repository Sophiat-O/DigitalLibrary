/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Subscriber class
    * which inherits from the Person class. This class has additional
    * attributes and is more complex than the other sub Person classes.
    ------------------------------------------------------------------------
 */

package Persons;
import LibraryInfo.Constants;
import LibraryInfo.Library;
import Medias.Media;

import java.io.*;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/* ------------------------------------------------------------------------- *
 * Subscriber class
 * Attributes:
 *    count: a shared variable to keep track of the number of subscribers
 *           this is useful in order to create a new unique id for subscribers
 *    email: the email address used to sign up
 *    password: the password used to sign up
 *    borrowList: a list of all the past and ongoing borrows of the subscriber
 *    wishList: a list of all the wishes of the subscriber
 *    newAvailable: a list of media that have been recently available and were
 *                  placed on hold by the user
 */
public class Subscriber extends Person{
    private static int count;
    private String email;
    private String password;
    private Map<BorrowDetails, Media> borrowList;
    private ArrayList<Media> wishList;
    private ArrayList<Media> newAvailable;

    /* ------------------------------------------------------------------------- *
     * Constructor of the class Subscriber which should be used when loading
     * subscriber info from a csv
     *
     * PARAMETER
     * id           The id of the subscriber
     * fName        The first name of the subscriber
     * lName        The last name of the subscriber
     * email        The email of the subscriber
     * password     The password of the subscriber
     * borrowList   The list of borrow medias of the subscriber
     * wishList     The list of wished medias of the subscriber
     *
     * RETURN
     * subscriber   A new instance of the class
     *
     */
    public Subscriber(int id, String fName, String lName, String email, String password, Map<BorrowDetails, Media> borrowList, ArrayList<Media> wishList){
        super(id, fName, lName);
        this.email = email;
        this.password = password;
        this.borrowList = borrowList;
        this.wishList = wishList;
        newAvailable = new ArrayList<>();
    }

    /* ------------------------------------------------------------------------- *
     * Constructor of the class Subscriber which should be used when creating a
     * new subscriber (sign up). The method add the new subscriber info to the
     * subscriber csv file.
     *
     * PARAMETER
     * fName        The first name of the subscriber
     * lName        The last name of the subscriber
     * email        The email of the subscriber
     * password     The password of the subscriber
     * fileName     The name of the file holding all the subscribers
     *
     * RETURN
     * subscriber   A new instance of the class
     *
     */
    public Subscriber(String fName, String lName, String email, String password, String fileName){
        super(count, fName, lName);
        this.email = email;
        this.password = password;
        this.borrowList = new HashMap<>();
        this.wishList = new ArrayList<>();
        this.newAvailable = new ArrayList<>();
        try (FileWriter f = new FileWriter(fileName, true);
           BufferedWriter b = new BufferedWriter(f);
           PrintWriter p = new PrintWriter(b);) {

            p.println(this.count + "," + fName + "," + lName + "," + email + "," + password + "," + ",");

        } catch (IOException i) {
            i.printStackTrace();
        }
        this.count += 1;
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the subscribers from a csv file given. This csv must
     * be well formatted (follows the header format). It requires the list of all
     * borrow details ordered by id as well as the list of all medias ordered by
     * id.
     *
     * PARAMETER
     * fileName         The name of the csv file
     * borrowdetails    The list of all borrowDetails loaded
     * medias           The list of all medias loaded
     *
     * RETURN
     * subscribers      The list of subscribers as loaded from the csv
     *
     */
    public static ArrayList<Subscriber> load(String fileName, List<BorrowDetails> borrowdetails, List<Media> medias){
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            // skipping header
            line = br.readLine();
            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                // decomposing borrowList information if it exists
                Map<BorrowDetails, Media> bList = new HashMap<>();
                if(attributes.length > 5 && attributes[5].contains(" ")) {
                    String[] bDListID = attributes[5].split(";");
                    // looping over all the items in borrow list
                    for (String c: bDListID){
                        String[] bInfo = c.split(" ");
                        // getting the borrowDetails item and media associated with the read id
                        bList.put(borrowdetails.get(Integer.parseInt(bInfo[1])), medias.get(Integer.parseInt(bInfo[0])));
                    }
                }
                // decomposing wish list if it exists
                ArrayList<Media> wList = new ArrayList<>();
                if(attributes.length > 6 || (attributes.length > 5 && !attributes[5].contains(" "))) {
                    String[] wListID = attributes[6].split(";");
                    for (String c : wListID) {
                        // getting the media item associated with the read id
                        wList.add(medias.get(Integer.parseInt(c)));
                    }
                }
                Subscriber subscriber = new Subscriber(Integer.parseInt(attributes[0]), attributes[1], attributes[2], attributes[3], attributes[4], bList, wList);

                // adding subscriber into ArrayList
                subscribers.add(subscriber);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        // setting the final number of loaded subscribers
        Subscriber.count = subscribers.size();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return subscribers;
    }

    /* ------------------------------------------------------------------------- *
     * This method borrows a media m for the subscriber.
     *
     * PARAMETER
     * m                The media to be borrowed
     *
     */
    public void borrow(Media m){
        // if the subscriber is borrowing a media that was on their notification
        if(newAvailable.contains(m))
            this.removeNotification(m);
        // notify library about the borrow, also remove user from the hold if he was
        Library.borrow(m, this.id);
        // setting start date and end date
        LocalDate d = Constants.loadDate;
        LocalDate rD = d.plusWeeks(4);
        BorrowDetails bD = new BorrowDetails(d, rD);

        // updating subscriber borrow list in csv
        String s = "";
        if(borrowList.isEmpty())
            s = m.getId() + " " + bD.getId();
        else{
            // rewriting the line with the previous borrow list
            for(Map.Entry<BorrowDetails, Media> entry: borrowList.entrySet())
            {
                BorrowDetails key = entry.getKey();
                Media val = entry.getValue();
                s += val.getId() + " " + key.getId() + ";";
            }
            // adding new borrow information
            s += m.getId() + " " + bD.getId();
        }
        List<String> csv = Constants.getCSV(Constants.subscribersFile);
        // retrieving the wish list
        String[] attributes = csv.get(id+1).split(",");
        String wList = "";
        if(wishList.isEmpty())
            wList = ",";
        else
            wList = "," + attributes[attributes.length - 1];
        // new subscriber line
        String newLine = attributes[0] + "," + attributes[1] + "," + attributes[2] + "," + attributes[3] + "," + attributes[4] + "," + s + wList;
        // modifying the line of the subscribers
        csv.set(id+1, newLine);
        // updating the csv
        Constants.writeCSV(Constants.subscribersFile, csv);
        borrowList.put(bD, m);
    }

    /* ------------------------------------------------------------------------- *
     * This method adds a media m on the wish list of the subscriber.
     *
     * PARAMETER
     * m                The media to be added
     *
     */
    public void addWish(Media m){
        // updating the wish list
        String s = "";
        if(wishList.isEmpty())
            s = String.valueOf(m.getId());
        else{
            // retrieving the previous wish list
            for (Media media: wishList) {
                s += media.getId() + ";";
            }
            // adding the new wish list
            s += String.valueOf(m.getId());
        }
        wishList.add(m);
        List<String> csv = Constants.getCSV(Constants.subscribersFile);
        // getting the previous line of the subscriber in the csv file
        String[] attributes = csv.get(id+1).split(",");
        // retrieving the previous borrow list
        String bList = "";
        if(borrowList.isEmpty())
            bList = ",";
        else
            bList = "," + attributes[5];
        String newLine = attributes[0] + "," + attributes[1] + "," + attributes[2] + "," + attributes[3] + "," + attributes[4] + bList + "," + s;
        // updating the line of the subscriber
        csv.set(id+1, newLine);
        // updating the csv file
        Constants.writeCSV(Constants.subscribersFile, csv);
    }

    /* ------------------------------------------------------------------------- *
     * This method removes a media m from the wish list of the subscriber.
     *
     * PARAMETER
     * m                The media to be removed
     *
     */
    public void removeWish(Media m){
        String s = "";
        if(wishList.contains(m)){
            wishList.remove(m);
            if(! wishList.isEmpty()){
                // retrieving previous wish list
                s += wishList.get(0).getId();
                for (int i = 1; i < wishList.size(); i++) {
                    s += ";" + wishList.get(i).getId();
                }
            }
            List<String> csv = Constants.getCSV(Constants.subscribersFile);
            // retrieving previous borrow information
            String[] attributes = csv.get(id+1).split(",");
            String bList = "";
            if(borrowList.isEmpty())
                bList = ",";
            else
                bList = "," + attributes[5];
            String newLine = attributes[0] + "," + attributes[1] + "," + attributes[2] + "," + attributes[3] + "," + attributes[4] + bList + "," + s;
            // updating line of the subscriber
            csv.set(id+1, newLine);
            // updating the csv file
            Constants.writeCSV(Constants.subscribersFile, csv);
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method extends the borrow deadline.
     *
     * PARAMETER
     * bD                The borrow to be extended
     *
     */
    public void extend(BorrowDetails bD){
        if(borrowList.containsKey(bD)) {
            // calling extend method in BorrowDetails
            bD.extend();
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method ends an ongoing borrow.
     *
     * PARAMETER
     * bD                The borrow to be ended
     *
     */
    public void returnMedia(BorrowDetails bD){
        if(borrowList.containsKey(bD)) {
            // calling return method in Library
            Library.returnMedia(borrowList.get(bD));
            // calling return method in BorrowDetails
            bD.returnMedia();
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method marks a media that was borrowed
     *
     * PARAMETER
     * bD                The borrow used to mark
     * score             The score to be added
     *
     */
    public void mark(BorrowDetails bD, double score){
        if(borrowList.containsKey(bD)){
            // calling the mark method in Media
            borrowList.get(bD).mark(score);
            // updating the mark status of the borrowDetails
            bD.setMarked();
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method returns the borrowDetails associated with one media.
     *
     * PARAMETER
     * m                The media we want to retrieve the borrow details for
     *
     */
    public  ArrayList<BorrowDetails> getDetails(Media m) {
        ArrayList<BorrowDetails> result = new ArrayList();
        if (borrowList.containsValue(m)) {
            for (Map.Entry<BorrowDetails, Media> entry : borrowList.entrySet()) {
                if (Objects.equals(entry.getValue(), m)) {
                    result.add(entry.getKey());
                }
            }
        }
        return result;
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the new available media for the subscribers from a
     * csv file given. This csv must be well formatted (follows the header format).
     * It requires the list of all subscribers ordered by id as well as the list
     * of all medias ordered by id.
     *
     * PARAMETER
     * fileName         The name of the csv file
     * subscribers      The list of all borrowDetails loaded
     * medias           The list of all medias loaded
     *
     */
    public static void loadNewAvailable(String fileName, ArrayList<Subscriber> subscribers, ArrayList<Media> medias){
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            // skipping header
            line = br.readLine();
            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");
                // updating the subscriber newAvailable list
                subscribers.get(Integer.parseInt(attributes[0])).newAvailable.add(medias.get(Integer.parseInt(attributes[1])));

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method adds a media to the newAvailable list.
     *
     * PARAMETER
     * m                The media to be added
     *
     */
    public void addNotification(Media m){
        // returning if the media is already added.
        if(newAvailable.contains(m))
            return;
        newAvailable.add(m);
        // adding new line in the csv file
        List<String> csv = Constants.getCSV(Constants.newAvailableFile);
        String newLine = this.id + "," + m.getId();
        csv.add(newLine);
        Constants.writeCSV(Constants.newAvailableFile, csv);
    }

    /* ------------------------------------------------------------------------- *
     * This method returns the borrowDetails associated with one media.
     *
     * PARAMETER
     * m                The media we want to retrieve the borrow details for
     *
     */
    public void removeNotification(Media m){
        if(!newAvailable.contains(m))
            return;
        newAvailable.remove(m);
        List<String> csv = Constants.getCSV(Constants.newAvailableFile);
        // removing line from csv file
        for(int i = 1; i < csv.size(); i++){
            String[] attributes = csv.get(i).split(",");
            if(Integer.parseInt(attributes[0]) == this.id && Integer.parseInt(attributes[1]) == m.getId())
                csv.remove(i);
        }
        Constants.writeCSV(Constants.newAvailableFile, csv);
    }

    /* ------------------Getters--------------------- */

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public Map<BorrowDetails, Media> getBorrowList() {
        return borrowList;
    }

    public ArrayList<Media> getWishList() {
        return wishList;
    }

    public int getId(){
        return id;
    }

    public ArrayList<Media> getNewAvailable() {
        return newAvailable;
    }
    

}