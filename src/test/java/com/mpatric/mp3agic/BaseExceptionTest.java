package com.mpatric.mp3agic;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseExceptionTest {

	@Test
	void generatesCorrectDetailedMessageForSingleException() {
		BaseException e = new BaseException("ONE");
		assertThat(e.getMessage()).isEqualTo("ONE");
		assertThat(e.getDetailedMessage()).isEqualTo("[com.mpatric.mp3agic.BaseException: ONE]");
	}

	@Test
	void generatesCorrectDetailedMessageForChainedBaseExceptions() {
		BaseException e1 = new BaseException("ONE");
		BaseException e2 = new UnsupportedTagException("TWO", e1);
		BaseException e3 = new NotSupportedException("THREE", e2);
		BaseException e4 = new NoSuchTagException("FOUR", e3);
		BaseException e5 = new InvalidDataException("FIVE", e4);
		assertThat(e5.getMessage()).isEqualTo("FIVE");
		assertThat(e5.getDetailedMessage()).isEqualTo("[com.mpatric.mp3agic.InvalidDataException: FIVE] caused by [com.mpatric.mp3agic.NoSuchTagException: FOUR] caused by [com.mpatric.mp3agic.NotSupportedException: THREE] caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] caused by [com.mpatric.mp3agic.BaseException: ONE]");
	}

	@Test
	void generatesCorrectDetailedMessageForChainedExceptionsWithOtherExceptionInMix() {
		BaseException e1 = new BaseException("ONE");
		BaseException e2 = new UnsupportedTagException("TWO", e1);
		Exception e3 = new Exception("THREE", e2);
		BaseException e4 = new NoSuchTagException("FOUR", e3);
		BaseException e5 = new InvalidDataException("FIVE", e4);
		assertThat(e5.getMessage()).isEqualTo("FIVE");
		assertThat(e5.getDetailedMessage()).isEqualTo("[com.mpatric.mp3agic.InvalidDataException: FIVE] caused by [com.mpatric.mp3agic.NoSuchTagException: FOUR] caused by [java.lang.Exception: THREE] caused by [com.mpatric.mp3agic.UnsupportedTagException: TWO] caused by [com.mpatric.mp3agic.BaseException: ONE]");
	}
}
