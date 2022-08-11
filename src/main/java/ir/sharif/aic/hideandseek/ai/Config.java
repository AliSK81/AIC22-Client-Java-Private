package ir.sharif.aic.hideandseek.ai;

import ir.sharif.aic.hideandseek.protobuf.AIProto.GameView;
import ir.sharif.aic.hideandseek.protobuf.AIProto.Path;

import java.util.ArrayList;

public class Config {

    private static Config instance;
    private final Grid grid;
    private final int[][] dist;
    private ArrayList<Integer>[] next;
    private double[][] cost;

    private Config(GameView gameView) {
        grid = new Grid(gameView);
        dist = grid.floyd();
        initCost(gameView);
        initNext(gameView);
    }

    public static Config getInstance(GameView gameView) {
        if (instance == null) {
            instance = new Config(gameView);
        }
        return instance;
    }

    private void initCost(GameView gameView) {
        int n = gameView.getConfig().getGraph().getNodesCount();
        cost = new double[n][n];
        for (Path path : gameView.getConfig().getGraph().getPathsList()) {
            int i = path.getFirstNodeId() - 1;
            int j = path.getSecondNodeId() - 1;
            cost[i][j] = cost[j][i] = path.getPrice();
        }
    }

    @SuppressWarnings("unchecked")
    private void initNext(GameView gameView) {
        int n = gameView.getConfig().getGraph().getNodesCount();
        next = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            next[i] = grid.getNeighborNodes(i);
        }
    }


    public double getPathCost(int fromNodeId, int toNodeId) {
        return cost[fromNodeId - 1][toNodeId - 1];
    }

    public int getMinDistance(int fromNodeId, int toNodeId) {
        return dist[fromNodeId - 1][toNodeId - 1];
    }

    public ArrayList<Integer> getNeighborNodes(int nodeId) {
        return next[nodeId - 1];
    }


}