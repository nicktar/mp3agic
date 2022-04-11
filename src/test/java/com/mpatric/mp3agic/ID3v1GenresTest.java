package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ID3v1GenresTest {

	@Test
	void returnsMinusOneForNonExistentGenre() throws Exception {
		assertThat(ID3v1Genres.matchGenreDescription("non existent")).isEqualTo(-1);
	}

	@Test
	void returnsCorrectGenreIdForFirstExistentGenre() throws Exception {
		assertThat(ID3v1Genres.matchGenreDescription("Blues")).isEqualTo(0);
	}

	@Test
	void returnsCorrectGenreIdForPolka() throws Exception {
		assertThat(ID3v1Genres.matchGenreDescription("Polka")).isEqualTo(75);
	}

	@Test
	void returnsCorrectGenreIdForLastExistentGenre() throws Exception {
		assertThat(ID3v1Genres.matchGenreDescription("Synthpop")).isEqualTo(147);
	}

	@Test
	void ignoresCase() throws Exception {
		assertThat(ID3v1Genres.matchGenreDescription("heavy METAL")).isEqualTo(137);
	}

}
