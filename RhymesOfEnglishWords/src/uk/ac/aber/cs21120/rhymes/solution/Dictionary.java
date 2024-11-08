package uk.ac.aber.cs21120.rhymes.solution;

import uk.ac.aber.cs21120.rhymes.interfaces.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

// Hashset if looped would return random data throughout the set
// for example if pronunciation has been added phoneme in the order
// P R EH1 Z AH0 N T but when looping through it displays then in random order
// I fixed this by changing the setUpListOfPronunciation() to returning A Set to a List.

// At first, I kept thinking I needed to use a hashmap but as time went one I realised a HashSet needed
// to be used because Word Class can contain many Pronunciation Class.

// Really hard to balance what class did what and what they all had to do inorder for this Dictionary class to
// Come together.

// Loading the dictionary takes an extremely long time
// I was being daft and changed my Hashset into a HashMap
// Increased the speed of loading the dictionary has gotten better but could be improved on.

// The CMU Dict downloaded from the Github contains more word whereas the alternative link contains the non-updates CMU Dict
// Therefore the JUnit testing

// getRhyme worked for most test but that cat Junit test because
// 10 words should rhyme with cat but it can't recognise it.
// I code could only find 67/77 words that rhymed with cat

// I temporarily added regex code to find the remaining 10 words that should rhyme with cat
// by finding all words in the CMU with "K AE1 T$" and compare it to the my list of the rhymes it could find.

// at-bat
// balyeat
// nonfat
// gujarat
// tit-for-tat
// sarratt
// rat-a-tat
// non-fat
// inmarsat
// landsat
// Turns out for at-bat after finding the vowel with stress 1 It didn't return it straight away.
// Instead, it returned the second final stressed vowel.

// Once all the jUnit code worked I worked on optimising the code to run faster
// By changing the addWord function where it checks if the words exist with using .getWord() instead of
// .contains because .containsValue() because .getWord has O(1) whereas .containsValue() has O(n)

// Sub 1 second on my laptop and 600ms on my pc
// While maintaining readability.
public class Dictionary implements IDictionary {

    private Map<String,IWord> dictionary = new HashMap<>();

    @Override
    public IWord getWord(String word) {
        return dictionary.getOrDefault(word,null);
    }

    @Override
    public void addWord(IWord word) {
        // Alternatively You Could Use dictionary.containsValue(word) but .contains has A O(n)
        // Whereas You Could Search For The wordString Which Has A O(1)
        if (dictionary.get(word.getWord()) != null){
            throw new IllegalArgumentException("This Word Already Exist Within The Dictionary");
        }

        dictionary.put(word.getWord(),word);
    }

    @Override
    public int getWordCount() {
        return dictionary.size();
    }

    @Override
    public int getPronunciationCount(){
        int amountOfPronunciation = 0;
        for (IWord word : dictionary.values()){
            amountOfPronunciation += word.getPronunciations().size();
        }

        return amountOfPronunciation;
    }

    @Override
    public void parseDictionaryLine(String line){
        String lineWord;
        String linePronunciation;
        int wordPronunciationIndexSplit;

        line = removeComment(line);
        line = removeBracket(line);

        wordPronunciationIndexSplit = indexBetweenWordAndPronunciation(line);
        lineWord = setWordFromLine(line,wordPronunciationIndexSplit);
        linePronunciation = setPronunciationFromLine(line,wordPronunciationIndexSplit);
        addWordsPronunciation(lineWord, linePronunciation);
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

    public void addWordsPronunciation(String lineWord, String linePronunciation) {
        IWord wordObject = getWord(lineWord);
        IPronunciation pronunciationObject;

        // If The Word Does Not Exist Within The Dictionary.
        if (wordObject == null) {

            // Add New Word To Dictionary.
            addWord(new Word(lineWord));

            // Get The New Word Object From Dictionary.
            wordObject = getWord(lineWord);

        }

        pronunciationObject = linePronunciationToObject(linePronunciation);

        wordObject.addPronunciation(pronunciationObject);
    }



    public IPronunciation linePronunciationToObject(String linePronunciation){
        IPronunciation pronunciationObject = new Pronunciation();

        for (String phoneme: linePronunciation.split(" ")){
            pronunciationObject.add(createPhoneme(phoneme));
        }

        return pronunciationObject;
    }

    public Phoneme createPhoneme(String line){
        Arpabet arpabetValue;
        int stressValue;

        arpabetValue = getArpabetValue(line);
        stressValue = getStressValue(line);

        return (new Phoneme(arpabetValue,stressValue));
    }

    public int getStressValue(String phoneme){
        int stressValue = -1;

        // Removes All Non-Numerical Characters From The String.
        // With The Stress Value Remaining As Long It Contains A Stress Value.
        phoneme = phoneme.replaceAll("[A-Z]","");

        // Turns String To An Integer If It Contains A Stress Value.
        if (!phoneme.isEmpty()){
            stressValue = Integer.parseInt(phoneme);
        }

        // Returns -1 As Stress Value If It Does Not Contain Stress.
        return stressValue;
    }

    public Arpabet getArpabetValue(String phoneme){
        Arpabet arpabet;

        // Removes All Numerical Characters From The String.
        phoneme = phoneme.replaceAll("[0-9]","");

        // Change String To A Arpabet Enum.
        // Throws An Exception If Enum Constant Does Not Exist
        // Meaning Given Argument Is Invalid.
        arpabet = Arpabet.valueOf(phoneme);

        return arpabet;
    }


    @Override
    public void loadDictionary(String fileName) {
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while (((line = reader.readLine())) != null){
                parseDictionaryLine(line);
            }

        }

        catch (IOException e) {
            System.out.println("File Couldn't Be Found");
        }
    }

    @Override
    public Set<String> getRhymes(String word) {
        IWord wordToRhyme = dictionary.get(word);
        Set<IPronunciation> wordToRhymePronunciationList = wordToRhyme.getPronunciations();
        boolean isWordToRhymeFound;

        IWord dictionaryWord;
        Set<IPronunciation> dictionarySetOfPronunciation;

        HashSet<String> rhymes = new HashSet<>();


        for (String dictionaryString: dictionary.keySet()){
            dictionaryWord = dictionary.get(dictionaryString);
            dictionarySetOfPronunciation = dictionaryWord.getPronunciations();
            isWordToRhymeFound = false;

            for (IPronunciation dictionaryPronunciation : dictionarySetOfPronunciation){

                for (IPronunciation pronunciation : wordToRhymePronunciationList){
                    if (pronunciation.rhymesWith(dictionaryPronunciation)){
                        isWordToRhymeFound = true;
                        rhymes.add(dictionaryString);
                        break;
                    }
                }

                if (isWordToRhymeFound){
                    break;
                }
            }
        }
        return rhymes;
    }


}
