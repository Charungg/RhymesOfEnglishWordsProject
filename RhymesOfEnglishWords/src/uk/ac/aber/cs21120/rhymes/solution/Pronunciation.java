/** @author Charlie Cheung
 *  Pronciation class contains the list of phoneme which represent a list of sounds which represent a word.
 *  Also contains the rhyme with functions which checks if two pronunciation class rhymes.
 */

package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.Arpabet;
import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;
import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;

import java.util.ArrayList;
import java.util.List;

public class Pronunciation implements IPronunciation {

    // Holds A ArrayList Of Phonemes Which Articulate Speeches Sounds Of A Word.
    private List<IPhoneme> listOfPhoneme = new ArrayList<>();


    /**
     * Adds the passed phoneme to the end of the listOfPhoneme ArrayList.
     * @param phoneme the phoneme to add.
     * @throws IllegalArgumentException if the passed phoneme is null.
     */
    @Override
    public void add(IPhoneme phoneme) {

        // Checks If The Passed Phoneme Is Null.
        if (phoneme == null){
            throw new IllegalArgumentException("Phoneme Passed Null");
        }

        // Adds Phoneme To The End Of listOfPhoneme.
        listOfPhoneme.add(phoneme);
    }


    /**
     * Gets the list of phonemes.
     * @return the list of phonemes from the listOfPhoneme attribute.
     */
    @Override
    public List<IPhoneme> getPhonemes() {
        return listOfPhoneme;
    }


    /**
     * Gets the final stressed vowel index from the listOfPhoneme.
     * @return the index of the final stressed vowel.
     */
    @Override
    public int findFinalStressedVowelIndex() {
        IPhoneme phoneme;
        Arpabet arpabet;
        int stressValue;
        // If No Vowels Are Found -1 Will Be Returned At The End Of The Function.
        int finalStressedVowelIndex = -1;

        // Loops From Back To Font Of The listOfPhoneme Indexes.
        for (int index = listOfPhoneme.size() - 1; index >= 0; index--){

            // Access Phoneme From The List And Get Arpabet And Stress Values.
            phoneme = listOfPhoneme.get(index);
            arpabet = phoneme.getArpabet();
            stressValue = phoneme.getStress();

            // If Primary Stress Is Found Return The Index Where It Was Found.
            if (stressValue == 1){
                return index;
            }

            // If Finding A Vowel For The First Time Then
            // finalStressedVowelIndex Will Hold Its Index
            else if (finalStressedVowelIndex == -1 && arpabet.isVowel()){
                finalStressedVowelIndex = index;
            }

            // If A Vowel Has Been Found Then It Will Hold The Index Of The Higher Stress Value
            // Compare To The finalStressedVowel Phoneme With Priority To The Former Vowel Found.
            else if (arpabet.isVowel()){
                finalStressedVowelIndex = findHighestStressedValueIndex(finalStressedVowelIndex,index);
            }
        }

        // Returns -1 If No Vowel Was Found.
        return finalStressedVowelIndex;
    }


    /**
     * Compares two phoneme from the listOfPhoneme stress value.
     * @param finalIndex within the listOfPhoneme which is the current finals stressed vowel index.
     * @param index within the listOfPhoneme which is a vowel.
     * @return the index that contains the highest stress value with priority to finalIndex.
     */
    public int findHighestStressedValueIndex(int finalIndex, int index){

        // Gets The Stress Value From Both Indexes.
        int finalStressValue = getStressValueFromIndex(finalIndex);
        int stressValue = getStressValueFromIndex(index);

        // If The StressValue Is A Primary Stress OR StressValue
        // Is A Secondary Stress And finalStressValue Is 0.
        // Return index Of StressValue.
        if(stressValue == 1 || stressValue > finalStressValue){
          return index;
        }

        // Return finalIndex Since stressValue Is Equals Or Less Than finalStressValue.
        return finalIndex;
    }


    /**
     * Get stress value from listOfPhoneme with passed index.
     * @param index within the listOfPhoneme to get stress value.
     * @return Phoneme stress value.
     */
    public int getStressValueFromIndex(int index){
        return listOfPhoneme.get(index).getStress();
    }


    /**
     * Checks if the current pronunciation rhymes with the other pronunciation.
     * @param other to have access to the other pronunciation object.
     * @throws IllegalArgumentException passed pronunciation cannot be null.
     * @return true if the current listOfPhonemes matches with other pronunciation listOfPhonemes.
     */
    @Override
    public boolean rhymesWith(IPronunciation other) {
        if (other == null){
            throw new IllegalArgumentException("Passed Pronunciation Cannot Be Null");
        }
        // Holds The Phoneme Of The Current Pronunciation And Passed Phoneme.
        IPhoneme phoneme;
        IPhoneme otherPhoneme;

        // Holds The Index Position Of Both Pronunciation Final Stressed Vowel Index.
        int indexFinalStressedVowel = findFinalStressedVowelIndex();
        int otherIndexFinalStressedVowel = other.findFinalStressedVowelIndex();

        // Returns False If There is No Vowels Found.
        // Also returns False If Pronunciation Is Length Zero Because No Vowels Can Be Found In It.
        if (indexFinalStressedVowel == -1 || otherIndexFinalStressedVowel == -1){
            return false;
        }

        // Gets Arpabet Of Both Phoneme Final Stressed Index;
        phoneme = getPhonemeFromIndex(indexFinalStressedVowel);
        otherPhoneme = (other.getPhonemes()).get(otherIndexFinalStressedVowel);

        // Checks If The Arpabet Is The Same And If The Constant Are The Same.
        return (phoneme.hasSameArpabet(otherPhoneme) && isSoundsAfterFinalStressedVowelSame(indexFinalStressedVowel,otherIndexFinalStressedVowel,other.getPhonemes()));
    }


    /**
     * Get Phoneme from listOfPhoneme with passed index.
     * @param index within the listOfPhoneme to get Phoneme.
     * @return Phoneme.
     */
    public IPhoneme getPhonemeFromIndex(int index){
        return listOfPhoneme.get(index);
    }


    /**
     * Checks if the sounds after both finals stressed vowels are the same.
     * @param finalStressedIndex is the index of the current listOfPhonemes final stressed index.
     * @param otherFinalStressedIndex is the index of the other listOfPhonemes final stressed index.
     * @param otherListOfPhoneme in order to have access to the other pronunciation listOfPhonemes.
     * @return true if both pronunciation listOfPhonemes rhymes, else return false.
     */
    public boolean isSoundsAfterFinalStressedVowelSame(int finalStressedIndex, int otherFinalStressedIndex, List<IPhoneme> otherListOfPhoneme){
        // Find The Remaining Sounds After Final Stressed Vowel.
        int remainingSounds = listOfPhoneme.size() - finalStressedIndex;
        int otherRemainingSounds = otherListOfPhoneme.size() - otherFinalStressedIndex;

        // If The Remaining Sounds Are Not The Same Then Return False.
        if (remainingSounds != otherRemainingSounds){
            return false;
        }

        // Loops The Remaining Sounds.
        for (int index = 0; index<remainingSounds; index++){

            // If Any Remaining Sounds Does Not Have The Same Arpabet Return False.
            if (!((listOfPhoneme.get(finalStressedIndex + index)).hasSameArpabet((otherListOfPhoneme.get(otherFinalStressedIndex + index))))){
                return false;
            }
        }

        // If The Remaining Sounds Have The Same Arpabet Return True.
        return true;
    }
}
