package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.ASTCreationInfo;
import jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.JavaFileParseException;

public class FileObject {
	public List<ImportDeclarationObject> importDeclarations = new ArrayList<>();
	public List<StaticImportDeclarationObject> staticImportDeclarations = new ArrayList<>();
	public List<TopLevelClassObject> topLevelClasses = new ArrayList<>();
	public List<TopLevelInterfaceObject> topLevelInterfaces = new ArrayList<>();
	public @Nullable String filePath;
	public @Nullable String packageName;

	public static FileObject fromCompilationUnit(final CompilationUnit compilationUnit) {
		final FileObject result = new FileObject();

		if (compilationUnit.getPackage() != null) {
			result.packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
		}

		@SuppressWarnings("unchecked")
		final List<ImportDeclaration> importDeclarations = compilationUnit.imports();
		for (final ImportDeclaration importDeclaration : importDeclarations) {
			if (importDeclaration.isStatic()) {
				result.staticImportDeclarations.add(StaticImportDeclarationObject.fromImportDeclaration(importDeclaration));
			} else {
				result.importDeclarations.add(ImportDeclarationObject.fromImportDeclaration(importDeclaration));
			}
		}

		@SuppressWarnings("unchecked")
		final List<AbstractTypeDeclaration> abstractTypeDeclarations = compilationUnit.types();
		for (final AbstractTypeDeclaration abstractTypeDeclaration : abstractTypeDeclarations) {
			if (abstractTypeDeclaration instanceof TypeDeclaration) {
				final TypeDeclaration typeDeclaration = (TypeDeclaration)abstractTypeDeclaration;
				if (typeDeclaration.isInterface()) {
					result.topLevelInterfaces.add(TopLevelInterfaceObject.fromTypeDeclaration(typeDeclaration));
				} else {
					result.topLevelClasses.add(TopLevelClassObject.fromTypeDeclaration(typeDeclaration));
				}
			}
		}

		return result;
	}

	public static FileObject fromASTCreationInfo(final ASTCreationInfo astCreationInfo) throws JavaFileParseException {
		try {
			final FileObject result = FileObject.fromCompilationUnit(astCreationInfo.getCompilationUnit());
			result.filePath = astCreationInfo.getJavaFilePath().map(Path::toString).orElse(null);
			return result;
		} catch (final IllegalArgumentException e) {
			final LinkedHashMap<String, List<IProblem>> errors = new LinkedHashMap<>(astCreationInfo.getErrorsInIncorrectVersions());
			errors.put(astCreationInfo.getEstimatedVersion(), Arrays.asList(new IProblem() {
				private int sourceEnd = 0;
				private int sourceLineNumber = 0;
				private int sourceStart = 0;
				private boolean isError = true;
				@Override public String[] getArguments() { return new String[0]; }
				@Override public int getID() { return IProblem.Unclassified; }
				@Override public String getMessage() { return e.getMessage(); }
				@Override public char[] getOriginatingFileName() { return astCreationInfo.getJavaFilePath().map(Path::getFileName).map(Path::toString).map(String::toCharArray).orElse(new char[0]); }
				@Override public int getSourceEnd() { return this.sourceEnd; }
				@Override public int getSourceLineNumber() { return this.sourceLineNumber; }
				@Override public int getSourceStart() { return this.sourceStart; }
				@Override public boolean isError() { return this.isError; }
				@Override public boolean isWarning() { return !this.isError; }
				@Override public void setSourceEnd(final int arg0) { this.sourceEnd = arg0; }
				@Override public void setSourceLineNumber(final int arg0) { this.sourceLineNumber = arg0; }
				@Override public void setSourceStart(final int arg0) { this.sourceStart = arg0; }
			}));
			final JavaFileParseException javaFileParseException = new JavaFileParseException(errors);
			javaFileParseException.initCause(e);
			throw javaFileParseException;
		}
	}
}
