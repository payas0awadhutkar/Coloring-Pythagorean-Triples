package com.ensoftcorp.pythagoras.triples;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.markup.IMarkup;
import com.ensoftcorp.atlas.core.markup.Markup;
import com.ensoftcorp.atlas.core.markup.MarkupProperty;
import com.ensoftcorp.atlas.core.markup.PropertySet;
import com.ensoftcorp.atlas.core.markup.MarkupProperty.LayoutDirection;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.atlas.ui.viewer.graph.DisplayUtil;
import com.ensoftcorp.atlas.ui.viewer.graph.SaveUtil;
import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;

public class TripleGenerator {
	
	public static Q generate(int limit) {
		
		AtlasSet<Node> nodes = Query.universe().eval().nodes();
		Set<Node> previousNodes = new HashSet<Node>();
		for(Node node: nodes) {
			previousNodes.add(node);
		}
		
		for(Node previousNode: previousNodes) {
			Graph.U.delete(previousNode);
		}
		
		Set<Triple> triples = _generate(limit);
		
		System.out.println("Number of Unqiue Triples: " + triples.size());
		
		List<Triple> sortedTriples = new ArrayList<Triple>();
		sortedTriples.addAll(triples);
		Collections.sort(sortedTriples, new Comparator<Triple>() {

			@Override
			public int compare(Triple o1, Triple o2) {
				return Integer.compare(o1.c(), o2.c());
			}
		});
		
		Set<Integer> sides = new HashSet<Integer>();
		Set<Integer> hypotenuses = new HashSet<Integer>();
		for(Triple triple: triples) {
			sides.add(triple.a());
			sides.add(triple.b());
			
			hypotenuses.add(triple.c());
		}
		
		List<Integer> sortedHypotenuses = new ArrayList<Integer>();
		sortedHypotenuses.addAll(hypotenuses);
		Collections.sort(sortedHypotenuses, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o1, o2);
			}
		});
		
		Map<Triple, Node> tripleToNodeMap = new HashMap<Triple, Node>();
		for(Triple triple: triples) {
			Node tripleNode = Graph.U.createNode();
			tripleNode.putAttr(XCSG.name, triple.toString());
			tripleNode.tag("XCSG.Triple");
			
			tripleToNodeMap.put(triple, tripleNode);
		}
		
		Set<Triple> equivalentTriples = new HashSet<Triple>();
		for(int i = 0; i < sortedTriples.size(); i++) {
			Triple t1 = sortedTriples.get(i);
			for(int j = 0; j < i; j++) {
				Triple t2 = sortedTriples.get(j);
				if(t1.equivalent(t2)) {
					equivalentTriples.add(t1);
					equivalentTriples.add(t2);
					System.out.println("======");
					System.out.println(t1 + "------" + t2);
					System.out.println("======");
					
					Node t1Node = tripleToNodeMap.get(t1);
					Node t2Node = tripleToNodeMap.get(t2);
					Edge commonEdge = Graph.U.createEdge(t1Node, t2Node);
					commonEdge.tag("XCSG.Shares");
				}
			}
		}
		
		System.out.println("Equivalent Triples Count: " + equivalentTriples.size());
		
		Q query = Query.universe().nodes("XCSG.Triple");
		
		IMarkup markup = new IMarkup() {

			@Override
			public PropertySet get(GraphElement element) {
				PropertySet defaultSet= new PropertySet();
				if(element instanceof Edge)
					defaultSet.set(MarkupProperty.EDGE_LAYOUT_DIRECTION, MarkupProperty.LayoutDirection.NONE);
					
				return defaultSet;
			}
			
		};
		AtlasSet<Edge> edges = Query.universe().edges("XCSG.Shares").eval().edges();
//		for(Edge edge: edges) {
//			markup.setEdge(Common.toQ(edge), MarkupProperty.EDGE_LAYOUT_DIRECTION, LayoutDirection.NONE);
//		}
		query = query.induce(Common.toQ(edges)).retainEdges();
		
		return query;
		
		//DisplayUtil.displayGraph(markup, query.eval());
		
//		Map<Integer, Node> sideToNodeMap = new HashMap<Integer, Node>();
//		
//		for(int i = 0; i < sortedHypotenuses.size(); i++) {
//			int hypotenuse = sortedHypotenuses.get(i);
//			for(Triple triple: sortedTriples) {
//				int a = triple.a();
//				int b = triple.b();
//				int c = triple.c();
//				if(c > hypotenuse) {
//					break;
//				} else if(c < hypotenuse) {
//					continue;
//				}
//				
//				Node aNode = null;
//				if(sideToNodeMap.containsKey(a)) {
//					aNode = sideToNodeMap.get(a);
//				} else {
//					aNode = Graph.U.createNode();
//					aNode.putAttr(XCSG.name, String.valueOf(a));
//					aNode.tag("XCSG.Side");
//					sideToNodeMap.put(a, aNode);
//				}
//				
//				Node bNode = null;
//				if(sideToNodeMap.containsKey(b)) {
//					bNode = sideToNodeMap.get(b);
//				} else {
//					bNode = Graph.U.createNode();
//					bNode.putAttr(XCSG.name, String.valueOf(b));
//					bNode.tag("XCSG.Side");
//					sideToNodeMap.put(b, bNode);
//				}
//				
//				Node cNode = null;
//				if(sideToNodeMap.containsKey(c)) {
//					cNode = sideToNodeMap.get(c);
//				} else {
//					cNode = Graph.U.createNode();
//					cNode.putAttr(XCSG.name, String.valueOf(c));
//					cNode.tag("XCSG.Hypotenuse");
//					sideToNodeMap.put(c, cNode);
//				}
//				
//				Edge aToCEdge = Graph.U.createEdge(aNode, cNode);
//				aToCEdge.tag("XCSG.Forms");
//				aToCEdge.putAttr("XCSG.OtherSide", b);
//				
//				Edge bToCEdge = Graph.U.createEdge(bNode, cNode);
//				bToCEdge.tag("XCSG.Forms");
//				bToCEdge.putAttr("XCSG.OtherSide", a);
//			}
//			
//			Q query = Query.universe().nodes("XCSG.Side", "XCSG.Hypotenuse");
//			
//			Markup markup = new Markup();
//			markup.setNode(query.nodes("XCSG.Side"), MarkupProperty.NODE_BACKGROUND_COLOR, Color.LIGHT_GRAY);
//			markup.setNode(query.nodes("XCSG.Hypotenuse"), MarkupProperty.NODE_BACKGROUND_COLOR, Color.BLUE);
//			AtlasSet<Edge> edges = Query.universe().edges("XCSG.Forms").eval().edges();
//			for(Edge edge: edges) {
//				markup.setEdge(Common.toQ(edge), MarkupProperty.LABEL_TEXT, edge.getAttr("XCSG.OtherSide").toString());
//			}
//			
//			query = query.induce(Common.toQ(edges)).retainEdges();
//			
//			AtlasSet<Node> freeNodes = new AtlasHashSet<Node>();
//			for(Node node: query.roots().eval().nodes()) {
//				if(node.out().size() == 1) {
//					freeNodes.add(node);
//				}
//			}
//			markup.setNode(Common.toQ(freeNodes), MarkupProperty.NODE_BACKGROUND_COLOR, Color.YELLOW);
//			Graph graph = query.eval();
//			//saveGraph(hypotenuse, graph, markup);
//
//		}
				

	}
	
	public static synchronized void saveGraph(int hypotenuse, Graph graph, Markup markup) {
		//DisplayUtil.displayGraph(markup, query.eval());
   	 SaveUtil.saveGraph(new File("/Users/atamrawi/Desktop/primtive-pythogrean-triples/" + hypotenuse + ".png"), graph, markup);
     System.out.println("Saving on " + hypotenuse);
	}
	
	private static Set<Triple> _generate(int limit) {
		Set<Triple> triplets = new HashSet<Triple>();
		for(int c = 5; c <= limit; c++) {
			for(int a = 1; a < c; a++) {
				double candidateB = Math.sqrt(c*c - a*a);
				int b = (int) candidateB;
				if(Math.abs(b - candidateB) < 0.000001) {
					//int gcd = gcd(a,gcd(b,c));
					//if(gcd == 1) {
						triplets.add(new Triple(a, b, c));
						System.out.println(a + ", " + b + ", " + c);
					//}
				}
			}
		}
		return triplets;
	}

	
    public static int gcd(int num1, int num2) 
    {
        if (num2 == 0) {
            return num1;
        }
    
        return gcd (num2, num1 % num2);
    }
    
}
