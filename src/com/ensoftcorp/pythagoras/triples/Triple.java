package com.ensoftcorp.pythagoras.triples;

import java.util.Objects;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

//a^2 + b^2 = c^2, a < b

public class Triple {

	private Node tripleNode;
	
	private Node aNode, bNode, cNode;
	
	private int a,b,c;
	
	public Triple(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.createTripleNode();
	}
	
	private void createTripleNode() {
		this.aNode = Graph.U.createNode();
		aNode.tag(TripleXCSG.Number);
		aNode.putAttr(XCSG.name, this.a + "");
		this.bNode = Graph.U.createNode();
		bNode.tag(TripleXCSG.Number);
		bNode.putAttr(XCSG.name, this.b + "");
		this.cNode = Graph.U.createNode();
		this.cNode.tag(TripleXCSG.Number);
		this.cNode.putAttr(XCSG.name, this.c + "");
		Edge e1 = Graph.U.createEdge(aNode, cNode);
		e1.tag(TripleXCSG.Triple_Edge);
		Edge e2 = Graph.U.createEdge(bNode, cNode);
		e2.tag(TripleXCSG.Triple_Edge);
		this.tripleNode = Graph.U.createNode();
		this.tripleNode.tag(TripleXCSG.Number);
		String tripleName = this.toString();
		this.tripleNode.putAttr(XCSG.name, tripleName);
		Edge e3 = Graph.U.createEdge(this.tripleNode, this.aNode);
		e3.tag(XCSG.Contains);
		Edge e4 = Graph.U.createEdge(this.tripleNode, this.bNode);
		e4.tag(XCSG.Contains);
		Edge e5 = Graph.U.createEdge(this.tripleNode, this.cNode);
		e5.tag(XCSG.Contains);
	}
	
	public Node getTripleNode() {
		return this.tripleNode;
	}
	
	public int a() {
		return this.a;
	}
	
	public int b() {
		return this.b;
	}
	
	public int c() {
		return this.c;
	}
	
	public Node aNode() {
		return this.aNode;
	}
	
	public Node bNode() {
		return this.bNode;
	}
	
	public Node cNode() {
		return this.cNode;
	}
	
	@Override
	public String toString() {
		return "(" + this.a + "," + this.b + "," + this.c + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triple other = (Triple) obj;
		return a == other.a && b == other.b && c == other.c;
	}

}
