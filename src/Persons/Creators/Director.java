/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Director class which
    * inherits from the Creator class.
    ------------------------------------------------------------------------
 */
package Persons.Creators;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Director extends Creator {

    // Public constructor
    public Director(int id, String fName, String lName){
        super(id, fName, lName);
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the directors from a csv file given. This csv must be
     * well formatted (follows the header format).
     *
     * PARAMETER
     * fileName     The name of the csv file
     *
     * RETURN
     * directors    The list of directors as loaded from the csv
     *
     */
    public static ArrayList<Director> load(String fileName){
        ArrayList<Director> directors = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            //skipping header
            line = br.readLine();
            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");
                Director director = new Director(Integer.parseInt(attributes[0]), attributes[1], attributes[2]);

                // adding director into ArrayList
                directors.add(director);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return directors;
    }
}