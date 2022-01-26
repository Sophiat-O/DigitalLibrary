/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Video class which
    * inherits from the class Media.
    ------------------------------------------------------------------------
 */
package Medias;

import Persons.Actor;
import Persons.Creators.Director;

import java.util.ArrayList;

/* ------------------------------------------------------------------------- *
 * Book class
 * Attributes:
 *    directors: the list of directors of the video
 *    audience: the audience of the video
 *    distribution: the list of actors of the video
 */
public class Video extends Media{
    private ArrayList<Director> directors;
    private String audience;
    private ArrayList<Actor> distribution;

    // Public constructor
    public Video(int id, String title, String imgPath, String description, ArrayList<Director> directors, double score, int reviewNum, String audience, ArrayList<Actor> distribution){
        super(id, title, imgPath, description, score, reviewNum);
        this.directors = directors;
        this.audience = audience;
        this.distribution = distribution;
    }

    /* ------------------Getters--------------------- */

    public ArrayList<Director> getDirectors() {
        return directors;
    }

    public String getAudience() {
        return audience;
    }

    public ArrayList<Actor> getDistribution() {
        return distribution;
    }
}
