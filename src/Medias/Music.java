/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Music class which
    * inherits from the class Media.
    ------------------------------------------------------------------------
 */
package Medias;

import Persons.Creators.Singer;

import java.util.ArrayList;

public class Music extends Media{
    private ArrayList<Singer> singers;

    // Public constructor<
    public Music(int id, String title, String imgPath, String description, ArrayList<Singer> singers, double score, int reviewNum){
        super(id, title, imgPath, description, score, reviewNum);
        this.singers = singers;
    }

    /* ------------------Getters--------------------- */

    public ArrayList<Singer> getSingers() {
        return singers;
    }
}