package com.example.seabattle.Statistics;

public class GameStat {
    private String player1;
    private String player2;
    private String result;
    private String score;
    private String time;

    public GameStat() {}

    public GameStat(String player1, String player2, String result, String score, String time) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.score = score;
        this.time = time;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
