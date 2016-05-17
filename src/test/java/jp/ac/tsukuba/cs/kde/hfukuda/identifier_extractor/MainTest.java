package jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects.AccessLevel;
import jp.ac.tsukuba.cs.kde.hfukuda.identifier_extractor.objects.FileObject;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MainTest extends TestCase {
	public MainTest(final String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(MainTest.class);
	}

	public void testAllXtestTests() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/AllXtextTests.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("edu.byu.ee.phdl.xtext.tests", fileObject.packageName);
			Assert.assertEquals("org.junit.runner", fileObject.importDeclarations.get(0).qualifier);
			Assert.assertEquals("RunWith", fileObject.importDeclarations.get(0).typeName);
			Assert.assertEquals("org.junit.runners", fileObject.importDeclarations.get(1).qualifier);
			Assert.assertEquals("Suite", fileObject.importDeclarations.get(1).typeName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("AllXtextTests", fileObject.topLevelClasses.get(0).name);
			Assert.assertNull(null, fileObject.topLevelClasses.get(0).annotations.get(0).qualifier);
			Assert.assertEquals("RunWith", fileObject.topLevelClasses.get(0).annotations.get(0).typeName);
			Assert.assertEquals("Suite", fileObject.topLevelClasses.get(0).annotations.get(1).qualifier);
			Assert.assertEquals("SuiteClasses", fileObject.topLevelClasses.get(0).annotations.get(1).typeName);
		}
	}
	public void testConnectionNotFoundException() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/ConnectionNotFoundException.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("javax.microedition.io", fileObject.packageName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("ConnectionNotFoundException", fileObject.topLevelClasses.get(0).name);
			Assert.assertEquals("IOException", fileObject.topLevelClasses.get(0).superclass.name);
			Assert.assertEquals("io", fileObject.topLevelClasses.get(0).superclass.qualifier.name);
			Assert.assertEquals("java", fileObject.topLevelClasses.get(0).superclass.qualifier.qualifier.name);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).constructors.get(0).accessLevel);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).constructors.get(1).accessLevel);
			Assert.assertFalse(fileObject.topLevelClasses.get(0).constructors.get(1).parameters.get(0).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).constructors.get(1).parameters.get(0).type.qualifier);
			Assert.assertEquals("String", fileObject.topLevelClasses.get(0).constructors.get(1).parameters.get(0).type.name);
			Assert.assertEquals("param", fileObject.topLevelClasses.get(0).constructors.get(1).parameters.get(0).name);
		}
	}
	public void testConstants() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/Constants.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("org.openengage.assistance", fileObject.packageName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("Constants", fileObject.topLevelClasses.get(0).name);
			Assert.assertEquals(AccessLevel.PACKAGE_PRIVATE, fileObject.topLevelClasses.get(0).fields.get(0).accessLevel);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(0).isStatic);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(0).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).fields.get(0).type.qualifier);
			Assert.assertEquals("String", fileObject.topLevelClasses.get(0).fields.get(0).type.name);
			Assert.assertEquals("MODULE_DIRECTORY", fileObject.topLevelClasses.get(0).fields.get(0).name);
			Assert.assertEquals(AccessLevel.PACKAGE_PRIVATE, fileObject.topLevelClasses.get(0).fields.get(1).accessLevel);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(1).isStatic);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(1).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).fields.get(1).type.qualifier);
			Assert.assertEquals("String", fileObject.topLevelClasses.get(0).fields.get(1).type.name);
			Assert.assertEquals("AUDIO_FILE_EXTENSION", fileObject.topLevelClasses.get(0).fields.get(1).name);
			Assert.assertEquals(AccessLevel.PACKAGE_PRIVATE, fileObject.topLevelClasses.get(0).fields.get(2).accessLevel);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(2).isStatic);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(2).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).fields.get(2).type.qualifier);
			Assert.assertEquals("String", fileObject.topLevelClasses.get(0).fields.get(2).type.name);
			Assert.assertEquals("IMAGE_FILE_EXTENSION", fileObject.topLevelClasses.get(0).fields.get(2).name);
		}
	}
	public void testContext() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/Context.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("zk.canvas", fileObject.packageName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("Context", fileObject.topLevelClasses.get(0).name);
		}
	}
	public void testFeatureNode() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/FeatureNode.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("liblinear", fileObject.packageName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("FeatureNode", fileObject.topLevelClasses.get(0).name);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).fields.get(0).accessLevel);
			Assert.assertFalse(fileObject.topLevelClasses.get(0).fields.get(0).isStatic);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(0).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).fields.get(0).type.qualifier);
			Assert.assertEquals("int", fileObject.topLevelClasses.get(0).fields.get(0).type.name);
			Assert.assertEquals("index", fileObject.topLevelClasses.get(0).fields.get(0).name);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).fields.get(1).accessLevel);
			Assert.assertFalse(fileObject.topLevelClasses.get(0).fields.get(1).isStatic);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).fields.get(1).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).fields.get(1).type.qualifier);
			Assert.assertEquals("double", fileObject.topLevelClasses.get(0).fields.get(1).type.name);
			Assert.assertEquals("value", fileObject.topLevelClasses.get(0).fields.get(1).name);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).constructors.get(0).accessLevel);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(0).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(0).type.qualifier);
			Assert.assertEquals("int", fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(0).type.name);
			Assert.assertEquals("index", fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(0).name);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(1).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(1).type.qualifier);
			Assert.assertEquals("double", fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(1).type.name);
			Assert.assertEquals("value", fileObject.topLevelClasses.get(0).constructors.get(0).parameters.get(1).name);
		}
	}
	public void testRedirect() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/Redirect.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("br.gov.frameworkdemoiselle.annotation", fileObject.packageName);
			Assert.assertEquals("java.lang.annotation", fileObject.staticImportDeclarations.get(0).qualifier);
			Assert.assertEquals("ElementType", fileObject.staticImportDeclarations.get(0).typeName);
			Assert.assertEquals("TYPE", fileObject.staticImportDeclarations.get(0).lastName);
			Assert.assertEquals("java.lang.annotation", fileObject.staticImportDeclarations.get(1).qualifier);
			Assert.assertEquals("RetentionPolicy", fileObject.staticImportDeclarations.get(1).typeName);
			Assert.assertEquals("RUNTIME", fileObject.staticImportDeclarations.get(1).lastName);
			Assert.assertEquals("java.lang.annotation", fileObject.importDeclarations.get(0).qualifier);
			Assert.assertEquals("Retention", fileObject.importDeclarations.get(0).typeName);
			Assert.assertEquals("java.lang.annotation", fileObject.importDeclarations.get(1).qualifier);
			Assert.assertEquals("Target", fileObject.importDeclarations.get(1).typeName);
		}
	}
	public void testRPNHelper() throws IOException, JavaFileParseException {
		try (final InputStream is = MainTest.class.getClassLoader().getResourceAsStream("jp/ac/tsukuba/cs/kde/hfukuda/identifier_extractor/RPNHelper.java")) {
			final FileObject fileObject = FileObject.fromASTCreationInfo(ASTParserUtils.createAST(IOUtils.toString(is, StandardCharsets.ISO_8859_1)));
			Assert.assertEquals("com.k_int.z3950.util", fileObject.packageName);
			Assert.assertEquals("com.k_int.codec.runtime", fileObject.importDeclarations.get(0).qualifier);
			Assert.assertEquals("*", fileObject.importDeclarations.get(0).typeName);
			Assert.assertEquals("com.k_int.codec.util", fileObject.importDeclarations.get(1).qualifier);
			Assert.assertEquals("*", fileObject.importDeclarations.get(1).typeName);
			Assert.assertEquals("com.k_int.gen.Z39_50_APDU_1995", fileObject.importDeclarations.get(2).qualifier);
			Assert.assertEquals("*", fileObject.importDeclarations.get(2).typeName);
			Assert.assertEquals("com.k_int.util.RPNQueryRep", fileObject.importDeclarations.get(3).qualifier);
			Assert.assertEquals("*", fileObject.importDeclarations.get(3).typeName);
			Assert.assertEquals("java.util", fileObject.importDeclarations.get(4).qualifier);
			Assert.assertEquals("Vector", fileObject.importDeclarations.get(4).typeName);
			Assert.assertEquals("java.util", fileObject.importDeclarations.get(5).qualifier);
			Assert.assertEquals("Enumeration", fileObject.importDeclarations.get(5).typeName);
			Assert.assertEquals("java.math", fileObject.importDeclarations.get(6).qualifier);
			Assert.assertEquals("BigInteger", fileObject.importDeclarations.get(6).typeName);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).accessLevel);
			Assert.assertEquals("RPNHelper", fileObject.topLevelClasses.get(0).name);
			Assert.assertEquals(AccessLevel.PUBLIC, fileObject.topLevelClasses.get(0).methods.get(0).accessLevel);
			Assert.assertTrue(fileObject.topLevelClasses.get(0).methods.get(0).isStatic);
			Assert.assertNull(fileObject.topLevelClasses.get(0).methods.get(0).returnValueType.qualifier);
			Assert.assertEquals("RPNQuery_type", fileObject.topLevelClasses.get(0).methods.get(0).returnValueType.name);
			Assert.assertEquals("RootNodeToZRPNStructure", fileObject.topLevelClasses.get(0).methods.get(0).name);
			Assert.assertFalse(fileObject.topLevelClasses.get(0).methods.get(0).parameters.get(0).isFinal);
			Assert.assertNull(fileObject.topLevelClasses.get(0).methods.get(0).parameters.get(0).type.qualifier);
			Assert.assertEquals("RootNode", fileObject.topLevelClasses.get(0).methods.get(0).parameters.get(0).type.name);
			Assert.assertEquals("query_tree", fileObject.topLevelClasses.get(0).methods.get(0).parameters.get(0).name);
			Assert.assertNull(fileObject.topLevelClasses.get(0).methods.get(0).thrownExceptions.get(0).qualifier);
			Assert.assertEquals("InvalidQueryException", fileObject.topLevelClasses.get(0).methods.get(0).thrownExceptions.get(0).typeName);
		}
	}
}
