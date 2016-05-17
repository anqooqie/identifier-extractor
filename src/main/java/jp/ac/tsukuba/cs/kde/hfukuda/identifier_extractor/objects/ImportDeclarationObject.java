package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;

public class ImportDeclarationObject {
	public String qualifier;
	public String typeName;

	public static ImportDeclarationObject fromImportDeclaration(final ImportDeclaration importDeclaration) {
		if (importDeclaration.isStatic()) throw new IllegalArgumentException("importDeclarationがstaticです。");

		final ImportDeclarationObject result = new ImportDeclarationObject();

		if (importDeclaration.isOnDemand()) {
			result.qualifier = importDeclaration.getName().getFullyQualifiedName();
			result.typeName = "*";
		} else {
			if (importDeclaration.getName().isSimpleName()) throw new IllegalArgumentException("importされている名前が完全修飾名ではありません。");
			final QualifiedName qualifiedTypeName = (QualifiedName)importDeclaration.getName();
			result.qualifier = qualifiedTypeName.getQualifier().getFullyQualifiedName();
			result.typeName = qualifiedTypeName.getName().getIdentifier();
		}

		return result;
	}
}
