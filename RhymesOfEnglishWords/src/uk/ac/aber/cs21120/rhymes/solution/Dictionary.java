/** @author Charlie Cheung
 *  Dictionary class which reads ~135,000 words from the CMU dictionary and stores it as a HashMap.
 *  For every line read from the CMU dictionary it is either a new word or new pronunciation and be added acordingly to the dictionary HashMap.
 *  Contains function to remove comments or remove duplication indicate from the CMU lines.
 *  Rhyme functions which returns all English words that rhymes with a given word.
 */

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

    // Holds The Word String As The Key And The Word Object That Correspond To It
    private Map<String,IWord> dictionary = new HashMap<>();

    /**
     * Gets the word object which tied to the passed English word.
     * @param word the spelling of the English word
     * @return a word object if it can be found from the dictionary HashMap else returns null.
     */
    @Override
    public IWord getWord(String word) {
        // Returns the word object but if nothing is found return null.
        return dictionary.getOrDefault(word,null);
    }


    /**
     * Add word object to the dictionary HashMap if it doesn't exit.
     * @param word is the word objects checked to see if it can be added to the dictionary.
     * @throws IllegalArgumentException if the word object English word already exit within the dictionary.
     */
    @Override
    public void addWord(IWord word) {
        // Dictionary checks if the passed word objects exist within itself.
        // If it doesn't exist then an argument will be thrown.
        if (dictionary.get(word.getWord()) != null){
            throw new IllegalArgumentException("This Word Already Exist Within The Dictionary");
        }

        // Adds the word objects English word as the key and word object as the value to the dictionary
        dictionary.put(word.getWord(),word);
    }


    /**
     * Gets the amount of word count that exist within the dictionary.
     * @return the amount of keys that exist within the dictionary.
     */
    @Override
    public int getWordCount() {
        return dictionary.size();
    }

    /**
     * Gets the sum of every pronunciation in every word.
     * @return the sum of every values sizes within the dictionary keys.
     */
    @Override
    public int getPronunciationCount(){
        int amountOfPronunciation = 0;

        // Loops through every pronunciation which are the dictionary values.
        for (IWord word : dictionary.values()){
            // Gets the size of every pronunciation and adds it to the total.
            amountOfPronunciation += word.getPronunciations().size();
        }

        return amountOfPronunciation;
    }

    /**
     * Reads a line from the CMU dictionary text file and load it into the dictionary attribute.
     * @param line will be a line from the CMU dictionary.
     */
    @Override
    public void parseDictionaryLine(String line){
        String lineWord;
        String linePronunciation;
        int wordPronunciationIndexSplit;

        // Removes any comment and duplication word indication from the line.
        line = removeWordDuplicationIndication(line);
        line = removeComment(line);

        // Finds the index that splits the word and pronunciation.
        wordPronunciationIndexSplit = indexBetweenWordAndPronunciation(line);

        // Extracts the word and pronunciation from the line with the use of wordPronunciationIndexSplit.
        lineWord = setWordFromLine(line,wordPronunciationIndexSplit);
        linePronunciation = setPronunciationFromLine(line,wordPronunciationIndexSplit);

        // Adds the word and pronunciation to the dictionary.
        addWordsPronunciation(lineWord, linePronunciation);
    }


    /**
     * Removes the comment part of the line.
     * @param line is a line from the CMU dictionary.
     * @return the line without comments.
     */
    public String removeComment(String line){
        // Finds the index with comment indication.
        int indexOfComment = line.indexOf('#');

        // If indexOf is not -1 then the line contains a comment.
        if (indexOfComment != -1){
            // Return the line without the comment.
            return line.substring(0,indexOfComment);
        }

        // If the lines do not have a comment.
        // Return the line.
        return line;
    }

    /**
     * Removes the word duplication indication of the line.
     * @param line is a line from the CMU dictionary.
     * @return the line without the word duplication indication.
     */
    public String removeWordDuplicationIndication(String line){
        String firstHalf;
        String secondHalf;

        // Finds the index with word duplication indication
        int indexOfBracket = line.indexOf('(');

        // If indexOf is not -1 then the line contains word duplication.
        if (indexOfBracket != -1){
            // Gets the line before and after the word duplication.
            firstHalf = line.substring(0,indexOfBracket);
            secondHalf = line.substring(indexOfBracket + 3);

            // Returns the first and second half's together through string methods.
            return (firstHalf.concat(secondHalf));
        }

        // If the line does not have word duplication.
        // Return the line.
        return line;
    }


    /**
     * Gets the index that splits the line into word and pronunciation.
     * @param line a line from the CMU dictionary.
     * @return the index that splits line.
     */
    public int indexBetweenWordAndPronunciation(String line){
        // Finds the first index that contains a space.
        return (line.indexOf(" "));
    }

    /**
     * Gets the word line section from the line.
     * @param line is a line from the CMU dictionary.
     * @param split is the index that separate word and pronunciation.
     * @return the word line section.
     */
    public String setWordFromLine(String line, int split){
        // Returns the beginning of the line to the split index.
        return (line.substring(0, split));
    }

    /**
     * Gets the pronunciation line section from the line.
     * @param line is a line from the CMU dictionary.
     * @param split is the index that separate word and pronunciation.
     * @return the pronunciation line section.
     */
    public String setPronunciationFromLine(String line, int split){
        // Returns the split index to the end of the line.
        return (line.substring(split + 1));
    }


    /**
     * Adds the word and pronunciation to the dictionary.
     * @param lineWord is the word section of the CMU dictionary line.
     * @param linePronunciation is the pronunciation section of the CMU dictionary line.
     */
    public void addWordsPronunciation(String lineWord, String linePronunciation) {
        // Gets the word object from the line word if it exists.
        IWord wordObject = getWord(lineWord);
        // Is a variable that will hold a pronunciation object.
        IPronunciation pronunciationObject;

        // If the word does not exist within the dictionary.
        if (wordObject == null) {

            // Add new word to dictionary.
            addWord(new Word(lineWord));

            // Get the new word object from dictionary.
            wordObject = getWord(lineWord);

        }

        // Holds the object version of the pronunciation line.
        pronunciationObject = linePronunciationToObject(linePronunciation);

        // Adds pronunciation to the word object.
        wordObject.addPronunciation(pronunciationObject);
    }


    /**
     * Convert the string line pronunciation into a pronunciation object.
     * @param linePronunciation is the pronunciation section of the CMU dictionary line.
     * @return a pronunciation object derived from the line pronunciation.
     */
    public IPronunciation linePronunciationToObject(String linePronunciation){
        // Instantiate a pronunciation object.
        IPronunciation pronunciationObject = new Pronunciation();

        // Loops through every phoneme within the pronunciation line.
        for (String phoneme: linePronunciation.split(" ")){
            // Convert each string phoneme into the phoneme object
            // and added to the pronunciation object.
            pronunciationObject.add(createPhoneme(phoneme));
        }

        return pronunciationObject;
    }


    /**
     * Converts a string phoneme to a phoneme object.
     * @param linePhoneme is a string version of a phoneme.
     * @return the phoneme objects version of the string phoneme.
     */
    public Phoneme createPhoneme(String linePhoneme){
        Arpabet arpabetValue;
        int stressValue;

        // Extract the arpabet and stress value from the string phoneme.
        arpabetValue = getArpabetValue(linePhoneme);
        stressValue = getStressValue(linePhoneme);

        // Instantiate a phoneme object with their corresponding arpabet and stress value
        return (new Phoneme(arpabetValue,stressValue));
    }


    /**
     * Gets the stress value of the phoneme
     * @param phoneme is the string phoneme.
     * @return the stress value of the phoneme.
     */
    public int getStressValue(String phoneme){
        // Default stress is -1.
        int stressValue = -1;

        // Removes all non-numerical characters from the string.
        // With The stress value remaining as long tt contains a stress value.
        phoneme = phoneme.replaceAll("[A-Z]","");

        // Turns string to an integer ff tt contains a stress value.
        if (!phoneme.isEmpty()){
            stressValue = Integer.parseInt(phoneme);
        }

        // Returns -1 as stress value if it does not contain stress.
        return stressValue;
    }

    public Arpabet getArpabetValue(String phoneme){
        Arpabet arpabet;

        // Removes all numerical characters from the string.
        phoneme = phoneme.replaceAll("[0-9]","");

        // Change string to an arpabet enum.
        // Throws an exception if enum constant does not exist
        // Meaning given argument is invalid.
        arpabet = Arpabet.valueOf(phoneme);

        return arpabet;
    }


    /**
     * Loads all the content within the CMU dictionary text file into the dictionary HashMap.
     * @param fileName is the CMU dictionary text file name.
     */
    @Override
    public void loadDictionary(String fileName) {
        // Tries to find the CMU text file.
        try{
            String line;
            // Buffered reader that reads from the CMU dictionary text file.
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            // Iterates each line of the CMU dictionary as long the next line is not empty.
            while (((line = reader.readLine())) != null){
                // Passes the CMU dictionary line to be loaded into the dictionary HashMap.
                parseDictionaryLine(line);
            }

        }

        // If the CMU text file cannot be found.
        catch (IOException e) {
            System.out.println("File Couldn't Be Found");
        }
    }


    /**
     * Gets all the words that rhymes with the given word.
     * @param word is used to find all the word that rhymes with it on the dictionary.
     * @return a set of English word that rhymes with the passed word.
     */
    @Override
    public Set<String> getRhymes(String word) {
        // Gets the word object from the passed word.
        IWord wordToRhyme = dictionary.get(word);

        // Gets a set of pronunciation from the passed word object.
        Set<IPronunciation> pronunciationSetToRhyme = wordToRhyme.getPronunciations();
        Set<IPronunciation> dictionarySetOfPronunciation;

        // All words from the dictionary that rhymes with the passed word
        // will be added to the rhymes HashMap.
        HashSet<String> rhymes = new HashSet<>();

        // Loops through each entity of the dictionary.
        for (Map.Entry<String,IWord> dictionary : dictionary.entrySet()){
            // Gets a pronunciation set from the dictionary word object.
            dictionarySetOfPronunciation = dictionary.getValue().getPronunciations();

            // If the pronunciation set from the passed word rhymes with the dictionary pronunciation set.
            if (isPronunciationRhymesWithOtherPronunciation(pronunciationSetToRhyme, dictionarySetOfPronunciation)){
                // Adds the dictionary word to the rhyme HashSet.
                rhymes.add(dictionary.getKey());
            }
        }

        // Returns all dictionary words that rhymes with the passed word.
        return rhymes;
    }


    /**
     * Checks if either pronunciation has any pronunciation that rhymes with each other.
     * @param pronunciationSetToRhyme is a set of pronunciation from the given word.
     * @param dictionarySetOfPronunciation is a set of pronunciation from the dictionary word.
     * @return true if either pronunciation sets rhymes with each other, else return false.
     */
    public boolean isPronunciationRhymesWithOtherPronunciation(Set<IPronunciation> pronunciationSetToRhyme, Set<IPronunciation> dictionarySetOfPronunciation){
        // Loops through the passed word pronunciation set.
        for (IPronunciation pronunciationToRhyme : pronunciationSetToRhyme) {

            // Loops through the dictionary word pronunciation set.
            for (IPronunciation dictionaryPronunciation : dictionarySetOfPronunciation){

                // Checks if the passed pronunciation rhymes with dictionary pronunciation.
                if (pronunciationToRhyme.rhymesWith(dictionaryPronunciation)) {
                    return true;
                }
            }
        }

        // Returns false if either set of pronunciation does not rhyme with each other.
        return false;
    }

}
