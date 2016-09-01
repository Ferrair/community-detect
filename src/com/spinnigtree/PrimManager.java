package com.spinnigtree;


import com.base.Graph;

import java.util.List;

/**
 * Created on 2016/3/31.
 * cost数组的更新是关键:cost更新的是 V中顶点和 U的距离（U是一个整体）
 * 注意Dijkstra算法是V中顶点与初始顶点v0的距离
 *
 * @author 王启航
 * @version 1.0
 */
public class PrimManager {

    private static final int MAX = Integer.MAX_VALUE;


    public static void run(Graph mGraph, int startIndex) {
        int nodeNum = mGraph.size();
        int[] cost = new int[nodeNum]; //存储第i个节点与U的最小weight
        List<Graph.AdjNode> mNode = mGraph.mGraph;

        //Init1---更新所有startIndex的可达边的权重
        Graph.AdjEdge edge = mNode.get(startIndex).firstEdge;
        while (edge != null) {
            cost[edge.pointTo] = edge.weight;
            edge = edge.next;
        }
        //Init2---更新其他的边(不可达的边-> MAX,startIndex ->0)
        for (int i = 0; i < nodeNum; ++i) {
            if (i != startIndex && cost[i] == 0) {
                cost[i] = MAX;
            }
        }

        //Prim Main
        for (int i = 0; i < nodeNum; ++i) {
            if (i == startIndex)
                continue;
            //在cost里面找最小权值的边对应的顶点 即minIndex
            int minValue = MAX;
            int minIndex = 0;
            for (int j = 0; j < nodeNum; ++j) {
                if (cost[j] != 0 && cost[j] < minValue) {
                    minValue = cost[j];
                    minIndex = j;
                }
            }
            System.out.println("Vertex-> " + minIndex);

            //添加到U里面去(即cost数组为0即可)
            cost[minIndex] = 0;

            //更新cost[]
            for (int k = 0; k < nodeNum; ++k) {
                int tempWeight = getWeight(mGraph, k, minIndex);
                //cost[k]不再U里面，并且weight可以更新 就更新
                if (cost[k] != 0 && (cost[k] > tempWeight)) {
                    cost[k] = tempWeight;
                }
            }
        }
    }

    //若2个顶点不可达 返回MAX
    private static int getWeight(Graph mGraph, int from, int to) {
        Graph.AdjEdge edge = mGraph.mGraph.get(from).firstEdge;
        while (edge != null) {
            if (edge.pointTo == to)
                return edge.weight;
            edge = edge.next;
        }
        return MAX;
    }
}
