package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTParserUtils {
	public static final String[] TARGET_VERSIONS = {JavaCore.VERSION_1_7, JavaCore.VERSION_1_6, JavaCore.VERSION_1_5, JavaCore.VERSION_1_4};

	private ASTParserUtils() {}

	// 型解決できなかった系のエラー
	// ここではjavaファイルのパースさえできればいいので、警告と同じ扱いをする
	private static final int[] quasiWarns = {
		IProblem.UndefinedType, // 0x01000002
		IProblem.NotVisibleType, // 0x01000003
		IProblem.IncompatibleTypesInEqualityOperator, // 0x0100000F
		IProblem.IncompatibleTypesInConditionalOperator, // 0x01000010
		IProblem.TypeMismatch, // 0x01000011
		IProblem.IllegalCast, // 0x0100009C
		IProblem.InvalidClassInstantiation, // 0x0100009D
		IProblem.SuperInterfaceMustBeAnInterface, // 0x0100013B
		IProblem.CannotThrowType, // 0x01000140
		IProblem.HierarchyHasProblems, // 0x01000147
		IProblem.ImportNotFound, // 0x10000186
		IProblem.NonGenericType, // 0x0100020C
		IProblem.TypeArgumentMismatch, // 0x0100020E
		IProblem.GenericMethodTypeArgumentMismatch, // 0x0100021F
		IProblem.GenericConstructorTypeArgumentMismatch, // 0x01000220
		IProblem.ParameterizedMethodArgumentTypeMismatch, // 0x01000226
		IProblem.IncompatibleTypesInForeach, // 0x01000244
		IProblem.UndefinedField, // 0x02000046
		IProblem.NotVisibleField, // 0x02000047
		IProblem.UnresolvedVariable, // 0x02000053
		IProblem.UndefinedName, // 0x22000032, 型であることが確定しきれない場合
		IProblem.UndefinedMethod, // 0x04000064, static importしているとメソッドだけ解決できない、という状況が起こり得る
		IProblem.NotVisibleMethod, // 0x04000065
		IProblem.ParameterMismatch, // 0x04000073
		IProblem.MissingTypeInMethod, // 0x04000078
		IProblem.AbstractMethodMustBeImplemented, // 0x04000190, 実装してるはずなのに何故かこのエラーが出る
		IProblem.IncompatibleReturnType, // 0x04000194
		IProblem.UndefinedAnnotationMember, // 0x04000263
		IProblem.MethodMustOverrideOrImplement, // 0x0400027A
		IProblem.MissingTypeInConstructor, // 0x08000081
		IProblem.UndefinedConstructor, // 0x08000082
		IProblem.NotVisibleConstructor, // 0x08000083
		IProblem.InvalidTypeForCollection, // 0x20000245
		IProblem.StaticMethodRequested, // 0x240000C9
	};
	public static ASTCreationInfo createAST(final Path javaFilePath) throws IOException, JavaFileParseException {
		return createAST(
				Optional.of(javaFilePath),
				new String(Files.readAllBytes(javaFilePath), StandardCharsets.ISO_8859_1)
		);
	}
	public static ASTCreationInfo createAST(final String sourceCode) throws JavaFileParseException {
		return createAST(Optional.empty(), sourceCode);
	}
	private static ASTCreationInfo createAST(final Optional<Path> javaFilePath, final String sourceCode) throws JavaFileParseException {
		final LinkedHashMap<String, List<IProblem>> errors = new LinkedHashMap<>();
		for (final String javaVersion : TARGET_VERSIONS) {
			final List<IProblem> errorsInSpecificVersion = new ArrayList<>();
			final ASTParser parser = getConfiguredASTParser(javaVersion, javaFilePath.map(Path::getFileName).map(Path::toString), sourceCode);

			Optional<CompilationUnit> unit;
			try {
				unit = Optional.of((CompilationUnit)parser.createAST(new NullProgressMonitor()));
				for (final IProblem problem : unit.get().getProblems()) {
					if (problem.isError() && !ArrayUtils.contains(quasiWarns, problem.getID())) {
						errorsInSpecificVersion.add(problem);
					}
				}
			} catch (final NullPointerException | NoClassDefFoundError | ArrayIndexOutOfBoundsException e) { // createAST()がこれらの例外を吐くことがある（原因不明）
				unit = Optional.empty();
				errorsInSpecificVersion.add(new IProblem() {
					private int sourceEnd = 0;
					private int sourceLineNumber = 0;
					private int sourceStart = 0;
					private boolean isError = true;
					@Override public String[] getArguments() { return new String[0]; }
					@Override public int getID() { return IProblem.Unclassified; }
					@Override public String getMessage() { return e.getClass().getName() + ": " + e.getMessage(); }
					@Override public char[] getOriginatingFileName() { return javaFilePath.map(Path::getFileName).map(Path::toString).map(String::toCharArray).orElse(new char[0]); }
					@Override public int getSourceEnd() { return this.sourceEnd; }
					@Override public int getSourceLineNumber() { return this.sourceLineNumber; }
					@Override public int getSourceStart() { return this.sourceStart; }
					@Override public boolean isError() { return this.isError; }
					@Override public boolean isWarning() { return !this.isError; }
					@Override public void setSourceEnd(final int arg0) { this.sourceEnd = arg0; }
					@Override public void setSourceLineNumber(final int arg0) { this.sourceLineNumber = arg0; }
					@Override public void setSourceStart(final int arg0) { this.sourceStart = arg0; }
				});
			}

			if (errorsInSpecificVersion.isEmpty()) {
				return new ASTCreationInfo(javaFilePath, unit.get(), errors);
			}

			errors.put(javaVersion, errorsInSpecificVersion);
		}

		throw new JavaFileParseException(errors);
	}

	private static ASTParser getConfiguredASTParser(final String javaVersion, final Optional<String> javaFileName, final String sourceCode) {
		final ASTParser parser = ASTParser.newParser(AST.JLS4);
		{
			@SuppressWarnings("unchecked")
			final Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(javaVersion, options);
			parser.setCompilerOptions(options);
		}
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setResolveBindings(true);
		parser.setEnvironment(new String[0], new String[0], null, true);
		parser.setUnitName(javaFileName.orElse(null));
		parser.setSource(sourceCode.toCharArray());

		return parser;
	}
}
