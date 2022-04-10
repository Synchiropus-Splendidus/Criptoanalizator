package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {

    private static Scrambler scrambler;

    public static void main(String[] args) {
        String keyValue = "Введите значение ключа.";
        String resultDestination = "Записать текст в файл?\n\t1-да;\n\t2-нет.";
        String fileName = "Введите название файла";
        String errorMessage = "Вы ввели неправильные данные!";


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Добро пожеловать в программу \"КРИПТОАНАЛИЗАТОР\". \nВыберите режим работы:");
        System.out.println("""
                \t1 - зашифровать сообщение;\s
                \t2 - расшифровать сообщение;
                \t3 - криптоанализ методом перебора;
                \t4 - расшифровать методом частотного анализа;
                \t5 - расшифровать методом частотного анализа с анализом введенного текста;
                \t6 - криптоанализ методом Brute Force;
                \t7 - выход;""");

        int mode = optionChoose();

        //Режимы работы программы
        switch (mode) {
            case 1 -> {
                System.out.println(keyValue);
                try {
                    int key = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(key);
                    String encryptedText = scrambler.toEncryptText(textSourceChoose());
                    System.out.printf("Результат шифрования (ключь %d):\n\t%s\n", key, encryptedText);
                    System.out.println(resultDestination);
                    int choose = optionChoose();
                    if (choose == 1) {
                        System.out.println(fileName);
                        writeText(encryptedText, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println(errorMessage);
                }
            }

            case 2 -> {
                System.out.println(keyValue);
                try {
                    int shift = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(shift);
                    System.out.println("Введите текст для расшифровки.");
                    String text = textSourceChoose();
                    String decryptedText = scrambler.toDecryptText(text);
                    System.out.printf("Результат дешифрования (ключь %d):\n\t%s\n", shift, decryptedText);
                    System.out.println(resultDestination);
                    int choose = optionChoose();
                    if (choose == 1) {
                        System.out.println(fileName);
                        writeText(decryptedText, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println(errorMessage);
                }
            }
            case 3 -> {
                System.out.println("Введите предпологаемую велечину ключа.");
                try {
                    int newKey = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(newKey);
                    System.out.println("Введите текст для расшифровки.");
                    String text = textSourceChoose();
                    System.out.printf("Начинаю взлом кода при помощи мануального перебора, всего - %d вариантов.\n", scrambler.getValueOfVariations());
                    boolean onAction = true;
                    String result = null;
                    while (onAction) {
                        result = scrambler.unCodeManualBruteForce(text, newKey);
                        System.out.printf("Результат: ключь - %d\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n",
                                newKey, result);
                        int choose = optionChoose();
                        if (choose == 1) {
                            newKey++;
                        } else {
                            onAction = false;
                        }
                    }
                    System.out.println(resultDestination);
                    int select = optionChoose();
                    if (select == 1) {
                        System.out.println(fileName);
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println(errorMessage);
                }
            }

            case 4 -> {
                scrambler = new Scrambler(0);
                System.out.println("Введите текст для расшифровки.");
                String textToDecrypt = textSourceChoose();
                System.out.println("Начинаю взлом кода.\n \tРезультат:");
                boolean onAction = true;
                int key = 0;
                String result = null;
                while (onAction) {
                    result = scrambler.toDecryptTextWithTextAnalysis(textToDecrypt, key);
                    System.out.printf("Результат:\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n", result);
                    int choose = optionChoose();
                    if (choose == 1) {
                        key++;
                    } else {
                        onAction = false;
                    }
                }
                System.out.println(resultDestination);
                int select = optionChoose();
                if (select == 1) {
                    System.out.println(fileName);
                    try {
                        writeText(result, reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            case 5 -> {
                scrambler = new Scrambler(0);
                System.out.println("Введите пример текста");
                String textExample = textSourceChoose();
                System.out.println("Введите текст для расшифровки.");
                String textToDecrypt = textSourceChoose();
                System.out.println("Начинаю взлом кода.\n \tРезультат:");
                boolean onAction = true;
                int key = 0;
                while (onAction) {
                    System.out.printf("Результат:\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n",
                            scrambler.textDecryptWithSampleTextAnalysis(textExample, textToDecrypt, key));
                    int choose = optionChoose();
                    if (choose == 1) {
                        key++;
                    } else {
                        onAction = false;
                    }
                }
            }

            case 6 -> {
                try {
                    scrambler = new Scrambler(0);
                    System.out.println("Введите текст для расшифровки.");
                    String textToEncrypt = textSourceChoose();
                    boolean onAction = true;
                    String result = null;
                    while (onAction) {
                        result = scrambler.encryptTextWithBruteForce(textToEncrypt);
                        System.out.printf("Результат: \n\t%s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n", result);
                        int choose = optionChoose();
                        if (choose == 2) {
                            onAction = false;
                        }
                    }

                    System.out.println(resultDestination);
                    int select = optionChoose();
                    if (select == 1) {
                        System.out.println(fileName);
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println(errorMessage);
                }

            }
            case 7 -> System.out.println("Выход.");
        }
    }

    //выбор режима работы программы
    public static int optionChoose() {
        int choose = 7;
        boolean onAction = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (onAction) {
            try {
                choose = Integer.parseInt(reader.readLine());
                if (choose > 0 && choose < 8) {
                    onAction = false;
                } else {
                    throw new IOException();
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Вы ввели неправильные данные, попробуйте ещё раз!");
            }
        }
        return choose;
    }

    //Чтение текста из файла
    public static String readText(String fileName) {
        String result = null;
        try {
            result = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            System.err.println("Вы ввели не правильные данные!");
        }
        return result;
    }

    //Запись текста в файл
    public static void writeText(String text, String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Файл создан!");
            } else {
                System.out.println("Такой файл существует!");
            }
            PrintWriter writer = new PrintWriter(file);
            writer.write(text);
            System.out.println("Файл записан.");
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
   }

    //Выбор источника текста
    public static String textSourceChoose() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ввод текста из:\n\t1- консоли;\n\t2-файла.");
        int sourceChoose = optionChoose();
        String text = null;
        if (sourceChoose == 1) {
            try {
                System.out.println("Введите текст.");
                text = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (sourceChoose == 2) {
            System.out.println("Введите адрес файла");
            try {
                text = readText(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text;
    }
}
