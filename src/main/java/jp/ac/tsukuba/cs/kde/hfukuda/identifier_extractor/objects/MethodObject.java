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

public class MethodObject {
	public List<QualifiedNameObject> annotations = new ArrayList<>();
	public List<TypeParameterObject> typeParameters = new ArrayList<>();
	public List<ParameterObject> parameters = new ArrayList<>();
	public List<QualifiedNameObject> thrownExceptions = new ArrayList<>();
	public AccessLevel accessLevel;
	public boolean isAbstract;
	public boolean isStatic;
	public boolean isFinal;
	public boolean isSynchronized;
	public boolean isNative;
	public boolean isStrictfp;
	public TypeNameObject returnValueType;
	public String name;

	public static MethodObject fromMethodDeclaration(final MethodDeclaration methodDeclaration) {
		if (methodDeclaration.isConstructor()) throw new IllegalArgumentException("methodDeclarationはコンストラクタです。");
		final TypeDeclaration typeDeclaration = (TypeDeclaration)methodDeclaration.getParent();
		final boolean isInterface = typeDeclaration.isInterface();
		@SuppressWarnings("unchecked")
		final boolean isFinal = ((List<IExtendedModifier>)typeDeclaration.modifiers()).stream().filter(IExtendedModifier::isModifier).map(modifier -> (Modifier)modifier).anyMatch(Modifier::isFinal);
		@SuppressWarnings("unchecked")
		final boolean isStrictfp = ((List<IExtendedModifier>)typeDeclaration.modifiers()).stream().filter(IExtendedModifier::isModifier).map(modifier -> (Modifier)modifier).anyMatch(Modifier::isStrictfp);

		final MethodObject result = new MethodObject();

		result.returnValueType = TypeNameObject.fromType(methodDeclaration.getReturnType2());
		result.returnValueType.dimension += methodDeclaration.getExtraDimensions();
		result.name = methodDeclaration.getName().getIdentifier();

		if (isInterface) {
			result.accessLevel = AccessLevel.PUBLIC;
			result.isAbstract = true;
			result.isStatic = false;
			result.isFinal = false;
			result.isSynchronized = false;
			result.isNative = false;
			result.isStrictfp = isStrictfp;
		} else {
			result.accessLevel = AccessLevel.PACKAGE_PRIVATE;
			result.isAbstract = false;
			result.isStatic = false;
			result.isFinal = isFinal;
			result.isSynchronized = false;
			result.isNative = false;
			result.isStrictfp = isStrictfp;
		}
		@SuppressWarnings("unchecked")
		final List<IExtendedModifier> modifiersAndAnnotations = methodDeclaration.modifiers();
		for (final IExtendedModifier modifierOrAnnotation : modifiersAndAnnotations) {
			if (modifierOrAnnotation.isModifier()) {
				final Modifier modifier = (Modifier)modifierOrAnnotation;
				if (modifier.isPublic()) result.accessLevel = AccessLevel.PUBLIC;
				else if (modifier.isProtected()) result.accessLevel = AccessLevel.PROTECTED;
				else if (modifier.isPrivate()) {
					result.accessLevel = AccessLevel.PRIVATE;
					result.isFinal = true;
				}
				else if (modifier.isAbstract()) result.isAbstract = true;
				else if (modifier.isStatic()) result.isStatic = true;
				else if (modifier.isFinal()) result.isFinal = true;
				else if (modifier.isSynchronized()) result.isSynchronized = true;
				else if (modifier.isNative()) result.isNative = true;
				else if (modifier.isStrictfp()) result.isStrictfp = true;
				else throw new IllegalArgumentException("メソッドに付かない修飾子が付いています:　" + modifier.getKeyword().toString());
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
