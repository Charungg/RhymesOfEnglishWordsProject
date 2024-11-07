package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;
import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Pronunciation implements IPronunciation {

    private List<IPhoneme> listOfPhoneme = new LinkedList<>();

    @Override
    public void add(IPhoneme phoneme) {
        if (phoneme == null){
            throw new IllegalArgumentException("Phoneme Passed Null");
        }

        listOfPhoneme.add(phoneme);
    }


    @Override
    public List<IPhoneme> getPhonemes() {
        return listOfPhoneme;
    }


    @Override
    public int findFinalStressedVowelIndex() {
        IPhoneme phoneme;
        int stressValue;
        // If No Vowels Are Found -1 Will Be Returned.
        int finalStressedVowelIndex = -1;

        for (int index = listOfPhoneme.size() - 1; index >= 0; index--){
            phoneme = listOfPhoneme.get(index);
            stressValue = phoneme.getStress();

            if (finalStressedVowelIndex == -1 && isPhonemeAVowel(phoneme)){
                finalStressedVowelIndex = index;
            }

            else if (stressValue == 1){
                return index;
            }

            else if (isPhonemeAVowel(phoneme)){
                finalStressedVowelIndex = findHighestStressedValueIndex(finalStressedVowelIndex,index);
            }
        }

        return finalStressedVowelIndex;
    }

    public boolean isPhonemeAVowel(IPhoneme phoneme){
        return ((phoneme.getArpabet()).isVowel());
    }

    public int findHighestStressedValueIndex(int finalIndex, int index){
       int finalStressValue = listOfPhoneme.get(finalIndex).getStress();
       int stressValue = listOfPhoneme.get(index).getStress();

       if(stressValue > finalStressValue || stressValue == 1){
          return index;
       }

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


        // Checks If The Araphabet Is The Same And If The Constant Are The Same.
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
