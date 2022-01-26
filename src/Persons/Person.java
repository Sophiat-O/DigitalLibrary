/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Person class which
    * is the parent class for all the categories of persons in our system
    * this includes subscribers, creators, and actors.
    ------------------------------------------------------------------------
 */
package Persons;

/* ------------------------------------------------------------------------- *
 * Person class
 * Attributes:
 *    id: an id for each instance
 *    fName: the first name of the person
 *    lName: the last name of the person
 */
public abstract class Person {
    protected int id;
    protected String fName;
    protected String lName;

    // Super constructor
    public Person(int id, String fName, String lName){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
    }

    @Override
    public String toString() {
        return "First name: " + fName + "\n Last name: " + lName;
    }

    /* ------------------Getters--------------------- */

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }
}