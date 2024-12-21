package uk.ac.aber.cs21120.rhymes.tests;

import org.junit.jupiter.api.*;
import uk.ac.aber.cs21120.rhymes.interfaces.*;
import uk.ac.aber.cs21120.rhymes.solution.Dictionary;
import uk.ac.aber.cs21120.rhymes.solution.Phoneme;
import uk.ac.aber.cs21120.rhymes.solution.Pronunciation;
import uk.ac.aber.cs21120.rhymes.solution.Word;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraJUnitTests {

    public static final String CMU_DICT = "cmudict.dict.txt";

    @Test
    void testNotHasSameArpabet(){
        IPhoneme phoneme = new Phoneme(Arpabet.AH,1);
        IPhoneme otherPhoneme = new Phoneme(Arpabet.AO,1);

        assertFalse(phoneme.hasSameArpabet(otherPhoneme));
    }


    /**
     * Test findFinalStressedVowelIndex() to find the index of the right most
     * primary stress when there is two primary stress vowels in a pronunciation.
     * Ensures the code can deal with two primary stress vowels.
     */
    @Test
    void testGetRightMostPrimaryStressVowelIndex(){
        IPronunciation pronunciation = new Pronunciation();
        pronunciation.add(new Phoneme(Arpabet.AE , 1));
        pronunciation.add(new Phoneme(Arpabet.T , -1));
        pronunciation.add(new Phoneme(Arpabet.B , -1));
        pronunciation.add(new Phoneme(Arpabet.AE , 1));
        pronunciation.add(new Phoneme(Arpabet.T , -1));

        assertEquals(3,pronunciation.findFinalStressedVowelIndex());
    }


    @Test
    void testisSoundsAfterFinalStressedVowelSameWithDifferentLengths(){
        // orange AO1 R AH0 N JH
        Pronunciation pronunciation1 = new Pronunciation();
        pronunciation1.add(new Phoneme(Arpabet.AO , 1));
        pronunciation1.add(new Phoneme(Arpabet.R , -1));
        pronunciation1.add(new Phoneme(Arpabet.AH , 0));
        pronunciation1.add(new Phoneme(Arpabet.N , -1));
        pronunciation1.add(new Phoneme(Arpabet.JH , -1));

        // multiple M AH1 L T AH0 P AH0 L
        IPronunciation pronunciation2 = new Pronunciation();
        pronunciation2.add(new Phoneme(Arpabet.M , -1));
        pronunciation2.add(new Phoneme(Arpabet.AH , 1));
        pronunciation2.add(new Phoneme(Arpabet.L , -1));
        pronunciation2.add(new Phoneme(Arpabet.T , -1));
        pronunciation2.add(new Phoneme(Arpabet.AH , 0));
        pronunciation2.add(new Phoneme(Arpabet.P , -1));
        pronunciation2.add(new Phoneme(Arpabet.AH , 0));
        pronunciation2.add(new Phoneme(Arpabet.L , -1));

        assertFalse(pronunciation1.isSoundsAfterFinalStressedVowelSame(0,1,pronunciation2.getPhonemes()));
    }


    @Test
    void testRemoveWordDuplicationIndicatorFromLine(){
        Dictionary dictionary = new Dictionary();
        String line = "activity(2) AE0 K T IH1 V IH0 T IY0";

        assertEquals("activity AE0 K T IH1 V IH0 T IY0", dictionary.removeWordDuplicationIndication(line));
    }


    @Test
    void testRemoveCommentFromLine(){
        Dictionary dictionary = new Dictionary();
        String line = "d'artagnan D AH0 R T AE1 NG Y AH0 N # foreign french";

        assertEquals("d'artagnan D AH0 R T AE1 NG Y AH0 N", dictionary.removeComment(line));
    }


    @Test
    void testRemoveCommentAndWordDuplicationIndicatorFromLine(){
        Dictionary dictionary = new Dictionary();
        String line = "fine(2) F IH1 N AH0 # org, irish";

        line = dictionary.removeComment(line);
        line = dictionary.removeWordDuplicationIndication(line);

        assertEquals("fine F IH1 N AH0", line);
    }


    @Test
    void testGetRhymesOfWordWithWordDuplicationIndicatorAndComment(){
        IDictionary dictionary = new Dictionary();
        Set<String> rhymes;
        dictionary.loadDictionary(CMU_DICT);

        // fine(2) F IH1 N AH0 # org, irish
        rhymes = dictionary.getRhymes("fine");

        assertEquals(true, rhymes.contains("corinna"));
        assertEquals(true, rhymes.contains("devine"));
        assertEquals(120, rhymes.size());
    }


    @Test
    void testGetRhymesOfWordWithComments(){
        IDictionary dictionary = new Dictionary();
        Set<String> rhymes;
        dictionary.loadDictionary(CMU_DICT);

        // findling(2) F IH1 N D L IH0 NG
        rhymes = dictionary.getRhymes("findling");

        assertTrue(rhymes.contains("kindling"));
        assertTrue(rhymes.contains("pindling"));
        assertTrue(rhymes.contains("rekindling"));
        assertEquals(6,rhymes.size());
    }


    @Test
    void testGetRhymeWordWithApostrophe(){
        IDictionary d = new Dictionary();
        d.loadDictionary(CMU_DICT);

        Set<String> rhymes;
        rhymes = d.getRhymes("abbey's");

        assertEquals(true,rhymes.contains("cabbies"));
        assertEquals(true,rhymes.contains("abbey's"));
        assertEquals(2, rhymes.size());
    }
}
