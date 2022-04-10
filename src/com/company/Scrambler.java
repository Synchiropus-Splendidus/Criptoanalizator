package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Scrambler {

    private final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',',
            ':', '-', '!', '?'};

    //массив букв по частоте использования
    private final char[] LETTERFREQUENCY = {'о', 'е', 'а', 'и', 'н', 'т', 'с', 'р', 'в', 'л', 'к', 'м', 'д', 'п', 'у',
            'я', 'ы', 'ь', 'г', 'з', 'б', 'ч', 'й', 'х', 'ж', 'ш', 'ю', 'ц', 'щ', 'э', 'ф', 'ъ', 'ё'};

    private int key;

    private final String keyValue = "Значение ключа: ";

    public Scrambler(int key) {
        if (key > ALPHABET.length - 1 || key < -ALPHABET.length - 1) {
            this.key = Math.abs(key % (ALPHABET.length - 1));
        } else {
            this.key = Math.abs(key);
        }
    }

    //получение количества возможных вариантов
    public int getValueOfVariations() {
        return ALPHABET.length;
    }

    //метод для шифрования текста согласно выбранного ключа
    public String toEncryptText(String incomingText) {
        StringBuilder encryptedText = new StringBuilder();
        String textToEncrypt = incomingText.toLowerCase();
        for (char letter : textToEncrypt.toCharArray()) {
            for (int i = 0; i < ALPHABET.length; i++) {
                if (letter == ALPHABET[i]) {
                    int newLetterNumber = i + key;
                    if (newLetterNumber >= ALPHABET.length) {
                        newLetterNumber -= ALPHABET.length;
                    }
                    encryptedText.append(ALPHABET[newLetterNumber]);
                } else if (letter == ' ') {
                    encryptedText.append(' ');
                    break;
                }
            }
        }
        return encryptedText.toString();
    }

    //метод расшифровывает текст используя ключь
    public String toDecryptText(String incomingText) {
        StringBuilder decryptedText = new StringBuilder();
        String textToDecrypt = incomingText.toLowerCase();
        for (char letter : textToDecrypt.toCharArray()) {
            for (int i = 0; i < ALPHABET.length; i++) {
                if (letter == ALPHABET[i]) {
                    int newLetterNumber = i - key;
                    if (newLetterNumber < 0) {
                        newLetterNumber += ALPHABET.length;
                    } else if (newLetterNumber >= ALPHABET.length) {
                        newLetterNumber -= ALPHABET.length;
                    }
                    decryptedText.append(ALPHABET[newLetterNumber]);
                } else if (letter == ' ') {
                    decryptedText.append(' ');
                    break;
                }
            }
        }
        return decryptedText.toString();
    }

    //метод расшифровывает текст вручную - каждый раз увеличивает значение ключа на 1
    public String unCodeManualBruteForce(String incomingText, int newKey) {
        key = newKey;
        return toDecryptText(incomingText);
    }

    //метод расшифровывает текст, подбирая ключь. Работает при наличии знаков припинания.
    public String encryptTextWithBruteForce(String incomingText) {
        Character[] symbols = {'.', ';', ',', ':', '!', '?'};//задаем символы, которые должны быть в конце слова
        boolean onAction = true;
        String decryptedText = toDecryptText(incomingText); //раскодируем текст
        while (onAction) {
            List<String> words = List.of(decryptedText.split(" "));//делим текст на слова
            label:
            for (Character symbol : symbols) {// проверяем на наличие символа в середине слова
                for (String decryptedWord : words) {
                    for (int j = 0; j < decryptedWord.length() - 1; j++) {
                        if (decryptedWord.charAt(j) == symbol) {
                            key++;
                            break label;
                        }
                        if (decryptedWord.charAt((decryptedWord.length() - 1)) == symbol) {
                            onAction = false;
                        }
                    }
                }
            }
            decryptedText = toDecryptText(incomingText);
        }
        System.out.println(keyValue + key);
        return decryptedText;
    }

    //расшифровывает текст при помощи анализа текста
    public String toDecryptTextWithTextAnalysis(String incomingText, int key) {
        //определение буквы с самой большой частотой
        TreeMap<Character, Integer> letterFrequencyInText = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letter : incomingText.toLowerCase().toCharArray()) {
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

        //определение ключа
        int letterNumberInAlphabet = 0;
        int letterNumberAfterKeyUse = 0;
        for (int y = 0; y < ALPHABET.length; y++) {
            if (LETTERFREQUENCY[key] == ALPHABET[y]) {
                letterNumberInAlphabet = y;
            } else if (ALPHABET[y] == letterWithMaxFrequency) {
                letterNumberAfterKeyUse = y;
            }
        }
        this.key = letterNumberAfterKeyUse - letterNumberInAlphabet;
        System.out.println(keyValue + this.key);
        return toDecryptText(incomingText);
    }

    //расшифровывает текст при помощи анализа образца похожего текста
    public String textDecryptWithSampleTextAnalysis(String textSample, String incomingText, int key) {
        //Анализ образца текста на часто встречащую букву
        TreeMap<Integer, Character> letterFrequencyInSampleText = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letterInExample : textSample.toLowerCase().toCharArray()) {
                if (letterInExample == letterInAlphabet) {
                    letterFrequency++;
                }
            }
            letterFrequencyInSampleText.put(letterFrequency, letterInAlphabet);
        }
        List<Character> sampleLetterFrequency = new ArrayList<>(letterFrequencyInSampleText.values());

        //Подсчет наиболее часто встречающейся буквы в шифрованной части
        TreeMap<Character, Integer> letterFrequencyInText = new TreeMap<>();
        for (char letterInAlphabet : ALPHABET) {
            int letterFrequency = 0;
            for (char letter : incomingText.toLowerCase().toCharArray()) {
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

        //Определение ключа
        int numberInAlphabet = 0;
        int numberAfterKey = 0;
        for (int i = 0; i < ALPHABET.length; i++) {
            if (sampleLetterFrequency.get(sampleLetterFrequency.size() - 1 - key) == ALPHABET[i]) {
                numberInAlphabet = i;
            } else if (letterWithMaxFrequency == ALPHABET[i]) {
                numberAfterKey = i;
            }
        }
        this.key = numberAfterKey - numberInAlphabet;
        System.out.println(keyValue + this.key);
        return toDecryptText(incomingText);
    }
}



