package com.example.praveen_investate.encryption_decryption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/** @author E Praveen Kumar  */

public class PsychoCipher {
    private HashMap<Integer, List<String>> numToRandAlpha;
    private ArrayList<String> list1;
    private ArrayList<String> list2;
    private String text;
    private String encryptedString;

    public PsychoCipher(String str) {
        text = str;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        numToRandAlpha = initializeNumToRandAlpha();
        convertTextToNumberRepresentation();
        convertNumbersToBinary();
        generateEncryptedString();
    }

    private HashMap<Integer, List<String>> initializeNumToRandAlpha() {

        HashMap<Integer, List<String>> map = new HashMap<>();
        List<String> zero = Arrays.asList("q", "w", "e", "r", "t", "y", "u",
                "i", "p", "a", "s", "d", "f", "g", "h","6","8","9",
                "j", "k", "l", "&", "*", ")", "~","]");
        List<String> one = Arrays.asList("z", "x", "c", "v", "b", "n", "m",
                "!", "@", "#", "%", "^","1","2","3","4","5","(","{","}","[");
        map.put(0, zero);
        map.put(1, one);

        return map;
    }

    private void convertTextToNumberRepresentation() {

        for (int i = 0; i < text.length(); i++) {

            list1.add(((int) text.charAt(i)) + "");
        }

    }

    private void convertNumbersToBinary() {

        for (String string : list1) {

            list2.add(Integer.toBinaryString(Integer.parseInt(string)));
        }

    }

    private void generateEncryptedString() {

        String[] randomSpace = new String("qwertyuiopasdfghjklzxcvbnm").toUpperCase().split("");
        encryptedString = "";

        for (String string : list2) {
            StringBuilder encryptedPart = new StringBuilder();
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == '0') {
                    encryptedPart.append(getRandomCharacterFromList(0));
                } else {
                    encryptedPart.append(getRandomCharacterFromList(1));
                }
            }
            encryptedString += encryptedPart.toString();
            encryptedString += randomSpace[new Random().nextInt(randomSpace.length)];
        }

    }

    private String getRandomCharacterFromList(int key) {
        List<String> characterList = numToRandAlpha.get(key);
        String randomCharacter = characterList.get(new Random().nextInt(characterList.size()));

        return randomCharacter;
    }

    public String getEncryptedString() {
        return encryptedString;
    }
    /*
     * * @author E Praveen Kumar * *
     *                             *
     */

    /*
     * HOW IT WORKS ?
     *
     1> onstructor Encryption(String str) is invoked, initializing the input text, along with empty list1, list2, and invoking the method to populate numToRandAlpha with a mapping of binary digits to randomized alphanumeric characters.

     2> initializeNumToRandAlpha() constructs a HashMap, associating the binary keys 0 and 1 to distinct collections of randomized characters, including both alphabetic and special symbols, ensuring variability in the encryption.

     3> convertTextToNumberRepresentation() iterates over each character in text, converting each character into its corresponding ASCII integer representation, subsequently appending the resulting stringified numeric values into list1 for further binary conversion.

     4> convertNumbersToBinary() processes each numeric string in list1, transforming these ASCII values into their equivalent binary string forms and storing the binary results in list2, preserving the order of the original text.

     5> generateEncryptedString() initiates an encryption loop, where each binary string in list2 is iterated character by character, mapping each binary digit (0 or 1) to a randomly selected character from the corresponding list in numToRandAlpha, dynamically constructing encrypted segments.

     6> An additional random alphabetic character is appended after each encrypted binary string, ensuring that the final encrypted message includes randomized spacing for obfuscation and enhanced security.

     T     7> the fully constructed encrypted string is stored in encryptedString, ready for retrieval through getEncryptedString().

     *
     *
     *
     */

}
