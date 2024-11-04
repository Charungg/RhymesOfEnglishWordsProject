package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.IPronunciation;
import uk.ac.aber.cs21120.rhymes.interfaces.IWord;

import java.util.HashSet;
import java.util.Set;

public class Word implements IWord {

    private String word;

    private Set<IPronunciation> setOfPronunciation = new HashSet<>();


    public Word(String word){
        this.word = word;
    }
    @Override
    public String getWord() {
        return word;
    }

    @Override
    public void addPronunciation(IPronunciation pronunciation) {
        if (pronunciation == null){
            throw new IllegalArgumentException("Argument Passed Cannot Be Null");
        }

        setOfPronunciation.add(pronunciation);
    }

    @Override
    public Set<IPronunciation> getPronunciations() {
        return setOfPronunciation;
    }
}
