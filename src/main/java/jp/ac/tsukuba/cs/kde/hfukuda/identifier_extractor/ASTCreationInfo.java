package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTCreationInfo {
	private final Optional<Path> javaFilePath;
	private final CompilationUnit compilationUnit;
	private final LinkedHashMap<String, List<IProblem>> errorsInIncorrectVersions;

	public ASTCreationInfo(final Optional<Path> javaFilePath, final CompilationUnit compilationUnit, final LinkedHashMap<String, List<IProblem>> errorsInIncorrectVersions) {
		this.javaFilePath = javaFilePath;
		this.compilationUnit = compilationUnit;
		this.errorsInIncorrectVersions = errorsInIncorrectVersions;
	}

	public Optional<Path> getJavaFilePath() {
		return this.javaFilePath;
	}
	public CompilationUnit getCompilationUnit() {
		return this.compilationUnit;
	}
	public LinkedHashMap<String, List<IProblem>> getErrorsInIncorrectVersions() {
		return this.errorsInIncorrectVersions;
	}
	public String getEstimatedVersion() {
		return ASTParserUtils.TARGET_VERSIONS[this.errorsInIncorrectVersions.size()];
	}
}
