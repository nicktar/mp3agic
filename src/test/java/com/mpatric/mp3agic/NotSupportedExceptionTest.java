package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

class NotSupportedExceptionTest {
	@Test
	void defaultConstructor() {
		NotSupportedException exception = new NotSupportedException();
		assertNull(exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	void constructorWithMessage() {
		NotSupportedException exception = new NotSupportedException("A message");
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertNull(exception.getCause());
	}

	@Test
	void constructorWithMessageAndCause() {
		Throwable exceptionCause = new IllegalArgumentException("Bad argument");
		NotSupportedException exception = new NotSupportedException("A message", exceptionCause);
		assertThat(exception.getMessage()).isEqualTo("A message");
		assertThat(exception.getCause()).isEqualTo(exceptionCause);
	}
}
