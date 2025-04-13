/*
 * TC: O(V + E)
 * SC: O(V + E) for graph and O(V) for ranks
 */
import java.util.*;

public class CriticalConnections {

    // This class is used to represent an edge in the graph
    record Edge(int u, int v) {}

    List<List<Integer>> graph;
    Set<Edge> edges;
    int[] ranks;

    /*
     * This method finds all critical connections in a given undirected graph.
     * It uses a depth-first search (DFS) approach to identify edges that,
     * if removed, would increase the number of connected components in the graph.
     */
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        graph = new ArrayList<>();
        edges = new HashSet<>();
        ranks = new int[n];
        Arrays.fill(ranks, -1);
        for(int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        // Constructing the graph and edges from the
        // given connections
        for(List<Integer> conn: connections) {
            int u = conn.get(0);
            int v = conn.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
            edges.add(new Edge(Math.min(u, v), Math.max(u, v)));
        }
        // Start DFS from the first node (0) with initial rank 0
        dfs(0, 0);

        List<List<Integer>> result = new ArrayList<>();
        for(Edge edge: edges) {
            result.add(Arrays.asList(edge.u, edge.v));
        }
        return result;
    }
    /*
     * This method performs a depth-first search (DFS) on the graph to find
     * the ranks of nodes and identify critical connections.
     * It recursively explores the graph, updating the ranks of nodes
     * and removing edges that do not meet the criteria for critical connections.
     */
    int dfs(int u, int rank) {
        // If the rank of the current node is already set, return it
        if(ranks[u] != -1) {
            return ranks[u];
        }
        // Set the rank of the current node
        ranks[u] = rank;
        // Initialize the minimum rank to the next rank
        int minRank = rank + 1;
        // Iterate through all adjacent nodes
        for(int v: graph.get(u)) {
            // If the adjacent node is already visited, skip it
            if(ranks[v] == -1 || ranks[v] != rank - 1) {
                // If the adjacent node is not visited, perform DFS on it
                int vRank = dfs(v, rank + 1);
                // If the rank of the adjacent node is less than or equal to the current rank,
                // remove the edge from the set of edges
                if(vRank <= rank) {
                    edges.remove(new Edge(Math.min(u, v), Math.max(u, v)));
                }
                // Update the minimum rank
                minRank = Math.min(minRank, vRank);
            }
        }
        // Return the minimum rank found during the DFS
        return minRank;
    }
}
