package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.compiler.IProblem;

public class JavaFileParseException extends Exception {
	private final LinkedHashMap<String, List<IProblem>> errors;

	public JavaFileParseException(final LinkedHashMap<String, List<IProblem>> errors) {
		super(createMessage(errors));
		this.errors = errors;
	}

	public LinkedHashMap<String, List<IProblem>> getErrors() {
		return this.errors;
	}

	private static String createMessage(final LinkedHashMap<String, List<IProblem>> errors) {
		final StringBuilder sb = new StringBuilder();
		errors.forEach((javaVersion, errorsInSpecificVersion) -> {
			for (final IProblem error : errorsInSpecificVersion) {
				sb.append(String.format("[Error in %s(%08X)] l%d: %s%n", javaVersion, error.getID(), error.getSourceLineNumber(), error.getMessage()));
			}
		});
		return sb.toString();
	}
}
