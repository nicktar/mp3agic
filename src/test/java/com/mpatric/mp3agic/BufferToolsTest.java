package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BufferToolsTest {

	private static final byte BYTE_T = 0x54;
	private static final byte BYTE_A = 0x41;
	private static final byte BYTE_G = 0x47;
	private static final byte BYTE_DASH = 0x2D;
	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	private static final byte BYTE_90 = -0x70;
	private static final byte BYTE_44 = 0x44;
	private static final byte BYTE_E0 = -0x20;
	private static final byte BYTE_F0 = -0x10;
	private static final byte BYTE_81 = -0x7F;
	private static final byte BYTE_ESZETT = -0x21;


	// byte buffer to string
	@Test
	public void shouldExtractStringFromStartOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH};
		assertThat(BufferTools.byteBufferToString(buffer, 0, 3)).isEqualTo("TAG");
	}

	@Test
	public void shouldExtractStringFromEndOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertThat(BufferTools.byteBufferToString(buffer, 5, 3)).isEqualTo("TAG");
	}

	@Test
	public void shouldExtractStringFromMiddleOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertThat(BufferTools.byteBufferToString(buffer, 5, 3)).isEqualTo("TAG");
	}

	@Test
	public void shouldExtractUnicodeStringFromMiddleOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, 0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, BYTE_DASH, BYTE_DASH};
		assertThat(BufferTools.byteBufferToString(buffer, 2, 8, "UTF-16BE")).isEqualTo("\u03B3\u03B5\u03B9\u03AC");
	}

	@Test
	public void shouldThrowExceptionForOffsetBeforeStartOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertThrows(StringIndexOutOfBoundsException.class, () -> BufferTools.byteBufferToString(buffer, -1, 4));
	}

	@Test
	public void shouldThrowExceptionForOffsetAfterEndOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertThrows(StringIndexOutOfBoundsException.class, () -> BufferTools.byteBufferToString(buffer, buffer.length, 1));
	}

	@Test
	public void shouldThrowExceptionForLengthExtendingBeyondEndOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertThrows(StringIndexOutOfBoundsException.class, () -> BufferTools.byteBufferToString(buffer, buffer.length - 2, 3));
	}


	// string to byte buffer
	@Test
	public void shouldConvertStringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 0, original.length());
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertThat(converted).isEqualTo(original);
	}

	@Test
	public void shouldConvertSubstringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 2, original.length() - 5);
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertThat(converted).isEqualTo("34567890QWERTYU");
	}

	@Test
	public void shouldConvertUnicodeStringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 0, original.length(), "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertThat(converted).isEqualTo(original);
	}

	@Test
	public void shouldConvertUnicodeSubstringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 2, original.length() - 5, "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertThat(converted).isEqualTo("\u03B9\u03AC ");
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, 20})
	public void shouldThrowAnExceptionWhenConvertingStringToBytesWithOffsetOutOfRange(int offset) {
		String original = "1234567890QWERTYUIOP";
		assertThrows(StringIndexOutOfBoundsException.class, () -> BufferTools.stringToByteBuffer(original, offset, 1));
	}

	@ParameterizedTest
	@MethodSource("createParamsForShouldThrowAnExceptionWhenConvertingStringToBytesWithLengthOutOfRange")
	public void shouldThrowAnExceptionWhenConvertingStringToBytesWithLengthOutOfRange(int offset, int length) {
		String original = "1234567890QWERTYUIOP";
		assertThrows(StringIndexOutOfBoundsException.class, () -> BufferTools.stringToByteBuffer(original, offset, length));
	}

	static Stream<Arguments> createParamsForShouldThrowAnExceptionWhenConvertingStringToBytesWithLengthOutOfRange() {
		return Stream.of(
				Arguments.of(0, -1),
				Arguments.of(0, 21),
				Arguments.of(3, 18)
		);
	}

	// string into existing byte buffer
	@Test
	public void shouldCopyStringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0);
		byte[] expectedBuffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, 0, 0, 0, 0, 0, 0};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopyUnicodeStringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0, 0};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopyStringToEndOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 6);
		byte[] expectedBuffer = {0, 0, 0, 0, 0, 0, BYTE_T, BYTE_A, BYTE_G, BYTE_DASH};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopyUnicodeStringToEndOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 2, "UTF-16BE");
		byte[] expectedBuffer = {0, 0, 0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopySubstringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0);
		byte[] expectedBuffer = {BYTE_A, BYTE_G, 0, 0, 0, 0, 0, 0, 0, 0};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopyUnicodeSubstringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0, 0, 0, 0, 0, 0};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@Test
	public void shouldCopySubstringToMiddleOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 4);
		byte[] expectedBuffer = {0, 0, 0, 0, BYTE_A, BYTE_G, 0, 0, 0, 0};
		assertThat(buffer).isEqualTo(expectedBuffer);
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, 4})
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithOffsetOutOfRange(int offset) {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		assertThrows(StringIndexOutOfBoundsException.class,
				() -> BufferTools.stringIntoByteBuffer(s, offset, 1, buffer, 0));
	}

	@ParameterizedTest
	@MethodSource("createDataForShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithLengthOutOfRange")
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithLengthOutOfRange(int offset, int length) {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		assertThrows(StringIndexOutOfBoundsException.class,
				() -> BufferTools.stringIntoByteBuffer(s, offset, length, buffer, 0));
	}

	public static Stream<Arguments> createDataForShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithLengthOutOfRange() {
		return Stream.of(
				Arguments.of(0,-1),
				Arguments.of(0,5),
				Arguments.of(3, 2)
		);
	}

	@ParameterizedTest
	@MethodSource("createDataForShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithDestinationOffsetOutOfRange")
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithDestinationOffsetOutOfRange(int length, int destOffset) {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		assertThrows(StringIndexOutOfBoundsException.class,
				() -> BufferTools.stringIntoByteBuffer(s, 0, length, buffer, destOffset));
	}

	static Stream<Arguments> createDataForShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithDestinationOffsetOutOfRange() {
		return Stream.of(
				Arguments.of(1, 10),
				Arguments.of(4, 7)
		);
	}
	// trim strings

	@Test
	public void shouldRightTrimStringsCorrectly() throws UnsupportedEncodingException {
		assertThat(BufferTools.trimStringRight("")).isEqualTo("");
		assertThat(BufferTools.trimStringRight(" ")).isEqualTo("");
		assertThat(BufferTools.trimStringRight("TEST")).isEqualTo("TEST");
		assertThat(BufferTools.trimStringRight("TEST   ")).isEqualTo("TEST");
		assertThat(BufferTools.trimStringRight("   TEST")).isEqualTo("   TEST");
		assertThat(BufferTools.trimStringRight("   TEST   ")).isEqualTo("   TEST");
		assertThat(BufferTools.trimStringRight("TEST\t\r\n")).isEqualTo("TEST");
		assertThat(BufferTools.trimStringRight("TEST" + BufferTools.byteBufferToString(new byte[]{0, 0}, 0, 2)))
				.isEqualTo("TEST");
	}

	@Test
	public void shouldRightTrimUnicodeStringsCorrectly() throws UnsupportedEncodingException {
		assertThat(BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC")).isEqualTo("\u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC   ")).isEqualTo("\u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC")).isEqualTo("   \u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC   ")).isEqualTo("   \u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC\t\r\n")).isEqualTo("\u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC" + BufferTools.byteBufferToString(new byte[]{0, 0}, 0, 2)))
				.isEqualTo("\u03B3\u03B5\u03B9\u03AC");
	}

	@Test
	public void shouldRightPadStringsCorrectly() {
		assertThat(BufferTools.padStringRight("1234", 3, ' ')).isEqualTo("1234");
		assertThat(BufferTools.padStringRight("123", 3, ' ')).isEqualTo("123");
		assertThat(BufferTools.padStringRight("12", 3, ' ')).isEqualTo("12 ");
		assertThat(BufferTools.padStringRight("1", 3, ' ')).isEqualTo("1  ");
		assertThat(BufferTools.padStringRight("", 3, ' ')).isEqualTo("   ");
	}

	@Test
	public void shouldRightPadUnicodeStringsCorrectly() {
		assertThat(BufferTools.padStringRight("\u03B3\u03B5\u03B9\u03AC", 3, ' ')).isEqualTo("\u03B3\u03B5\u03B9\u03AC");
		assertThat(BufferTools.padStringRight("\u03B3\u03B5\u03B9", 3, ' ')).isEqualTo("\u03B3\u03B5\u03B9");
		assertThat(BufferTools.padStringRight("\u03B3\u03B5", 3, ' ')).isEqualTo("\u03B3\u03B5 ");
		assertThat(BufferTools.padStringRight("\u03B3", 3, ' ')).isEqualTo("\u03B3  ");
	}

	@Test
	public void shouldPadRightWithNullCharacters() {
		assertThat(BufferTools.padStringRight("123", 3, '\00')).isEqualTo("123");
		assertThat(BufferTools.padStringRight("12", 3, '\00')).isEqualTo("12\00");
		assertThat(BufferTools.padStringRight("1", 3, '\00')).isEqualTo("1\00\00");
		assertThat(BufferTools.padStringRight("", 3, '\00')).isEqualTo("\00\00\00");
	}

	@Test
	public void shouldExtractBitsCorrectly() {
		byte b = -0x36; // 11001010
		assertThat(BufferTools.checkBit(b, 0)).isFalse();
		assertThat(BufferTools.checkBit(b, 1)).isTrue();
		assertThat(BufferTools.checkBit(b, 2)).isFalse();
		assertThat(BufferTools.checkBit(b, 3)).isTrue();
		assertThat(BufferTools.checkBit(b, 4)).isFalse();
		assertThat(BufferTools.checkBit(b, 5)).isFalse();
		assertThat(BufferTools.checkBit(b, 6)).isTrue();
		assertThat(BufferTools.checkBit(b, 7)).isTrue();
	}

	@Test
	public void shouldSetBitsInBytesCorrectly() {
		byte b = -0x36; // 11001010
		assertThat(BufferTools.setBit(b, 7, true)).isEqualTo(-0x36); // 11010010
		assertThat(BufferTools.setBit(b, 0, true)).isEqualTo(-0x35); // 11001011
		assertThat(BufferTools.setBit(b, 4, true)).isEqualTo(-0x26); // 11011010
		assertThat(BufferTools.setBit(b, 0, false)).isEqualTo(-0x36); // 11010010
		assertThat(BufferTools.setBit(b, 7, false)).isEqualTo(0x4A); // 01001010
		assertThat(BufferTools.setBit(b, 3, false)).isEqualTo(-0x3E); // 11000010
	}

	@Test
	public void shouldUnpackIntegerCorrectly() {
		assertThat(BufferTools.unpackInteger(BYTE_FF, BYTE_FB, BYTE_90, BYTE_44)).isEqualTo(0xFFFB9044);
		assertThat(BufferTools.unpackInteger((byte) 0, (byte) 0, (byte) 0, BYTE_81)).isEqualTo(0x00000081);
		assertThat(BufferTools.unpackInteger((byte) 0, (byte) 0, (byte) 1, (byte) 1)).isEqualTo(0x00000101);
	}

	@Test
	public void shouldUnpackSynchsafeIntegersCorrectly() {
		assertThat(BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x09, (byte) 0x41)).isEqualTo(1217);
		assertThat(BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x09, (byte) 0x4B)).isEqualTo(1227);
		assertThat(BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x07, (byte) 0x6A)).isEqualTo(1002);
		assertThat(BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 2, (byte) 1)).isEqualTo(0x0101);
		assertThat(BufferTools.unpackSynchsafeInteger((byte) 8, (byte) 4, (byte) 2, (byte) 1)).isEqualTo(0x01010101);
	}

	@Test
	public void shouldPackIntegerCorrectly() {
		assertThat(BufferTools.packInteger(0xFFFB9044)).isEqualTo(new byte[]{BYTE_FF, BYTE_FB, BYTE_90, BYTE_44});
	}

	@Test
	public void shouldPackSynchsafeIntegersCorrectly() {
		assertThat(BufferTools.packSynchsafeInteger(1217)).isEqualTo(new byte[]{(byte) 0, (byte) 0, (byte) 0x09, (byte) 0x41});
		assertThat(BufferTools.packSynchsafeInteger(1227)).isEqualTo(new byte[]{(byte) 0, (byte) 0, (byte) 0x09, (byte) 0x4B});
		assertThat(BufferTools.packSynchsafeInteger(1002)).isEqualTo(new byte[]{(byte) 0, (byte) 0, (byte) 0x07, (byte) 0x6A});
		assertThat(BufferTools.packSynchsafeInteger(0x0101)).isEqualTo(new byte[]{(byte) 0, (byte) 0, (byte) 2, (byte) 1});
		assertThat(BufferTools.packSynchsafeInteger(0x01010101)).isEqualTo(new byte[]{(byte) 8, (byte) 4, (byte) 2, (byte) 1});
	}

	@Test
	public void shouldPackAndUnpackIntegerBackToOriginalValue() {
		int original = 12345;
		byte[] bytes = BufferTools.packInteger(original);
		int unpacked = BufferTools.unpackInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertThat(unpacked).isEqualTo(original);
	}

	@Test
	public void shouldPackAndUnpackSynchsafeIntegerBackToOriginalValue() {
		int original = 12345;
		byte[] bytes = BufferTools.packSynchsafeInteger(original);
		int unpacked = BufferTools.unpackSynchsafeInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertThat(unpacked).isEqualTo(original);
	}

	@Test
	public void shouldCopyBuffersWithValidOffsetsAndLengths() {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		assertThat(BufferTools.copyBuffer(buffer, 0, buffer.length)).isEqualTo(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
		assertThat(BufferTools.copyBuffer(buffer, 1, buffer.length - 1)).isEqualTo(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
		assertThat(BufferTools.copyBuffer(buffer, 0, buffer.length - 1)).isEqualTo(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
		assertThat(BufferTools.copyBuffer(buffer, 1, buffer.length - 2)).isEqualTo(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
		assertThat(BufferTools.copyBuffer(buffer, 4, 1)).isEqualTo(new byte[]{4});
	}

	@ParameterizedTest
	@MethodSource("createDataForThrowsExceptionWhenCopyingBufferWithInvalidOffsetAndOrLength")
	public void throwsExceptionWhenCopyingBufferWithInvalidOffsetAndOrLength(int offset, int length) {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

		assertThrows(ArrayIndexOutOfBoundsException.class, () -> BufferTools.copyBuffer(buffer, offset, length));
	}

	static Stream<Arguments> createDataForThrowsExceptionWhenCopyingBufferWithInvalidOffsetAndOrLength() {
		return Stream.of(
				Arguments.of(-1, 10),
				Arguments.of(10, 1),
				Arguments.of(1, 10)
		);
	}

	@Test
	public void shouldDetermineUnsynchronisationSizesCorrectly() {
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{})).isEqualTo(0);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, 1, BYTE_FB})).isEqualTo(0);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FB})).isEqualTo(1);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{0, BYTE_FF, BYTE_FB, 0})).isEqualTo(1);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{0, BYTE_FF})).isEqualTo(1);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FB, 0, BYTE_FF, BYTE_FB})).isEqualTo(2);
		assertThat(BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FF, BYTE_FF})).isEqualTo(3);
	}

	@Test
	public void shouldDetermineSynchronisationSizesCorrectly() {
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{})).isEqualTo(0);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 1, BYTE_FB})).isEqualTo(0);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FB})).isEqualTo(1);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{0, BYTE_FF, 0, BYTE_FB, 0})).isEqualTo(1);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{0, BYTE_FF, 0})).isEqualTo(1);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FB, 0, BYTE_FF, 0, BYTE_FB})).isEqualTo(2);
		assertThat(BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0})).isEqualTo(3);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseFFExBytesCorrectly() {
		byte[] buffer = {BYTE_FF, BYTE_FB, 2, 3, 4, BYTE_FF, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, BYTE_F0};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FB, 2, 3, 4, BYTE_FF, 0, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, BYTE_F0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertThat(unsynchronised).isEqualTo(expectedBuffer);
		assertThat(synchronised).isEqualTo(buffer);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseFF00BytesCorrectly() {
		byte[] buffer = {BYTE_FF, 0, 2, 3, 4, BYTE_FF, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0};
		byte[] expectedBuffer = {BYTE_FF, 0, 0, 2, 3, 4, BYTE_FF, 0, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertThat(unsynchronised).isEqualTo(expectedBuffer);
		assertThat(synchronised).isEqualTo(buffer);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseBufferFullOfFFsCorrectly() {
		byte[] buffer = {BYTE_FF, BYTE_FF, BYTE_FF, BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertThat(unsynchronised).isEqualTo(expectedBuffer);
		assertThat(synchronised).isEqualTo(buffer);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseBufferMinimalBufferCorrectly() {
		byte[] buffer = {BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertThat(unsynchronised).isEqualTo(expectedBuffer);
		assertThat(synchronised).isEqualTo(buffer);
	}

	@Test
	public void shouldReturnOriginalBufferIfNoUnynchronisationOrSynchronisationIsRequired() {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(buffer);
		assertThat(unsynchronised).isEqualTo(buffer);
		assertThat(synchronised).isEqualTo(buffer);
	}

	@Test
	public void shouldReplaceTokensWithSpecifiedStrings() {
		String source = "%1-%2 something %1-%3";
		assertThat(BufferTools.substitute(source, "%1", "ONE")).isEqualTo("ONE-%2 something ONE-%3");
		assertThat(BufferTools.substitute(source, "%2", "TWO")).isEqualTo("%1-TWO something %1-%3");
		assertThat(BufferTools.substitute(source, "%3", "THREE")).isEqualTo("%1-%2 something %1-THREE");
	}

	@Test
	public void shouldReturnOriginalStringIfTokenToSubstituteDoesNotExistInString() {
		String source = "%1-%2 something %1-%3";
		assertThat(BufferTools.substitute(source, "%X", "XXXXX")).isEqualTo("%1-%2 something %1-%3");
	}

	@Test
	public void shouldReturnOriginalStringForSubstitutionWithEmptyString() {
		String source = "%1-%2 something %1-%3";
		assertThat(BufferTools.substitute(source, "", "WHATEVER")).isEqualTo("%1-%2 something %1-%3");
	}

	@Test
	public void shouldSubstituteEmptyStringWhenDestinationStringIsNull() {
		String source = "%1-%2 something %1-%3";
		assertThat(BufferTools.substitute(source, "%1", null)).isEqualTo("-%2 something -%3");
	}

	@Test
	public void shouldConvertNonAsciiCharactersToQuestionMarksInString() {
		assertThat(BufferTools.asciiOnly("ü12¬34ü567¬¬¬89ü")).isEqualTo("?12?34?567???89?");
	}

	@Test
	public void convertsBufferContainingHighAscii() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G};
		assertThat(BufferTools.byteBufferToString(buffer, 0, 3)).isEqualTo("T" + (char) (223) + "G");
	}

	// finding terminators

	@Test
	public void findsSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 1)).isEqualTo(4);
	}

	@Test
	public void findsFirstSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 1)).isEqualTo(4);
	}

	@Test
	public void findsFirstSingleTerminatorAfterFromIndex() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 5, 1)).isEqualTo(7);
	}

	@Test
	public void findsSingleTerminatorWhenFirstElement() {
		byte[] buffer = {0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 1)).isEqualTo(0);
	}

	@Test
	public void findsSingleTerminatorWhenLastElement() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 1)).isEqualTo(4);
	}

	@Test
	public void ReturnsMinusOneWhenNoSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 1)).isEqualTo(-1);
	}

	@Test
	public void findsDoubleTerminator() {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(4);
	}

	@Test
	public void findsNotFindDoubleTerminatorIfNotOnEvenByte() {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(-1);
	}

	@Test
	public void findsFirstDoubleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(4);
	}

	@Test
	public void findsFirstDoubleTerminatorOnAnEvenByte() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, 0, 0, BYTE_T, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(8);
	}

	@Test
	public void findsFirstDoubleTerminatorAfterFromIndex() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertThat(BufferTools.indexOfTerminator(buffer, 6, 2)).isEqualTo(8);
	}

	@Test
	public void findsDoubleTerminatorWhenFirstElement() {
		byte[] buffer = {0, 0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(0);
	}

	@Test
	public void findsDoubleTerminatorWhenLastElement() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(4);
	}

	@Test
	public void returnsMinusOneWhenNoDoubleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertThat(BufferTools.indexOfTerminator(buffer, 0, 2)).isEqualTo(-1);
	}
}
