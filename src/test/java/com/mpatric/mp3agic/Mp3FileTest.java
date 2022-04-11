package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Mp3FileTest {

	private static final String fs = File.separator;
	private static final String MP3_WITH_NO_TAGS = "src" + fs + "test" + fs + "resources" + fs + "notags.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v1andv23tags.mp3";
	private static final String MP3_WITH_DUMMY_START_AND_END_FRAMES = "src" + fs + "test" + fs + "resources" + fs + "dummyframes.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v1andv23andcustomtags.mp3";
	private static final String MP3_WITH_ID3V23_UNICODE_TAGS = "src" + fs + "test" + fs + "resources" + fs + "v23unicodetags.mp3";
	private static final String NOT_AN_MP3 = "src" + fs + "test" + fs + "resources" + fs + "notanmp3.mp3";
	private static final String MP3_WITH_INCOMPLETE_MPEG_FRAME = "src" + fs + "test" + fs + "resources" + fs + "incompletempegframe.mp3";

	@TempDir
	private Path tempDir;

	@Test
	void shouldLoadMp3WithNoTags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 41);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 256);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 1024);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 5000);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 41);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 256);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 1024);
		loadAndCheckTestMp3WithNoTags(new File(MP3_WITH_NO_TAGS), 5000);
	}

	@Test
	void shouldLoadMp3WithId3Tags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 5000);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 41);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 256);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 1024);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS), 5000);
	}

	@Test
	void shouldLoadMp3WithFakeStartAndEndFrames() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 5000);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 41);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 256);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 1024);
		loadAndCheckTestMp3WithTags(new File(MP3_WITH_DUMMY_START_AND_END_FRAMES), 5000);
	}

	@Test
	void shouldLoadMp3WithCustomTag() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 41);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 256);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 1024);
		loadAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 5000);
	}

	@Test
	void shouldThrowExceptionForFileThatIsNotAnMp3() throws Exception {
		InvalidDataException exception = assertThrows(InvalidDataException.class, () -> new Mp3File(NOT_AN_MP3));
		assertThat(exception).extracting(Exception::getMessage)
				.isEqualTo("No mpegs frames found");
	}

	@Test
	void shouldThrowExceptionForFileThatIsNotAnMp3ForFileConstructor() {
			InvalidDataException exception = assertThrows(InvalidDataException.class, () -> new Mp3File(new File(NOT_AN_MP3)));
			assertThat(exception).extracting(Exception::getMessage)
							.isEqualTo("No mpegs frames found");
	}

	@Test
	void shouldFindProbableStartOfMpegFramesWithPrescan() throws IOException {
		Mp3FileForTesting mp3File = new Mp3FileForTesting(MP3_WITH_ID3V1_AND_ID3V23_TAGS);
		testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
	}

	@Test
	void shouldFindProbableStartOfMpegFramesWithPrescanForFileConstructor() throws IOException {
		Mp3FileForTesting mp3File = new Mp3FileForTesting(new File(MP3_WITH_ID3V1_AND_ID3V23_TAGS));
		testShouldFindProbableStartOfMpegFramesWithPrescan(mp3File);
	}

	private void testShouldFindProbableStartOfMpegFramesWithPrescan(Mp3FileForTesting mp3File) {
		assertThat(mp3File.preScanResult).isEqualTo(0x44B);
	}

	@Test
	void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile() throws Exception {
		Mp3File mp3File = new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3File);
	}

	@Test
	void shouldThrowExceptionIfSavingMp3WithSameNameAsSourceFileForFileConstructor() throws Exception {
		Mp3File mp3File = new Mp3File(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
		testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(mp3File);
	}

	private void testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile(Mp3File mp3File) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mp3File.save(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
		assertThat(exception).extracting(Exception::getMessage)
				.isEqualTo("Save filename same as source filename");
	}

	@Test
	void shouldSaveLoadedMp3WhichIsEquivalentToOriginal() throws Exception {
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 41);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 256);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 1024);
		copyAndCheckTestMp3WithCustomTag(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS), 5000);
	}

	@Test
	void shouldLoadAndCheckMp3ContainingUnicodeFields() throws Exception {
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
		loadAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 41);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 256);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 1024);
		loadAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 5000);
	}

	@Test
	void shouldSaveLoadedMp3WithUnicodeFieldsWhichIsEquivalentToOriginal() throws Exception {
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 41);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 256);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 1024);
		copyAndCheckTestMp3WithUnicodeFields(MP3_WITH_ID3V23_UNICODE_TAGS, 5000);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 41);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 256);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 1024);
		copyAndCheckTestMp3WithUnicodeFields(new File(MP3_WITH_ID3V23_UNICODE_TAGS), 5000);
	}

	@Test
	void shouldIgnoreIncompleteMpegFrame() throws Exception {
		Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256);
		testShouldIgnoreIncompleteMpegFrame(mp3File);
	}

	@Test
	void shouldIgnoreIncompleteMpegFrameForFileConstructor() throws Exception {
		Mp3File mp3File = new Mp3File(new File(MP3_WITH_INCOMPLETE_MPEG_FRAME), 256);
		testShouldIgnoreIncompleteMpegFrame(mp3File);
	}

	private void testShouldIgnoreIncompleteMpegFrame(Mp3File mp3File) throws Exception {
		assertThat(mp3File.getXingOffset()).isEqualTo(0x44B);
		assertThat(mp3File.getStartOffset()).isEqualTo(0x5EC);
		assertThat(mp3File.getEndOffset()).isEqualTo(0xF17);
		assertThat(mp3File.hasId3v1Tag()).isTrue();
		assertThat(mp3File.hasId3v2Tag()).isTrue();
		assertThat(mp3File.getFrameCount()).isEqualTo(5);
	}

	@Test
	void shouldInitialiseProperlyWhenNotScanningFile() throws Exception {
		Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256, false);
		testShouldInitialiseProperlyWhenNotScanningFile(mp3File);
	}

	@Test
	void shouldInitialiseProperlyWhenNotScanningFileForFileConstructor() throws Exception {
		Mp3File mp3File = new Mp3File(new File(MP3_WITH_INCOMPLETE_MPEG_FRAME), 256, false);
		testShouldInitialiseProperlyWhenNotScanningFile(mp3File);
	}

	private void testShouldInitialiseProperlyWhenNotScanningFile(Mp3File mp3File) throws Exception {
		assertThat(mp3File.hasId3v1Tag()).isTrue();
		assertThat(mp3File.hasId3v2Tag()).isTrue();
	}

	@Test
	void shouldRemoveId3v1Tag() throws Exception {
		testShouldRemoveId3v1Tag(new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
	}

	@Test
	void shouldRemoveId3v1TagForFileConstructor() throws Exception {
		testShouldRemoveId3v1Tag(new Mp3File(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS)));
	}

	private void testShouldRemoveId3v1Tag(Mp3File mp3File) throws Exception {
		String saveFilename = mp3File.getFilename() + ".copy";
		try {
			mp3File.removeId3v1Tag();
			mp3File.save(saveFilename);
			Mp3File newMp3File = new Mp3File(saveFilename);
			assertThat(newMp3File.hasId3v1Tag()).isFalse();
			assertThat(newMp3File.hasId3v2Tag()).isTrue();
			assertThat(newMp3File.hasCustomTag()).isTrue();
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	@Test
	void shouldRemoveId3v2Tag() throws Exception {
		testShouldRemoveId3v2Tag(new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
	}

	@Test
	void shouldRemoveId3v2TagForFileConstructor() throws Exception {
		testShouldRemoveId3v2Tag(new Mp3File(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS)));
	}

	private void testShouldRemoveId3v2Tag(Mp3File mp3File) throws Exception {
		String saveFilename = mp3File.getFilename() + ".copy";
		try {
			mp3File.removeId3v2Tag();
			mp3File.save(saveFilename);
			Mp3File newMp3File = new Mp3File(saveFilename);
			assertThat(newMp3File.hasId3v1Tag()).isTrue();
			assertThat(newMp3File.hasId3v2Tag()).isFalse();
			assertThat(newMp3File.hasCustomTag()).isTrue();
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	@Test
	void shouldRemoveCustomTag() throws Exception {
		testShouldRemoveCustomTag(new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
	}

	@Test
	void shouldRemoveCustomTagForFileConstructor() throws Exception {
		testShouldRemoveCustomTag(new Mp3File(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS)));
	}

	private void testShouldRemoveCustomTag(Mp3File mp3File) throws Exception {
		String saveFilename = tempDir.resolve(mp3File.getFilename()).toString();
		mp3File.removeCustomTag();
		mp3File.save(saveFilename);
		Mp3File newMp3File = new Mp3File(saveFilename);
		assertThat(newMp3File.hasId3v1Tag()).isTrue();
		assertThat(newMp3File.hasId3v2Tag()).isTrue();
		assertThat(newMp3File.hasCustomTag()).isFalse();
	}

	@Test
	void shouldRemoveId3v1AndId3v2AndCustomTags() throws Exception {
		testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS));
	}

	@Test
	void shouldRemoveId3v1AndId3v2AndCustomTagsForFileConstructor() throws Exception {
		testShouldRemoveId3v1AndId3v2AndCustomTags(new Mp3File(new File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS)));
	}

	private void testShouldRemoveId3v1AndId3v2AndCustomTags(Mp3File mp3File) throws Exception {
		String saveFilename = tempDir.resolve(mp3File.getFilename()).toString();
		mp3File.removeId3v1Tag();
		mp3File.removeId3v2Tag();
		mp3File.removeCustomTag();
		mp3File.save(saveFilename);
		Mp3File newMp3File = new Mp3File(saveFilename);
		assertThat(newMp3File.hasId3v1Tag()).isFalse();
		assertThat(newMp3File.hasId3v2Tag()).isFalse();
		assertThat(newMp3File.hasCustomTag()).isFalse();
	}

	private Mp3File copyAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3File mp3File = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
		return copyAndCheckTestMp3WithCustomTag(mp3File);
	}

	private Mp3File copyAndCheckTestMp3WithCustomTag(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3File mp3File = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
		return copyAndCheckTestMp3WithCustomTag(mp3File);
	}

	private Mp3File copyAndCheckTestMp3WithCustomTag(Mp3File mp3File) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
		String saveFilename = mp3File.getFilename() + ".copy";
		try {
			mp3File.save(saveFilename);
			Mp3File copyMp3file = loadAndCheckTestMp3WithCustomTag(saveFilename, 5000);
			assertThat(copyMp3file.getId3v1Tag()).isEqualTo(mp3File.getId3v1Tag());
			assertThat(copyMp3file.getId3v2Tag()).isEqualTo(mp3File.getId3v2Tag());
			assertThat(copyMp3file.getCustomTag()).isEqualTo(mp3File.getCustomTag());
			return copyMp3file;
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	private Mp3File copyAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3File mp3File = loadAndCheckTestMp3WithUnicodeFields(filename, bufferLength);
		return copyAndCheckTestMp3WithUnicodeFields(mp3File);
	}

	private Mp3File copyAndCheckTestMp3WithUnicodeFields(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		Mp3File mp3File = loadAndCheckTestMp3WithUnicodeFields(filename, bufferLength);
		return copyAndCheckTestMp3WithUnicodeFields(mp3File);
	}

	private Mp3File copyAndCheckTestMp3WithUnicodeFields(Mp3File mp3File) throws NotSupportedException, IOException, UnsupportedTagException, InvalidDataException {
		String saveFilename = mp3File.getFilename() + ".copy";
		try {
			mp3File.save(saveFilename);
			Mp3File copyMp3file = loadAndCheckTestMp3WithUnicodeFields(saveFilename, 5000);
			assertThat(copyMp3file.getId3v2Tag()).isEqualTo(mp3File.getId3v2Tag());
			return copyMp3file;
		} finally {
			TestHelper.deleteFile(saveFilename);
		}
	}

	private Mp3File loadAndCheckTestMp3WithNoTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithNoTags(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithNoTags(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithNoTags(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithNoTags(Mp3File mp3File) {
		assertThat(mp3File.getXingOffset()).isEqualTo(0x000);
		assertThat(mp3File.getStartOffset()).isEqualTo(0x1A1);
		assertThat(mp3File.getEndOffset()).isEqualTo(0xB34);
		assertThat(mp3File.hasId3v1Tag()).isFalse();
		assertThat(mp3File.hasId3v2Tag()).isFalse();
		assertThat(mp3File.hasCustomTag()).isFalse();
		return mp3File;
	}

	private Mp3File loadAndCheckTestMp3WithTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithTags(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithTags(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithTags(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithTags(Mp3File mp3File) {
		assertThat(mp3File.getXingOffset()).isEqualTo(0x44B);
		assertThat(mp3File.getStartOffset()).isEqualTo(0x5EC);
		assertThat(mp3File.getEndOffset()).isEqualTo(0xF7F);
		assertThat(mp3File.hasId3v1Tag()).isTrue();
		assertThat(mp3File.hasId3v2Tag()).isTrue();
		assertThat(mp3File.hasCustomTag()).isFalse();
		return mp3File;
	}

	private Mp3File loadAndCheckTestMp3WithUnicodeFields(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithUnicodeFields(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithUnicodeFields(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithUnicodeFields(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithUnicodeFields(Mp3File mp3File) {
		assertThat(mp3File.getXingOffset()).isEqualTo(0x0CA);
		assertThat(mp3File.getStartOffset()).isEqualTo(0x26B);
		assertThat(mp3File.getEndOffset()).isEqualTo(0xBFE);
		assertThat(mp3File.hasId3v1Tag()).isFalse();
		assertThat(mp3File.hasId3v2Tag()).isTrue();
		assertThat(mp3File.hasCustomTag()).isFalse();
		return mp3File;
	}

	private Mp3File loadAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithCustomTag(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithCustomTag(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		return loadAndCheckTestMp3WithCustomTag(mp3File);
	}

	private Mp3File loadAndCheckTestMp3WithCustomTag(Mp3File mp3File) {
		assertThat(mp3File.getXingOffset()).isEqualTo(0x44B);
		assertThat(mp3File.getStartOffset()).isEqualTo(0x5EC);
		assertThat(mp3File.getEndOffset()).isEqualTo(0xF7F);
		assertThat(mp3File.hasId3v1Tag()).isTrue();
		assertThat(mp3File.hasId3v2Tag()).isTrue();
		assertThat(mp3File.hasCustomTag()).isTrue();
		return mp3File;
	}

	private Mp3File loadAndCheckTestMp3(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = new Mp3File(filename, bufferLength);
		return loadAndCheckTestMp3(mp3File);
	}

	private Mp3File loadAndCheckTestMp3(File filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = new Mp3File(filename, bufferLength);
		return loadAndCheckTestMp3(mp3File);
	}

	private Mp3File loadAndCheckTestMp3(Mp3File mp3File) {
		assertThat(mp3File.hasXingFrame()).isTrue();
		assertThat(mp3File.getFrameCount()).isEqualTo(6);
		assertThat(mp3File.getVersion()).isEqualTo(MpegFrame.MPEG_VERSION_1_0);
		assertThat(mp3File.getLayer()).isEqualTo(MpegFrame.MPEG_LAYER_3);
		assertThat(mp3File.getSampleRate()).isEqualTo(44100);
		assertThat(mp3File.getChannelMode()).isEqualTo(MpegFrame.CHANNEL_MODE_JOINT_STEREO);
		assertThat(mp3File.getEmphasis()).isEqualTo(MpegFrame.EMPHASIS_NONE);
		assertThat(mp3File.isOriginal()).isTrue();
		assertThat(mp3File.isCopyright()).isFalse();
		assertThat(mp3File.getXingBitrate()).isEqualTo(128);
		assertThat(mp3File.getBitrate()).isEqualTo(125);
		assertThat((mp3File.getBitrates().get(224)).getValue()).isEqualTo(1);
		assertThat((mp3File.getBitrates().get(112)).getValue()).isEqualTo(1);
		assertThat((mp3File.getBitrates().get(96)).getValue()).isEqualTo(2);
		assertThat((mp3File.getBitrates().get(192)).getValue()).isEqualTo(1);
		assertThat((mp3File.getBitrates().get(32)).getValue()).isEqualTo(1);
		assertThat(mp3File.getLengthInMilliseconds()).isEqualTo(156);
		return mp3File;
	}

	private static class Mp3FileForTesting extends Mp3File {

		int preScanResult;

		Mp3FileForTesting(String filename) throws IOException {
			SeekableByteChannel file = Files.newByteChannel(Paths.get(filename), StandardOpenOption.READ);
			preScanResult = preScanFile(file);
		}

		Mp3FileForTesting(File filename) throws IOException {
			SeekableByteChannel file = Files.newByteChannel(filename.toPath(), StandardOpenOption.READ);
			preScanResult = preScanFile(file);
		}
	}
}
