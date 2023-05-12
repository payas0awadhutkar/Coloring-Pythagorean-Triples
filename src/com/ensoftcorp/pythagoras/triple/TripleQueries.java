package com.ensoftcorp.pythagoras.triple;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.query.Query;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.script.CommonQueries;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

public class TripleQueries {
	
	public static Q computeOrbit(Q triple) {
		Q orbit = Common.empty();
		
		
		
		return Common.empty();
	}
	
	public static Q myInduce(Q nodes) {
		AtlasSet<GraphElement> result = new AtlasHashSet<GraphElement>();
		AtlasSet<Node> nodes1 = new AtlasHashSet<Node>();
		AtlasSet<Node> nodes2 = new AtlasHashSet<Node>();
		nodes1.addAll(nodes.eval().nodes());
		nodes2.addAll(nodes.eval().nodes());
		Q e = Query.universe().edges(TripleXCSG.Share_Edge);
		for(Node node1: nodes1) {
			for(Node node2: nodes2) {
				Q node1Q = Common.toQ(node1);
				Q node2Q = Common.toQ(node2);
				if(!CommonQueries.isEmpty(e.successors(node1Q).intersection(node2Q))) {
					AtlasSet<Edge> inducedEdges = e.forwardStep(node1Q).intersection(node1Q.union(node2Q)).eval().edges();
					result.addAll(inducedEdges);
				}
			}
		}
		return Common.toQ(result);
	}

}
