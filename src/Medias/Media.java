/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Media class.
    * This class is used to handle information regarding a media.
    ------------------------------------------------------------------------
 */

package Medias;

import LibraryInfo.Constants;
import Persons.Actor;
import Persons.Creators.Author;
import Persons.Creators.Director;
import Persons.Creators.Singer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/* ------------------------------------------------------------------------- *
 * Media class
 * Attributes:
 *    id: the id of the media
 *    title: the title of the media
 *    description: the description of the media
 *    imgPath: the path to the image of the media
 *    score: the score of the media
 *    reviewNum: the number of reviews of the media
 */
public abstract class Media {
    protected int id;
    protected String title;
    protected String description;
    protected String imgPath;
    private Double score;
    private int reviewNum;

    // Super constructor
    public Media(int id, String title, String imgPath, String description, Double score, int reviewNum){
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.description = description;
        this.score = score;
        this.reviewNum = reviewNum;
    }

    /* ------------------------------------------------------------------------- *
     * This methods adds a mark to a media
     *
     * PARAMETER
     * score         The score to be added in the total score
     *
     */
    public void mark(double score){
        // computing new score
        this.score = (this.score * reviewNum + score) / (reviewNum + 1);
        // increasing the total number of reviews
        reviewNum += 1;
        List<String> csv = Constants.getCSV(Constants.mediasFile);
        String[] attributes = csv.get(id+1).split(",");
        String newLine = attributes[0] + "," + attributes[1] + "," + attributes[2] + "," + attributes[3] + "," + attributes[4] + "," + attributes[5] + "," + this.score +  "," + reviewNum;
        // adding audience and actors if it's a video
        if(attributes.length == 9)
            newLine += "," + attributes[8] + ",";
        else if(attributes.length == 10)
            newLine += "," + attributes[8] + "," + attributes[9];
        // updating the line of the media
        csv.set(id+1, newLine);
        // updating the media csv file
        Constants.writeCSV(Constants.mediasFile, csv);
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
        final Media other = (Media) obj;
        if (this.id == other.getId()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n Description: " + description ;
    }

    /* ------------------------------------------------------------------------- *
     * This method loads all the medias from a csv file given. This csv must
     * be well formatted (follows the header format). It requires the list of all
     * creators ordered by id as well as the list of all actors ordered by
     * id.
     *
     * PARAMETER
     * fileName         The name of the csv file
     * singers          The list of all singers
     * directors        The list of all directors
     * authors          The list of all authors
     * actors           The list of all actors
     *
     * RETURN
     * medias           The list of medias as loaded from the csv
     *
     */
    public static ArrayList<Media> load(String fileName, List<Singer> singers, List<Director> directors, List<Author> authors, List<Actor> actors){
        ArrayList<Media> medias = new ArrayList<>();
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
                String[] creatorsID = attributes[5].split(";");
                Media media = null;
                // checking which type of media it is and creating the corresponding object
                switch (attributes[1]){
                    case "0":
                        // reading list of directors
                        ArrayList<Director> dList = new ArrayList<>();
                        for (String c: creatorsID){
                            dList.add(directors.get(Integer.parseInt(c)));
                        }
                        // reading list of actors (if any)
                        ArrayList<Actor> aList = new ArrayList<>();
                        if(attributes.length > 9){
                            String[] actorsID = attributes[9].split(";");
                            for (String c: actorsID){
                                aList.add(actors.get(Integer.parseInt(c)));
                            }
                        }
                        // creating new media as a video
                        media = new Video(Integer.parseInt(attributes[0]), attributes[2], attributes[3], attributes[4], dList, Double.parseDouble(attributes[6]), Integer.parseInt(attributes[7]), attributes[8], aList);
                        break;
                    case "1":
                        // reading list of singers
                        ArrayList<Singer> sList = new ArrayList<>();
                        for (String c: creatorsID){
                            sList.add(singers.get(Integer.parseInt(c)));
                        }
                        // creating new media as a music
                        media = new Music(Integer.parseInt(attributes[0]), attributes[2], attributes[3], attributes[4], sList, Double.parseDouble(attributes[6]), Integer.parseInt(attributes[7]));
                        break;
                    case "2":
                        // reading list of authors
                        ArrayList<Author> auList = new ArrayList<>();
                        for (String c: creatorsID){
                            auList.add(authors.get(Integer.parseInt(c)));
                        }
                        // creating new media as a book
                        media = new Book(Integer.parseInt(attributes[0]), attributes[2], attributes[3], attributes[4], auList, Double.parseDouble(attributes[6]), Integer.parseInt(attributes[7]));
                        break;
                    default:
                        break;
                }

                // adding media into ArrayList
                medias.add(media);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return medias;
    }

    /* ------------------Getters--------------------- */

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgPath(){
        return imgPath;
    }

    public int getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public int getReviewNum() {
        return reviewNum;
    }

}
