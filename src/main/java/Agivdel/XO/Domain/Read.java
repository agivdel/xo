package Agivdel.XO.Domain;

/**
 * Пока здесь вывод в консоль.
 * в будущем нужно добавить другие способы вывода.
 * Передавать всю переменную класса Data (а не только массив gameField) на данном этапе избыточно,
 * но в будущем может пригодится.
 */
public class Read {

    static void print() {
        Data data = new Data();
        String[] gameField = data.getGameTable();//это оставить здесь, а вывод на печать перенести в другой класс,
        // как и вывод на фрейм

        System.out.println(gameField[6] + " " + gameField[7] + " " + gameField[8]);
        System.out.println(gameField[3] + " " + gameField[4] + " " + gameField[5]);
        System.out.println(gameField[0] + " " + gameField[1] + " " + gameField[2]);
        System.out.println("\n_________________");
    }
}