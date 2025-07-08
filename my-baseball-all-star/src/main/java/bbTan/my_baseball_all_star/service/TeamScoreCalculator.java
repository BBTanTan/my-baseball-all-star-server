package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import java.util.List;

public class TeamScoreCalculator {

    private static final double MAX_PLAYER_SCORE = 100.0;
    private static final double MAX_TEAM_SCORE = 20.0;
    private static final double BASE_WEIGHT = 1.0;

    public static Integer calculate(TeamRoaster team) {
        List<Double> playerScores = team.readPlayerScores();
        List<Long> playerChoiceCounts = team.getPlayerChoiceCounts();

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (int i = 0; i < playerScores.size(); i++) {
            double score = playerScores.get(i);
            long choiceCount = playerChoiceCounts.get(i);
            double weight = calculateWeight(choiceCount);
            weightedSum += score * weight;
            totalWeight += weight;
        }

        double weightedAverage = calculateWeightedAverage(weightedSum, totalWeight);
        return scaleToTeamScore(weightedAverage);
    }

    private static double calculateWeight(long choiceCount) {
        return Math.log(choiceCount + 1) + BASE_WEIGHT;
    }

    private static double calculateWeightedAverage(double weightedSum, double totalWeight) {
        return totalWeight > 0 ? weightedSum / totalWeight : 0.0;
    }

    private static int scaleToTeamScore(double weightedAverage) {
        double normalized = weightedAverage / MAX_PLAYER_SCORE;
        double teamScore = normalized * MAX_TEAM_SCORE;
        return (int) Math.round(teamScore);
    }
}

