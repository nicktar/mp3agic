package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ID3v2WWWFrameDataTest {
	@Test
	void getsAndSetsId() {
		ID3v2WWWFrameData frameData = new ID3v2WWWFrameData(false);
		frameData.setUrl("My URL");
		assertThat(frameData.getUrl()).isEqualTo("My URL");
	}
}
