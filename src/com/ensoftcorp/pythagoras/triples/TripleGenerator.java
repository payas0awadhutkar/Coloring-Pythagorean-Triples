package com.ensoftcorp.pythagoras.triples;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

public class TripleGenerator {

	private static AtlasSet<Node> tripleNodes;

	private static Set<Triple> triples;

	public static void generate(int n) {

		if(n < 5) {
			return;
		}

		tripleNodes = new AtlasHashSet<Node>();
		triples = new HashSet<Triple>();
		int m = (int) Math.sqrt(2*n);
		for(int b = 1; b < m; b = b + 2) {
			for(int a = b + 2; a < Math.ceil(Math.sqrt(2*n - b*b)); a = a + 2) {
				if(gcd(a,b) == 1) {
					// triple is 2ab, a^2 - b^2, a^2 + b^2
					int xp = a*b;
					int yp = (a*a - b*b)/2;
					int t = 0;
					// we enforce x < y for regularity
					if(xp > yp) {
						t = xp;
						xp = yp;
						yp = t;
					}
					int zp = (a*a + b*b)/2;
					for(int i = 1; i <= Math.ceil(n/zp); i++) {
						// Log.info(x+"," +y+","+z);
						int x = xp*i;
						AtlasSet<Node> xmatches = new AtlasHashSet<Node>();
						xmatches.addAll(Query.universe().selectNode(XCSG.name,x + "").eval().nodes());
						int y = yp*i;
						AtlasSet<Node> ymatches = new AtlasHashSet<Node>();
						ymatches.addAll(Query.universe().selectNode(XCSG.name,y + "").eval().nodes());
						int z = zp*i;
						AtlasSet<Node> zmatches = new AtlasHashSet<Node>();
						zmatches.addAll(Query.universe().selectNode(XCSG.name,z + "").eval().nodes());
						Triple triple = new Triple(x,y,z);
						triples.add(triple);
						Node tripleNode = triple.getTripleNode();
						tripleNodes.add(tripleNode);
						Node xNode = triple.aNode();
						if(xmatches != null) {
							for(Node xmatch: xmatches) {
								if(!xmatch.equals(xNode)) {
									Edge e = Graph.U.createEdge(xmatch, xNode);
									e.tag(TripleXCSG.Number_Edge);
									Node matchTripleNode = xmatch.oneIn(XCSG.Contains).from();
									Edge e2 = Graph.U.createEdge(matchTripleNode, tripleNode);
									e2.tag(TripleXCSG.Share_Edge);
								}
							} 
						}
						if(ymatches != null) {
							Node yNode = triple.bNode();
							for(Node ymatch: ymatches) {
								if(!ymatch.equals(yNode)) {
									Edge e = Graph.U.createEdge(ymatch, yNode);
									e.tag(TripleXCSG.Number_Edge);
									Node matchTripleNode = ymatch.oneIn(XCSG.Contains).from();
									Edge e2 = Graph.U.createEdge(matchTripleNode, tripleNode);
									e2.tag(TripleXCSG.Share_Edge);
								}
							} 
						}
						Node zNode = triple.cNode();
						if(zmatches != null) {
							for(Node zmatch: zmatches) {
								if(!zmatch.equals(zNode)) {
									Edge e = Graph.U.createEdge(zmatch, zNode);
									e.tag(TripleXCSG.Number_Edge);
									Node matchTripleNode = zmatch.oneIn(XCSG.Contains).from();
									Edge e2 = Graph.U.createEdge(matchTripleNode, tripleNode);
									e2.tag(TripleXCSG.Share_Edge);
								}
							} 
						}
					}				
				}
			}
		}
		formConnections();
	}

	private static void formConnections() {

	}

	private static int gcd(int a, int b) {
		if (b == 0) {
			return a;
		}
		return gcd(b, a % b);
	}

}
