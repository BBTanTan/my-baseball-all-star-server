package bbTan.my_baseball_all_star.service;

import bbTan.my_baseball_all_star.domain.TeamRoaster;
import bbTan.my_baseball_all_star.global.exception.AllStarException;
import bbTan.my_baseball_all_star.global.exception.ExceptionCode;
import java.util.ArrayList;
import java.util.List;

public class TeamScoreCalculator {

    private static final double SCORE_MIN = 1.0;
    private static final double SCORE_MAX = 90.0;
    private static final double SCORE_RANGE = SCORE_MAX - SCORE_MIN;
    private static final int MAX_MAPPED_SCORE = 15;
    private static final int MAPPED_SCORE_BUCKETS = MAX_MAPPED_SCORE + 1;
    private static final int PLAYER_COUNT = 12;

    public static Integer calculate(TeamRoaster team) {
        List<Double> playerScores = team.readPlayerScores();
        List<Long> playerChoiceCounts = team.getPlayerChoiceCounts();

        List<Integer> mappedScores = mapScores(playerScores);
        double[] probabilities = calculateProbabilities(playerChoiceCounts);
        return chooseScore(mappedScores, probabilities);
    }

    private static List<Integer> mapScores(List<Double> playerScores) {
        List<Integer> mappedScores = new ArrayList<>();
        for (double score : playerScores) {
            double cdf = (score - SCORE_MIN) / SCORE_RANGE;
            int mapped = Math.min(MAX_MAPPED_SCORE, (int)(cdf * MAPPED_SCORE_BUCKETS));
            mappedScores.add(mapped);
        }
        return mappedScores;
    }

    private static double[] calculateProbabilities(List<Long> counts) {
        long total = counts.stream().mapToLong(Long::longValue).sum();
        if (total == 0) total = 1;

        double[] probabilities = new double[PLAYER_COUNT];
        for (int i = 0; i < PLAYER_COUNT; i++) {
            probabilities[i] = (double) counts.get(i) / total;
        }
        return probabilities;
    }

    private static int chooseScore(List<Integer> mappedScores, double[] probabilities) {
        double r = Math.random();
        double cumulative = 0.0;
        for (int i = 0; i < PLAYER_COUNT; i++) {
            cumulative += probabilities[i];
            if (r <= cumulative) {
                return mappedScores.get(i);
            }
        }
        return mappedScores.get(PLAYER_COUNT - 1);
    }
}

