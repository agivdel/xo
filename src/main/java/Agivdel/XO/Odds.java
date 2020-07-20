package Agivdel.XO;

class Odds {

    private int odds_O = 0;
    private int odds_X = 0;
    private String indexLine = "";

    static Odds[][] Table;//массив принадлежит всему классу

    static void init() {//создаем неравномерный массив клеток-объектов класса Odds
        Table = new Odds[][]{new Odds[3], new Odds[2], new Odds[3],
                             new Odds[2], new Odds[4], new Odds[2],
                             new Odds[3], new Odds[2], new Odds[3]};

        for (int cell = 0; cell < Table.length; cell++) {
            int line = 0;
            for (String baseLine : Fin.BASE_LINES) {
                if (baseLine.contains(String.valueOf(cell))) {
                    Table[cell][line] = new Odds();
                    Table[cell][line].setIndexLine(baseLine);
                    line++;
                }
            }
        }
    }

    static void writeTable(int gameCell, String signXO) {
        for (int cell = 0; cell < Table.length; cell++) {
            for (int line = 0; line < Table[cell].length; line++) {
                if (Field.game[cell].equals(Fin.SIGN_EMPTY)
                        && Table[cell][line].getIndexLine().contains(String.valueOf(gameCell))) {
                    Table[cell][line].OddsPlus(signXO);
                }
            }
        }
    }

    void setIndexLine(String indexLine) {
        this.indexLine = indexLine;
    }

    public String getIndexLine() {
        return indexLine;
    }

    void OddsPlus(String signXO) {
        if (signXO.equals(Fin.X)) {
            this.odds_X++;
        } else this.odds_O++;
    }

    public int getOdds(String signXO) {
        if (signXO.equals(Fin.X)) {
            return odds_X;
        } else return odds_O;
    }

    public boolean isOnlyOneSign() {
        return this.odds_O == 0 | this.odds_X == 0;
    }
}
