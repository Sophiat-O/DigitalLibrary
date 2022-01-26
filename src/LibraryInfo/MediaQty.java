/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: Tasnim Safadi
    *          Omotolani Subair
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the MediaQty class.
    * This class is used to keep additional information regarding the
    * medias available in the digital library.
    ------------------------------------------------------------------------
 */
package LibraryInfo;

import Medias.Media;
import Persons.Subscriber;
import java.util.ArrayList;
import java.util.List;

/* ------------------------------------------------------------------------- *
 * MediaQty class
 * Attributes:
 *    count: a shared variable to create new id of MediaQty
 *    id: the id of MediaQty
 *    quantity: the quantity available
 *    subscribersHold: the list of subscribers that put a hold
 */
public class MediaQty {
    private static int count = 0;
    private int id;
    private int quantity;
    private ArrayList<Subscriber> subscribersHold;

    // Public constructor
    public MediaQty(int quantity, ArrayList<Subscriber> subscribersHold){
        id = count;
        this.quantity = quantity;
        this.subscribersHold = subscribersHold;
        count += 1;
    }

    /* ------------------------------------------------------------------------- *
     * This method adds a subscriber on the hold list
     *
     * PARAMETER
     * subscriber       The subscriber to add
     *
     */
    public void placeHold(Subscriber subscriber){
        String s = "";
        // retrieving the previous list of subscribers on hold
        if(subscribersHold.isEmpty())
            s = String.valueOf(subscriber.getId());
        else{
            for (Subscriber sub: subscribersHold) {
                s += sub.getId() + ";";
            }
            s += String.valueOf(subscriber.getId());
        }
        List<String> csv = Constants.getCSV(Constants.qtyFile);
        String[] attributes = csv.get(id+1).split(",");
        String newLine = attributes[0] + "," + attributes[1] + "," + attributes[2] + "," + s;
        // updating the line of information for this MediaQty
        csv.set(id+1, newLine);
        // updating the csv file
        Constants.writeCSV(Constants.qtyFile, csv);
        subscribersHold.add(subscriber);
    }

    /* ------------------------------------------------------------------------- *
     * This method updates the quantity of a media and removes the subscriber of
     * id "id" if they are on the hold list.
     *
     * PARAMETER
     * q                The quantity to update
     * id               The id of the subscriber borrowing the media
     *
     */
    public void borrow(int q, int id){
        quantity += q;
        List<String> csv = Constants.getCSV(Constants.qtyFile);
        String[] attributes = csv.get(this.id+1).split(",");
        String holdList = ",";
        // retrieving previous subscribersHold list
        ArrayList<Subscriber> newHold = new ArrayList<>(subscribersHold);
        if(! subscribersHold.isEmpty()){
            // treating first value on its own
            int i = 0;
            if(subscribersHold.get(0).getId() == id)
                newHold.remove(subscribersHold.get(0));
            else {
                holdList += subscribersHold.get(0).getId();
                i = 1;
            }
            while(i < subscribersHold.size()){
                if(i == 0)
                    holdList += subscribersHold.get(i).getId();
                else if(subscribersHold.get(i).getId() == id)
                    newHold.remove(subscribersHold.get(i));
                else
                    holdList += ";" + subscribersHold.get(i).getId();
                i++;
            }
        }
        subscribersHold = newHold;
        String newLine = attributes[0] + "," + quantity + "," + attributes[2] + holdList;
        // updating the line of information for MediaQty
        csv.set(this.id+1, newLine);
        // updating the csv file
        Constants.writeCSV(Constants.qtyFile, csv);
    }

    /* ------------------------------------------------------------------------- *
     * This method updates a media quantity
     *
     * PARAMETER
     * q                The quantity to update
     *
     */
    public void returnMedia(int q){
        quantity += q;
        // retrieving previous MediaQty information
        List<String> csv = Constants.getCSV(Constants.qtyFile);
        String[] attributes = csv.get(id+1).split(",");
        String holdList = "";
        if(subscribersHold.isEmpty())
            holdList = ",";
        else
            holdList = "," + attributes[attributes.length - 1];
        String newLine = attributes[0] + "," + quantity + "," + attributes[2] + holdList;
        // updating the line of information for MediaQty
        csv.set(id+1, newLine);
        // updating the csv file
        Constants.writeCSV(Constants.qtyFile, csv);
    }

    /* ------------------------------------------------------------------------- *
     * This method notifies all the subscribers in the subscribersHold list that
     * a media is available. It is called when quantity goes from 0 to 1.
     *
     * PARAMETER
     * m               The media now available
     *
     */
    public void notifySubscribers(Media m){
        for(Subscriber s: subscribersHold)
            s.addNotification(m);
    }

    /* ------------------------------------------------------------------------- *
     * This method removes all notifications for subscribers on the hold list.
     * It is called when quantity goes down to 0.
     *
     * PARAMETER
     * m               The media now available
     *
     */
    public void removeNotification(Media m){
        for(Subscriber s: subscribersHold)
            s.removeNotification(m);
    }

    /* ------------------Getters--------------------- */

    public int getId(){
        return id;
    }

    public int getQuantity(){
        return quantity;
    }

    public ArrayList<Subscriber> getSubscribersHold() {
        return subscribersHold;
    }

}
