package com.backend.investate.EncryptionNDecryption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decryption {
    private String text = "";
    private String ORIGINAL_TEXT="" ;
    private List<String> list1;
    private List<String> list2;
    private HashMap<Integer, List<String>> map;

    public Decryption(String text) {
        this.text = text;
        list1 = Arrays.asList(splitOnUpperCase(text));
        list2 = new ArrayList<>();
        map = initializeNumToRandAlpha();
      //  System.out.println(list1);
        alphaToNum();
       // System.out.println(list2);
        binToChar();
    }

    private HashMap<Integer, List<String>> initializeNumToRandAlpha() {
       // System.out.println("Initializing random character lists...");
        HashMap<Integer, List<String>> map = new HashMap<>();
        List<String> zero = Arrays.asList("q", "w", "e", "r", "t", "y", "u", "i", "p", "a", "s", "d", "f", "g", "h",
                "j", "k", "l", "&", "*", ")", "~","6","8","9","]");
        List<String> one = Arrays.asList("1","2","3","4","5","z", "x", "c", "v", "b", "n", "m", "!", "@", "#", "%", "^","(","{","}","[");
        map.put(0, zero);
        map.put(1, one);
      //  System.out.println("Random character lists initialized.");
        return map;
    }

    private void alphaToNum() {
        for (String string : list1) {
            String x = "";
            for (int i = 0; i < string.length(); i++) {
                for(Map.Entry<Integer,List<String>> m : map.entrySet()){
                    if(m.getValue().contains(string.charAt(i)+"")){
                        x += m.getKey();
                    }
                }
            }
            list2.add(x);
        }
    }
    private void binToChar(){
        for (String string : list2) {
           // System.out.println(Integer.parseInt(string,2));
            ORIGINAL_TEXT += (char)Integer.parseInt(string,2);
        }
    }
    public static String[] splitOnUpperCase(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (currentPart.length() > 0) {
                    parts.add(currentPart.toString());
                }
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }
        return parts.toArray(new String[0]);
    }
    public String getDecryptedString(){
        return ORIGINAL_TEXT;
    }
    /**
     * * @author E Praveen Kumar * *
     *                             *
    * */
}
