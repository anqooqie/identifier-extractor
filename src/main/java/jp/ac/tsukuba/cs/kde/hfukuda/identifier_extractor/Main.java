package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects.FileObject;
import net.arnx.jsonic.JSON;
import throwing.stream.ThrowingStream;

public class Main {
	public static void main(final String[] args) throws IOException {
		final Path stdin = Files.createTempFile("stdin", ".tmp");
		Files.copy(System.in, stdin, StandardCopyOption.REPLACE_EXISTING);
		try (final Stream<String> lines = Files.lines(stdin)) {
			ThrowingStream.of(lines, IOException.class).parallel().forEach(line -> {
				try {
					final ASTCreationInfo astCreationInfo = ASTParserUtils.createAST(Paths.get(line));
					final FileObject fileObject = FileObject.fromASTCreationInfo(astCreationInfo);
					final String json = JSON.encode(fileObject, false);
					synchronized (System.out) {
						System.out.println(json);
					}
				} catch (final JavaFileParseException e) {
					synchronized (System.err) {
						System.err.println(line);
						System.err.print(e.getMessage());
						System.err.println();
					}
				}
			});
		} finally {
			Files.delete(stdin);
		}
	}
}
