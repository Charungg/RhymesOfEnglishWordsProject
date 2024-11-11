package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.Arpabet;
import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;
import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;

import java.util.ArrayList;
import java.util.List;

// Upon completing all junit test I looked back to see what data structure I should use.
// ArrayList Is Used Over LinkedList Both Have A Search Function Of O(n)
// But As For Adding Elements ArrayList Has O(1) But Worse-Case O(n)
// Whereas LinkedList Will Always Have O(n).
// By Default ArrayList When Installed Has Capacity Of 10 Which Mostly More Than Enough.
// ArrayList Is Better For Permanently Storing Data And Accessing Data

// Pronunciation Class
public class Pronunciation implements IPronunciation {

    // Holds A ArrayList Of Phonemes Which Articulate Speeches Sounds Of A Word.
    private List<IPhoneme> listOfPhoneme = new ArrayList<>();

    // Adds The Passed Phoneme To The End Of The listOfPhoneme ArrayList.
    @Override
    public void add(IPhoneme phoneme) {

        // Checks If The Passed Phoneme Is Null.
        if (phoneme == null){
            throw new IllegalArgumentException("Phoneme Passed Null");
        }

        // Adds Phoneme To The End Of listOfPhoneme.
        listOfPhoneme.add(phoneme);
    }

    // Returns The listOfPhoneme ArrayList.
    @Override
    public List<IPhoneme> getPhonemes() {
        return listOfPhoneme;
    }


    // Returns The Index Of The Final Stressed Vowel Within The listOfPhoneme ArrayList.
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


    // Returns The Index With Highest Stress Value With Priority To finalIndex
    public int findHighestStressedValueIndex(int finalIndex, int index){

        // Gets The Stress Value From Both Indexes.
        int finalStressValue = listOfPhoneme.get(finalIndex).getStress();
        int stressValue = listOfPhoneme.get(index).getStress();

        // If The StressValue Is A Primary Stress OR StressValue
        // Is A Secondary Stress And finalStressValue Is 0.
        // Return index Of StressValue.
        if(stressValue == 1 || stressValue > finalStressValue){
          return index;
        }

        // Return finalIndex Since stressValue Is Equals Or Less Than finalStressValue.
        return finalIndex;
    }

    @Override
    public boolean rhymesWith(IPronunciation other) {
        IPhoneme phoneme;
        IPhoneme otherPhoneme;

        int indexFinalStressedVowel = findFinalStressedVowelIndex();
        int otherIndexFinalStressedVowel = other.findFinalStressedVowelIndex();

        // Returns False If There is No Vowels Found.
        // Also returns False If Pronunciation Is Length Zero Because No Vowels Can Be Found In It.
        if (indexFinalStressedVowel == -1 || otherIndexFinalStressedVowel == -1){
            return false;
        }

        // Gets Arphabet Of Both Phoneme Final Stressed Index;
        phoneme = listOfPhoneme.get(indexFinalStressedVowel);
        otherPhoneme = (other.getPhonemes()).get(other.findFinalStressedVowelIndex());


        // Checks If The Arpabet Is The Same And If The Constant Are The Same.
        return (phoneme.hasSameArpabet(otherPhoneme) && isSameConstant(indexFinalStressedVowel,otherIndexFinalStressedVowel,other.getPhonemes()));
    }


    public boolean isSameConstant(int finalStressedIndex, int otherFinalStressedIndex, List<IPhoneme> otherListOfPhoneme){
        int pronunciationConstantSize = listOfPhoneme.size() - finalStressedIndex;
        int otherPronunciationConstantSize = otherListOfPhoneme.size() - otherFinalStressedIndex;

        if (pronunciationConstantSize != otherPronunciationConstantSize){
            return false;
        }

        for (int check = 0; check<pronunciationConstantSize; check++){
            if (!((listOfPhoneme.get(finalStressedIndex + check)).hasSameArpabet((otherListOfPhoneme.get(otherFinalStressedIndex + check))))){
                return false;
            }
        }

        return true;
    }
}
