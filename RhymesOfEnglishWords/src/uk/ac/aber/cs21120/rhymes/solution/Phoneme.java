// Java Collections Framework Limitation
// Only Use java.util, java.lang
// Any resource you use must be acknowledged in the document

package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.Arpabet;
import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;

// Minor problem with IllegalArgumentException requiring java.lang.Throwable.
// An understanding wordings better such as Vowels and NonVowels valid stress values

// Phoneme Class Which Creates Objects That Holds A Arpabet And Stress Value,
// As A Pair They Articulate Speech Sounds.
public class Phoneme implements IPhoneme{

    // Holds The Phoneme Enum Constant.
    private Arpabet phoneme;

    // Holds The Stress Value Tied To The Phoneme.
    private int stress;

    // Constructor For Phoneme That Will Instantiate A Arpabet And Stress Value
    public Phoneme(Arpabet phoneme, int stress){
        this.phoneme = phoneme;
        this.stress = stress;

        // Phoneme Passed Must Not Be Null.
        if (!isPhonemeValid()){
            throw new IllegalArgumentException("Phoneme Value Cannot be NULL");
        }

        // Stress Value Passed Must Be A Valid Stress Value.
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


    // Returns The Phoneme's Arpabet Attribute.
    public Arpabet getArpabet() {
        return phoneme;
    }

    // Returns The Stress Value Tied To Phoneme.
    public int getStress() {
        return stress;
    }

    // Checks If The Passed Phoneme Has The Same Arpabet As The Attribute Phoneme.
    public boolean hasSameArpabet(IPhoneme other) {

        // Passed Phoneme Must Not Be Null.
        if (other == null){
            throw new IllegalArgumentException("Arpabet Passed Is Cannot Be Null");
        }

        // Returns True If Both Arpabet Is The Same, Else Returns False.
        return (phoneme.equals(other.getArpabet()));
    }

    // Checks If The Passed Phoneme Is Not Null.
    public boolean isPhonemeValid(){
        return phoneme != null;
    }


    // Checks If The Stress Value Is Valid By Either Being -1, 0 , 1 Or 2.
    public boolean isStressValueValid(){
        return stress >= -1 && stress <= 2;
    }


    // Checks If The Vowel Stress Value Is Not -1.
    public boolean isVowelPhonemeStressValueValid(){
        return (stress != -1);
    }


    // Checks If The Non-Vowel Stress Value Is -1.
    public boolean isNonVowelPhonemeStressValueValid() {
        return (stress == -1);
    }
}

