package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.*;

import java.util.*;


// Hashset if looped would return random data throughout the set
// for example if pronunciation has been added phoneme in the order
// P R EH1 Z AH0 N T but when looping through it displays then in random order
// I fixed this by changing the setUpListOfPronunciation() to returning A Set to a List.

// At first, I kept thinking I needed to use a hashmap but as time went one I realised a HashSet needed
// to be used because Word Class can contain many Pronunciation Class.

// Really hard to balance what class did what and what they all had to do inorder for this Dictionary class to
// Come together.

public class Dictionary implements IDictionary {

    private HashSet<IWord> dictionary = new HashSet<>();

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
        int amountOfPronunciation = 0;
        for (IWord i : dictionary){
            amountOfPronunciation += ((i.getPronunciations()).size());
        }

        return amountOfPronunciation;
    }

    @Override
    public void parseDictionaryLine(String line){
        String lineWord;
        String linePronunciation;
        int wordPronunciationIndexSplit;

        if (line == null){
            throw new IllegalArgumentException("Argument Passed Cannot Be Null");
        }

        line = removeComment(line);
        line = removeBracket(line);

        wordPronunciationIndexSplit = indexBetweenWordAndPronunciation(line);
        lineWord = setWordFromLine(line,wordPronunciationIndexSplit);
        linePronunciation = setPronunciationFromLine(line,wordPronunciationIndexSplit);
        addWordAndPronunciation(lineWord, linePronunciation);
    }

    public String removeComment(String line){
        int indexOfComment = line.indexOf('#');
        if (indexOfComment != -1){
            return line.substring(0,indexOfComment);
        }
        return line;
    }

    public String removeBracket(String line){
        String firstHalf;
        String secondHalf;
        int indexOfBracket = line.indexOf('(');

        if (indexOfBracket != -1){
            firstHalf = line.substring(0,indexOfBracket);
            secondHalf = line.substring(indexOfBracket + 3);
            return (firstHalf.concat(secondHalf));
        }

        return line;
    }

    public int indexBetweenWordAndPronunciation(String line){
        return (line.indexOf(" "));
    }

    public String setWordFromLine(String line, int split){
        return (line.substring(0, split));
    }

    public String setPronunciationFromLine(String line, int split){
        return (line.substring(split + 1));
    }

    public void addWordAndPronunciation(String lineWord, String linePronunciation) {
        IWord wordObject = getWord(lineWord);

        // Breaks Down The Pronunciation Line Into A List Of Phoneme.
        List<IPhoneme> listOfPhoneme = setUpListOfPronunciation(linePronunciation);

        // If The Word Does Not Exist Within The Dictionary.
        if (wordObject == null) {

            // Add New Word To Dictionary.
            addWord(new Word(lineWord));

            // Get The New Word Object From Dictionary.
            wordObject = getWord(lineWord);

            // List Of Phoneme Will Be Added To Pronunciation Object And Passed To The Word Object.
            addingPhonemeToPronunciation(wordObject,listOfPhoneme);
        }

        // Adds Pronunciation To Existing Word In The Dictionary.
        else {
            // List Of Phoneme Will Be Added To Pronunciation Object And Passed To The Word Object.
            addingPhonemeToPronunciation(wordObject,listOfPhoneme);
        }
    }

    public void addingPhonemeToPronunciation(IWord wordObject, List<IPhoneme> listOfPhoneme){
        IPronunciation pronunciationObject = new Pronunciation();

        for (IPhoneme currentPhoneme: listOfPhoneme){
            pronunciationObject.add(currentPhoneme);
        }

        wordObject.addPronunciation(pronunciationObject);
    }


    public List<IPhoneme> setUpListOfPronunciation(String linePronunciation){
        List<IPhoneme> listOfPhoneme = new LinkedList<>();

        for (String phoneme: linePronunciation.split(" ")){
            listOfPhoneme.add((createPhoneme(phoneme)));
        }

        return listOfPhoneme;
    }

    public Phoneme createPhoneme(String line){
        Arpabet arpabetValue;
        int stressValue;

        arpabetValue = getArpabetValue(line);
        stressValue = getStressValue(line);

        return (new Phoneme(arpabetValue,stressValue));
    }

    public int getStressValue(String line){
        int stressValue = -1;

        // Removes All Non-Numerical Characters From The String.
        // With The Stress Value Remaining As Long It Contains A Stress Value.
        line = line.replaceAll("[a-z]|[A-Z]","");

        // Turns String To An Integer If It Contains A Stress Value.
        if (!line.isEmpty()){
            stressValue = Integer.parseInt(line);
        }

        // Returns -1 As Stress Value If It Does Not Contain Stress.
        return stressValue;
    }

    public Arpabet getArpabetValue(String line){
        Arpabet arpabet;

        // Removes All Numerical Characters From The String.
        line = line.replaceAll("[0-9]","");

        // Change String To A Arpabet Enum.
        // Throws An Exception If Enum Constant Does Not Exist
        // Meaning Given Argument Is Invalid.
        arpabet = Arpabet.valueOf(line);

        return arpabet;
    }

    @Override
    public void loadDictionary(String fileName) {

    }

    @Override
    public Set<String> getRhymes(String word) {
        return null;
    }
}
