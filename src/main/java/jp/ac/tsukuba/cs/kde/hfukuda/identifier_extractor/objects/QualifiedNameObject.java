package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import javax.annotation.Nullable;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class QualifiedNameObject {
	public @Nullable String qualifier;
	public String typeName;

	public static QualifiedNameObject fromName(final Name name) {
		final QualifiedNameObject result = new QualifiedNameObject();
		if (name.isQualifiedName()) {
			final QualifiedName qualifiedName = (QualifiedName)name;
			result.qualifier = qualifiedName.getQualifier().getFullyQualifiedName();
			result.typeName = qualifiedName.getName().getIdentifier();
		} else {
			final SimpleName simpleName = (SimpleName)name;
			result.qualifier = null;
			result.typeName = simpleName.getIdentifier();
		}
		return result;
	}
}
