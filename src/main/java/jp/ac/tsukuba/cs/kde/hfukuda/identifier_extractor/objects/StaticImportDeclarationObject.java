package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;

public class StaticImportDeclarationObject {
	public String qualifier;
	public String typeName;
	public String lastName;

	public static StaticImportDeclarationObject fromImportDeclaration(final ImportDeclaration importDeclaration) {
		if (!importDeclaration.isStatic()) throw new IllegalArgumentException("importDeclarationがstaticではありません。");

		final StaticImportDeclarationObject result = new StaticImportDeclarationObject();

		if (importDeclaration.isOnDemand()) {
			if (importDeclaration.getName().isSimpleName()) throw new IllegalArgumentException("importされている名前が完全修飾名ではありません。");
			final QualifiedName qualifiedTypeName = (QualifiedName)importDeclaration.getName();
			result.qualifier = qualifiedTypeName.getQualifier().getFullyQualifiedName();
			result.typeName = qualifiedTypeName.getName().getIdentifier();
			result.lastName = "*";
		} else {
			if (importDeclaration.getName().isSimpleName()) throw new IllegalArgumentException("importされている名前が完全修飾名ではありません。");
			final QualifiedName qualifiedIdentifierName = (QualifiedName)importDeclaration.getName();
			if (qualifiedIdentifierName.getQualifier().isSimpleName()) throw new IllegalArgumentException("importされている名前が完全修飾名ではありません。");
			final QualifiedName qualifiedTypeName = (QualifiedName)qualifiedIdentifierName.getQualifier();
			result.qualifier = qualifiedTypeName.getQualifier().getFullyQualifiedName();
			result.typeName = qualifiedTypeName.getName().getIdentifier();
			result.lastName = qualifiedIdentifierName.getName().getIdentifier();
		}

		return result;
	}
}
