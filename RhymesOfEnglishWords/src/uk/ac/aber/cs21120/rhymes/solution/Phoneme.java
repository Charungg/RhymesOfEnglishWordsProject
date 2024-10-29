// Java Collections Framework Limitation
// Only Use java.util, java.lang
// Any resource you use must be acknowledged in the document

package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.Arpabet;
import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;

// Minor problem with IllegalArgumentException requiring java.lang.Throwable.
// An understanding wordings better such as Vowels and NonVowels valid stress values

public class Phoneme implements IPhoneme{

    private Arpabet phoneme;
    private int stress;

    public Phoneme(Arpabet phoneme, int stress) throws IllegalArgumentException{
        this.phoneme = phoneme;
        this.stress = stress;

        //
        // TO DO:
        // You can make this look better
        //

        if (!isPhonemeValid()){
            throw new IllegalArgumentException("Phoneme Value Cannot be NULL");
        }

        else if (!isStressValueValid()){
            throw new IllegalArgumentException("Stress Value Is Invalid (Must be either -1,0,1,2");
        }

        else if (phoneme.isVowel() && !isVowelPhonemeStressValueValid()){
            throw new IllegalArgumentException("Vowel Phoneme Stress Value Must Not Be -1");
        }

        else if (!(phoneme.isVowel()) && !isNonVowelPhonemeStressValueValid()){
            throw new IllegalArgumentException("Non Vowel Phoneme Stress Value Must Be -1");
        }
    }


    public Arpabet getArpabet() {
        return phoneme;
    }


    public int getStress() {
        return stress;
    }


    public boolean hasSameArpabet(IPhoneme other) {
        if (other == null){
            throw new IllegalArgumentException("Arpabet Passed Is Null");
        }

        return (phoneme.equals(other.getArpabet()));
    }


    public boolean isPhonemeValid(){
        return phoneme != null;
    }


    public boolean isStressValueValid(){
        int validStressValue[] = {-1,0,1,2};

        for (int stressValue: validStressValue){
            if (stress == stressValue){
                return true;
            }
        }

        return false;
    }


    public boolean isVowelPhonemeStressValueValid(){
        return (stress != -1);
    }


    public boolean isNonVowelPhonemeStressValueValid() {
        return (stress == -1);
    }
}

