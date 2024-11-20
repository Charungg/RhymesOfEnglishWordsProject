/** @author Charlie Cheung
 *  Phoneme class which creates an object that holds an arpabet and stress value,
 *  as a pair they articulate speech sounds.
 */

package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.Arpabet;
import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;

public class Phoneme implements IPhoneme{

    private Arpabet phoneme; // Holds the arpabet enum constant.

    private int stress; // Holds the stress value.


    /**
     * Constructor to instantiate the phoneme class
     * @param phoneme stores the arpabet constant to represent a sound.
     * @param stress stores the stress value to indicate stress level.
     * @throws IllegalArgumentException if either the phoneme and stress values are invalid.
     */

    public Phoneme(Arpabet phoneme, int stress){
        this.phoneme = phoneme;
        this.stress = stress;

        // Phoneme passed must not be null.
        if (!isPhonemeValid()){
            throw new IllegalArgumentException("Phoneme Value Cannot be NULL");
        }

        // Stress value passed must be a valid stress value.
        else if (!isStressValueValid()){
            throw new IllegalArgumentException("Stress Value Is Invalid (Must be either -1,0,1,2");
        }

        // A Vowel Phoneme Passed Must Not Have A Negative Stress Value.
        else if (phoneme.isVowel() && !isVowelPhonemeStressValueValid()){
            throw new IllegalArgumentException("Vowel Phoneme Stress Value Must Not Be -1");
        }

        // A Non-Vowel Phoneme Passed Must Have A Negative Stress Value.
        else if (!(phoneme.isVowel()) && !isNonVowelPhonemeStressValueValid()){
            throw new IllegalArgumentException("Non-Vowel Phoneme Stress Value Must Not Have A Positive Stress Value");
        }
    }


    /**
     * Gets the phoneme's arpabet constant.
     * @return arpabet from the phoneme attribute.
     */
    public Arpabet getArpabet() {
        return phoneme;
    }


    /**
     * Gets the stress value.
     * @return stress value from the stress attribute.
     */
    public int getStress() {
        return stress;
    }


    /**
     * Checks if the passed phoneme has the same arpabet current phoneme object.
     * @param other passed phoneme in order to have access to its arpabet constant.
     * @throws IllegalArgumentException if the passed phoneme is null.
     * @return true if both phonemes has the same arpabet, else returns false.
     */
    public boolean hasSameArpabet(IPhoneme other) {

        // Passed Phoneme Must Not Be Null.
        if (other == null){
            throw new IllegalArgumentException("Arpabet Passed Is Cannot Be Null");
        }

        // Returns True If Both Arpabet Is The Same, Else Returns False.
        return (phoneme.equals(other.getArpabet()));
    }


    /**
     * Checks if the passed phoneme is not null.
     * @return true if passed phoneme is not null.
     */
    public boolean isPhonemeValid(){
        return phoneme != null;
    }


    /**
     * Checks if the stress value is valid
     * @return true if the stress value is either being -1, 0, 1 or 2, else returns false.
     */
    public boolean isStressValueValid(){
        return stress >= -1 && stress <= 2;
    }


    /**
     * Checks if the vowel's stress value is valid.
     * @return true if the stress value is not -1.
     */
    public boolean isVowelPhonemeStressValueValid(){
        return (stress != -1);
    }


    /**
     * Checks if the non-vowel stress value is -1.
     * @return true if the stress value is -1.
     */
    public boolean isNonVowelPhonemeStressValueValid() {
        return (stress == -1);
    }
}

