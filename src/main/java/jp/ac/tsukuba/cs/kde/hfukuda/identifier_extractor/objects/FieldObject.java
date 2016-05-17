package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldObject {
	public List<QualifiedNameObject> annotations = new ArrayList<>();
	public AccessLevel accessLevel;
	public boolean isStatic;
	public boolean isFinal;
	public boolean isTransient;
	public boolean isVolatile;
	public TypeNameObject type;
	public String name;

	public static FieldObject fromFieldFragment(final VariableDeclarationFragment fieldFragment) {
		if (!(fieldFragment.getParent() instanceof FieldDeclaration)) throw new IllegalArgumentException("fieldFragmentはフィールド宣言の一部ではありません。");
		final FieldDeclaration fieldDeclaration = (FieldDeclaration)fieldFragment.getParent();
		final boolean isInterface = ((TypeDeclaration)fieldDeclaration.getParent()).isInterface();

		final FieldObject result = new FieldObject();

		result.type = TypeNameObject.fromType(fieldDeclaration.getType());
		result.type.dimension += fieldFragment.getExtraDimensions();
		result.name = fieldFragment.getName().getIdentifier();

		if (isInterface) {
			result.accessLevel = AccessLevel.PUBLIC;
			result.isStatic = true;
			result.isFinal = true;
			result.isTransient = false;
			result.isVolatile = false;
		} else {
			result.accessLevel = AccessLevel.PACKAGE_PRIVATE;
			result.isStatic = false;
			result.isFinal = false;
			result.isTransient = false;
			result.isVolatile = false;
		}
		@SuppressWarnings("unchecked")
		final List<IExtendedModifier> modifiersAndAnnotations = fieldDeclaration.modifiers();
		for (final IExtendedModifier modifierOrAnnotation : modifiersAndAnnotations) {
			if (modifierOrAnnotation.isModifier()) {
				final Modifier modifier = (Modifier)modifierOrAnnotation;
				if (modifier.isPublic()) result.accessLevel = AccessLevel.PUBLIC;
				else if (modifier.isProtected()) result.accessLevel = AccessLevel.PROTECTED;
				else if (modifier.isPrivate()) result.accessLevel = AccessLevel.PRIVATE;
				else if (modifier.isStatic()) result.isStatic = true;
				else if (modifier.isFinal()) result.isFinal = true;
				else if (modifier.isTransient()) result.isTransient = true;
				else if (modifier.isVolatile()) result.isVolatile = true;
			} else {
				result.annotations.add(QualifiedNameObject.fromName(((Annotation)modifierOrAnnotation).getTypeName()));
			}
		}

		return result;
	}
}
