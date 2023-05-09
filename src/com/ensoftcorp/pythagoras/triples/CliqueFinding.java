package com.ensoftcorp.pythagoras.triples;

import java.util.HashMap;
import java.util.Map;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

// Java implementation of the approach
public class CliqueFinding {

	private int MAX = 10000;

	private int[] store;
	private int n;

// Graph
	private int[][] graph;

// Degree of the vertices
	private int[] d;

	private Map<Integer, String> idToNameMap;
	
	private int k;
	
	private int cliqueCount;

	public CliqueFinding(Q q, int k) {
		this.k = k;
		this.cliqueCount = 0;
		this.store = new int[MAX];
		this.idToNameMap = new HashMap<Integer, String>();
		this.graph = new int[MAX][MAX];
		this.d = new int[MAX];
		
		AtlasSet<Node> nodes = q.eval().nodes();
		this.n = (int) nodes.size();
		
		Map<String, Integer> nameToIdMap = new HashMap<String, Integer>();
		int count = 0;
		for (Node node : nodes) {
			count++;
			idToNameMap.put(count, node.getAttr(XCSG.name).toString());
			nameToIdMap.put(node.getAttr(XCSG.name).toString(), count);
		}

		AtlasSet<Edge> qEdges = q.eval().edges();
		int edges[][] = new int[(int) qEdges.size()][2];

		int index = 0;
		for (Edge edge : qEdges) {
			int[] arr = new int[2];
			Node from = edge.from();
			Node to = edge.to();
			int fromNode = nameToIdMap.get(from.getAttr(XCSG.name).toString());
			int toNode = nameToIdMap.get(to.getAttr(XCSG.name).toString());
			arr[0] = fromNode;
			arr[1] = toNode;
			edges[index] = arr;
			index++;
		}
		int size = edges.length;
		

		for (int i = 0; i < size; i++) {
			graph[edges[i][0]][edges[i][1]] = 1;
			graph[edges[i][1]][edges[i][0]] = 1;
			d[edges[i][0]]++;
			d[edges[i][1]]++;
		}
		
	}

// Function to check if the given set of vertices
// in store array is a clique or not
	private boolean is_clique(int b) {
		// Run a loop for all the set of edges
		// for the select vertex
		for (int i = 1; i < b; i++) {
			for (int j = i + 1; j < b; j++)

				// If any edge is missing
				if (this.graph[this.store[i]][this.store[j]] == 0)
					return false;
		}
		return true;
	}

// Function to print the clique
	private void print(int n) {
		System.out.println("$$$======================");
		for (int i = 1; i < n; i++) {
			String triple = this.idToNameMap.get(this.store[i]);
			//System.out.println(triple);
			if(i == 1) {
				this.cliqueCount++;
			}
		}
		System.out.println("======================$$$");
	}

// Function to find all the cliques of size s
	
	public void findCliques() {
		_findCliques(0, 1, k);
		System.out.println("Clique Count of Size [" + this.k + "]= " + this.cliqueCount);
	}
	
	private void _findCliques(int i, int l, int s) {
		// Check if any vertices from i+1 can be inserted
		for (int j = i + 1; j <= n - (s - l); j++)

			// If the degree of the graph is sufficient
			if (d[j] >= s - 1) {

				// Add the vertex to store
				store[l] = j;

				// If the graph is not a clique of size k
				// then it cannot be a clique
				// by adding another edge
				if (is_clique(l + 1))

					// If the length of the clique is
					// still less than the desired size
					if (l < s)

						// Recursion to add vertices
						_findCliques(j, l + 1, s);

					// Size is met
					else
						print(l + 1);
			}
	}

}

