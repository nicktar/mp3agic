package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.assertj.core.api.Assertions.assertThat;

class ID3v2ObseletePictureFrameDataTest {

	private static final String TEST_MIME_TYPE = "image/png";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

	@Test
	void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2ObseletePictureFrameData frameData1 = new ID3v2ObseletePictureFrameData(false, TEST_MIME_TYPE, (byte) 0, new EncodedText((byte) 1, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2ObseletePictureFrameData frameData2 = new ID3v2ObseletePictureFrameData(false, TEST_MIME_TYPE, (byte) 0, new EncodedText((byte) 1, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void shouldReadFrameData() throws Exception {
		byte[] bytes = {0x00, 'P', 'N', 'G', 0x01, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0x00, 1, 2, 3, 4, 5};
		ID3v2ObseletePictureFrameData frameData = new ID3v2ObseletePictureFrameData(false, bytes);
		assertThat(frameData.getMimeType()).isEqualTo(TEST_MIME_TYPE);
		assertThat(frameData.getPictureType()).isEqualTo((byte) 1);
		assertThat(frameData.getDescription()).isEqualTo(new EncodedText((byte) 0, TEST_DESCRIPTION));
		assertArrayEquals(DUMMY_IMAGE_DATA, frameData.getImageData());
	}

	@Test
	void shouldReadFrameDataWithUnicodeDescription() throws Exception {
		byte[] bytes = {0x01, 'P', 'N', 'G', 0x01, (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, 1, 2, 3, 4, 5};
		ID3v2ObseletePictureFrameData frameData = new ID3v2ObseletePictureFrameData(false, bytes);
		assertThat(frameData.getMimeType()).isEqualTo(TEST_MIME_TYPE);
		assertThat(frameData.getPictureType()).isEqualTo((byte) 1);
		assertThat(frameData.getDescription()).isEqualTo(new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE));
		assertArrayEquals(DUMMY_IMAGE_DATA, frameData.getImageData());
	}
}
