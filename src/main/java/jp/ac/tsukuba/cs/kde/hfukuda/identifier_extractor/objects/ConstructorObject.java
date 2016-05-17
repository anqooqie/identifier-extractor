package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

public class ConstructorObject {
	public List<QualifiedNameObject> annotations = new ArrayList<>();
	public List<TypeParameterObject> typeParameters = new ArrayList<>();
	public List<ParameterObject> parameters = new ArrayList<>();
	public List<QualifiedNameObject> thrownExceptions = new ArrayList<>();
	public AccessLevel accessLevel;
	public boolean isStrictfp;

	public static ConstructorObject fromMethodDeclaration(final MethodDeclaration methodDeclaration) {
		if (!methodDeclaration.isConstructor()) throw new IllegalArgumentException("methodDeclarationはコンストラクタではありません。");
		final TypeDeclaration typeDeclaration = (TypeDeclaration)methodDeclaration.getParent();
		@SuppressWarnings("unchecked")
		final boolean isStrictfp = ((List<IExtendedModifier>)typeDeclaration.modifiers()).stream().filter(IExtendedModifier::isModifier).map(modifier -> (Modifier)modifier).anyMatch(Modifier::isStrictfp);

		final ConstructorObject result = new ConstructorObject();

		result.accessLevel = AccessLevel.PACKAGE_PRIVATE;
		result.isStrictfp = isStrictfp;
		@SuppressWarnings("unchecked")
		final List<IExtendedModifier> modifiersAndAnnotations = methodDeclaration.modifiers();
		for (final IExtendedModifier modifierOrAnnotation : modifiersAndAnnotations) {
			if (modifierOrAnnotation.isModifier()) {
				final Modifier modifier = (Modifier)modifierOrAnnotation;
				if (modifier.isPublic()) result.accessLevel = AccessLevel.PUBLIC;
				else if (modifier.isProtected()) result.accessLevel = AccessLevel.PROTECTED;
				else if (modifier.isPrivate()) result.accessLevel = AccessLevel.PRIVATE;
				else throw new IllegalArgumentException("コンストラクタに付かない修飾子が付いています:　" + modifier.getKeyword().toString());
			} else {
				result.annotations.add(QualifiedNameObject.fromName(((Annotation)modifierOrAnnotation).getTypeName()));
			}
		}

		@SuppressWarnings("unchecked")
		final List<TypeParameter> typeParameters = methodDeclaration.typeParameters();
		for (final TypeParameter typeParameter : typeParameters) {
			result.typeParameters.add(TypeParameterObject.fromTypeParameter(typeParameter));
		}

		@SuppressWarnings("unchecked")
		final List<Name> thrownExceptions = methodDeclaration.thrownExceptions();
		for (final Name thrownException : thrownExceptions) {
			result.thrownExceptions.add(QualifiedNameObject.fromName(thrownException));
		}

		@SuppressWarnings("unchecked")
		final List<SingleVariableDeclaration> parameterDeclarations = methodDeclaration.parameters();
		for (final SingleVariableDeclaration parameterDeclaration : parameterDeclarations) {
			result.parameters.add(ParameterObject.fromParameterDeclaration(parameterDeclaration));
		}

		return result;
	}
}
