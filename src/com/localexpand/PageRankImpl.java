package com.localexpand;

import com.base.Graph;

import java.util.List;

/**
 * Created on 2016/5/21.
 * 在复杂社区中，计算每个节点的PangRank的值
 *
 * @author 王启航
 * @version 1.0
 */
public class PageRankImpl implements PageRank {
    public double PRCent[];
    public double PRCens[];
    List<Graph.AdjNode> mGraph;

    double res;
    double threshold;
    double c;

    int size;

    public PageRankImpl(Graph mGraph) {
        this.mGraph = mGraph.mGraph;
        size = mGraph.size();
        init();
    }

    private void init() {
        this.c = 0.85;
        this.threshold = 0.4; // 0.4经测试最合适
        this.res = threshold + 1.0;

        PRCent = new double[size];
        PRCens = new double[size];
        for (int i = 0; i < size; ++i) {
            PRCens[i] = 1.0 / size;
        }
    }

    /**
     * Calculate the centrality of all node in this Graph.
     *
     * 论文图3.2代码实现过程.
     */
    @Override()
    public double[] centrality() {
        while (res > threshold) {
            for (int i = 0; i < size; ++i) {
                PRCent[i] = 0;
            }

            for (int i = 0; i < size; ++i) {
                Graph.AdjNode targetNode = mGraph.get(i);
                if (targetNode.firstEdge == null) {
                    continue;
                }
                for (int j = 0; j < size; ++j) {
                    if (targetNode.getWeight(j) == -1) {
                        continue;
                    }
                    //Calculate the Centrality of node j.
                    PRCent[j] += PRCens[i] * ((double) targetNode.getWeight(j) / (double) targetNode.getAllWeight());
                }
            }

            for (int i = 0; i < size; ++i) {
                PRCent[i] = c * PRCent[i] + (1 - c) * (1 / size);
            }

            double sum = 0;
            for (int i = 0; i < size; ++i) {
                sum += Math.abs(PRCens[i] - PRCent[i]);
            }
            res = sum;
            System.out.println("Res-> " + res);
            System.arraycopy(PRCent, 0, PRCens, 0, size);
        }
        return PRCens;
    }

    @Override()
    public void debug() {
        for (int i = 0; i < size; i++) {
            System.out.println("PangRank: " + i + " " + PRCens[i]);
        }
    }
}
