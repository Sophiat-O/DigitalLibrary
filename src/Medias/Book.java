/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T.S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Book class which
    * inherits from the class Media.
    ------------------------------------------------------------------------
 */

package Medias;

import Persons.Creators.Author;

import java.util.ArrayList;

/* ------------------------------------------------------------------------- *
 * Book class
 * Attributes:
 *    authors: the list of authors of the book
 */
public class Book extends Media{
    private ArrayList<Author> authors;

    // Public constructor
    public Book(int id, String title, String imgPath, String description, ArrayList<Author> authors, double score, int reviewNum){
        super(id, title, imgPath, description, score, reviewNum);
        this.authors = authors;
    }

    /* ------------------Getters--------------------- */

    public ArrayList<Author> getAuthors() {
        return authors;
    }
}