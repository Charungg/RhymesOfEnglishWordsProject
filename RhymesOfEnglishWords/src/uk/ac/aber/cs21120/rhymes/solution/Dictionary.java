package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.IDictionary;
import uk.ac.aber.cs21120.rhymes.interfaces.IWord;

import java.util.HashSet;
import java.util.Set;

public class Dictionary implements IDictionary {

    private HashSet<IWord> dictionary= new HashSet<>();

    @Override
    public IWord getWord(String word) {
        for (IWord dictionaryWord : dictionary){
            if ((dictionaryWord.getWord().equals(word))){
                return dictionaryWord;
            }
        }

        return null;
    }

    @Override
    public void addWord(IWord word) {
//        Why DOes THis Not WOrk??
//        if (dictionary.contains(word)){
//            throw new IllegalArgumentException("This Word Already Exist Within The Dictionary");
//        }


        for (IWord i : dictionary){
            if (i.getWord().equals(word.getWord())){
                throw new IllegalArgumentException("This Word Already Exist Within The Dictionary");
            }
        }

        dictionary.add(word);
    }

    @Override
    public int getWordCount() {
        return dictionary.size();
    }

    @Override
    public int getPronunciationCount(){
        int amountOfProunciation = 0;
        for (IWord i : dictionary){
            amountOfProunciation += ((i.getPronunciations()).size());
        }

        return amountOfProunciation;
    }

    @Override
    public void parseDictionaryLine(String line) {

    }

    @Override
    public void loadDictionary(String fileName) {

    }

    @Override
    public Set<String> getRhymes(String word) {
        return null;
    }
}
