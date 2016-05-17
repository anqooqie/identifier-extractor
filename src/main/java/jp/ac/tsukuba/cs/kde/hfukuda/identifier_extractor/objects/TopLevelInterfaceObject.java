package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class TopLevelInterfaceObject {
	public List<QualifiedNameObject> annotations = new ArrayList<>();
	public List<TypeNameObject> superInterfaces = new ArrayList<>();
	public List<TypeParameterObject> typeParameters = new ArrayList<>();
	public List<FieldObject> fields = new ArrayList<>();
	public List<MethodObject> methods = new ArrayList<>();
	public AccessLevel accessLevel;
	public boolean isAbstract;
	public boolean isStrictfp;
	public String name;

	public static TopLevelInterfaceObject fromTypeDeclaration(final TypeDeclaration typeDeclaration) {
		if (!typeDeclaration.isPackageMemberTypeDeclaration()) throw new IllegalArgumentException("typeDeclarationがトップレベル型ではありません。");
		if (!typeDeclaration.isInterface()) throw new IllegalArgumentException("typeDeclarationがクラスです。");

		final TopLevelInterfaceObject result = new TopLevelInterfaceObject();

		result.name = typeDeclaration.getName().getIdentifier();

		result.accessLevel = AccessLevel.PACKAGE_PRIVATE;
		result.isAbstract = true;
		result.isStrictfp = false;
		@SuppressWarnings("unchecked")
		final List<IExtendedModifier> modifiersAndAnnotations = typeDeclaration.modifiers();
		for (final IExtendedModifier modifierOrAnnotation : modifiersAndAnnotations) {
			if (modifierOrAnnotation.isModifier()) {
				final Modifier modifier = (Modifier)modifierOrAnnotation;
				if (modifier.isPublic()) result.accessLevel = AccessLevel.PUBLIC;
				else if (modifier.isAbstract()) result.isAbstract = true;
				else if (modifier.isStrictfp()) result.isStrictfp = true;
				else throw new IllegalArgumentException("トップレベルインタフェースに付かない修飾子が付いています:　" + modifier.getKeyword().toString());
			} else {
				result.annotations.add(QualifiedNameObject.fromName(((Annotation)modifierOrAnnotation).getTypeName()));
			}
		}

		@SuppressWarnings("unchecked")
		final List<Type> superInterfaceTypes = typeDeclaration.superInterfaceTypes();
		for (final Type superInterfaceType : superInterfaceTypes) {
			result.superInterfaces.add(TypeNameObject.fromType(superInterfaceType));
		}

		@SuppressWarnings("unchecked")
		final List<TypeParameter> typeParameters = typeDeclaration.typeParameters();
		for (final TypeParameter typeParameter : typeParameters) {
			result.typeParameters.add(TypeParameterObject.fromTypeParameter(typeParameter));
		}

		for (final FieldDeclaration fieldDeclaration : typeDeclaration.getFields()) {
			@SuppressWarnings("unchecked")
			final List<VariableDeclarationFragment> fieldFragments = fieldDeclaration.fragments();
			for (final VariableDeclarationFragment fieldFragment : fieldFragments) {
				result.fields.add(FieldObject.fromFieldFragment(fieldFragment));
			}
		}

		for (final MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
			if (methodDeclaration.isConstructor()) {
				throw new IllegalArgumentException("インタフェースはコンストラクタを持てません。");
			} else {
				result.methods.add(MethodObject.fromMethodDeclaration(methodDeclaration));
			}
		}

		return result;
	}
}
