package com.localexpand;

import java.util.List;

/**
 * Created on 2016/5/23.
 *
 * @author 王启航
 * @version 1.0
 */
public interface Center {

    List<Node> findCenter(double centrality[]);

    void debug();
}
