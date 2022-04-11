package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidDataExceptionTest {
	@Test
	void defaultConstructor() {
		InvalidDataException exception = new InvalidDataException();
		assertThat(exception.getMessage()).isNull();
		assertThat(exception.getCause()).isNull();
	}

	@Test
	void constructorWithMessage() {
		InvalidDataException exception = new InvalidDataException("A message");
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertThat(exception.getCause()).isNull();
	}

	@Test
	void constructorWithMessageAndCause() {
		Throwable exceptionCause = new IllegalArgumentException("Bad argument");
		InvalidDataException exception = new InvalidDataException("A message", exceptionCause);
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertThat(exception.getCause()).isEqualTo(exceptionCause);
	}
}
