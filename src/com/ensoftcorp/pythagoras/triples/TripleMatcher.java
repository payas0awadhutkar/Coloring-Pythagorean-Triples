package com.ensoftcorp.pythagoras.triples;

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

public class TripleMatcher {
	
	public static Q matchTriplesQ(Q triples) {
		return matchTriples(triples.eval().nodes());
	}

	public static Q matchTriples(AtlasSet<Node> triples) {
		Q filteredTriples = Common.empty();
		Q shareEdges = Query.universe().edges(TripleXCSG.Share_Edge);
		Q memberEdges = Query.universe().edges(TripleXCSG.TSetMember);
		for(Node tripleNode: triples) {
			Q tR1Q = Common.empty();
			Q tripleNodeQ = Common.toQ(tripleNode);
			Q connections = shareEdges.forwardStep(tripleNodeQ);
			if(connections.eval().edges().size() >= 6) {
				Q connectionTSets = Common.empty();
				Q tSets = memberEdges.predecessors(tripleNodeQ);
				for(Node connection: connections.eval().nodes()) {
					Q connectionNodeQ = Common.toQ(connection);
					connectionTSets = connectionTSets.union(memberEdges.predecessors(connectionNodeQ));
				}
				
				for(Node connectionTSet: connectionTSets.eval().nodes()) {
					Q connectionTSetQ = Common.toQ(connectionTSet);
					Q connectionTSetMembers = memberEdges.successors(connectionTSetQ);
					int flag = 1;
					for(Node tSet: tSets.eval().nodes()) {
						Q tSetQ = Common.toQ(tSet);
						Q tSetMembers = memberEdges.successors(tSetQ);
						if(CommonQueries.isEmpty(tSetMembers.intersection(connectionTSetMembers))) {
							flag = 0;
							break;
						}
					}
					if(flag == 1) {
						if(!CommonQueries.isEmpty(connectionTSetQ.difference(tSets))) {
							tR1Q = tR1Q.union(connectionTSetQ);
						}
					}
				}
				if(!CommonQueries.isEmpty(tR1Q)) {
					filteredTriples = filteredTriples.union(tripleNodeQ);
				}
			}
		}
		return filteredTriples;
	}
	
//	public static Q matchTriples2(Q tripleNodes) {
//		
//	}
	
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
