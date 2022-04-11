package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

class ID3v2PictureFrameDataTest {

	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;

	private static final String TEST_MIME_TYPE = "mime/type";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

	@Test
	void equalsItself() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameData).isEqualTo(frameData);
	}

	@Test
	void notEqualToNull() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertFalse(frameData.equals(null));
	}

	@Test
	void notEqualToDifferentClass() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertFalse(frameData.equals("8"));
	}

	@Test
	void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfUnsynchronizationNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(true, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfMimeTypeNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, "other mime type", (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfMimeTypeIsNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfMimeTypeIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfPictureTypeNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 4, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfDescriptionNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, "other description"), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfDescriptionIsNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfDescriptionIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfImageDataNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), new byte[]{});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfImageDataNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfImageDataIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void hashCodeIsConsistent() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameData.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void equalObjectsHaveSameHashCode() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameDataAgain = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertThat(frameDataAgain.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, 4, 5};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x01, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, 1, 2, 3, 4, 5};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void shouldUnsynchroniseAndSynchroniseDataWhenPackingAndUnpacking() throws Exception {
		byte[] data = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0, 0, 4, 5};
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(true, data);
		byte[] expectedImageData = {1, 2, 3, BYTE_FF, BYTE_FB, BYTE_FF, BYTE_FB, BYTE_FF, 0, 4, 5};
		assertArrayEquals(expectedImageData, frameData.getImageData());
		byte[] bytes = frameData.toBytes();
		assertArrayEquals(data, bytes);
	}

	@Test
	void getsAndSetsMimeType() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setMimeType("Mime Type 1");
		assertThat(frameData.getMimeType()).isEqualTo("Mime Type 1");
	}

	@Test
	void getsAndSetsPictureType() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setPictureType((byte) 4);
		assertThat(frameData.getPictureType()).isEqualTo((byte) 4);
	}

	@Test
	void getsAndSetsDescription() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		EncodedText description = new EncodedText("my description");
		frameData.setDescription(description);
		assertThat(frameData.getDescription()).isEqualTo(description);
	}

	@Test
	void getsAndSetsImageData() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setImageData(new byte[]{1, 2});
		assertArrayEquals(new byte[]{1, 2}, frameData.getImageData());
	}
}
