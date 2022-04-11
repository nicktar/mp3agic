package com.mpatric.mp3agic;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class ID3v22TagTest {

    private static final byte ZERO = 0;
    private static final byte[] inputBytes = new byte[ID3v22Tag.FLAGS_OFFSET + 1];
    private static final byte[] outputBytes = new byte[ID3v22Tag.FLAGS_OFFSET + 1];

    @Before
    void setUp() throws Exception {
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = 0;
        outputBytes[ID3v22Tag.FLAGS_OFFSET] = 0;
    }

    @Test
    void shouldUnpackAndPackOffUnsynchronizationBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.UNSYNCHRONISATION_BIT, false);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertFalse(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.UNSYNCHRONISATION_BIT));
    }

    @Test
    void shouldUnpackAndPackOnUnsynchronizationBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.UNSYNCHRONISATION_BIT, true);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertTrue(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.UNSYNCHRONISATION_BIT));
    }

    @Test
    void shouldUnpackAndPackOffCompressionBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.COMPRESSION_BIT, false);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertFalse(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.COMPRESSION_BIT));
    }

    @Test
    void shouldUnpackAndPackOnCompressionBit() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        inputBytes[ID3v22Tag.FLAGS_OFFSET] = BufferTools.setBit(ZERO, ID3v22Tag.COMPRESSION_BIT, true);
        id3tag.unpackFlags(inputBytes);
        id3tag.packFlags(outputBytes, 0);
        assertTrue(BufferTools.checkBit(outputBytes[ID3v22Tag.FLAGS_OFFSET], ID3v22Tag.COMPRESSION_BIT));
    }

    @Test
    void shouldStoreAndRetrieveItunesComment() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String comment = "COMMENT";
        id3tag.setItunesComment(comment);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getItunesComment()).isEqualTo(comment);
    }

    @Test
    void shouldStoreAndRetrieveLyrics() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String lyrics = "La-la-la";
        id3tag.setLyrics(lyrics);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getLyrics()).isEqualTo(lyrics);
    }

    @Test
    void shouldStoreAndRetrievePublisher() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String publisher = "PUBLISHER";
        id3tag.setPublisher(publisher);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getPublisher()).isEqualTo(publisher);
    }

    @Test
    void shouldStoreAndRetrieveKey() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String key = "KEY";
        id3tag.setKey(key);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getKey()).isEqualTo(key);
    }

    @Test
    void shouldStoreAndRetrieveBPM() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final int bpm = 8 * 44100;
        id3tag.setBPM(bpm);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getBPM()).isEqualTo(bpm);
    }

    @Test
    void shouldStoreAndRetrieveDate() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String date = "DATE";
        id3tag.setDate(date);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getDate()).isEqualTo(date);
    }

    @Test
    void shouldStoreAndRetrieveAlbumArtist() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String albumArtist = "ALBUMARTIST";
        id3tag.setAlbumArtist(albumArtist);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getAlbumArtist()).isEqualTo(albumArtist);
    }

    @Test
    void shouldStoreAndRetrieveGrouping() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String grouping = "GROUPING";
        id3tag.setGrouping(grouping);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getGrouping()).isEqualTo(grouping);
    }

    @Test
    void shouldStoreAndRetrieveCompilation() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final boolean compilation = true;
        id3tag.setCompilation(compilation);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.isCompilation()).isEqualTo(compilation);
    }

    @Test
    void shouldStoreAndRetrievePartOfSet() throws Exception {
        final ID3v22Tag id3tag = new ID3v22Tag();
        final String partOfSet = "PARTOFSET";
        id3tag.setPartOfSet(partOfSet);
        final byte[] bytes = id3tag.toBytes();
        final ID3v22Tag newId3tag = new ID3v22Tag(bytes);
        assertThat(newId3tag.getPartOfSet()).isEqualTo(partOfSet);
    }

}
