package com.mpatric.mp3agic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ID3v2TagTest {

	private static final byte BYTE_I = 0x49;
	private static final byte BYTE_D = 0x44;
	private static final byte BYTE_3 = 0x33;
	private static final byte[] ID3V2_HEADER = {BYTE_I, BYTE_D, BYTE_3, 4, 0, 0, 0, 0, 2, 1};

	@Test
	void shouldInitialiseFromHeaderBlockWithValidHeaders() throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		header[3] = 2;
		header[4] = 0;
		ID3v2 id3v2tag;
		id3v2tag = createTag(header);
		assertThat(id3v2tag.getVersion()).isEqualTo("2.0");
		header[3] = 3;
		id3v2tag = createTag(header);
		assertThat(id3v2tag.getVersion()).isEqualTo("3.0");
		header[3] = 4;
		id3v2tag = createTag(header);
		assertThat(id3v2tag.getVersion()).isEqualTo("4.0");
	}

	@Test
	void shouldCalculateCorrectDataLengthsFromHeaderBlock() throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		ID3v2 id3v2tag = createTag(header);
		assertThat(id3v2tag.getDataLength()).isEqualTo(257);
		header[8] = 0x09;
		header[9] = 0x41;
		id3v2tag = createTag(header);
		assertThat(id3v2tag.getDataLength()).isEqualTo(1217);
	}

	@Test
	void shouldThrowExceptionForNonSupportedVersionInId3v2HeaderBlock() throws NoSuchTagException, InvalidDataException {
		byte[] header = BufferTools.copyBuffer(ID3V2_HEADER, 0, ID3V2_HEADER.length);
		header[3] = 5;
		header[4] = 0;
			assertThrows(UnsupportedTagException.class, () -> ID3v2TagFactory.createTag(header));
	}

	@Test
	void shouldSortId3TagsAlphabetically() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		Map<String, ID3v2FrameSet> frameSets = id3v2tag.getFrameSets();
		Iterator<ID3v2FrameSet> frameSetIterator = frameSets.values().iterator();
		String lastKey = "";
		while (frameSetIterator.hasNext()) {
			ID3v2FrameSet frameSet = frameSetIterator.next();
			assertThat(frameSet.getId().compareTo(lastKey)).isGreaterThan(0);
			lastKey = frameSet.getId();
		}
	}

	@Test
	void shouldReadFramesFromMp3With32Tag() throws IOException, NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3v2tag.getVersion()).isEqualTo("3.0");
		assertThat(id3v2tag.getLength()).isEqualTo(0x44B);
		assertThat(id3v2tag.getFrameSets().size()).isEqualTo(12);
		assertThat((id3v2tag.getFrameSets().get("TENC")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("WXXX")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TCOP")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TOPE")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TCOM")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("COMM")).getFrames().size()).isEqualTo(2);
		assertThat((id3v2tag.getFrameSets().get("TPE1")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TALB")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TRCK")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TYER")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TCON")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TIT2")).getFrames().size()).isEqualTo(1);
	}

	@Test
	void shouldReadId3v2WithFooter() throws IOException, NoSuchTagException, UnsupportedTagException, InvalidDataException {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv24tags.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3v2tag.getVersion()).isEqualTo("4.0");
		assertThat(id3v2tag.getLength()).isEqualTo(0x44B);
	}

	@Test
	void shouldReadTagFieldsFromMp3With24tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v24tagswithalbumimage.mp3");
		ID3v2 id3v24tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3v24tag.getVersion()).isEqualTo("4.0");
		assertThat(id3v24tag.getTrack()).isEqualTo("1");
		assertThat(id3v24tag.getArtist()).isEqualTo("ARTIST123456789012345678901234");
		assertThat(id3v24tag.getTitle()).isEqualTo("TITLE1234567890123456789012345");
		assertThat(id3v24tag.getAlbum()).isEqualTo("ALBUM1234567890123456789012345");
		assertThat(id3v24tag.getGenre()).isEqualTo(0x0d);
		assertThat(id3v24tag.getGenreDescription()).isEqualTo("Pop");
		assertThat(id3v24tag.getComment()).isEqualTo("COMMENT123456789012345678901");
		assertThat(id3v24tag.getLyrics()).isEqualTo("LYRICS1234567890123456789012345");
		assertThat(id3v24tag.getComposer()).isEqualTo("COMPOSER23456789012345678901234");
		assertThat(id3v24tag.getOriginalArtist()).isEqualTo("ORIGARTIST234567890123456789012");
		assertThat(id3v24tag.getCopyright()).isEqualTo("COPYRIGHT2345678901234567890123");
		assertThat(id3v24tag.getUrl()).isEqualTo("URL2345678901234567890123456789");
		assertThat(id3v24tag.getCommercialUrl()).isEqualTo("COMMERCIALURL234567890123456789");
		assertThat(id3v24tag.getCopyrightUrl()).isEqualTo("COPYRIGHTURL2345678901234567890");
		assertThat(id3v24tag.getArtistUrl()).isEqualTo("OFFICIALARTISTURL23456789012345");
		assertThat(id3v24tag.getAudiofileUrl()).isEqualTo("OFFICIALAUDIOFILE23456789012345");
		assertThat(id3v24tag.getAudioSourceUrl()).isEqualTo("OFFICIALAUDIOSOURCE234567890123");
		assertThat(id3v24tag.getRadiostationUrl()).isEqualTo("INTERNETRADIOSTATIONURL23456783");
		assertThat(id3v24tag.getPaymentUrl()).isEqualTo("PAYMENTURL234567890123456789012");
		assertThat(id3v24tag.getPublisherUrl()).isEqualTo("PUBLISHERURL2345678901234567890");
		assertThat(id3v24tag.getEncoder()).isEqualTo("ENCODER234567890123456789012345");
		assertThat(id3v24tag.getAlbumImage().length).isEqualTo(1885);
		assertThat(id3v24tag.getAlbumImageMimeType()).isEqualTo("image/png");
	}

	@Test
	void shouldReadTagFieldsFromMp3With32tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getTrack()).isEqualTo("1");
		assertThat(id3tag.getArtist()).isEqualTo("ARTIST123456789012345678901234");
		assertThat(id3tag.getTitle()).isEqualTo("TITLE1234567890123456789012345");
		assertThat(id3tag.getAlbum()).isEqualTo("ALBUM1234567890123456789012345");
		assertThat(id3tag.getYear()).isEqualTo("2001");
		assertThat(id3tag.getGenre()).isEqualTo(0x0d);
		assertThat(id3tag.getGenreDescription()).isEqualTo("Pop");
		assertThat(id3tag.getComment()).isEqualTo("COMMENT123456789012345678901");
		assertThat(id3tag.getLyrics()).isEqualTo("LYRICS1234567890123456789012345");
		assertThat(id3tag.getComposer()).isEqualTo("COMPOSER23456789012345678901234");
		assertThat(id3tag.getOriginalArtist()).isEqualTo("ORIGARTIST234567890123456789012");
		assertThat(id3tag.getCopyright()).isEqualTo("COPYRIGHT2345678901234567890123");
		assertThat(id3tag.getUrl()).isEqualTo("URL2345678901234567890123456789");
		assertThat(id3tag.getEncoder()).isEqualTo("ENCODER234567890123456789012345");
		assertThat(id3tag.getAlbumImage().length).isEqualTo(1885);
		assertThat(id3tag.getAlbumImageMimeType()).isEqualTo("image/png");
	}

	@Test
	void shouldConvert23TagToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v23Tag(data);
		assertThat(data.length).isEqualTo(2340);
		assertThat(id3tagCopy).isEqualTo(id3tag);
	}

	@Test
	void shouldConvert24TagWithFooterToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setFooter(true);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v24Tag(data);
		assertThat(data.length).isEqualTo(2350);
		assertThat(id3tagCopy).isEqualTo(id3tag);
	}

	@Test
	void shouldConvert24TagWithPaddingToBytesAndBackToEquivalentTag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setPadding(true);
		byte[] data = id3tag.toBytes();
		ID3v2 id3tagCopy = new ID3v24Tag(data);
		assertThat(data.length).isEqualTo(2340 + AbstractID3v2Tag.PADDING_LENGTH);
		assertThat(id3tagCopy).isEqualTo(id3tag);
	}

	@Test
	void shouldNotUsePaddingOnA24TagIfItHasAFooter() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setFooter(true);
		id3tag.setPadding(true);
		byte[] data = id3tag.toBytes();
		assertThat(data.length).isEqualTo(2350);
	}

	@Test
	void shouldExtractGenreNumberFromCombinedGenreStringsCorrectly() {
		ID3v23TagForTesting id3tag = new ID3v23TagForTesting();
			assertThrows(NumberFormatException.class, () -> id3tag.extractGenreNumber(""));
		assertThat(id3tag.extractGenreNumber("13")).isEqualTo(13);
		assertThat(id3tag.extractGenreNumber("(13)")).isEqualTo(13);
		assertThat(id3tag.extractGenreNumber("(13)Pop")).isEqualTo(13);
	}

	@Test
	void shouldExtractGenreDescriptionFromCombinedGenreStringsCorrectly() {
		ID3v23TagForTesting id3tag = new ID3v23TagForTesting();
		assertThat(id3tag.extractGenreDescription("")).isNull();
		assertThat(id3tag.extractGenreDescription("(13)")).isEqualTo("");
		assertThat(id3tag.extractGenreDescription("(13)Pop")).isEqualTo("Pop");
	}

	@Test
	void shouldSetCombinedGenreOnTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertThat(genre).isEqualTo("(13)Pop");
	}

	@Test
	void testSetGenreDescriptionOn23Tag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Jazz");
		assertThat(id3tag.getGenreDescription()).isEqualTo("Jazz");
		assertThat(id3tag.getGenre()).isEqualTo(8);

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertThat(genre).isEqualTo("(8)Jazz");
	}

	@Test
	void testSetGenreDescriptionOn23TagWithUnknownGenre() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
			assertThrows(IllegalArgumentException.class, () -> id3tag.setGenreDescription("Bebop"));
	}

	@Test
	void testSetGenreDescriptionOn24Tag() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Jazz");
		assertThat(id3tag.getGenreDescription()).isEqualTo("Jazz");
		assertThat(id3tag.getGenre()).isEqualTo(8);

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertThat(genre).isEqualTo("Jazz");
	}

	@Test
	void testSetGenreDescriptionOn24TagWithUnknownGenre() throws Exception {
		ID3v2 id3tag = new ID3v24Tag();
		setTagFields(id3tag);
		id3tag.setGenreDescription("Bebop");
		assertThat(id3tag.getGenreDescription()).isEqualTo("Bebop");
		assertThat(id3tag.getGenre()).isEqualTo(-1);

		Map<String, ID3v2FrameSet> frameSets = id3tag.getFrameSets();
		ID3v2FrameSet frameSet = frameSets.get("TCON");
		List<ID3v2Frame> frames = frameSet.getFrames();
		ID3v2Frame frame = frames.get(0);
		byte[] bytes = frame.getData();
		String genre = BufferTools.byteBufferToString(bytes, 1, bytes.length - 1);
		assertThat(genre).isEqualTo("Bebop");
	}

	@Test
	void shouldReadCombinedGenreInTag() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		byte[] bytes = id3tag.toBytes();
		ID3v2 id3tagFromData = new ID3v23Tag(bytes);
		assertThat(id3tagFromData.getGenre()).isEqualTo(13);
		assertThat(id3tagFromData.getGenreDescription()).isEqualTo("Pop");
	}

	@Test
	void shouldGetCommentAndItunesComment() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/withitunescomment.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getComment()).isEqualTo("COMMENT123456789012345678901");
		assertThat(id3tag.getItunesComment()).isEqualTo(" 00000A78 00000A74 00000C7C 00000C6C 00000000 00000000 000051F7 00005634 00000000 00000000");
	}

	@Test
	void shouldReadFramesFromMp3WithObselete32Tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/obsolete.mp3");
		ID3v2 id3v2tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3v2tag.getVersion()).isEqualTo("2.0");
		assertThat(id3v2tag.getLength()).isEqualTo(0x3c5a2);
		assertThat(id3v2tag.getFrameSets().size()).isEqualTo(10);
		assertThat((id3v2tag.getFrameSets().get("TCM")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("COM")).getFrames().size()).isEqualTo(2);
		assertThat((id3v2tag.getFrameSets().get("TP1")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TAL")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TRK")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TPA")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TYE")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("PIC")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TCO")).getFrames().size()).isEqualTo(1);
		assertThat((id3v2tag.getFrameSets().get("TT2")).getFrames().size()).isEqualTo(1);
	}

	@Test
	void shouldReadTagFieldsFromMp3WithObselete32tag() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/obsolete.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getYear()).isEqualTo("2009");
		assertThat(id3tag.getTrack()).isEqualTo("4/15");
		assertThat(id3tag.getAlbumImageMimeType()).isEqualTo("image/png");
		assertThat(id3tag.getGenre()).isEqualTo(40);
		assertThat(id3tag.getGenreDescription()).isEqualTo("Alt Rock");
		assertThat(id3tag.getTitle()).isEqualTo("NAME1234567890123456789012345678901234567890");
		assertThat(id3tag.getArtist()).isEqualTo("ARTIST1234567890123456789012345678901234567890");
		assertThat(id3tag.getComposer()).isEqualTo("COMPOSER1234567890123456789012345678901234567890");
		assertThat(id3tag.getAlbum()).isEqualTo("ALBUM1234567890123456789012345678901234567890");
		assertThat(id3tag.getComment()).isEqualTo("COMMENTS1234567890123456789012345678901234567890");
	}

	@Test
	void shouldReadTagFieldsWithUnicodeDataFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23unicodetags.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getArtist()).isEqualTo("\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5"); // greek
		assertThat(id3tag.getTitle()).isEqualTo("\u4E2D\u6587"); // chinese
		assertThat(id3tag.getAlbum()).isEqualTo("\u3053\u3093\u306B\u3061\u306F"); // japanese
		assertThat(id3tag.getComposer()).isEqualTo("\u0AB9\u0AC7\u0AB2\u0ACD\u0AB2\u0ACB"); // gujarati
	}

	@Test
	void shouldSetTagFieldsWithUnicodeDataAndSpecifiedEncodingCorrectly() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		id3tag.setArtist("\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5");
		id3tag.setTitle("\u4E2D\u6587");
		id3tag.setAlbum("\u3053\u3093\u306B\u3061\u306F");
		id3tag.setComment("\u03C3\u03BF\u03C5");
		id3tag.setComposer("\u0AB9\u0AC7\u0AB2\u0ACD\u0AB2\u0ACB");
		id3tag.setOriginalArtist("\u03B3\u03B5\u03B9\u03AC");
		id3tag.setCopyright("\u03B3\u03B5");
		id3tag.setUrl("URL");
		id3tag.setEncoder("\u03B9\u03AC");
		byte[] albumImage = TestHelper.loadFile("src/test/resources/image.png");
		id3tag.setAlbumImage(albumImage, "image/png", ID3v23Tag.PICTURETYPE_OTHER, "\u03B3\u03B5\u03B9\u03AC");
	}

	@Test
	void shouldExtractChapterTOCFramesFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithchapters.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);

		ArrayList<ID3v2ChapterTOCFrameData> chapterTOCs = id3tag.getChapterTOC();
		assertThat(chapterTOCs.size()).isEqualTo(1);

		ID3v2ChapterTOCFrameData tocFrameData = chapterTOCs.get(0);
		assertThat(tocFrameData.getId()).isEqualTo("toc1");
		assertThat(tocFrameData.getChildren()).isEqualTo(new String[]{"ch1", "ch2", "ch3"});

		ArrayList<ID3v2Frame> subFrames = tocFrameData.getSubframes();
		assertThat(subFrames.size()).isEqualTo(0);
	}

	@Test
	void shouldExtractChapterTOCAndChapterFramesFromMp3() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithchapters.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);

		ArrayList<ID3v2ChapterFrameData> chapters = id3tag.getChapters();
		assertThat(chapters.size()).isEqualTo(3);

		ID3v2ChapterFrameData chapter1 = chapters.get(0);
		assertThat(chapter1.getId()).isEqualTo("ch1");
		assertThat(chapter1.getStartTime()).isEqualTo(0);
		assertThat(chapter1.getEndTime()).isEqualTo(5000);
		assertThat(chapter1.getStartOffset()).isEqualTo(-1);
		assertThat(chapter1.getEndOffset()).isEqualTo(-1);

		ArrayList<ID3v2Frame> subFrames1 = chapter1.getSubframes();
		assertThat(subFrames1.size()).isEqualTo(1);
		ID3v2Frame subFrame1 = subFrames1.get(0);
		assertThat(subFrame1.getId()).isEqualTo("TIT2");
		ID3v2TextFrameData frameData1 = new ID3v2TextFrameData(false, subFrame1.getData());
		assertThat(frameData1.getText().toString()).isEqualTo("start");

		ID3v2ChapterFrameData chapter2 = chapters.get(1);
		assertThat(chapter2.getId()).isEqualTo("ch2");
		assertThat(chapter2.getStartTime()).isEqualTo(5000);
		assertThat(chapter2.getEndTime()).isEqualTo(10000);
		assertThat(chapter2.getStartOffset()).isEqualTo(-1);
		assertThat(chapter2.getEndOffset()).isEqualTo(-1);

		ArrayList<ID3v2Frame> subFrames2 = chapter2.getSubframes();
		assertThat(subFrames2.size()).isEqualTo(1);
		ID3v2Frame subFrame2 = subFrames2.get(0);
		assertThat(subFrame2.getId()).isEqualTo("TIT2");
		ID3v2TextFrameData frameData2 = new ID3v2TextFrameData(false, subFrame2.getData());
		assertThat(frameData2.getText().toString()).isEqualTo("5 seconds");

		ID3v2ChapterFrameData chapter3 = chapters.get(2);
		assertThat(chapter3.getId()).isEqualTo("ch3");
		assertThat(chapter3.getStartTime()).isEqualTo(10000);
		assertThat(chapter3.getEndTime()).isEqualTo(15000);
		assertThat(chapter3.getStartOffset()).isEqualTo(-1);
		assertThat(chapter3.getEndOffset()).isEqualTo(-1);

		ArrayList<ID3v2Frame> subFrames3 = chapter3.getSubframes();
		assertThat(subFrames3.size()).isEqualTo(1);
		ID3v2Frame subFrame3 = subFrames3.get(0);
		assertThat(subFrame3.getId()).isEqualTo("TIT2");
		ID3v2TextFrameData frameData3 = new ID3v2TextFrameData(false, subFrame3.getData());
		assertThat(frameData3.getText().toString()).isEqualTo("10 seconds");
	}

	@Test
	void shouldReadTagFieldsFromMp3With32tagResavedByMp3tagWithUTF16Encoding() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage-utf16le.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getTrack()).isEqualTo("1");
		assertThat(id3tag.getArtist()).isEqualTo("ARTIST123456789012345678901234");
		assertThat(id3tag.getTitle()).isEqualTo("TITLE1234567890123456789012345");
		assertThat(id3tag.getAlbum()).isEqualTo("ALBUM1234567890123456789012345");
		assertThat(id3tag.getYear()).isEqualTo("2001");
		assertThat(id3tag.getGenre()).isEqualTo(0x01);
		assertThat(id3tag.getGenreDescription()).isEqualTo("Classic Rock");
		assertThat(id3tag.getComment()).isEqualTo("COMMENT123456789012345678901");
		assertThat(id3tag.getComposer()).isEqualTo("COMPOSER23456789012345678901234");
		assertThat(id3tag.getOriginalArtist()).isEqualTo("ORIGARTIST234567890123456789012");
		assertThat(id3tag.getCopyright()).isEqualTo("COPYRIGHT2345678901234567890123");
		assertThat(id3tag.getUrl()).isEqualTo("URL2345678901234567890123456789");
		assertThat(id3tag.getEncoder()).isEqualTo("ENCODER234567890123456789012345");
		assertThat(id3tag.getAlbumImage().length).isEqualTo(1885);
		assertThat(id3tag.getAlbumImageMimeType()).isEqualTo("image/png");
	}

	@Test
	void shouldRemoveAlbumImageFrame() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v1andv23tagswithalbumimage.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getAlbumImage().length).isEqualTo(1885);
		id3tag.clearAlbumImage();
		assertThat(id3tag.getAlbumImage()).isNull();
	}

	@Test
	void shouldReadBPM() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpm.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getBPM()).isEqualTo(84);
	}

	@Test
	void shouldReadFloatingPointBPM() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpmfloat.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getBPM()).isEqualTo(84);
	}

	@Test
	void shouldReadFloatingPointBPMWithCommaDelimiter() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithbpmfloatwithcomma.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getBPM()).isEqualTo(84);
	}

	@Test
	void shouldReadWmpRating() throws Exception {
		byte[] buffer = TestHelper.loadFile("src/test/resources/v23tagwithwmprating.mp3");
		ID3v2 id3tag = ID3v2TagFactory.createTag(buffer);
		assertThat(id3tag.getWmpRating()).isEqualTo(3);
	}

	@Test
	void shouldWriteWmpRating() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		final int expectedUnsetValue = -1;
		assertThat(id3tag.getWmpRating()).isEqualTo(expectedUnsetValue);
		final int newValue = 4;
		id3tag.setWmpRating(newValue);
		assertThat(id3tag.getWmpRating()).isEqualTo(newValue);
	}

	@Test
	void shouldIgnoreInvalidWmpRatingOnWrite() throws Exception {
		ID3v2 id3tag = new ID3v23Tag();
		setTagFields(id3tag);
		final int originalValue = id3tag.getWmpRating();
		final int invalidValue = 6;
		id3tag.setWmpRating(invalidValue);
		assertThat(id3tag.getWmpRating()).isEqualTo(originalValue);
	}


	private void setTagFields(ID3v2 id3tag) throws IOException {
		id3tag.setTrack("1");
		id3tag.setArtist("ARTIST");
		id3tag.setTitle("TITLE");
		id3tag.setAlbum("ALBUM");
		id3tag.setYear("1954");
		id3tag.setGenre(0x0d);
		id3tag.setComment("COMMENT");
		id3tag.setComposer("COMPOSER");
		id3tag.setOriginalArtist("ORIGINALARTIST");
		id3tag.setCopyright("COPYRIGHT");
		id3tag.setUrl("URL");
		id3tag.setCommercialUrl("COMMERCIALURL");
		id3tag.setCopyrightUrl("COPYRIGHTURL");
		id3tag.setArtistUrl("OFFICIALARTISTURL");
		id3tag.setAudiofileUrl("OFFICIALAUDIOFILEURL");
		id3tag.setAudioSourceUrl("OFFICIALAUDIOSOURCEURL");
		id3tag.setRadiostationUrl("INTERNETRADIOSTATIONURL");
		id3tag.setPaymentUrl("PAYMENTURL");
		id3tag.setPublisherUrl("PUBLISHERURL");
		id3tag.setEncoder("ENCODER");
		byte[] albumImage = TestHelper.loadFile("src/test/resources/image.png");
		id3tag.setAlbumImage(albumImage, "image/png");
	}

	private ID3v2 createTag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		ID3v2TagFactoryForTesting factory = new ID3v2TagFactoryForTesting();
		return factory.createTag(buffer);
	}

	static class ID3v22TagForTesting extends ID3v22Tag {

		ID3v22TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	static class ID3v23TagForTesting extends ID3v23Tag {

		ID3v23TagForTesting() {
			super();
		}

		ID3v23TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	static class ID3v24TagForTesting extends ID3v24Tag {

		ID3v24TagForTesting(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			super(buffer);
		}

		@Override
		protected int unpackFrames(byte[] buffer, int offset, int framesLength) {
			return offset;
		}
	}

	static class ID3v2TagFactoryForTesting {

		static final int MAJOR_VERSION_OFFSET = 3;

		ID3v2 createTag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
			int majorVersion = buffer[MAJOR_VERSION_OFFSET];
			switch (majorVersion) {
				case 2:
					return new ID3v22TagForTesting(buffer);
				case 3:
					return new ID3v23TagForTesting(buffer);
				case 4:
					return new ID3v24TagForTesting(buffer);
			}
			throw new UnsupportedTagException("Tag version not supported");
		}
	}
}
