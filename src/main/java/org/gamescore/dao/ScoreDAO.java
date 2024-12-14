package org.gamescore.dao;

import org.gamescore.model.Score;
import org.gamescore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public boolean save(Score score) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO scores (user_id, game_id, score) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, score.getUserId());
                statement.setInt(2, score.getGameId());
                statement.setInt(3, score.getScore());
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Score> getHighestScores() {
        List<Score> highestScores = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT game_id, MAX(score) AS highest_score FROM scores GROUP BY game_id";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int gameId = resultSet.getInt("game_id");
                    int highestScore = resultSet.getInt("highest_score");
                    highestScores.add(new Score(gameId, highestScore));  // Add to list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highestScores;
    }

    public List<Score> getHighestScoresForUser(int userId) {
        List<Score> highestScores = new ArrayList<>();
        String query = "SELECT game_id, MAX(score) AS highest_score FROM scores WHERE user_id = ? GROUP BY game_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int gameId = resultSet.getInt("game_id");
                    int highestScore = resultSet.getInt("highest_score");
                    highestScores.add(new Score(userId, gameId, highestScore));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return highestScores;
    }

    public List<Score> getTop10ScoresForGame(int gameId) {
        List<Score> topScores = new ArrayList<>();
        String query = "SELECT user_id, score FROM scores WHERE game_id = ? ORDER BY score DESC LIMIT 10";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, gameId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    int score = resultSet.getInt("score");
                    topScores.add(new Score(userId, gameId, score));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topScores;
    }
}

