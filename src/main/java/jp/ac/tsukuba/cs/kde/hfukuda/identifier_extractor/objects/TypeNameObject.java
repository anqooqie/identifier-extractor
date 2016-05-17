package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

public class TypeNameObject {
	public boolean isWildcard;
	public @Nullable TypeNameObject upperBound;
	public @Nullable TypeNameObject lowerBound;

	public int dimension;
	public List<TypeNameObject> typeParameters = new ArrayList<>();
	public @Nullable TypeNameObject qualifier;
	public String name;

	public static TypeNameObject fromType(final Type type) {
		final TypeNameObject result = new TypeNameObject();

		if (type.isWildcardType()) {
			result.isWildcard = true;
			final WildcardType wildcardType = (WildcardType)type;
			final Type bound = wildcardType.getBound();
			if (bound == null) {
				result.upperBound = null;
				result.lowerBound = null;
			} else if (wildcardType.isUpperBound()) {
				result.upperBound = TypeNameObject.fromType(bound);
				result.lowerBound = null;
			} else {
				result.upperBound = null;
				result.lowerBound = TypeNameObject.fromType(bound);
			}
			result.dimension = 0;
			result.qualifier = null;
			result.name = "?";
		} else {
			result.isWildcard = false;
			result.upperBound = null;
			result.lowerBound = null;

			Type currentType = type;

			if (currentType.isArrayType()) {
				final ArrayType arrayType = (ArrayType)currentType;
				result.dimension = arrayType.getDimensions();
				currentType = arrayType.getElementType();
			} else {
				result.dimension = 0;
			}

			if (currentType.isParameterizedType()) {
				final ParameterizedType parameterizedType = (ParameterizedType)currentType;
				@SuppressWarnings("unchecked")
				final List<Type> typeArguments = parameterizedType.typeArguments();
				for (final Type typeArgument : typeArguments) {
					result.typeParameters.add(TypeNameObject.fromType(typeArgument));
				}
				currentType = parameterizedType.getType();
			}

			if (currentType.isPrimitiveType()) {
				result.qualifier = null;
				result.name = ((PrimitiveType)currentType).getPrimitiveTypeCode().toString();
			} else if (currentType.isSimpleType()) {
				final TypeNameObject typeNameObject = TypeNameObject.fromName(((SimpleType)currentType).getName());
				result.qualifier = typeNameObject.qualifier;
				result.name = typeNameObject.name;
			} else if (currentType.isQualifiedType()) {
				final QualifiedType qualifiedType = (QualifiedType)currentType;
				result.qualifier = TypeNameObject.fromType(qualifiedType.getQualifier());
				result.name = qualifiedType.getName().getIdentifier();
			} else {
				throw new IllegalArgumentException("出現しないはずの型が出現しています: " + currentType.getClass().getCanonicalName());
			}
		}

		return result;
	}
	public static final TypeNameObject fromName(final Name name) {
		final TypeNameObject result = new TypeNameObject();

		result.isWildcard = false;
		result.upperBound = null;
		result.lowerBound = null;
		result.dimension = 0;
		if (name.isQualifiedName()) {
			final QualifiedName qualifiedName = (QualifiedName)name;
			result.qualifier = TypeNameObject.fromName(qualifiedName.getQualifier());
			result.name = qualifiedName.getName().getIdentifier();
		} else {
			final SimpleName simpleName = (SimpleName)name;
			result.qualifier = null;
			result.name = simpleName.getIdentifier();
		}

		return result;
	}
}
