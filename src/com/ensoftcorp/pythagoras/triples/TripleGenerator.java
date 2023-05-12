package com.ensoftcorp.pythagoras.triples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.log.Log;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

public class TripleGenerator {

	private static AtlasSet<Node> tripleNodes;

	private static Map<Node,Triple> tripleMap;

	public static Q run(int n) {
		tripleNodes = new AtlasHashSet<Node>();
		tripleMap = new HashMap<Node,Triple>();
		generate(n);
		formTSetNodes(n);
		return Common.empty();
	}

	private static void formTSetNodes(int n) {
		Q numberNodesQ = Query.universe().nodes(TripleXCSG.Number);
		for(int i = 1; i <= n; i++) {
			Node iTSetNode = Graph.U.createNode();
			iTSetNode.tag(TripleXCSG.TSet);
			iTSetNode.putAttr(XCSG.name, "T(" + i + ")");
			Set<Integer> nums = new HashSet<Integer>();
			Q iTripleNodesQ = numberNodesQ.selectNode(XCSG.name,i+"");
			AtlasSet<Node> iTripleNodes = iTripleNodesQ.parent().eval().nodes();
			for(Node iTripleNode: iTripleNodes) {
				Edge e = Graph.U.createEdge(iTSetNode, iTripleNode);
				e.tag(TripleXCSG.TSetMember);
				Triple triple = tripleMap.get(iTripleNode);
				nums.add(triple.a());
				nums.add(triple.b());
				nums.add(triple.c());
			}
			iTSetNode.putAttr(TripleXCSG.tSetLiterals, nums.toString());
			for(Node iTripleNode1: iTripleNodes) {
				for(Node iTripleNode2: iTripleNodes) {
					if(!iTripleNode1.equals(iTripleNode2)) {
						Edge e = Graph.U.createEdge(iTripleNode1, iTripleNode2);
						e.tag(TripleXCSG.Share_Edge);
						e.putAttr(TripleXCSG.sharedNumber, i + "");
					}
				}
			}
		}
	}

	public static void generate(int n) {

		if(n < 5) {
			return;
		}

		//		int x,y,z = 0;
		//		int a = 2;
		//		while(Math.sqrt(n) - a > 0) {
		//			for(int b = 1; b < a; b++) {
		//				if(a > 87 && b == 9) {
		//					System.out.println();
		//				}
		//				if(a % 2 == 0 || b % 2 == 0) {
		//					if(gcd(a,b) == 1) {
		//						z = a*a + b*b;
		//						if(z <= n) {
		//							x = a*a - b*b;
		//							y = 2*a*b;
		//							if(x > y) {
		//								int t = x;
		//								x = y;
		//								y = t;
		//							}
		//							Triple triple = new Triple(x,y,z);
		//							triples.add(triple);
		//							tripleNodes.add(triple.getTripleNode());
		//							// scale triples
		//							int k = 2;
		//							int kx = k*x;
		//							int ky = k*y;
		//							int kz = k*z;
		//							while(kz <= n) {
		//								Triple ktriple = new Triple(kx,ky,kz);
		//								triples.add(ktriple);
		//								tripleNodes.add(ktriple.getTripleNode());
		//								k = k + 1;
		//								kx = k*x;
		//								ky = k*y;
		//								kz = k*z;
		//							}
		//						}
		//					}
		//				}
		//				b = b + 1;
		//			}
		//			a = a + 1;
		//		}

		for(int a = 2; a < n; a++) {
			for(int b = 1; b < a; b++) {
				int csquare = a*a + b*b;
				if(isPerfectSquare(csquare)) {
					int c = (int) Math.sqrt(csquare);
					if(c <= n) {
						Triple triple = new Triple(b,a,c);
						Node tripleNode = triple.getTripleNode();
						tripleNodes.add(tripleNode);
						tripleMap.put(tripleNode, triple);
					}
				}
			}
		}

		Log.info("Number of Triples: " + tripleNodes.size());
	}

	private static boolean isPerfectSquare(int x) {
		if (x >= 0) {
			int rootX = (int) Math.sqrt(x);
			return ((rootX * rootX) == x);
		}
		return false;
	}

	//	private static int gcd(int a, int b) {
	//		if (b == 0) {
	//			return a;
	//		}
	//		return gcd(b, a % b);
	//	}

}
