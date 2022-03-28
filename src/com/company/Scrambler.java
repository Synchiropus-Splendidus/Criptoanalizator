package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Scrambler {

    private final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',',
            ':', '-', '!', '?'};

    private final char[] LETTERFREQUENCY = {'о', 'е', 'а', 'и', 'н', 'т', 'с', 'р', 'в', 'л', 'к', 'м', 'д', 'п', 'у',
            'я', 'ы', 'ь', 'г', 'з', 'б', 'ч', 'й', 'х', 'ж', 'ш', 'ю', 'ц', 'щ', 'э', 'ф', 'ъ', 'ё'};

    private int SHIFT;

    public Scrambler(int SHIFT) {
        if (SHIFT > ALPHABET.length - 1 || SHIFT < -ALPHABET.length - 1) {
            this.SHIFT = Math.abs(SHIFT % (ALPHABET.length - 1));
        } else {
            this.SHIFT = Math.abs(SHIFT);
        }
    }

    public int getVariation() {
        return ALPHABET.length;
    }

    public String code(String text) {
        StringBuilder codedResult = new StringBuilder();
        String textToCode = text.toLowerCase();
        for (char letter : textToCode.toCharArray()) {
            for (int i = 0; i < ALPHABET.length; i++) {
                if (letter == ALPHABET[i]) {
                    int newLetterNumber = i + SHIFT;
                    if (newLetterNumber >= ALPHABET.length) {
                        newLetterNumber -= ALPHABET.length;
                    }
                    codedResult.append(ALPHABET[newLetterNumber]);
                } else if (letter == ' ') {
                    codedResult.append(' ');
                    break;
                }
            }
        }
        return codedResult.toString();
    }

    public String unCode(String text) {
        StringBuilder unCodedResult = new StringBuilder();
        String textToCode = text.toLowerCase();
        for (char letter : textToCode.toCharArray()) {
            for (int i = 0; i < ALPHABET.length; i++) {
                if (letter == ALPHABET[i]) {
                    int newLetterNumber = i - SHIFT;
                    if (newLetterNumber < 0) {
                        newLetterNumber += ALPHABET.length;
                    } else if (newLetterNumber >= ALPHABET.length) {
                        newLetterNumber -= ALPHABET.length;
                    }
                    unCodedResult.append(ALPHABET[newLetterNumber]);
                } else if (letter == ' ') {
                    unCodedResult.append(' ');
                    break;
                }
            }
        }
        return unCodedResult.toString();
    }

    public String unCodeManualBruteForce(String text, int newShift) {
        SHIFT = newShift;
        return unCode(text);
    }

    public String BruteForce(String text) {
        //работает при наличии знаков припинания
        SHIFT = 1;
        Character[] simbols = {'.', ';', ',', ':'};
        boolean onAction = true;
        String uncodedText = unCode(text); //раскодируем текст
        while (onAction) {
            List<String> words = List.of(uncodedText.split(" "));//делим на слова
            for (int a = 0; a < simbols.length; a++) {// проверяем на наличие символа в середине слова
                for (int i = 0; i < words.size(); i++) {
                    String uncodedWord = words.get(i);
                    for (int j = 0; j < uncodedWord.length() - 1; j++) {
                        if (uncodedWord.charAt(j) == simbols[a]) {
                            SHIFT++;
                            i = words.size();
                            a = simbols.length;
                            break;
                        }
                        if (uncodedWord.charAt((uncodedWord.length() - 1)) == simbols[a]) {
                            onAction = false;
                        }
                    }
                }
            }
            uncodedText = unCode(text);
        }
        System.out.printf("Сдвиг: %d.\n", SHIFT);
        return uncodedText;
    }

    public String codeHackWithFrequenceAnalize(String text, int count) {
        //определение буквы с самой большой частотой
        TreeMap<Character, Integer> letterFrequencyInText = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letter : text.toLowerCase().toCharArray()) {
                if (letter == letterInAlphabet) {
                    letterFrequency++;
                }
            }
            letterFrequencyInText.put(letterInAlphabet, letterFrequency);
        }

        int maxFrequency = 0;
        char letterWithMaxFrequency = ' ';
        for (Map.Entry<Character, Integer> entry : letterFrequencyInText.entrySet()) {
            if (maxFrequency < entry.getValue()) {
                letterWithMaxFrequency = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        //System.out.println(letterFrequencyInText);
        //определение сдвига
        int numberInAlphabet = 0;
        int numberAfterShift = 0;
        for (int y = 0; y < ALPHABET.length; y++) {
            if (LETTERFREQUENCY[count] == ALPHABET[y]) {
                numberInAlphabet = y;
            } else if (ALPHABET[y] == letterWithMaxFrequency) {
                numberAfterShift = y;
            }
        }
        SHIFT = numberAfterShift - numberInAlphabet;
        System.out.printf("Велечина сдвига: %d\n", SHIFT);
        return unCode(text);
    }

    public String codeHackWithTextAnalize(String textExample, String textToUncode, int count) {
        //Анализ примера текста на часто встречащую букву
        TreeMap<Integer, Character> letterFrequencyInExample = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letterInExample : textExample.toLowerCase().toCharArray()) {
                if (letterInExample == letterInAlphabet) {
                    letterFrequency++;
                }
            }
            letterFrequencyInExample.put(letterFrequency, letterInAlphabet);
        }
        List<Character> exampleletterFrequency = new ArrayList<>(letterFrequencyInExample.values());

        System.out.println("пример" + letterFrequencyInExample);

        //Подсчет наиболее часто встречающейся буквы в шифрованной части
        TreeMap<Character, Integer> letterFrequencyInText = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letter : textToUncode.toLowerCase().toCharArray()) {
                if (letter == letterInAlphabet) {
                    letterFrequency++;
                }
            }
            letterFrequencyInText.put(letterInAlphabet, letterFrequency);
        }
        System.out.println("TEXT:" + letterFrequencyInText);

        int maxFrequency = 0;
        char letterWithMaxFrequency = ' ';
        for (Map.Entry<Character, Integer> entry : letterFrequencyInText.entrySet()) {
            if (maxFrequency < entry.getValue()) {
                letterWithMaxFrequency = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        //Определение сдвига
        int numberInAlphabet = 0;
        int numberAfterShift = 0;
        for (int i = 0; i < ALPHABET.length; i++) {
            if (exampleletterFrequency.get(exampleletterFrequency.size() - 1 - count) == ALPHABET[i]) {
                numberInAlphabet = i;
            } else if (letterWithMaxFrequency == ALPHABET[i]) {
                numberAfterShift = i;
            }
        }
        SHIFT = numberAfterShift - numberInAlphabet;
        System.out.printf("Велечина сдвига: %d\n", SHIFT);
        return unCode(textToUncode);
    }
}



