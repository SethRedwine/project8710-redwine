/*
 * @(#) ASTAnalyzer.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package analysis;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jface.dialogs.MessageDialog;

import model.progelement.MethodElement;
import util.UtilAST;
import util.UtilPath;

public class RenameMethodAnalyzer {
	final String JAVANATURE = "org.eclipse.jdt.core.javanature";
	private MethodElement curMethodElem;
	private String newMethodName;
	private IMethod iMethod;

	public RenameMethodAnalyzer(MethodElement methodElem, String newMethodName) {
		this.curMethodElem = methodElem;
		this.newMethodName = newMethodName;
	}

	public void analyze() throws CoreException {
		// =============================================================
		// 1st step: Project
		// =============================================================
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			if (!project.isOpen() || !project.isNatureEnabled(JAVANATURE)) { // Check if we have a Java project.
				continue;
			}
			analyzePackages(JavaCore.create(project).getPackageFragments());
		}
	}

	protected void analyzePackages(IPackageFragment[] packages) throws CoreException, JavaModelException {
		// =============================================================
		// 2nd step: Packages
		// =============================================================
		for (IPackageFragment iPackage : packages) {
			if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE && //
					iPackage.getCompilationUnits().length >= 1 && //
					iPackage.getElementName().equals(curMethodElem.getPkgName())) {
				analyzeCompilationUnit(iPackage.getCompilationUnits());
			}
		}
	}

	private void analyzeCompilationUnit(ICompilationUnit[] iCompilationUnits) throws JavaModelException {
		// =============================================================
		// 3rd step: ICompilationUnits
		// =============================================================
		for (ICompilationUnit iCUnit : iCompilationUnits) {
			String nameICUnit = UtilPath.getClassNameFromJavaFile(iCUnit.getElementName());
			if (nameICUnit.equals(this.curMethodElem.getClassName()) == false) {
				continue;
			}
			CompilationUnit cUnit = UtilAST.parse(iCUnit);

			ASTVisitor iMethodFinder = new ASTVisitor() {
				@Override
				public boolean visit(MethodDeclaration node) {
					if (node.getName().getFullyQualifiedName().equals(curMethodElem.getMethodName())) {
						IJavaElement javaElement = node.resolveBinding().getJavaElement();
						if (javaElement instanceof IMethod) {
							iMethod = (IMethod) javaElement;
						}
					}
					return true;
				}
			};
			cUnit.accept(iMethodFinder);
			if (Flags.isPublic(iMethod.getFlags())) {
				MessageDialog.openInformation(null, "Warning",
						String.format("WRN: Cannot rename %s because it's a public method.", iMethod.getElementName()));
				return;
			}
			UtilAST.rename(iMethod, this.newMethodName, IJavaRefactorings.RENAME_METHOD);
		}
	}
}
