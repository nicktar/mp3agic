package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

class MutableIntegerTest {
	@Test
	void initializesValue() {
		MutableInteger integer = new MutableInteger(8);
		assertThat(integer.getValue()).isEqualTo(8);
	}

	@Test
	void incrementsValue() {
		MutableInteger integer = new MutableInteger(8);
		integer.increment();
		assertThat(integer.getValue()).isEqualTo(9);
	}

	@Test
	void setsValue() {
		MutableInteger integer = new MutableInteger(8);
		integer.setValue(5);
		assertThat(integer.getValue()).isEqualTo(5);
	}

	@Test
	void equalsItself() {
		MutableInteger integer = new MutableInteger(8);
		assertThat(integer).isEqualTo(integer);
	}

	@Test
	void equalIfValueEqual() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger eightAgain = new MutableInteger(8);
		assertThat(eightAgain).isEqualTo(eight);
	}

	@Test
	void notEqualToNull() {
		MutableInteger integer = new MutableInteger(8);
		assertFalse(integer.equals(null));
	}

	@Test
	void notEqualToDifferentClass() {
		MutableInteger integer = new MutableInteger(8);
		assertFalse(integer.equals("8"));
	}

	@Test
	void notEqualIfValueNotEqual() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger nine = new MutableInteger(9);
		assertNotEquals(eight, nine);
	}

	@Test
	void hashCodeIsConsistent() {
		MutableInteger integer = new MutableInteger(8);
		assertThat(integer.hashCode()).isEqualTo(integer.hashCode());
	}

	@Test
	void equalObjectsHaveSameHashCode() {
		MutableInteger eight = new MutableInteger(8);
		MutableInteger eightAgain = new MutableInteger(8);
		assertThat(eightAgain.hashCode()).isEqualTo(eight.hashCode());
	}
}
