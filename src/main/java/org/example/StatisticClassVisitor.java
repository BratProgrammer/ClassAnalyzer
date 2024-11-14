package org.example;

import org.objectweb.asm.*;

// Класс StatisticClassVisitor наследует ClassVisitor и собирает статистику о классе
public class StatisticClassVisitor extends ClassVisitor {
    // Переменные для хранения статистики
    private int loopOpcodes = 0; // Счетчик опкодов циклов
    private int conditionalBranches = 0; // Счетчик условных переходов
    private int variableDeclarations = 0; // Счетчик объявлений переменных
    private int fieldCount = 0; // Счетчик полей класса

    // Конструктор класса, инициализирует родительский класс с версией ASM
    public StatisticClassVisitor() {
        super(Opcodes.ASM9);
    }

    // Метод для обработки полей класса
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        fieldCount++; // Увеличиваем счетчик полей
        return super.visitField(access, name, desc, signature, value); // Вызываем метод родителя
    }

    // Метод для обработки методов класса
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // Переменная cv (ClassVisitor) объявляется при вызове метода super в конструкторе
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions); // Получаем метод
        if (methodVisitor == null) {
            return new StatisticMethodVisitor(); // Если метод не найден, создаем новый StatisticMethodVisitor
        }
        return methodVisitor; // Возвращаем найденный метод
    }

    // Вложенный класс для сбора статистики о методах
    private class StatisticMethodVisitor extends MethodVisitor {

        // Конструктор класса, инициализирует родительский класс с версией ASM
        public StatisticMethodVisitor() {
            super(Opcodes.ASM9);
        }

        // Метод для обработки инструкций работы с переменными
        @Override
        public void visitVarInsn(int opcode, int var) {
            // Проверяем, является ли инструкция загрузкой переменной
            if (opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD) {
                variableDeclarations++; // Увеличиваем счетчик объявлений переменных
            }
            super.visitVarInsn(opcode, var); // Вызываем метод родителя
        }

        // Метод для обработки условных переходов
        @Override
        public void visitJumpInsn(int opcode, Label label) {
            // Проверяем, является ли инструкция условным переходом
            if (opcode == Opcodes.IFEQ || opcode == Opcodes.IFNE || opcode == Opcodes.IFLT || opcode == Opcodes.IFGE ||
                    opcode == Opcodes.IFGT || opcode == Opcodes.IFLE || opcode == Opcodes.IF_ICMPEQ ||
                    opcode == Opcodes.IF_ICMPNE || opcode == Opcodes.IF_ICMPLT || opcode == Opcodes.IF_ICMPGE ||
                    opcode == Opcodes.IF_ICMPGT || opcode == Opcodes.IF_ICMPLE || opcode == Opcodes.IF_ACMPEQ ||
                    opcode == Opcodes.IF_ACMPNE) {
                conditionalBranches++; // Увеличиваем счетчик условных переходов
            } else if (opcode == Opcodes.GOTO) {
                loopOpcodes++; // Увеличиваем счетчик опкодов циклов
            }
            super.visitJumpInsn(opcode, label); // Вызываем метод родителя
        }
    }

    // Метод, вызываемый в конце обработки класса
    @Override
    public void visitEnd() {
        // Выводим собранную статистику
        System.out.println("Статистика класса:");
        System.out.println("Количество опкодов циклов: " + loopOpcodes);
        System.out.println("Количество условных переходов: " + conditionalBranches);
        System.out.println("Количество объявлений переменных в методах: " + variableDeclarations);
        System.out.println("Количество полей в классе: " + fieldCount);
        super.visitEnd(); // Вызываем метод родителя
    }
}
