/** @author Charlie Cheung
 *  Word class that holds English word corresponding to a word object and
 *  a set of pronuncition that is tied to the word object.
 */

package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;
import uk.ac.aber.cs21120.rhymes.interfaces.IWord;

import java.util.HashSet;
import java.util.Set;

public class Word implements IWord {

    private String word; // Holds the English word.

    // Holds a set of pronunciation
    private Set<IPronunciation> setOfPronunciation = new HashSet<>();


    /**
     * Constructor to instantiate the word object
     * @param word stores the English word tied to this word object.
     */
    public Word(String word){
        this.word = word;
    }


    /**
     * Gets the English word.
     * @return the English word from the word attribute.
     */
    @Override
    public String getWord() {
        return word;
    }


    /**
     * Adds passed pronunciation object to the set of pronunciation.
     * @param pronunciation in order to have access to the other pronunciation object.
     * @throws IllegalArgumentException if the passed pronunciation is null.
     */
    @Override
    public void addPronunciation(IPronunciation pronunciation) {
        // If the passed pronunciation is null.
        // Then an Illegal Argument Exception will be thrown.
        if (pronunciation == null){
            throw new IllegalArgumentException("Argument Passed Cannot Be Null");
        }

        // Adds the other pronunciation object to the set of pronunciation.
        setOfPronunciation.add(pronunciation);
    }


    /**
     * Gets the set of pronunciation that represent the passed English word.
     * @return the set of pronunciation from the setOfPronunciation attribute.
     */
    @Override
    public Set<IPronunciation> getPronunciations() {
        return setOfPronunciation;
    }
}
