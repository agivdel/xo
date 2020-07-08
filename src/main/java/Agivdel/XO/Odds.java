package Agivdel.XO;

class Odds {

    private int odds_O = 0;
    private int odds_X = 0;
    private String indexLine = "";

    static final String[] BASELINES = {"678", "345", "012", "630", "741", "852", "048", "642"};
    static Odds[][] Table;

    public void setIndexLine(String indexLine) {
        this.indexLine = indexLine;
    }

    public String getIndexLine() {
        return indexLine;
    }

    static void init() {
        Table = new Odds[9][4];
        for (int cell = 0; cell < Table.length; cell++) {
            for (int line = 0; line < Table[cell].length; line++) {
                Table[cell][line] = new Odds();
            }
        }
        for (int cell = 0; cell < Table.length; cell++) {
            int line = 0;
            for (String baseLine : BASELINES) {
                if (baseLine.contains(String.valueOf(cell))) {
                    Table[cell][line].setIndexLine(baseLine);
                    line++;
                }
            }
        }
    }

    static void writeTable(int gameCell, String signXO) {
        for (int cell = 0; cell < Table.length; cell++) {
            for (int line = 0; line < Table[cell].length; line++) {
                if (Field.game[cell].equals(Field.SIGN_EMPTY)
                        && Table[cell][line].getIndexLine().contains(String.valueOf(gameCell))) {
                    Table[cell][line].OddsPlus(signXO);
                }
            }
        }
    }

    public void OddsPlus(String signXO) {
        if (signXO.equals("x")) {
            this.odds_X++;
        } else this.odds_O++;
    }

    public int getOdds(String signXO) {
        if (signXO.equals("x")) {
            return odds_X;
        } else return odds_O;
    }

    public boolean isOnlyOneSign() {
        return this.odds_O == 0 | this.odds_X == 0;
    }
}
