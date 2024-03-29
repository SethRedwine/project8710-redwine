package simpleVisitorPattern.visitor;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Brake;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;

public class MyAccessVisitor extends CartPartVisitor {
	@Override
	public void visit(Wheel part) {
		System.out.println("[DBG] Accessing the name property: " + part.getName());
	}

	@Override
	public void visit(Engine part) {
		System.out.println("[DBG] Accessing the name property: " + part.getName());
	}

	@Override
	public void visit(Body part) {
		System.out.println("[DBG] Accessing the name property: " + part.getName());
	}

	public void visit(Brake part) {
		System.out.println("[DBG] Accessing the name property: " + part.getName());
	}
}