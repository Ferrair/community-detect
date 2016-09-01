package com.localexpand;

import com.base.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/5/23.
 * 中心节点的查找：
 * 1.判断node的中心度是否大于阈值
 * 2.判断中心节点集合是否有该node的直接邻居
 *
 * @author 王启航
 * @version 1.0
 */
public class CenterImpl implements Center {

    List<Node> mList = new ArrayList<>();
    List<Graph.AdjNode> mGraph;
    private static double threshold;

    public CenterImpl(Graph mGraph) {
        this.mGraph = mGraph.mGraph;
        threshold = 1 / (double) mGraph.size();
    }

    @Override()
    public List<Node> findCenter(double centrality[]) {
        for (int i = 0; i < centrality.length; i++) {

            // Condition1 : the centrality of this node is greater than threshold.
            if (centrality[i] > threshold) {
                Node centerNode = new Node(i, centrality[i]);

                // Condition2 : the two centerNode can't connect.
                if (condition(centerNode)) {
                    mList.add(centerNode);
                }
            }
        }
        return mList;
    }

    @Override()
    public void debug() {
        for (Node node : mList) {
            System.out.println("CenterIndex: " + node.nodeIndex);
        }
    }

    /**
     * 2个中间节点不能直接相连
     * Todo 怎么选择这2个节点
     */
    private boolean condition(Node nodeToAdded) {
        // The First node that will be added in Center-Collection.
        if (mList.size() == 0) {
            return true;
        }
        Graph.AdjNode wantToAdd = mGraph.get(nodeToAdded.nodeIndex);

        for (Node centerNode : mList) {
            if (wantToAdd.isConnect(centerNode.nodeIndex)) {
                // Remove the lesser of the two node.
                /*if (nodeToAdded.centrality > centerNode.centrality) {
                    mList.remove(centerNode);
                }*/
                return true;
            }
        }
        return false;
    }
}
