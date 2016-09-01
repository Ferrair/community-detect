package com.localexpand;


import java.util.List;
import java.util.Map;

/**
 * Created on 2016/5/23.
 *
 * @author 王启航
 * @version 1.0
 */
public interface Expand {
    Map<Node, List<Node>> expand(List<Node> centerList, double centrality[]);
}
