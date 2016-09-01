package com.spinnigtree;


import java.util.ArrayList;
import java.util.List;

import com.base.Graph;

/**
 * Created on 2016/4/6.
 * Kruskal:构造最小生成树
 * 关键：防止回路
 *
 * @author 王启航
 * @version 1.0
 */
public class KruskalManager {
    private static final int MAX = Integer.MAX_VALUE;

    private static class Edge {
        int vex1, vex2;
        int weight;

        public Edge(int vex1, int vex2, int weight) {
            this.vex1 = vex1;
            this.vex2 = vex2;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "<" + vex1 + "," + vex2 + ">";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Edge) {
                Edge target = (Edge) obj;
                if (target.vex1 == this.vex1 && target.vex2 == this.vex2 && target.weight == this.weight)
                    return true;
                if (target.vex1 == this.vex2 && target.vex2 == this.vex1 && target.weight == this.weight)
                    return true;
                return false;
            }
            return false;
        }
    }

    private static int parent[];

    public static void run(Graph mGraph) {
        //Init1---生成每个边属性的列表
        List<Edge> mEdge = generateList(mGraph);
        int size = mEdge.size();
        //Init2---初始化parent数组
        parent = new int[size];

        //Kruskal Main
        for (int i = 0; i < size; ++i) {
            //寻找最小权值的边
            int minEdge = MAX;
            int targetEdge = -1;
            for (int j = 0; j < size; ++j) {
                if (mEdge.get(j).weight < minEdge) {
                    minEdge = mEdge.get(j).weight;
                    targetEdge = j;
                }
            }

            //判断是否有回路 检查parent数组
            int vex1 = find(mEdge.get(targetEdge).vex1);
            int vex2 = find(mEdge.get(targetEdge).vex2);
            mEdge.get(targetEdge).weight = MAX;

            //更新parent
            if (vex1 != vex2) {
                parent[vex1] = vex2;
                System.out.println(mEdge.get(targetEdge).vex1 + "  " + mEdge.get(targetEdge).vex2);
            }
        }
    }

    private static int find(int vex) {
        while (parent[vex] > 0)
            vex = parent[vex];
        return vex;
    }

    private static List<Edge> generateList(Graph mGraph) {
        List<Edge> mEdge = new ArrayList<>();
        for (Graph.AdjNode node : mGraph.mGraph) {
            Graph.AdjEdge edge = node.firstEdge;
            while (edge != null) {
                Edge targetEdge = new Edge(mGraph.mGraph.indexOf(node), edge.pointTo, edge.weight);
                if (!mEdge.contains(targetEdge))
                    mEdge.add(targetEdge);
                edge = edge.next;
            }
        }
        return mEdge;
    }

}
