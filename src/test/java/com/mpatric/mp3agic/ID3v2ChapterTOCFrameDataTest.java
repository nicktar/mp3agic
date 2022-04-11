package com.mpatric.mp3agic;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ID3v2ChapterTOCFrameDataTest {

	@Test
	void equalsItself() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData).isEqualTo(frameData);
	}

	@Test
	void shouldConsiderTwoEquivalentObjectsEqual() {
		String[] children = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);

		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfUnsynchronizationNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(true, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIsRootNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, false, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIsOrderedNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIdNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc2", new String[]{"ch1", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfIdIsNullOnOne() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void equalIfIdIsNullOnBoth() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, null, new String[]{"ch1", "ch2"});
		assertThat(frameData2).isEqualTo(frameData1);
	}

	@Test
	void notEqualIfChildrenNotEqual() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{"ch3", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfOneDoeNotHaveChildren() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfChildrenNullOnOne() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", null);
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc`", new String[]{"ch3", "ch2"});
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void notEqualIfOneHasSubframes() {
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertThat(frameData1).isNotEqualTo(frameData2);
	}

	@Test
	void hashCodeIsConsistent() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void equalObjectsHaveSameHashCode() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		ID3v2ChapterTOCFrameData frameDataAgain = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameDataAgain.hashCode()).isEqualTo(frameData.hashCode());
	}

	@Test
	void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		String[] children = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", children);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				't', 'o', 'c', '1', 0,
				3, 2,
				'c', 'h', '1', 0,
				'c', 'h', '2', 0,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte) 0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertThat(bytes).isEqualTo(expectedBytes);
		ID3v2ChapterTOCFrameData frameDataCopy = new ID3v2ChapterTOCFrameData(false, bytes);
		assertThat(frameDataCopy).isEqualTo(frameData);
	}

	@Test
	void toStringOnMostlyEmptyFrameData() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		assertThat(frameData.toString()).isEqualTo(
				"ID3v2ChapterTOCFrameData [isRoot=false, isOrdered=false, id=null, children=null, subframes=[]]");
	}

	@Test
	void toStringOnFullFrameData() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", new String[]{"ch1", "ch2"});
		assertThat(frameData.toString()).isEqualTo(
				"ID3v2ChapterTOCFrameData [isRoot=true, isOrdered=true, id=toc1, children=[ch1, ch2], subframes=[]]");
	}

	@Test
	void getsAndSetsIsRoot() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setRoot(true);
		assertThat(frameData.isRoot()).isTrue();
	}

	@Test
	void getsAndSetsIsOrdered() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setOrdered(true);
		assertThat(frameData.isOrdered()).isTrue();
	}

	@Test
	void getsAndSetsId() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setId("My ID");
		assertThat(frameData.getId()).isEqualTo("My ID");
	}

	@Test
	void getsAndSetsChildren() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		frameData.setChildren(new String[]{"ch1", "ch2"});
		assertThat(frameData.getChildren()).isEqualTo(new String[]{"ch1", "ch2"});
	}

	@Test
	void getsAndSetsSubframes() {
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false);
		ArrayList<ID3v2Frame> subframes = new ArrayList<>(2);
		subframes.add(new ID3v2Frame("", new byte[]{'c', 'h', '1', 0}));
		subframes.add(new ID3v2Frame("", new byte[]{1, 0, 1, 0}));
		frameData.setSubframes(subframes);
		assertThat(frameData.getSubframes()).isEqualTo(subframes);
	}
}
