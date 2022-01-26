/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: Tasnim Safadi
    *          Omotolani Subair
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Library class.
    * This class is used to handle the digital library stocks and subscribers.
    ------------------------------------------------------------------------
 */
package LibraryInfo;

import Medias.Media;
import Persons.Subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/* ------------------------------------------------------------------------- *
 * Library class
 * Attributes:
 *    library: the single instance of Library
 *    subscribers: the subscribers of the digital library
 *    medias: the medias available in the library
 */
public class Library {

    private static Library library = null;
    private static ArrayList<Subscriber> subscribers;
    private static Map<Media, MediaQty> medias;

    /* ------------------------------------------------------------------------- *
     * Constructor of the class Library which is private as only one instance of the
     * class can be created.
     *
     * RETURN
     * library       A new instance of the class
     *
     */
    private Library()
    {
        subscribers = new ArrayList<Subscriber>();
        medias = new HashMap<>();
    }

    /* ------------------------------------------------------------------------- *
     * Getter of the Library class instance which instantiates the class on the first call.
     *
     * RETURN
     * library       The single instance of the class Library
     *
     */
    public static Library getInstance()
    {
        if (library == null)
            library = new Library();

        return library;
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the media quantities from a csv file given. This csv
     * must be well formatted (follows the header format). It requires the list
     * of all subscribers ordered by id as well as the list of all medias ordered
     * by id.
     *
     * PARAMETER
     * fileName         The name of the csv file
     * subscribers      The list of all subscribers
     * medias           The list of all medias
     *
     */
    public static void load(String fileName, ArrayList<Subscriber> subscribers, List<Media> medias){
        Library.getInstance().subscribers = subscribers;
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            // skipping the header
            line = br.readLine();
            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");
                // reading the on hold subscriber list
                ArrayList<Subscriber> onHoldSubs = new ArrayList<>();
                if(attributes.length > 3){
                    String[] onHoldSubsID = attributes[3].split(";");
                    for (String c: onHoldSubsID){
                        System.out.println("helo");
                        onHoldSubs.add(subscribers.get(Integer.parseInt(c)));
                    }
                }
                // retrieving the media associated with the read information
                Media media = medias.get(Integer.parseInt(attributes[2]));
                // adding the new mediaQty with the corresponding media into the medias
                Library.getInstance().medias.put(media, new MediaQty(Integer.parseInt(attributes[1]), onHoldSubs));

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /* ------------------------------------------------------------------------- *
     * This method borrows media m for subscriber of id "id". The id is used to
     * remove the subscriber from the on hold list if they were.
     *
     * PARAMETER
     * m                The media to borrow
     * id               The id of the subscriber borrowing the media
     *
     */
    public static void borrow(Media m, int id){
        if(medias.containsKey(m)){
            if(medias.get(m).getQuantity() > 0)
                medias.get(m).borrow(-1, id);
            /* if quantity of the media goes back to 0 and some subscribers
             * had previously been notified that it's available, remove the notification */
            if(medias.get(m).getQuantity() == 0){
                medias.get(m).removeNotification(m);
            }
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method returns media m.
     *
     * PARAMETER
     * m                The media to return
     *
     */
    public static void returnMedia(Media m){
        if(medias.containsKey(m)){
            medias.get(m).returnMedia(1);
            /* if media quantity goes from 0 to 1, notify all
             * subscribers that were on hold that the media is available */
            if(medias.get(m).getQuantity() == 1)
                medias.get(m).notifySubscribers(m);
        }
    }

    /* ------------------------------------------------------------------------- *
     * This method places on hold media m for subscriber s.
     *
     * PARAMETER
     * m                The media to place on hold
     * s                The subscriber that wants to place a hold
     *
     */
    public static void placeHold(Media m, Subscriber s){
        if(medias.containsKey(m))
            medias.get(m).placeHold(s);
    }

    /* ------------------------------------------------------------------------- *
     * This method returns the quantity available of the media m.
     *
     * PARAMETER
     * m                The media to get the quantity of
     *
     * RETURN
     * q                The quantity available of m
     *
     */
    public static int getQuantity(Media m){
        if(medias.containsKey(m))
            return medias.get(m).getQuantity();
        return -1;
    }

    /* ------------------------------------------------------------------------- *
     * This method returns the list of subscribers that placed a hold on media m
     *
     * PARAMETER
     * m                The media to get the subscribers on hold for
     *
     * RETURN
     * subscribers      The list of subscribers that placed a hold on m
     *
     */
    public static ArrayList<Subscriber> getHold(Media m){
        if(medias.containsKey(m))
            return medias.get(m).getSubscribersHold();
        return null;
    }

    /* ------------------------------------------------------------------------- *
     * This method returns the list of subscribers in the digital library
     *
     * RETURN
     * subscribers      The list of subscribers in the digital library
     *
     */
    public static ArrayList<Subscriber> getSubscribers(){
        return subscribers;
    }

}
