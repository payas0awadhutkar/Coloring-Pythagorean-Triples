package com.ensoftcorp.pythagoras.triple;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.pythagoras.triples.util.TripleUtils;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

public class TripleGenerator {

	public static void run(int n) {
		generate(n);
		formTSetNodes(n);
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
				Set<Integer> tripleNums = TripleUtils.getNumbers(iTripleNode);
				nums.addAll(tripleNums);
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

	private static void generate(int n) {

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
		//					if(TripleUtils.gcd(a,b) == 1) {
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
				if(TripleUtils.isPerfectSquare(csquare)) {
					int c = (int) Math.sqrt(csquare);
					if(c <= n) {
						createTripleNode(a,b,c);
					}
				}
			}
		}

		
	}
	
	private static void createTripleNode(int a, int b, int c) {
		if(a > b) {
			int t = a;
			a = b;
			b = t;
		}
		Node aNode = Graph.U.createNode();
		aNode.tag(TripleXCSG.Number);
		aNode.putAttr(XCSG.name, a + "");
		Node bNode = Graph.U.createNode();
		bNode.tag(TripleXCSG.Number);
		bNode.putAttr(XCSG.name, b + "");
		Node cNode = Graph.U.createNode();
		cNode.tag(TripleXCSG.Number);
		cNode.putAttr(XCSG.name, c + "");
		Edge e1 = Graph.U.createEdge(aNode, cNode);
		e1.tag(TripleXCSG.Triple_Edge);
		Edge e2 = Graph.U.createEdge(bNode, cNode);
		e2.tag(TripleXCSG.Triple_Edge);
		Node tripleNode = Graph.U.createNode();
		tripleNode.tag(TripleXCSG.Triple);
		String tripleName = "(" + a + "," + b + "," + c + ")";
		tripleNode.putAttr(XCSG.name, tripleName);
		Edge e3 = Graph.U.createEdge(tripleNode, aNode);
		e3.tag(XCSG.Contains);
		Edge e4 = Graph.U.createEdge(tripleNode, bNode);
		e4.tag(XCSG.Contains);
		Edge e5 = Graph.U.createEdge(tripleNode, cNode);
		e5.tag(XCSG.Contains);
	}

}
