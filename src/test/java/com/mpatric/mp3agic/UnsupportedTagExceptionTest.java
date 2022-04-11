package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

class UnsupportedTagExceptionTest {

	@Test
	void defaultConstructor() {
		UnsupportedTagException exception = new UnsupportedTagException();
		assertNull(exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	void constructorWithMessage() {
		UnsupportedTagException exception = new UnsupportedTagException("A message");
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertNull(exception.getCause());
	}

	@Test
	void constructorWithMessageAndCause() {
		Throwable exceptionCause = new IllegalArgumentException("Bad argument");
		UnsupportedTagException exception = new UnsupportedTagException("A message", exceptionCause);
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertThat(exception.getCause()).isEqualTo(exceptionCause);
	}
}
