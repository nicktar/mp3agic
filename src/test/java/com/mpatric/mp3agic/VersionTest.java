package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionTest {
	@Test
	void returnsVersion() {
		assertThat(Version.getVersion()).isEqualTo("UNKNOWN-SNAPSHOT");
	}

	@Test
	void returnsUrl() {
		assertThat(Version.getUrl()).isEqualTo("http://github.com/mpatric/mp3agic");
	}

	@Test
	void returnsVersionAndUrlAsString() {
		assertThat(Version.asString()).isEqualTo("UNKNOWN-SNAPSHOT - http://github.com/mpatric/mp3agic");
	}
}
