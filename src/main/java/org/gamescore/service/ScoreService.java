package org.gamescore.service;

import org.gamescore.dao.ScoreDAO;
import org.gamescore.model.Score;

import java.util.List;

public class ScoreService {

    private ScoreDAO scoreDAO;

    public ScoreService() {
        this.scoreDAO = new ScoreDAO();
    }

    public boolean saveScore(Score score) {
        return scoreDAO.save(score);
    }

    public List<Score> getHighestScores() {
        return scoreDAO.getHighestScores();
    }

    public List<Score> getHighestScoresForUser(int userId) {
        return scoreDAO.getHighestScoresForUser(userId);
    }

    public List<Score> getTop10ScoresForGame(int gameId) {
        return scoreDAO.getTop10ScoresForGame(gameId);
    }
}

