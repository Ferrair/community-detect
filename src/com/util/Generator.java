package com.util;

import com.localexpand.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created on 2016/8/31.
 *
 * @author 王启航
 * @version 1.0
 */
public class Generator {
    public static void makeFile(String filePath, Map<Node, List<Node>> map) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (Map.Entry<Node, List<Node>> entry : map.entrySet()) {
                entry.getValue().forEach(node -> {
                    try {
                        writer.write(entry.getKey().nodeIndex + "," + node.nodeIndex + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
