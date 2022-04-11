package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MpegFrameTest {

	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	private static final byte BYTE_F9 = -0x07;
	private static final byte BYTE_F3 = -0x0D;
	private static final byte BYTE_F2 = -0x0E;
	private static final byte BYTE_A2 = -0x5E;
	private static final byte BYTE_AE = -0x52;
	private static final byte BYTE_DB = -0x25;
	private static final byte BYTE_EB = -0x15;
	private static final byte BYTE_40 = 0x40;
	private static final byte BYTE_02 = 0x02;

	@Test
	void testBitwiseLeftShiftOperationsOnLong() {
		long original = 0xFFFFFFFE; // 1111 1111 1111 1111 1111 1111 1111 1110
		long expectedShl1 = 0xFFFFFFFC; // 1111 1111 1111 1111 1111 1111 1111 1100
		long expectedShl28 = 0xE0000000; // 1110 0000 0000 0000 0000 0000 0000 0000
		long expectedShl30 = 0x80000000; // 1000 0000 0000 0000 0000 0000 0000 0000
		assertThat(original << 1).isEqualTo(expectedShl1);
		assertThat(original << 28).isEqualTo(expectedShl28);
		assertThat(original << 30).isEqualTo(expectedShl30);
	}

	@Test
	void testBitwiseRightShiftOperationsOnLong() {
		long original = 0x80000000; // 1000 0000 0000 0000 0000 0000 0000 0000
		long expectedShr1 = 0xC0000000; // 1100 0000 0000 0000 0000 0000 0000 0000
		long expectedShr28 = 0xFFFFFFF8; // 1111 1111 1111 1111 1111 1111 1111 1000
		long expectedShr30 = 0xFFFFFFFE; // 1111 1111 1111 1111 1111 1111 1111 1110
		assertThat(original >> 1).isEqualTo(expectedShr1);
		assertThat(original >> 28).isEqualTo(expectedShr28);
		assertThat(original >> 30).isEqualTo(expectedShr30);
	}

	@Test
	void testShiftingByteIntoBiggerNumber() {
		byte original = -0x02; // 1111 1110
		long originalAsLong = (original & 0xFF);
		byte expectedShl1 = -0x04; // 1111 1100
		long expectedShl8 = 0x0000FE00; // 0000 0000 0000 0000 1111 1110 0000 0000
		long expectedShl16 = 0x00FE0000; // 0000 0000 1111 1110 0000 0000 0000 0000
		long expectedShl23 = 0x7F000000; // 0111 1111 00000 0000 0000 0000 0000 0000
		assertThat(original << 1).isEqualTo(expectedShl1);
		assertThat(originalAsLong).isEqualTo(254);
		assertThat(originalAsLong << 8).isEqualTo(expectedShl8);
		assertThat(originalAsLong << 16).isEqualTo(expectedShl16);
		assertThat(originalAsLong << 23).isEqualTo(expectedShl23);
	}

	@Test
	void shouldExtractValidFields() {
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting();
		assertThat(mpegFrame.extractField(0xFFE00000, 0xFFE00000L)).isEqualTo(0x000007FF);
		assertThat(mpegFrame.extractField(0xFFEFFFFF, 0xFFE00000L)).isEqualTo(0x000007FF);
		assertThat(mpegFrame.extractField(0x11111155, 0x000000FFL)).isEqualTo(0x00000055);
		assertThat(mpegFrame.extractField(0xFFEFFF55, 0x000000FFL)).isEqualTo(0x00000055);
	}

	@Test
	void shouldExtractValidMpegVersion1Header() throws InvalidDataException {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_A2, BYTE_40};
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting(frameData);
		assertThat(mpegFrame.getVersion()).isEqualTo(MpegFrame.MPEG_VERSION_1_0);
		assertThat(mpegFrame.getLayer()).isEqualTo(MpegFrame.MPEG_LAYER_3);
		assertThat(mpegFrame.getBitrate()).isEqualTo(160);
		assertThat(mpegFrame.getSampleRate()).isEqualTo(44100);
		assertThat(mpegFrame.getChannelMode()).isEqualTo(MpegFrame.CHANNEL_MODE_JOINT_STEREO);
		assertThat(mpegFrame.getModeExtension()).isEqualTo("None");
		assertThat(mpegFrame.getEmphasis()).isEqualTo("None");
		assertThat(mpegFrame.isProtection()).isEqualTo(true);
		assertThat(mpegFrame.hasPadding()).isEqualTo(true);
		assertThat(mpegFrame.isPrivate()).isEqualTo(false);
		assertThat(mpegFrame.isCopyright()).isEqualTo(false);
		assertThat(mpegFrame.isOriginal()).isEqualTo(false);
		assertThat(mpegFrame.getLengthInBytes()).isEqualTo(523);
	}

	@Test
	void shouldProcessValidMpegVersion2Header() throws InvalidDataException {
		byte[] frameData = {BYTE_FF, BYTE_F3, BYTE_A2, BYTE_40};
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting(frameData);
		assertThat(mpegFrame.getVersion()).isEqualTo(MpegFrame.MPEG_VERSION_2_0);
		assertThat(mpegFrame.getLayer()).isEqualTo(MpegFrame.MPEG_LAYER_3);
		assertThat(mpegFrame.getBitrate()).isEqualTo(96);
		assertThat(mpegFrame.getSampleRate()).isEqualTo(22050);
		assertThat(mpegFrame.getChannelMode()).isEqualTo(MpegFrame.CHANNEL_MODE_JOINT_STEREO);
		assertThat(mpegFrame.getModeExtension()).isEqualTo("None");
		assertThat(mpegFrame.getEmphasis()).isEqualTo("None");
		assertThat(mpegFrame.isProtection()).isEqualTo(true);
		assertThat(mpegFrame.hasPadding()).isEqualTo(true);
		assertThat(mpegFrame.isPrivate()).isEqualTo(false);
		assertThat(mpegFrame.isCopyright()).isEqualTo(false);
		assertThat(mpegFrame.isOriginal()).isEqualTo(false);
		assertThat(mpegFrame.getLengthInBytes()).isEqualTo(627);
	}

	@Test
	void shouldThrowExceptionForInvalidFrameSync() {
		byte[] frameData = {BYTE_FF, BYTE_DB, BYTE_A2, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	@Test
	void shouldThrowExceptionForInvalidMpegVersion() {
		byte[] frameData = {BYTE_FF, BYTE_EB, BYTE_A2, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	@Test
	void shouldThrowExceptionForInvalidMpegLayer() {
		byte[] frameData = {BYTE_FF, BYTE_F9, BYTE_A2, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	@Test
	void shouldThrowExceptionForFreeBitrate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_02, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	@Test
	void shouldThrowExceptionForInvalidBitrate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_F2, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	@Test
	void shouldThrowExceptionForInvalidSampleRate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_AE, BYTE_40};
		assertThrows(InvalidDataException.class, () -> new MpegFrameForTesting(frameData));
	}

	static class MpegFrameForTesting extends MpegFrame {

		MpegFrameForTesting() {
			super();
		}


		MpegFrameForTesting(byte[] frameData) throws InvalidDataException {
			super(frameData);
		}

	}
}
