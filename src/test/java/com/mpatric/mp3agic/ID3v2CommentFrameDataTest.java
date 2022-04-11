package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ID3v2CommentFrameDataTest {

	private static final String TEST_LANGUAGE = "eng";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_VALUE = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_VALUE_UNICODE = "\u03C3\u03BF\u03C5";

	@Test
	void equalsItself() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertThat(frameData).isEqualTo(frameData);
	}

	@Test
	void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfUnsynchronizationNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(true, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfLanguageNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, "jap", new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfLanguageIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfLanguageIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfDescriptionNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, "other description"), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfDescriptionIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfDescriptionIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfCommentNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, "other comment"));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void notEqualIfCommentIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	void equalIfCommentIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void hashCodeIsConsistent() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertThat(frameData.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void equalObjectsHaveSameHashCode() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameDataAgain = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertThat(frameDataAgain.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'e', 'n', 'g', 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void constructorThrowsErrorWhenEncodingsDoNotMatch() {
		assertThrows(IllegalArgumentException.class, () -> new ID3v2CommentFrameData(false, TEST_LANGUAGE,
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, "description"),
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, "comment")));
	}

	@Test
	void shouldConvertFrameDataWithBlankDescriptionAndLanguageToBytesAndBackToEquivalentObject() throws Exception {
		byte[] bytes = {0, 0, 0, 0, 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameData.getLanguage()).isEqualTo("\00\00\00");
		assertThat(frameData.getDescription()).isEqualTo(new EncodedText(""));
		assertThat(frameData.getComment()).isEqualTo(new EncodedText(TEST_VALUE));
		assertArrayEquals(bytes, frameData.toBytes());
	}

	@Test
	void shouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {1, 'e', 'n', 'g', (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, (byte) 0xff, (byte) 0xfe, (byte) 0xc3, 0x03, (byte) 0xbf, 0x03, (byte) 0xc5, 0x03};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void convertsEmptyFrameDataToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, null, null, null);
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy.getLanguage()).isEqualTo("eng");
		assertThat(frameDataCopy.getDescription()).isEqualTo(new EncodedText(""));
		assertThat(frameDataCopy.getComment()).isEqualTo(new EncodedText(""));
	}

	@Test
	void convertsFrameDataWithNoLanguageToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, null, new EncodedText(TEST_DESCRIPTION), new EncodedText(TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy.getLanguage()).isEqualTo("eng");
		assertThat(frameDataCopy.getDescription()).isEqualTo(new EncodedText(TEST_DESCRIPTION));
		assertThat(frameDataCopy.getComment()).isEqualTo(new EncodedText(TEST_VALUE));
	}

	@Test
	void convertsFrameDataWithNoDescriptionToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText(TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy.getLanguage()).isEqualTo("eng");
		assertThat(frameDataCopy.getDescription()).isEqualTo(new EncodedText(""));
		assertThat(frameDataCopy.getComment()).isEqualTo(new EncodedText(TEST_VALUE));
	}

	@Test
	void convertsFrameDataWithNoDescriptionAndCommentIsUnicodeToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null,
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy.getLanguage()).isEqualTo("eng");
		assertThat(frameDataCopy.getDescription()).isEqualTo(new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, ""));
		assertThat(frameDataCopy.getComment()).isEqualTo(new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE));
	}

	@Test
	void convertsFrameDataWithNoCommentToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText(TEST_DESCRIPTION), null);
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertThat(frameDataCopy.getLanguage()).isEqualTo("eng");
		assertThat(frameDataCopy.getDescription()).isEqualTo(new EncodedText(TEST_DESCRIPTION));
		assertThat(frameDataCopy.getComment()).isEqualTo(new EncodedText(""));
	}

	@Test
	void getsAndSetsLanguage() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		frameData.setLanguage("my language");
		assertThat(frameData.getLanguage()).isEqualTo("my language");
	}

	@Test
	void getsAndSetsComment() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		EncodedText comment = new EncodedText("my comment");
		frameData.setComment(comment);
		assertThat(frameData.getComment()).isEqualTo(comment);
	}

	@Test
	void getsAndSetsDescription() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		EncodedText description = new EncodedText("my description");
		frameData.setDescription(description);
		assertThat(frameData.getDescription()).isEqualTo(description);
	}
}
