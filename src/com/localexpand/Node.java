package com.localexpand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/5/23.
 *
 * @author 王启航
 * @version 1.0
 */
public class Node {
    public double centrality;
    public int nodeIndex;
    public List<Integer> parent = new ArrayList<>();

    public Node(int nodeIndex, double centrality) {
        this.centrality = centrality;
        this.nodeIndex = nodeIndex;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Node) && (((Node) obj).nodeIndex == nodeIndex);
    }

    @Override
    public int hashCode() {
        return nodeIndex;
    }

    public void addParent(int parentIndex) {
        parent.add(parentIndex);
    }
}
