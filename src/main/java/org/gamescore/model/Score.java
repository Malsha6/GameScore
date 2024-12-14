package org.gamescore.model;

public class Score {

    private int id;
    private int userId;
    private int gameId;
    private int score;

    public Score(int userId, int gameId, int score) {
        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
    }

    public Score(int gameId, int score) {
        this.gameId = gameId;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{id=" + id + ", userId=" + userId + ", gameId=" + gameId + ", score=" + score + "}";
    }
}


