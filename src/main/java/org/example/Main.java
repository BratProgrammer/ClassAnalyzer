package org.example;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // Проверяем наличие пути до файла класса в аргументах
        if (args.length != 1) {
            System.out.println("Не указан путь до файла с классом");
            return;
        }

        // Получаем путь до файла
        String classFilePath = args[0];

        try {
            // Получаем байт-код класса
            FileInputStream classByteCode = new FileInputStream(classFilePath);

            // Записываем байт-код в специальный класс из библиотеки ASM,
            // предназначенный для чтения и анализа байт-кода других Java классов
            ClassReader classInformation = new ClassReader(classByteCode);

            // Создаём экземпляр нашего сборщика данных
            StatisticClassVisitor statisticVisitor = new StatisticClassVisitor();

            // Присоединяем обработчик к классу для сбора данных
            // 0 передаётся для обработки класса по умолчанию без дополнительных флагов
            // Данные будут выведены автоматически, так как мы переопределили метод visitEnd()
            classInformation.accept(statisticVisitor, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}