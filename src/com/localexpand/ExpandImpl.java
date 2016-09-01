package com.localexpand;

import com.base.Graph;

import java.util.*;

/**
 * Created on 2016/5/23.
 * 局部社区的扩张：
 * 1.在中心节点的邻居中，找出Fitness最大的节点，加入其社区。
 * 2.在找出这2个节点的邻居，找出Fitness最大的节点，加入其社区。
 * 3.一直迭代，直至某个节点加入社区后，社区的Fitness < 0.
 *
 * @author 王启航
 * @version 1.0
 */
public class ExpandImpl implements Expand {
    List<Graph.AdjNode> mGraph;
    /**
     * 存储了一个社区的所有结构
     * Key-> 社区的中心节点
     * Value-> 该中心节点对应的一个社区
     */
    Map<Node, List<Node>> mAllCommunity = new HashMap<>();
    private static final double alpha = 0.6; //Todo 怎么设置

    public ExpandImpl(Graph mGraph) {
        this.mGraph = mGraph.mGraph;
    }

    /**
     * @param centerList 中心节点的集合
     * @param centrality 每个节点的PageRank值
     * @return 由该图组成的社区
     */
    @Override
    public Map<Node, List<Node>> expand(List<Node> centerList, double centrality[]) {
        for (final Node aCenter : centerList) {
            // 由aCenter扩散的局部社区
            List<Node> localCommunity = new ArrayList<>();
            // 辅助队列
            Queue<Node> localCommunityQueue = new LinkedList<>();
            // 由aCenter扩张得到的社区
            localCommunityQueue.add(aCenter);
            localCommunity.add(aCenter);

            while (true) {
                Node aNodeInCommunity = localCommunityQueue.poll();

                // aCenter的局部社区已经形成了
                if (aNodeInCommunity == null)
                    break;

                List<Node> neighbors = findNeighbors(aCenter, localCommunity, centrality);

                Node maxFitnessNode = findNeighborsMaxFitnessInCommunity(neighbors, localCommunity);

                boolean allNodeFitnessIsPlus = allNodeIsValidOfFitness(maxFitnessNode, localCommunity);
                // 只有所有节点的Fitness都> 0 才可以加入.
                if (allNodeFitnessIsPlus) {
                    localCommunityQueue.add(maxFitnessNode);
                    localCommunity.add(maxFitnessNode);
                    maxFitnessNode.addParent(aCenter.nodeIndex);
                }

            }
            mAllCommunity.put(aCenter, localCommunity);
        }
        return mAllCommunity;
    }

    /**
     * 假设将 maxFitnessIndex 加入localCommunity社区,重新计算社区的Fitness
     */
    private boolean allNodeIsValidOfFitness(Node maxFitnessNode, List<Node> localCommunity) {
        if (maxFitnessNode == null)
            return false;
        List<Node> tempCommunity = new ArrayList<>();
        tempCommunity.add(maxFitnessNode);
        tempCommunity.addAll(localCommunity);

        // 对于每个节点对社区的Fitness值 只要有一个 < 0 就返回
        for (Node item : tempCommunity) {
            if (findOneNodeFitnessInCommunity(item, tempCommunity) < 0)
                return false;
        }

        return true;
    }

    /**
     * 找到一个邻居对这个社区的fitness值 贡献最大的一个
     */
    private Node findNeighborsMaxFitnessInCommunity(List<Node> neighbors, List<Node> localCommunity) {
        double maxFitness = -100;
        Node maxNode = null;

        for (Node neighbor : neighbors) {
            double fitnessForCommunity = findOneNodeFitnessInCommunity(neighbor, localCommunity);
            // System.out.println("Fitness " + neighbor.nodeIndex + " " + fitnessForCommunity);
            if (fitnessForCommunity > maxFitness) {
                maxFitness = fitnessForCommunity;
                maxNode = neighbor;
            }
        }
        if (maxNode == null)
            return null;
        // System.out.println("Max " + maxNode.nodeIndex);
        return maxNode;
    }

    private double findOneNodeFitnessInCommunity(Node targetNode, List<Node> localCommunity) {
        List<Node> include = new ArrayList<>();
        List<Node> exclude = new ArrayList<>();
        include.addAll(localCommunity);
        exclude.addAll(localCommunity);

        if (localCommunity.contains(targetNode)) {
            exclude.remove(targetNode);
        } else {
            include.add(targetNode);
        }
        double includeFit = calculateCommunityFitness(include);
        double excludeFit = calculateCommunityFitness(exclude);
        return includeFit - excludeFit;
    }

    private double calculateCommunityFitness(List<Node> community) {
        int allFitness = 0;
        int inFitness = 0;
        for (int i = 0; i < community.size(); ++i) {

            allFitness += mGraph.get(community.get(i).nodeIndex).degree;
            for (int j = i; j < community.size(); ++j) {
                if (mGraph.get(community.get(i).nodeIndex).isConnect(community.get(j).nodeIndex)) {
                    ++inFitness;
                }
            }
        }
        return (inFitness * 2) / Math.pow((allFitness * 2), alpha);
    }

    /**
     * 得到 aNodeInCommunity 的所有的邻居
     *
     * @param targetNode 被查找的节点
     */
    private List<Node> findNeighbors(Node targetNode, List<Node> localCommunity, double[] centrality) {
        List<Node> neighbors = new ArrayList<>();
        Graph.AdjEdge firstEdge = mGraph.get(targetNode.nodeIndex).firstEdge;
        while (firstEdge != null) {
            Node neighbor = new Node(firstEdge.pointTo, centrality[firstEdge.pointTo]);
            if (!localCommunity.contains(neighbor)) {
                neighbors.add(neighbor);
            }
            firstEdge = firstEdge.next;
        }
        return neighbors;
    }

}
