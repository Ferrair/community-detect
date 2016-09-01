package com.base;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created on 2016/3/31.
 *
 * 使用了邻接表实现的图，具有DFS，LFS遍历
 * @author 王启航
 * @version 1.0
 */
public class Graph {
    public List<AdjNode> mGraph = new ArrayList<>(); //每个节点
    public boolean[] visited; // 遍历时判断是否访问过
    private Queue<Integer> mQueue; // LFS所使用的队列

    public Graph(int nodeNum) {
        for (int i = 0; i < nodeNum; ++i)
            addNode();
    }

    /**
     * 存储图的每一条边的信息
     *
     */
    public class AdjEdge {
        public int weight;   //权重
        public int pointTo;  //该边所依附的点(弧头)
        public AdjEdge next; //弧尾相同的下一条边
    }

    public class AdjNode {
        public AdjEdge firstEdge; //该顶点的第一条边(随机选的)
        public int degree = 0;

        public void addEdge(AdjEdge newEdge) {
            newEdge.next = firstEdge;
            firstEdge = newEdge;
            ++degree;
        }

        public int getAllWeight() {
            AdjEdge edge = firstEdge;
            int weightSum = 0;
            while (edge != null) {
                weightSum += edge.weight;
                edge = edge.next;
            }
            return weightSum;
        }

        /**
         * return the weight between this node and pointTo-node.
         */
        public int getWeight(int pointTo) {
            AdjEdge edge = firstEdge;
            while (edge != null) {
                if (edge.pointTo == pointTo) {
                    return edge.weight;
                }
                edge = edge.next;
            }
            return -1;
        }

        /**
         * Judge whether this node is connect with nodeIndex;
         */
        public boolean isConnect(int nodeIndex) {
            AdjEdge edge = firstEdge;
            while (edge != null) {
                if (edge.pointTo == nodeIndex) {
                    return true;
                }
                edge = edge.next;
            }
            return false;
        }
    }

    public void addNode() {
        AdjNode newNode = new AdjNode();
        newNode.firstEdge = null;
        mGraph.add(newNode);
    }

    //构造无向边
    public void addEdge(int from, int to, int weight) {
        AdjNode fromNode = mGraph.get(from);
        AdjEdge fromEdge = new AdjEdge();
        fromEdge.weight = weight;
        fromEdge.pointTo = to;
        fromEdge.next = null;
        fromNode.addEdge(fromEdge);

        AdjNode toNode = mGraph.get(to);
        AdjEdge toEdge = new AdjEdge();
        toEdge.weight = weight;
        toEdge.pointTo = from;
        toEdge.next = null;
        toNode.addEdge(toEdge);
    }

    /**
     * 深度优先遍历：递归的方式
     */
    private void dfs(int startIndex) {

        System.out.println("Visit -> " + startIndex);
        visited[startIndex] = true;

        if (mGraph.get(startIndex).firstEdge != null) {
            AdjEdge edge = mGraph.get(startIndex).firstEdge;
            while (edge != null) {
                if (!visited[edge.pointTo])
                    dfs(edge.pointTo);
                edge = edge.next;   //在每个顶点所维护的链表上循环
            }
        }
    }

    /**
     *  广度优先遍历：队列
     */
    private void lfs(int startIndex) {
        mQueue.offer(startIndex);
        visited[startIndex] = true;
        while (mQueue.size() != 0) {
            int outElement = mQueue.poll();
            System.out.println("Visit -> " + outElement);
            AdjEdge edge = mGraph.get(outElement).firstEdge;
            while (edge != null) {
                if (!visited[edge.pointTo]) {
                    mQueue.offer(edge.pointTo);
                    visited[edge.pointTo] = true;
                }
                edge = edge.next;
            }

        }
    }

    public void traverse() {
        for (AdjNode node : mGraph) {
            AdjEdge edge = node.firstEdge;
            while (edge != null) {
                System.out.println("<" + mGraph.indexOf(node) + "," + edge.pointTo + ">  Weight " + edge.weight);
                edge = edge.next;
            }
        }
    }

    public void traverseDFS(int startIndex) {
        visited = new boolean[mGraph.size()];
        dfs(startIndex);
    }

    public void traverseLFS(int startIndex) {
        mQueue = new LinkedList<>();
        visited = new boolean[mGraph.size()];
        lfs(startIndex);
    }

    public int size() {
        return mGraph.size();
    }

    /**
     * 读取指定目录下的文件构造图
     *
     * @param filePath 路径
     */
    public static Graph build(String filePath,int verNum) {
        Graph graph = new Graph(verNum);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line = reader.readLine();
            while (line != null) {
                // 正则表达式进行匹配，匹配任意长度的空字符
                String[] edges = line.trim().split("\\s+");
                graph.addEdge(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]), Integer.parseInt(edges[2]));
                // System.out.println(edges[0] + " " + edges[1] + " " + edges[2]);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

}

