package com.study.folder2;

import java.util.Scanner;

/**
 * Крестики-нолики с проверкой ИИ вероятностей победы для каждого хода.
 * Используем три таблицы: одно-, двух- и трехмерный массивы.
 * Все игровое поле (все массивы) делится на клетки (9*cell).
 * В 2- и 3-м. массивах (таблицы подсчета шансов) каждая клетка делится на линии (4*line),
 * в 3-м. массиве (одна из таблиц подсчета шансов) линия делится на (3*unit).
 *
 * Каждая ячейка 3-м. массива (объект класса Field) содержит значения двух типов:
 * index: постоянный номер, переданный от номера клетки cell;
 * sign: знак "x" или "o".
 * Каждая линия 2-м. массива (объект класса OddsTable) содержит значение:
 * odds: это степень заполнения одной игровой вертикали/горизонтали/диагонали в долях от трех
 * (odds = 1 означает заполненную на одну третью какую-либо вертикаль/горизонталь/диагональ,
 * проходящюю через клетку).
 * В зависимости от числа таких вертикалей/горизонталей/диагоналей), проходящих через клетку,
 * линий в каждой клетке может быть 2 (центральные клетки сторон), 3 (угловые клетки) или 4 (центральная клетка).
 *
 * При прочих равных для выигрыша нужно заполнять клетки, в которых либо больше степень заполнения одной линии
 * (например, одна линия, заполненная на две третьих), либо больше линий одинаковой степени заполнения
 * (например, две линии, заполненные на одну треть).
 * Если клетки имеют равное число линий с одинаковой степенью заполнения, выбирать можно любую из этих клеток.
 *
 * Если противник имеет в какой-либо клетке линию со степенью заполнения, равной 2, это значит,
 * что для выигрыша ему нужно поставить в данную клетку свой знак.
 * Соответственно, противник может поставить свой знак в эту клетку, чтобы не допустить выигрыш другого игрока.
 */

public class XO_AI {

    private final String SIGN_X = "x";
    private final String SIGN_O = "o";
    private final String SIGN_EMPTY = ".";
    String[] gameField = new String[9];//одномерный массив из 9 клеток
    Field[][][] field;//массив объектов, хранит знаки игроков для подсчета шансов
    OddsLine[][] oddsTable;//массив объектов, хранит значение шансов (odds) для каждой линии
    private int gameCell;//клетка игрового поля (одномерного массива)
    private int unit = 0;//ячейка, счетчик для преебора элементов 2- и 3-м. массивов
    private int line = 0;//линия, счетчик для преебора элементов 2- и 3-м. массивов
    private int cell = 0;//клетка, счетчик для преебора элементов 2- и 3-м. массивов
    private int aiLevel;//выбор уровня сложности
    boolean firstTurnOfAI = true;//счетчик для первого хода, используется в методе aiTurn()



    public static void main(String[] args) {
        new XO_AI().game();//вызов метода game из класса XO_AI с помощью объекта класса XO_AI (т.к. вызов нестат.метода из стат.)

    } //конец метода psvm


    void game() {
        initGameField();//инициализация игрового поля
        initField();//инициализация таблицы подсчета шансов
        choiceAI();//выбор противника со случайным подбором ходов или осмысленным
        while (true) {
            //choice(SIGN_X);//подсчет шансов для каждой клетки для обоих игроков
            homoTurn();//ход хомо
            if (checkWin(SIGN_X)) {//проверка на выигрыш хомо или ничью:
                System.out.println("Вы выиграли!");//сообщить и выйти из цикла
                break;
            }
            if (isGameFieldFull()) {
                break;
            }

            aiTurn();//ход ИИ со случайным выбором
            printNumericKeypad();
            if (checkWin(SIGN_O)) {//проверка на выигрыш ИИ или ничью:
                System.out.println("Вы проиграли!");//сообщить и выйти из цикла
                break;
            }
            if (isGameFieldFull()) {
                break;
            }
        }
        printNumericKeypad();//печать игрового поля
        System.out.println("Конец игры.");
    }

    /**1.1. инициализация пустой игровой таблицы для показа ходов (c помощью одномерного массива из 9 клеток)*/
    void initGameField() {
        for (gameCell = 0; gameCell < 9; gameCell++) {
            gameField[gameCell] = SIGN_EMPTY;
        }
    }

    /**1.2. создание и инициализация двух пустых таблиц подсчета шансов (c помощью двух- и трехмерного массивов)*/
    void initField() {
        field = new Field[9][4][3];
        oddsTable = new OddsLine[9][4];

        /**вспомогательный массив для инициализации массива игрового поля*/
        int[][] baseLines =
                {
                        {6, 7, 8},
                        {3, 4, 5},
                        {0, 1, 2},
                        {6, 3, 0},
                        {7, 4, 1},
                        {8, 5, 2},
                        {0, 4, 8},
                        {6, 4, 2}
                };

        /**инициализация массивов обеих таблиц подсчета шансов*/
        for (cell = 0; cell < field.length; cell++) {
            for (line = 0; line < field[cell].length; line++) {
                oddsTable[cell][line] = new OddsLine();//новый объект для таблицы подсчета шансов
                for (unit = 0; unit < 3; unit++) {
                    field[cell][line][unit] = new Field(9);//заполнение индексов каждой линии девятками
                }
            }
        }

        /**часть линий в 3-мерном массиве вместо "9" заполняется номерами клеток*/
        for (cell = 0; cell < field.length; cell++){
            line = 0;
            for (int i = 0; i < baseLines.length; i++) {
                for (int j = 0; j < baseLines[i].length; j++) {
                    if (baseLines[i][j] == cell) {
                        for (j = 0; j < baseLines[i].length; j++) {
                            field[cell][line][j].setIndex(baseLines[i][j]);
                        }
                        line++;
                    }
                }
            }
        }

    }

    /**2.1 отображение текущей игровой таблицы (c помощью 1-м. массива из 9 клеток в порядке NumKeypad)*/
    void printNumericKeypad() {
        System.out.println();
        System.out.print(gameField[6] + " ");
        System.out.print(gameField[7] + " ");
        System.out.print(gameField[8] + " ");
        System.out.println();
        System.out.print(gameField[3] + " ");
        System.out.print(gameField[4] + " ");
        System.out.print(gameField[5] + " ");
        System.out.println();
        System.out.print(gameField[0] + " ");
        System.out.print(gameField[1] + " ");
        System.out.print(gameField[2] + " ");
        System.out.println();
    }

    /**2.2 отображение индексов текущей таблицы шансов (c помощью 3-м. массива в порядке NumKeypad)
     * в игре не используется, но нужна была для отладки*/
    void printFieldNumKeypad() {
        //вывод поля на печать со значениями поля экземпляра this.index в порядке NumKeypad
        System.out.println("Печать таблицы индексов ячеек:");
        cell = 6;
        do {
            for (line = 0; line < field[cell].length; line++) {
                for (int i = 1; i <= 3; i++) {
                    for (unit = 0; unit < field[cell][line].length; unit++) {//печать в ряд трех индексов одной линии, равной line
                        field[cell][line][unit].printIndexAndDotInstead9();
                    }
                    System.out.print("   ");
                    cell++;
                }
                if (cell == 3 && line == 3) break;
                if ((cell == 9 || cell == 6 || cell == 3) & line != 3) {
                    cell -= 3;
                } else if ((cell == 9 & line == 3) || (cell == 6 & line == 3)) {
                    cell -= 6;
                    System.out.println();//отделение друг от друга трех рядов клеток
                }
                System.out.println();//формирование колонок
            }
            if (cell == 3 && line == 3) break;
        }
        while (true);
    }


    /**3.0 выбор ИИ со случайными ходами или осмысленными*/
    int choiceAI (){
        Scanner ch = new Scanner(System.in);
        do {
            System.out.println("Нажмите 0 для случайного выбора хода противником и 1 для осмысленного");
            aiLevel = ch.nextInt();
            switch (aiLevel) {
                case 0:
                    break;
                case 1:
                    System.out.println("Нажмите 1 для выбора простого уровня сложности и 2 для продвинутого");
                    aiLevel = ch.nextInt();
                    //System.out.println("Крестики ходят первыми. Выберите крестики или нолики (x/o)Нажмите");
                    break;
            }
            return aiLevel;
        }
        while (aiLevel < 0 || aiLevel > 2);//если введено не 1, 2 или 3, повторяем запрос
    }


    /**3.1 ход хомо*/
    void homoTurn() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Ваш ход");
            gameCell = sc.nextInt() - 1;//ход считываем с клавиатуры NumKeypad
        }
        while (!isCellValid(gameCell));//бесконечный цикл, если НЕ(выбранная ячейка в диапазона 0..9 или пуста)=ИСТИНА
        gameField[gameCell] = SIGN_X;
        writeField(SIGN_X);//запись знака для хомо в таблицу шансов
    }

    /**3.2 ход ИИ (со случайным выбором или немного оcмысленный)*/
    void aiTurn() {
        do {
            if (aiLevel == 0){
                gameCell = (int) (Math.random() * 9);//если ИИ тупой, каждый ход делается случайно
            } else if (aiLevel == 1) {
                if (firstTurnOfAI) {
                    gameCell = (int) (Math.random() * 9);//если ИИ чуть-чуть умный, первый ход - случайный,
                    firstTurnOfAI = false;//счетчик первого хода сработал, далее ходы рассчитываются
                } else if (!firstTurnOfAI) {
                    gameCell = choice(SIGN_O);//а второй ход и далее - считаются
                }
            } else if (aiLevel == 2){
                if (firstTurnOfAI && gameField[4].equals(SIGN_EMPTY)) {
                    gameCell = 4;//если ИИ умный, а центральная клетка свободна, первый ход - на нее
                    firstTurnOfAI = false;//счетчик первого хода сработал, далее ходы рассчитываются
                } else {
                    gameCell = choice(SIGN_O);//а второй ход и далее - рассчитываются
                }
            }
        }
        while (!isCellValid(gameCell));//случайный выбор зациклен, если выбранная клетка не пустая
        gameField[gameCell] = SIGN_O;
        writeField(SIGN_O);//запись знака для ИИ в таблицу шансов
    }


    /**4. проверка правильности выбора ячейки - c помощью 1-м. массива*/
    //возвращает истину, если ячейка имеет номер между 0 и 9 и пустая от знаков "x" и "o"
    boolean isCellValid(int gameCell) {
        if (gameCell < 0 || gameCell >= 9) {
            System.out.println("Нажмите цифровую клавишу от 1 до 9");
            return false;
        }
        return gameField[gameCell] == SIGN_EMPTY;
    }

    /**5. проверка на ничью, возвращает истину, если все ячейки заполнены*/
    boolean isGameFieldFull() {
        for (gameCell = 0; gameCell < 9; gameCell++) {
            if (gameField[gameCell] == SIGN_EMPTY) {
                return false;
            }
        }
        System.out.println("Ничья");
        return true;
    }

    /**6. проверка победы (c помощью 1-м. массива)*/
    boolean checkWin(String dot) {
        //проверка на заполнение вертикалей и горизонталей
        if ((gameField[0].equals(dot) && gameField[3].equals(dot) && gameField[6].equals(dot)) ||
                (gameField[1].equals(dot) && gameField[4].equals(dot) && gameField[7].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[5].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[0].equals(dot) && gameField[1].equals(dot) && gameField[2].equals(dot)) ||
                (gameField[3].equals(dot) && gameField[4].equals(dot) && gameField[5].equals(dot)) ||
                (gameField[6].equals(dot) && gameField[7].equals(dot) && gameField[8].equals(dot)))
            return true;
        //проверка на заполнение диагоналей
        if ((gameField[0].equals(dot) && gameField[4].equals(dot) && gameField[8].equals(dot)) ||
                (gameField[2].equals(dot) && gameField[4].equals(dot) && gameField[6].equals(dot)))
            return true;

        return false;
    }


    /**7. перебор всех элементов массивов таблицы шансов (пустых клеток) и запись хода (шансов) обоих игроков*/
    void writeField(String signXO) {
        for (cell = 0; cell < field.length; cell++) {
            for (line = 0; line < field[cell].length; line++) {
                for (unit = 0; unit < field[cell][line].length; unit++) {
                    if (field[cell][line][unit].getIndex() == gameCell && gameField[cell] == SIGN_EMPTY) {//в пустых клетках
                        field[cell][line][unit].setSign(signXO);//вписываем знак во все unit, у которых index=gameCell
                        oddsTable[cell][line].OddsPlus(signXO);//увеличиваем значения odds для линии
                    }
                }
            }
        }
    }

    /**
     * 8. выбор клетки с максимальным числом в line - на эту клетку ИИ и ходит
     * выбираем только те клетки, для которых не поставлен знак (gameField[cell] == SIGN_EMPTY),
     * а также те, для которых не заполнены одновременено шансы обеих знаков в одной линии
     * Можно выбирать первую же клетку с чужим или своим odds, равным 2.
     * Если нет odds=2, смотрим все line, для которых odds=1 и выбираем случайно одну cell.
     * Метод должен вернуть одно значение int turn для выбора cell следующего хода ИИ
     * (или одного из двух ИИ, если играют два ИИ).
     *
     * Проблема: алгоритм дает приоритет срыву заполнения выигрышной линии соперника,
     * а финальное заполнение своей линии делается лишь во вторую очередь.
     * Возможно, причина в том, что алгоритм не успевает перебрать значения odds ВСЕХ клеток,
     * а выбирает первую встречную (и это оказывается клетка с odds=2 противника).
     */
    int choice(String signXO) {
        int turn = 0;
        int line1Counter = 0;
        int cellMark = 0;
        int cell1Counter = 0;
        int cell0Counter = 0;
        int randomTurn = 0;

        // В цикле с начальной проверкой условия: (пустая клетка) && (клетка, отмеченная лишь одним знаком):



        //вывод поля на печать со значениями поля экземпляра this.index в порядке NumKeypad
        System.out.println("\nПечать таблицы значений odds для X:");
        cell = 6;
        do {
            for (line = 0; line < 4; line++) {
                System.out.print(oddsTable[cell][line].getOdds(SIGN_X));

                System.out.print("   ");
                cell++;

                if (cell == 3 && line == 3) break;
                if ((cell == 9 || cell == 6 || cell == 3) & line != 3) {
                    cell -= 3;
                } else if ((cell == 9 & line == 3) || (cell == 6 & line == 3)) {
                    cell -= 6;
                    System.out.println();//отделение друг от друга трех рядов клеток
                }
                System.out.println();//формирование колонок
            }
            if (cell == 3 && line == 3) break;
        }
        while (true);

        System.out.println("\nПечать таблицы значений odds для O:");
        cell = 6;
        do {
            for (line = 0; line < 4; line++) {
                for (int i = 1; i <= 3; i++) {
                    for (unit = 0; unit < field[cell][line].length; unit++) {//печать в ряд трех индексов одной линии, равной line
                        System.out.print(oddsTable[cell][line].getOdds(SIGN_O));
                        //field[cell][line][unit].printIndexAndDotInstead9();
                    }
                    System.out.print("   ");
                    cell++;
                }
                if (cell == 3 && line == 3) break;
                if ((cell == 9 || cell == 6 || cell == 3) & line != 3) {
                    cell -= 3;
                } else if ((cell == 9 & line == 3) || (cell == 6 & line == 3)) {
                    cell -= 6;
                    System.out.println();//отделение друг от друга трех рядов клеток
                }
                System.out.println();//формирование колонок
            }
            if (cell == 3 && line == 3) break;
        }
        while (true);




        // Пробегаем по всем значениям cell oddsTable.
        for (cell = 0; cell < oddsTable.length; cell++) {
            // Пробегаем по всем значениям line oddsTable.
            for (line = 0; line < oddsTable[cell].length; line++) {
                // Смотрим шансы в каждой линии (odds в каждой line).
                // Если свой шанс в линии равен 2, выбираем клетку этой линии для хода и выходим из метода.
                if (oddsTable[cell][line].getOdds(signXO) == 2 && gameField[cell] == SIGN_EMPTY) {
                    turn = cell;
                    return turn;
                }
                // Если шанс противника в линии равен 2, выбираем клетку этой линии для хода и выходим из метода.
                if (oddsTable[cell][line].getAnotherOdds(signXO) == 2 && gameField[cell] == SIGN_EMPTY) {
                    turn = cell;
                    return turn;
                }
                // Если свой шанс в данной линии данной клетки равен 1, начинаем такие линии считать
                if (oddsTable[cell][line].getOdds(signXO) == 1 && gameField[cell] == SIGN_EMPTY) {
                    line1Counter++;
                    //помечаем клетку, для которой начали считать число линий с odds=1
                    cellMark = cell;
                    // Если таких линий 2 в данной клетке, выбираем клетку этой линии для хода и выходим из метода.
                    if (line1Counter == 2) {
                        turn = cell;
                        return turn;
                    }
                    // Если такая линия 1 в данной клетке, начинаем считать такие же клетки (с одной линией).
                    if (line1Counter == 1) {
                        cell1Counter++;
                    }
                }
                // Если таких линий нет в данной клетке, начинаем считать такие же клетки (без линий).
                else if (oddsTable[cell][line].getOdds(signXO) == 0 && oddsTable[cell][line].are2signsAtTheSameTime()) {
                    cell0Counter++;
                }

            }
            //окончание проверки линий данной клетки, обнуляем счетчик шансов клетки
            line1Counter = 0;
            //окончание проверки клеток.
        }
        //если пришли сюда, значит, в таблице шансов нет клеток с odds=2 (своих или чужих),
        //нет клеток, в которых есть две линии с odds=1 (line1Counter=2),
        //а остались лишь клетки, в каждой из которых есть:
        //либо одна линия с odds=1 (число таких клеток равно cell1Counter),
        //либо все линии имеют odds=0 (число  таких клекто равно cell0Counter).

        //если линия с odds=1 есть лишь в одной клетке, выбираем для хода ее
        if (cell1Counter == 1) {
            turn = cellMark;//если лишь одна клетка имеет линию с odds=1, для нее уже сработала метка cellMark
            return turn;
        }
        //если есть несколько клеток с одной линией с odds=1, выбираем случайно порядковый номер одной из этих клеток
        else if (cell1Counter > 1) {
            randomTurn = (int) (Math.random() * cell1Counter);
            cell1Counter = 0;
            for (cell = 0; cell < oddsTable.length; cell++) {
                for (line = 0; line < oddsTable[cell].length; line++) {
                    if (oddsTable[cell][line].getOdds(signXO) == 1 && gameField[cell] == SIGN_EMPTY) {
                        //когда порядковый номер клетки будет равен случайному randomTurn
                        if (cell1Counter == randomTurn) {
                            turn = cell;
                            return turn;
                        }
                        cell1Counter++;
                    }
                }
            }
        }
        //если есть несколько клеток, где линии не имеют odds>0, выбираем случайно одну из этих клеток,
        //исключая те клетки, где odds=0 из-за двух знаков в одной линии одновременно
        else if (cell0Counter > 1) {
            randomTurn = (int) (Math.random() * cell0Counter);
            cell0Counter = 0;
            for (cell = 0; cell < oddsTable.length; cell++) {
                for (line = 0; line < oddsTable[cell].length; line++) {
                    if (oddsTable[cell][line].getOdds(signXO) == 0 && oddsTable[cell][line].are2signsAtTheSameTime()) {
                        //когда порядковый номер клетки будет равен случайному randomTurn
                        if (cell0Counter == randomTurn) {
                            turn = cell;
                            return turn;
                        }
                        cell1Counter++;
                    }
                }
            }
        }
        return turn;
    }


}//конец класса XO_AI


class OddsLine {//2-м. массив
    /**Эти поля будут вычисляться; установить их через конструктор или сеттер нельзя
     * Если в одной line стоит одновременно два разных знака, odds равно нулю.
     */
    private int odds_O = 0; //шансы для ноликов
    private int odds_X = 0; //шансы для крестиков
    //private int odds = 0;

    //объекты создаются в начале игры при создании и инициализации таблицы шансов
    {
        odds_X = 0;
        odds_O = 0;
    }

    public OddsLine() {
    }//необязательно, если он единственный и пустой

    /**метод увеличивает степень заполненности линии*/
    public void OddsPlus(String signXO) {
        switch (signXO) {
            case "x":
                this.odds_X++;
                break;

            case "o":
                this.odds_O++;
                break;
        }
    }

    /**Метод возвращает шансы игрока для данной ячейки*/
    public int getOdds(String signXO) {
        int odds = 0;//лучше объявить эту переменную здесь или в классе?
        switch (signXO) {
            case "x":
                odds = odds_X;
                break;

            case "o":
                odds = odds_O;
                break;
        }
        return odds;
    }

    /**Метод возвращает шансы противника для данной ячейки*/
    public int getAnotherOdds(String signXO) {
        int odds = 0;
        switch (signXO) {
            case "x":
                odds = odds_O;
                break;

            case "o":
                odds = odds_X;
                break;
        }
        return odds;
    }


    /** Передаем для данного объекта значения odds_X и odds_O и сравниваем их.
    Если они одновременно больше нуля, возвращаем false, а значение odds обнуляем для обоих знаков.
    Метод возвращает истину, если линия помечена лишь одним знаком ("X" или "O")
     */
    public boolean are2signsAtTheSameTime() {
        if (this.odds_O > 0 && this.odds_X > 0) {
            this.odds_O = 0;
            this.odds_X = 0;
            return false;
        }
        return true;
    }

}//конец класса OddsLine


class Field {//3-м. массив

    private int index = 0;
    private String sign = "."; //почему не работает изначальная инициализация для sign?


    //констр.№1
    public Field(int index, String sign) {
        this.index = index;
        this.sign = sign;
    }

    //конст.№2
    public Field(int index) {
        this(index, ".");
    }

    public int getIndex() {
        return index;
    }

    //только для первоначальной инициализации поля
    public void setIndex(int index) {
        this.index = index;
    }

    public String getSign() {
        return sign;
    }

    //будет использоваться в игре
    public void setSign(String signXO) {
        this.sign = signXO;
    }

    /**Метод при выводе на печать 3-м. массива печатает точки в линиях, где индекс ячейки равен 9*/
    public void printIndexAndDotInstead9() {
        if (this.getIndex() == 9) {
            System.out.print(this.getSign());
        } else {
            System.out.print(this.getIndex());
        }
    }

}//конец класса Field


