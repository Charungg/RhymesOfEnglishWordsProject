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
        int indexFinalStressedVowel = findFinalStressedVowelIndex();
        int otherIndexFinalStressedVowel = other.findFinalStressedVowelIndex();

        // Returns False If There is No Vowels Found.
        // Also returns False If Pronunciation Is Length Zero Because No Vowels Can Be Found In It.
        if (indexFinalStressedVowel == -1 || otherIndexFinalStressedVowel == -1){
            return false;
        }

        IPhoneme phoneme = listOfPhoneme.get(indexFinalStressedVowel);
        IPhoneme otherPhoneme = (other.getPhonemes()).get(other.findFinalStressedVowelIndex());


        // Checks If The Araphabet Is The Same And If The Constant Are The Same If There Is Any.
        return (phoneme.hasSameArpabet(otherPhoneme) && isSameConstant(indexFinalStressedVowel,otherIndexFinalStressedVowel,listOfPhoneme,other.getPhonemes()));
    }


    public boolean isSameConstant(int index, int otherIndex, List<IPhoneme> phoneme, List<IPhoneme> otherPhoneme){
        int phonemeFinalIndex = phoneme.size() - 1;
        int otherPhonemeFinalIndex = otherPhoneme.size() - 1;
        boolean isSameConstant = true;

        // If There Is Nothing After The Final Stressed Vowel.
        if((index == phonemeFinalIndex && otherPhonemeFinalIndex == phonemeFinalIndex)){
            return true;
        }

        int gap = phonemeFinalIndex - index;
        int gap2 = otherPhonemeFinalIndex - otherIndex;

        if (gap>gap2){
            for (int i = 0; i<gap2 ; i++){
                if (!(phoneme.get(index + i).hasSameArpabet(otherPhoneme.get(otherIndex + 1)))){
                    return false;
                }
            }
        }

        else{
            for (int i = 0; i<gap; i++){
                if (!(phoneme.get(index + i).hasSameArpabet(otherPhoneme.get(otherIndex + 1)))){
                    return false;
                }
            }
        }

        return true;
    }
}
