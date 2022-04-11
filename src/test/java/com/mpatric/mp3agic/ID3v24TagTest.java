package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

class ID3v24TagTest {

    @Test
    void shouldStoreAndRetrieveRecordingTime() throws Exception {
        final ID3v24Tag id3tag = new ID3v24Tag();
        final String recordingTime = "01/01/2011 00:00:00";
        id3tag.setRecordingTime(recordingTime);
        final byte[] bytes = id3tag.toBytes();
        final ID3v24Tag newId3tag = new ID3v24Tag(bytes);
        assertThat(newId3tag.getRecordingTime()).isEqualTo(recordingTime);
    }

    @Test
    void shouldSetGenreDescription() throws Exception {
        final ID3v24Tag id3tag = new ID3v24Tag();
        final String genreDescription = "?????";
        id3tag.setGenreDescription(genreDescription);
        final byte[] bytes = id3tag.toBytes();
        final ID3v24Tag newId3tag = new ID3v24Tag(bytes);
        assertTrue(genreDescription, newId3tag.getFrameSets().containsKey(ID3v24Tag.ID_GENRE));
        final List<ID3v2Frame> frames = newId3tag.getFrameSets().get(ID3v24Tag.ID_GENRE).getFrames();
        assertThat(frames.size()).isEqualTo(1);
        final ID3v2TextFrameData frameData = new ID3v2TextFrameData(id3tag.hasUnsynchronisation(), frames.get(0).getData());
        assertThat(frameData.getText().toString()).isEqualTo(genreDescription);
    }
}
