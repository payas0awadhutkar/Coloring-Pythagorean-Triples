package com.ensoftcorp.pythagoras.triples.util;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

public class TripleUtils {
	
	public static Set<Integer> getNumbers(Q tripleQ) {
		Set<Integer> tripleNums = new HashSet<Integer>();
		for(Node tripleNode: tripleQ.eval().nodes()) {
			tripleNums.addAll(getNumbers(tripleNode));
		}
		return tripleNums;
	}
	
	public static Set<Integer> getNumbers(Node tripleNode) {
		Set<Integer> numbers = new HashSet<Integer>();
		Q numberNodes = Common.toQ(tripleNode).children();
		for(Node numberNode: numberNodes.eval().nodes()) {
			numbers.add(Integer.valueOf(numberNode.getAttr(XCSG.name).toString()));
		}
		return numbers;
	}
	
	public static boolean isPerfectSquare(int x) {
		if (x >= 0) {
			int rootX = (int) Math.sqrt(x);
			return ((rootX * rootX) == x);
		}
		return false;
	}

	//	public static int gcd(int a, int b) {
	//		if (b == 0) {
	//			return a;
	//		}
	//		return gcd(b, a % b);
	//	}

}
