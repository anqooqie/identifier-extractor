package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

public class TypeParameterObject {
	public String name;
	public List<TypeNameObject> upperBounds = new ArrayList<>();

	public static TypeParameterObject fromTypeParameter(final TypeParameter typeParameter) {
		final TypeParameterObject result = new TypeParameterObject();

		result.name = typeParameter.getName().getIdentifier();
		@SuppressWarnings("unchecked")
		final List<Type> typeBounds = typeParameter.typeBounds();
		for (final Type typeBound : typeBounds) {
			result.upperBounds.add(TypeNameObject.fromType(typeBound));
		}

		return result;
	}
}
