package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {

    private static Scrambler scrambler;

    public static void main(String[] args) {
        // write your code here
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
                System.out.println("Введите велечину сдвига.");
                try {
                    int shift = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(shift);
                    String result = scrambler.code(textSourceChoose());
                    System.out.printf("Результат шифрования (сдвиг %d):\n\t%s\n", shift, result);
                    System.out.println("Записать текст в файл?\n\t1-да;\n\t2-нет.");
                    int choose = optionChoose();
                    if (choose == 1) {
                        System.out.println("Введите название файла");
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println("Вы ввели неправильные данные!");
                }
            }

            case 2 -> {
                System.out.println("Введите велечину сдвига.");
                try {
                    int shift = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(shift);
                    System.out.println("Введите текст для расшифровки.");
                    String text = textSourceChoose();
                    String result = scrambler.unCode(text);
                    System.out.printf("Результат дешифрования (сдвиг %d):\n\t%s\n", shift, result);
                    System.out.println("Записать текст в файл?\n\t1-да;\n\t2-нет.");
                    int choose = optionChoose();
                    if (choose == 1) {
                        System.out.println("Введите название файла");
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println("Вы ввели неправильные данные!");
                }
            }
            case 3 -> {
                System.out.println("Введите предпологаемую велечину сдвига.");
                try {
                    int newShift = Integer.parseInt(reader.readLine());
                    scrambler = new Scrambler(newShift);
                    System.out.println("Введите текст для расшифровки.");
                    String text = textSourceChoose();
                    System.out.printf("Начинаю взлом кода при помощи мануального перебора, всего - %d вариантов.\n", scrambler.getVariation());
                    boolean onAction = true;
                    String result = null;
                    while (onAction) {
                        result = scrambler.unCodeManualBruteForce(text, newShift);
                        System.out.printf("Результат: сдвиг - %d\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n",
                                newShift, result);
                        int choose = optionChoose();
                        if (choose == 1) {
                            newShift++;
                        } else {
                            onAction = false;
                        }
                    }
                    System.out.println("Записать текст в файл?\n\t1-да;\n\t2-нет.");
                    int select = optionChoose();
                    if (select == 1) {
                        System.out.println("Введите название файла");
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println("Вы ввели неправильные данные!");
                }
            }

            case 4 -> {
                scrambler = new Scrambler(0);
                System.out.println("Введите текст для расшифровки.");
                String textToUncode = textSourceChoose();
                System.out.println("Начинаю взлом кода.\n \tРезультат:");
                boolean onAction = true;
                int count = 0;
                String result = null;
                while (onAction) {
                    result = scrambler.codeHackWithFrequenceAnalize(textToUncode, count);
                    System.out.printf("Результат:\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n", result);
                    int choose = optionChoose();
                    if (choose == 1) {
                        count++;
                    } else {
                        onAction = false;
                    }
                }
                System.out.println("Записать текст в файл?\n\t1-да;\n\t2-нет.");
                int select = optionChoose();
                if (select == 1) {
                    System.out.println("Введите название файла");
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
                String textToUncode = textSourceChoose();
                System.out.println("Начинаю взлом кода.\n \tРезультат:");
                boolean onAction = true;
                int count = 0;
                while (onAction) {
                    System.out.printf("Результат:\n \t %s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n",
                            scrambler.codeHackWithTextAnalize(textExample, textToUncode, count));
                    int choose = optionChoose();
                    if (choose == 1) {
                        count++;
                    } else {
                        onAction = false;
                    }
                }
            }

            case 6 -> {
                try {
                    scrambler = new Scrambler(0);
                    System.out.println("Введите текст для расшифровки.");
                    String text = textSourceChoose();
                    boolean onAction = true;
                    String result = null;
                    while (onAction) {
                        result = scrambler.BruteForce(text);
                        System.out.printf("Результат: \n\t%s \nПопытаться ещё раз? \n \t 1-да;\n\t 2-нет.\n", result);
                        int choose = optionChoose();
                        if (choose == 2) {
                            onAction = false;
                        }
                    }

                    System.out.println("Записать текст в файл?\n\t1-да;\n\t2-нет.");
                    int select = optionChoose();
                    if (select == 1) {
                        System.out.println("Введите название файла");
                        writeText(result, reader.readLine());
                    }
                } catch (IOException e) {
                    System.err.println("Вы ввели неправильные данные!");
                }

            }
        }
    }

    public static int optionChoose() {
        int choose = 5;
        boolean onAction = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (onAction) {
            try {
                choose = Integer.parseInt(reader.readLine());
                if (choose > 0 && choose < 7) {
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
            System.out.println("Done.");
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
