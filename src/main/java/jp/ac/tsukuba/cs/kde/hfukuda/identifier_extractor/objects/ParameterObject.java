package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class ParameterObject {
	public List<QualifiedNameObject> annotations = new ArrayList<>();
	public boolean isFinal;
	public boolean isVarargs;
	public TypeNameObject type;
	public String name;

	public static ParameterObject fromParameterDeclaration(final SingleVariableDeclaration parameterDeclaration) {
		if (!(parameterDeclaration.getParent() instanceof MethodDeclaration)) throw new IllegalArgumentException("parameterDeclarationは引数宣言ではありません。");

		final ParameterObject result = new ParameterObject();

		result.type = TypeNameObject.fromType(parameterDeclaration.getType());
		result.type.dimension += parameterDeclaration.getExtraDimensions();
		result.isVarargs = parameterDeclaration.isVarargs();
		result.name = parameterDeclaration.getName().getIdentifier();

		result.isFinal = false;
		@SuppressWarnings("unchecked")
		final List<IExtendedModifier> modifiersAndAnnotations = parameterDeclaration.modifiers();
		for (final IExtendedModifier modifierOrAnnotation : modifiersAndAnnotations) {
			if (modifierOrAnnotation.isModifier()) {
				final Modifier modifier = (Modifier)modifierOrAnnotation;
				if (modifier.isFinal()) result.isFinal = true;
				else throw new IllegalArgumentException("引数に付かない修飾子が付いています:　" + modifier.getKeyword().toString());
			} else {
				result.annotations.add(QualifiedNameObject.fromName(((Annotation)modifierOrAnnotation).getTypeName()));
			}
		}

		return result;
	}
}
