package com.ensoftcorp.pythagoras.triples;

import java.util.Objects;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.pythagoras.xcsg.TripleXCSG;

//x^2 + y^2 = z^2, x < y

public class Triple {

	private Node tripleNode;
	
	private Node xNode, yNode, zNode;
	
	private int x,y,z;
	
	public Triple(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.createTripleNode();
	}
	
	private void createTripleNode() {
		this.xNode = Graph.U.createNode();
		xNode.tag(TripleXCSG.Number);
		xNode.putAttr(XCSG.name, this.x + "");
		this.yNode = Graph.U.createNode();
		yNode.tag(TripleXCSG.Number);
		yNode.putAttr(XCSG.name, this.y + "");
		this.zNode = Graph.U.createNode();
		this.zNode.tag(TripleXCSG.Number);
		this.zNode.putAttr(XCSG.name, this.z + "");
		Edge e1 = Graph.U.createEdge(xNode, zNode);
		e1.tag(TripleXCSG.Triple_Edge);
		Edge e2 = Graph.U.createEdge(yNode, zNode);
		e2.tag(TripleXCSG.Triple_Edge);
		this.tripleNode = Graph.U.createNode();
		this.tripleNode.tag(TripleXCSG.Triple);
		String tripleName = this.toString();
		this.tripleNode.putAttr(XCSG.name, tripleName);
		Edge e3 = Graph.U.createEdge(this.tripleNode, this.xNode);
		e3.tag(XCSG.Contains);
		Edge e4 = Graph.U.createEdge(this.tripleNode, this.yNode);
		e4.tag(XCSG.Contains);
		Edge e5 = Graph.U.createEdge(this.tripleNode, this.zNode);
		e5.tag(XCSG.Contains);
	}
	
	public Node getTripleNode() {
		return this.tripleNode;
	}
	
	public int a() {
		return this.x;
	}
	
	public int b() {
		return this.y;
	}
	
	public int c() {
		return this.z;
	}
	
	public Node xNode() {
		return this.xNode;
	}
	
	public Node yNode() {
		return this.yNode;
	}
	
	public Node zNode() {
		return this.zNode;
	}
	
	public void clear() {
		Graph.U.delete(this.xNode);
		Graph.U.delete(this.yNode);
		Graph.U.delete(this.zNode);
		Graph.U.delete(this.tripleNode);
	}
	
	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + "," + this.z + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
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
		return x == other.x && y == other.y && z == other.z;
	}

}
