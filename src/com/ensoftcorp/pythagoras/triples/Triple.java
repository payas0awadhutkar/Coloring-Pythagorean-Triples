package com.ensoftcorp.pythagoras.triples;
import java.util.Arrays;
import java.util.Objects;

public class Triple {

	private int a;
	private int b;
	private int c;
	
	public Triple(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
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
	
	@Override
	public String toString() {
		return "[a=" + a + ", b=" + b + ", c=" + c + "]";
	}
	
	@Override
	public int hashCode() {
		int [] arr = {a, b, c};
		Arrays.sort(arr);
		return Objects.hash(arr[0], arr[1], arr[2]);
	}
	
	public boolean equivalent(Triple other) {
		boolean aSide = this.a == other.a || this.a == other.b || this.a == other.c;
		boolean bSide = this.b == other.a || this.b == other.b || this.b == other.c;
		boolean cSide = this.c == other.a || this.c == other.b || this.c == other.c;
		int count = 0;
		if(aSide) {
			count++;
		}
		if(bSide) {
			count++;
		}
		if(cSide) {
			count++;
		}
		if(count == 1) {
			return true;
		}
		return false;
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
		return ((a == other.a && b == other.b) || (a == other.b && b == other.a))&& c == other.c;
	}
}
