/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: T S
    *          Sophie S
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the Creator class which
    * inherits from the Person class.
    ------------------------------------------------------------------------
 */
package Persons.Creators;

import Persons.Person;

public abstract class Creator extends Person {

    // Super constructor
    public Creator(int id, String fName, String lName){
        super(id, fName, lName);
    }

}
