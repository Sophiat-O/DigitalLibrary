/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the BorrowDetails
    * class. This class is used to handle information regarding a borrow.
    ------------------------------------------------------------------------
 */
package Persons;

import LibraryInfo.Constants;
import Medias.Media;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

/* ------------------------------------------------------------------------- *
 * BorrowDetails class
 * Attributes:
 *    count: a shared variable to keep track of the number of borrows
 *           this is useful in order to create a new unique id for borrows
 *    id: a unique id
 *    startDate: the start date of the borrow
 *    endDate: the end date of the borrow
 *    status: the status of the borrow (1 ongoing, 0 the borrow has ended)
 *    marked: the status of marking of the borrow (1 it has been marked,
 *            0 otherwise)
 */
public class BorrowDetails {
    private static int count = 0;
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int status;
    private int marked;

    /* ------------------------------------------------------------------------- *
     * Constructor of the class BorrowDetails which should be used when loading
     * borrow info from a csv
     *
     * PARAMETER
     * id               The id of the borrow
     * startDate        The start date of the borrow
     * endDate          The end date of the borrow
     * status           The status of the borrow
     * marked           The marking of the borrow
     *
     * RETURN
     * borrowDetails    A new instance of the class
     *
     */
    public BorrowDetails(int id, LocalDate startDate, LocalDate endDate, int status, int marked){
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.marked = marked;
    }

    /* ------------------------------------------------------------------------- *
     * Constructor of the class BorrowDetails which should be used when a new
     * borrow is made
     *
     * PARAMETER
     * startDate        The start date of the borrow
     * endDate          The end date of the borrow
     *
     * RETURN
     * borrowDetails    A new instance of the class
     *
     */
    public BorrowDetails(LocalDate startDate, LocalDate endDate){
        this.id = count;
        this.startDate = startDate;
        this.endDate = endDate;
        // borrow is ongoing
        this.status = 1;
        // borrow could not have been marked
        this.marked = 0;

        // adding new borrow into csv
        try (FileWriter f = new FileWriter(Constants.borrowFile, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {

            p.println(id + "," + startDate + "," + endDate + ",1,0");

        } catch (IOException i) {
            i.printStackTrace();
        }
        count += 1;
    }

    // overriding equals method since its used as key in map
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BorrowDetails other = (BorrowDetails) obj;
        if (this.id == other.getId()) {
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Start date: " + startDate + "\n End date: " + endDate + "\n Status: " + status + "\n Marked: " + marked;
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the borrows from a csv file given. This csv must
     * be well formatted (follows the header format). It requires the list of all
     * medias ordered by id.
     *
     * PARAMETER
     * fileName         The name of the csv file
     * medias           The list of all medias loaded
     *
     * RETURN
     * borrowDetails    The list of borrowDetails as loaded from the csv
     *
     */
    public static ArrayList<BorrowDetails> load(String fileName, List<Media> medias){
        ArrayList<BorrowDetails> borrowDetails = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
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
                //Media media = medias.get(Integer.parseInt(attributes[3]));
                BorrowDetails bD = new BorrowDetails(Integer.parseInt(attributes[0]), LocalDate.parse(attributes[1]), LocalDate.parse(attributes[2]), Integer.parseInt(attributes[3]), Integer.parseInt(attributes[4]));

                // adding borrow details into ArrayList
                borrowDetails.add(bD);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }
        BorrowDetails.count = borrowDetails.size();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return borrowDetails;
    }

    /* ------------------------------------------------------------------------- *
     * This method extends by two weeks the end date of a borrow.
     *
     */
    public void extend(){
        endDate = endDate.plusWeeks(2);
        // updating the csv line of this borrow
        updateLine(id);
    }

    /* ------------------------------------------------------------------------- *
     * This method return a borrowed media.
     *
     */
    public void returnMedia(){
        status = 0;
        endDate = java.time.LocalDate.now();
        // updating the csv line of this borrow
        updateLine(id);
    }

    /* ------------------------------------------------------------------------- *
     * This method updates the line of the borrow of id 'id'
     *
     */
    private void updateLine(int id){
        List<String> csv = Constants.getCSV(Constants.borrowFile);
        String newLine = id + "," + startDate + "," + endDate + "," + status + "," + marked;
        csv.set(id+1, newLine);
        Constants.writeCSV(Constants.borrowFile, csv);
    }

    /* ------------------------------------------------------------------------- *
     * This method updates the value of marked
     *
     */
    public void setMarked(){
        marked = 1;
        // updating the line of this borrow
        updateLine(id);
    }

    /* ------------------Getters--------------------- */

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getStatus() {
        return status;
    }

    public int getMarked() {
        return marked;
    }
}