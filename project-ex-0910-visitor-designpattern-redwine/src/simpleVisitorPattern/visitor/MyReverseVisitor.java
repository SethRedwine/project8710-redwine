package simpleVisitorPattern.visitor;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Brake;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;
import util.UtilFile;

public class MyReverseVisitor extends CartPartVisitor {
	@Override
	public void visit(Wheel part) {
		part.setName(UtilFile.reverseWords(part.getName()));
		part.setModelNumberWheel(UtilFile.reverseWords(part.getModelNumberWheel()));
		part.setModelYearWheel(UtilFile.reverseWords(part.getModelYearWheel()));
	}

	@Override
	public void visit(Engine part) {
		part.setName(UtilFile.reverseWords(part.getName()));
		part.setModelNumberEngine(UtilFile.reverseWords(part.getModelNumberEngine()));
		part.setModelYearEngine(UtilFile.reverseWords(part.getModelYearEngine()));
	}

	@Override
	public void visit(Body part) {
		part.setName(UtilFile.reverseWords(part.getName()));
		part.setModelNumberBody(UtilFile.reverseWords(part.getModelNumberBody()));
		part.setModelYearBody(UtilFile.reverseWords(part.getModelYearBody()));
	}

	@Override
	public void visit(Brake part) {
		part.setName(UtilFile.reverseWords(part.getName()));
		part.setModelNumberBrake(UtilFile.reverseWords(part.getModelNumberBrake()));
		part.setModelYearBrake(UtilFile.reverseWords(part.getModelYearBrake()));
	}
}