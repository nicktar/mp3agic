package com.mpatric.mp3agic;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ID3v2ChapterFrameDataTest {

	@Test
	void equalsItself() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertThat(frameData).isEqualTo(frameData);
	}

	@Test
	void shouldConsiderTwoEquivalentObjectsEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfUnsynchronizationNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(true, "ch1", 1, 380, 3, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIdNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIdIsNullOnOne() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void equalIfIdIsNullOnBoth() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, null, 1, 380, 3, 400);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfStartTimeNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 2, 380, 3, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfEndTimeNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 280, 3, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfStartOffsetNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 2, 400);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfEndOffsetNotEqual() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 200);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfOneHasSubframes() {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void hashCodeIsConsistent() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertThat(frameData.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void equalObjectsHaveSameHashCode() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2ChapterFrameData frameDataAgain = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertThat(frameDataAgain.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				'c', 'h', '1', 0,
				0, 0, 0, 1,
				0, 0, 1, (byte) 0x7c,
				0, 0, 0, 3,
				0, 0, 1, (byte) 0x90,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte) 0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertThat(bytes).isEqualTo(expectedBytes);
		ID3v2ChapterFrameData frameDataCopy = new ID3v2ChapterFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void toStringOnMostlyEmptyFrameData() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		assertThat(frameData.toString()).isEqualTo(
				"ID3v2ChapterFrameData [id=null, startTime=0, endTime=0, startOffset=0, endOffset=0, subframes=[]]");
	}

	@Test
	void toStringOnFullFrameData() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		assertThat(frameData.toString()).isEqualTo(
				"ID3v2ChapterFrameData [id=ch1, startTime=1, endTime=380, startOffset=3, endOffset=400, subframes=[]]");
	}

	@Test
	void getsAndSetsId() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setId("My ID");
		assertThat(frameData.getId()).isEqualTo("My ID");
	}

	@Test
	void getsAndSetsStartTime() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setStartTime(9);
		assertThat(frameData.getStartTime()).isEqualTo(9);
	}

	@Test
	void getsAndSetsEndTime() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setEndTime(9);
		assertThat(frameData.getEndTime()).isEqualTo(9);
	}

	@Test
	void getsAndSetsStartOffset() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setStartOffset(9);
		assertThat(frameData.getStartOffset()).isEqualTo(9);
	}

	@Test
	void getsAndSetsEndOffset() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		frameData.setEndOffset(9);
		assertThat(frameData.getEndOffset()).isEqualTo(9);
	}

	@Test
	void getsAndSetsSubframes() {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false);
		ArrayList<ID3v2Frame> subframes = new ArrayList<>(2);
		subframes.add(new ID3v2Frame("", new byte[]{'c', 'h', '1', 0}));
		subframes.add(new ID3v2Frame("", new byte[]{1, 0, 1, 0}));
		frameData.setSubframes(subframes);
		assertThat(frameData.getSubframes()).isEqualTo(subframes);
	}
}
