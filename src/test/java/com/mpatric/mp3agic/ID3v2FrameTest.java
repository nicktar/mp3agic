package com.mpatric.mp3agic;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

class ID3v2FrameTest {

	private static final String T_FRAME = "TPE1000 000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String W_FRAME = "WXXX000!0000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String C_FRAME = "COMM000$0000000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";

	@Test
	void shouldReadValid32TFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer("xxxxx" + T_FRAME, 0, 5 + T_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 9);
		ID3v2Frame frame = new ID3v2Frame(bytes, 5);
		assertThat(frame.getLength()).isEqualTo(42);
		assertThat(frame.getId()).isEqualTo("TPE1");
		String s = "0ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	void shouldReadValid32WFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(W_FRAME + "xxxxx", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertThat(frame.getLength()).isEqualTo(43);
		assertThat(frame.getId()).isEqualTo("WXXX");
		String s = "00ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	void shouldReadValid32CFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(C_FRAME, 0, C_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertThat(frame.getLength()).isEqualTo(46);
		assertThat(frame.getId()).isEqualTo("COMM");
		String s = "00000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	void shouldPackAndUnpackHeaderToGiveEquivalentObject() throws Exception {
		byte[] bytes = new byte[26];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ('A' + i);
		}
		ID3v2Frame frame = new ID3v2Frame("TEST", bytes);
		byte[] newBytes = frame.toBytes();
		ID3v2Frame frameCopy = new ID3v2Frame(newBytes, 0);
		assertThat(frameCopy.getId()).isEqualTo("TEST");
		assertThat(frameCopy).isEqualTo(frame);
	}

	@Test
	void shouldCorrectlyUnpackHeader() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(W_FRAME + "?????", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		final ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertFalse(frame.hasDataLengthIndicator());
		assertFalse(frame.hasCompression());
		assertFalse(frame.hasEncryption());
		assertFalse(frame.hasGroup());
		assertFalse(frame.hasPreserveFile());
		assertFalse(frame.hasPreserveTag());
		assertFalse(frame.isReadOnly());
		assertFalse(frame.hasUnsynchronisation());
	}

	@Test
	void shouldStoreAndRetrieveData() throws Exception {
		final byte[] oldBytes = BufferTools.stringToByteBuffer(C_FRAME, 0, C_FRAME.length());
		TestHelper.replaceNumbersWithBytes(oldBytes, 0);
		final ID3v2Frame frame = new ID3v2Frame(oldBytes, 0);
		final byte[] newBytes = BufferTools.stringToByteBuffer(W_FRAME + "?????", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(newBytes, 0);
		frame.setData(newBytes);
		final byte[] expectedBytes = BufferTools.stringToByteBuffer(W_FRAME, 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	void shouldCorrectlyImplementHashCodeAndEquals() throws Exception {
		EqualsVerifier.forClass(ID3v2Frame.class)
				.usingGetClass()
				.suppress(Warning.NONFINAL_FIELDS)
				.verify();
	}

}
