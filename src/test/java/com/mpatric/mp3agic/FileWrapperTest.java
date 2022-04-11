package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FileWrapperTest {
	private static final String fs = File.separator;
	private static final String VALID_FILENAME = "src" + fs + "test" + fs + "resources" + fs + "notags.mp3";
	private static final long VALID_FILE_LENGTH = 2869;
	private static final String NON_EXISTENT_FILENAME = "just-not.there";
	private static final String MALFORMED_FILENAME = "malformed.\0";

	@Test
	void shouldReadValidFilename() throws IOException {
		FileWrapper fileWrapper = new FileWrapper(VALID_FILENAME);
		System.out.println(fileWrapper.getFilename());
		System.out.println(VALID_FILENAME);
		assertThat(VALID_FILENAME).isEqualTo(fileWrapper.getFilename());
		assertThat(fileWrapper.getLastModified()).isGreaterThan(0);
		assertThat(VALID_FILE_LENGTH).isEqualTo(fileWrapper.getLength());
	}

	@Test
	void shouldReadValidFile() throws IOException {
		FileWrapper fileWrapper = new FileWrapper(new File(VALID_FILENAME));
		System.out.println(fileWrapper.getFilename());
		System.out.println(VALID_FILENAME);
		assertThat(VALID_FILENAME).isEqualTo(fileWrapper.getFilename());
		assertThat(fileWrapper.getLastModified()).isGreaterThan(0);
		assertThat(VALID_FILE_LENGTH).isEqualTo(fileWrapper.getLength());
	}

	@Test
	void shouldReadValidPath() throws IOException {
		FileWrapper fileWrapper = new FileWrapper(Paths.get(VALID_FILENAME));
		System.out.println(fileWrapper.getFilename());
		System.out.println(VALID_FILENAME);
		assertThat(VALID_FILENAME).isEqualTo(fileWrapper.getFilename());
		assertThat(fileWrapper.getLastModified()).isGreaterThan(0);
		assertThat(VALID_FILE_LENGTH).isEqualTo(fileWrapper.getLength());
	}

	@Test
	void shouldFailForNonExistentFile() {
		assertThrows(FileNotFoundException.class, () -> new FileWrapper(NON_EXISTENT_FILENAME));
	}

	@Test
	void shouldFailForMalformedFilename() {
		assertThrows(InvalidPathException.class, () -> new FileWrapper(MALFORMED_FILENAME));
	}

	@Test
	void shouldFailForNullFilename() {
		assertThrows(NullPointerException.class, () -> new FileWrapper((String) null));
	}

	@Test
	void shouldFailForNullFilenameFile() {
		assertThrows(NullPointerException.class, () -> new FileWrapper((java.io.File) null));
	}

	@Test
	void shouldFailForNullPath() {
		assertThrows(NullPointerException.class, () -> new FileWrapper((java.nio.file.Path) null));
	}
}
