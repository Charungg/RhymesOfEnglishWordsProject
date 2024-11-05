package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.IPhoneme;
import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// To Do:
// If You Have Time Use Just Array And See If It's Nicer.

public class Pronunciation implements IPronunciation {

    private List<IPhoneme> listOfPhoneme = new LinkedList<>();

    // This Will Hold The
    private Map<Integer, Integer> StressedVowelIndex = new HashMap<>();

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
        IPhoneme selectedPhoneme;
        int selectedPhonemeStressValue;

        // This Iterates Through The List Of Phoneme But Backwards.
        for (int index = listOfPhoneme.size() - 1; index>=0; index--){

            selectedPhoneme = listOfPhoneme.get(index);
            selectedPhonemeStressValue = selectedPhoneme.getStress();

            // This Checks Whether Phoneme Is A Vowel And If
            // Not Then Put -1 Corresponding To That Index.
            if (!isPhonemeAVowel(selectedPhoneme)){
                StressedVowelIndex.put(index,-1);
            }

            // If The Primary Stress Is Found Early Then Return Index Position.
            // This Helps With Efficiency Since It Won't Go Through All The List.
            else if(selectedPhonemeStressValue == 1){
                return index;
            }

            // Each Phoneme Will Correspond To Their Stress Value.
            else{
                StressedVowelIndex.put(index,selectedPhonemeStressValue);
            }
        }

        // Once The Sequence Of Phoneme Highest Stress Value
        // Is Constructed Then The Next Highest Stress Value.
        return getNextHighestStressValue();
    }

    private int getNextHighestStressValue(){
        //
        if(StressedVowelIndex.containsValue(2)){
            for (int key : StressedVowelIndex.keySet()){
                if (StressedVowelIndex.get(key) == 2){
                    return key;
                }
            }
        }

        else if(StressedVowelIndex.containsValue(0)){
            for (int key : StressedVowelIndex.keySet()){
                if (StressedVowelIndex.get(key) == 0){
                    return key;
                }
            }
        }

        return -1;
    }

    @Override
    public boolean rhymesWith(IPronunciation other) {
        return false;
    }

    public boolean isPhonemeAVowel(IPhoneme phoneme){
        return ((phoneme.getArpabet()).isVowel());
    }



}
