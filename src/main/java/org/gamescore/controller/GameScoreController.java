package org.gamescore.controller;

import org.gamescore.model.Score;
import org.gamescore.service.ScoreService;

import java.util.List;

public class GameScoreController {

    private final ScoreService scoreService;

    public GameScoreController() {
        this.scoreService = new ScoreService();
    }

    public String saveScore(int userId, int gameId, int scoreValue) {
        try {
            Score newScore = new Score(userId, gameId, scoreValue);
            boolean isSaved = scoreService.saveScore(newScore);

            if (isSaved) {
                return "<html><body><h1>Score saved successfully!</h1></body></html>";
            } else {
                return "<html><body><h1>Error saving score</h1></body></html>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h1>Error saving score</h1></body></html>";
        }
    }

    public String getHighestScoresForUser(int userId) {
        try {
            List<Score> highestScores = scoreService.getHighestScoresForUser(userId);

            StringBuilder response = new StringBuilder("<html><body><h1>Highest Scores for User ID: " + userId + "</h1><ul>");
            for (Score score : highestScores) {
                response.append("<li>Game ID: ").append(score.getGameId())
                        .append(" - Highest Score: ").append(score.getScore())
                        .append("</li>");
            }
            response.append("</ul></body></html>");
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h1>Error retrieving highest scores</h1></body></html>";
        }
    }

    public String getTop10ScoresForGame(int gameId) {
        try {
            List<Score> topScores = scoreService.getTop10ScoresForGame(gameId);

            StringBuilder response = new StringBuilder("<html><body><h1>Top 10 Scores for Game ID: " + gameId + "</h1><ul>");
            for (Score score : topScores) {
                response.append("<li>User ID: ").append(score.getUserId())
                        .append(" - Score: ").append(score.getScore())
                        .append("</li>");
            }
            response.append("</ul></body></html>");
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h1>Error retrieving top 10 scores</h1></body></html>";
        }
    }
}

