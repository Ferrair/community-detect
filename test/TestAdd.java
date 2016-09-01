import com.base.Graph;
import com.localexpand.*;
import com.util.Generator;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created on 2016/3/31.
 *
 * @author 王启航
 * @version 1.0
 */
public class TestAdd {

    @Test
    public void test() {
       /*
        Graph graph = new Graph(8);
        graph.addEdge(0, 1, 7);
        graph.addEdge(0, 2, 5);
        graph.addEdge(0, 4, 5);
        graph.addEdge(0, 5, 5);
        graph.addEdge(0, 6, 5);
        graph.addEdge(0, 7, 3);
        graph.addEdge(3, 5, 4);
        graph.addEdge(6, 7, 3);
        */

        Graph graph = Graph.build("E:/network.txt", 36);

        PageRank pageRank = new PageRankImpl(graph);
        double[] centrality = pageRank.centrality();
        pageRank.debug();

        Center centerImpl = new CenterImpl(graph);
        List<Node> centerList = centerImpl.findCenter(centrality);
        centerImpl.debug();

        Expand expand = new ExpandImpl(graph);
        Map<Node, List<Node>> mAll = expand.expand(centerList, centrality);
        Generator.makeFile("E:/network1.txt", mAll);
        // Todo 每个节点都要在一个社区里面？
    }
}
