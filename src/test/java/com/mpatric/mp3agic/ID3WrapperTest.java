package com.mpatric.mp3agic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class ID3WrapperTest {
	//region getId3v1Tag
	@Test
	void returnsV1Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getId3v1Tag()).isEqualTo(id3v1Tag);
	}

	@Test
	void returnsNullV1Tag() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		assertThat(wrapper.getId3v1Tag()).isEqualTo(null);
	}
	//endregion

	//region getId3v2Tag
	@Test
	void returnsV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getId3v2Tag()).isEqualTo(id3v2Tag);
	}

	@Test
	void returnsNullV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getId3v2Tag()).isEqualTo(null);
	}
	//endregion

	//region getTrack
	@Test
	void getTrackReturnsV2TagsTrackBeforeV1TagsTrack() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack("V2 Track");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTrack()).isEqualTo("V2 Track");
	}

	@Test
	void getTrackReturnsV1TagsTrackIfV2TagsTrackIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTrack()).isEqualTo("V1 Track");
	}

	@Test
	void getTrackReturnsV1TagsTrackIfV2TagsTrackDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTrack(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTrack()).isEqualTo("V1 Track");
	}

	@Test
	void getTrackReturnsV1TagsTrackIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTrack("V1 Track");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getTrack()).isEqualTo("V1 Track");
	}

	@Test
	void getTrackReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getTrack()).isNull();
	}
	//endregion

	//region setTrack
	@Test
	void setsTrackOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setTrack("a track");
		assertThat(id3v1Tag.getTrack()).isEqualTo("a track");
		assertThat(id3v2Tag.getTrack()).isEqualTo("a track");
	}

	@Test
	void setsTrackOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setTrack("a track");
		assertThat(id3v1Tag.getTrack()).isEqualTo("a track");
	}

	@Test
	void setsTrackOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setTrack("a track");
		assertThat(id3v2Tag.getTrack()).isEqualTo("a track");
	}

	@Test
	void setTrackDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setTrack("a track");
	}
	//endregion

	//region getArtist
	@Test
	void getArtistReturnsV2TagsArtistBeforeV1TagsArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist("V2 Artist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getArtist()).isEqualTo("V2 Artist");
	}

	@Test
	void getArtistReturnsV1TagsArtistIfV2TagsArtistIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getArtist()).isEqualTo("V1 Artist");
	}

	@Test
	void getArtistReturnsV1TagsArtistIfV2TagsArtistDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setArtist(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getArtist()).isEqualTo("V1 Artist");
	}

	@Test
	void getArtistReturnsV1TagsArtistIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setArtist("V1 Artist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getArtist()).isEqualTo("V1 Artist");
	}

	@Test
	void getArtistReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getArtist()).isNull();
	}
	//endregion

	//region setArtist
	@Test
	void setsArtistOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setArtist("an artist");
		assertThat(id3v1Tag.getArtist()).isEqualTo("an artist");
		assertThat(id3v2Tag.getArtist()).isEqualTo("an artist");
	}

	@Test
	void setsArtistOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setArtist("an artist");
		assertThat(id3v1Tag.getArtist()).isEqualTo("an artist");
	}

	@Test
	void setsArtistOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setArtist("an artist");
		assertThat(id3v2Tag.getArtist()).isEqualTo("an artist");
	}

	@Test
	void setArtistDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setArtist("an artist");
	}
	//endregion

	//region getTitle
	@Test
	void getTitleReturnsV2TagsTitleBeforeV1TagsTitle() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle("V2 Title");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTitle()).isEqualTo("V2 Title");
	}

	@Test
	void getTitleReturnsV1TagsTitleIfV2TagsTitleIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTitle()).isEqualTo("V1 Title");
	}

	@Test
	void getTitleReturnsV1TagsTitleIfV2TagsTitleDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setTitle(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getTitle()).isEqualTo("V1 Title");
	}

	@Test
	void getTitleReturnsV1TagsTitleIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setTitle("V1 Title");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getTitle()).isEqualTo("V1 Title");
	}

	@Test
	void getTitleReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getTitle()).isNull();
	}
	//endregion

	//region setTitle
	@Test
	void setsTitleOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setTitle("a title");
		assertThat(id3v1Tag.getTitle()).isEqualTo("a title");
		assertThat(id3v2Tag.getTitle()).isEqualTo("a title");
	}

	@Test
	void setsTitleOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setTitle("a title");
		assertThat(id3v1Tag.getTitle()).isEqualTo("a title");
	}

	@Test
	void setsTitleOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setTitle("a title");
		assertThat(id3v2Tag.getTitle()).isEqualTo("a title");
	}

	@Test
	void setTitleDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setTitle("a title");
	}
	//endregion

	//region getAlbum
	@Test
	void getAlbumReturnsV2TagsAlbumBeforeV1TagsAlbum() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum("V2 Album");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getAlbum()).isEqualTo("V2 Album");
	}

	@Test
	void getAlbumReturnsV1TagsAlbumIfV2TagsAlbumIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getAlbum()).isEqualTo("V1 Album");
	}

	@Test
	void getAlbumReturnsV1TagsAlbumIfV2TagsAlbumDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbum(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getAlbum()).isEqualTo("V1 Album");
	}

	@Test
	void getAlbumReturnsV1TagsAlbumIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setAlbum("V1 Album");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getAlbum()).isEqualTo("V1 Album");
	}

	@Test
	void getAlbumReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getAlbum()).isNull();
	}
	//endregion

	//region setAlbum
	@Test
	void setsAlbumOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbum("an album");
		assertThat(id3v1Tag.getAlbum()).isEqualTo("an album");
		assertThat(id3v2Tag.getAlbum()).isEqualTo("an album");
	}

	@Test
	void setsAlbumOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbum("an album");
		assertThat(id3v1Tag.getAlbum()).isEqualTo("an album");
	}

	@Test
	void setsAlbumOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setAlbum("an album");
		assertThat(id3v2Tag.getAlbum()).isEqualTo("an album");
	}

	@Test
	void setAlbumDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setAlbum("an album");
	}
	//endregion

	//region getYear
	@Test
	void getYearReturnsV2TagsYearBeforeV1TagsYear() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear("V2 Year");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getYear()).isEqualTo("V2 Year");
	}

	@Test
	void getYearReturnsV1TagsYearIfV2TagsYearIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getYear()).isEqualTo("V1 Year");
	}

	@Test
	void getYearReturnsV1TagsYearIfV2TagsYearDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setYear(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getYear()).isEqualTo("V1 Year");
	}

	@Test
	void getYearReturnsV1TagsYearIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setYear("V1 Year");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getYear()).isEqualTo("V1 Year");
	}

	@Test
	void getYearReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getYear()).isNull();
	}
	//endregion

	//region setYear
	@Test
	void setsYearOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setYear("a year");
		assertThat(id3v1Tag.getYear()).isEqualTo("a year");
		assertThat(id3v2Tag.getYear()).isEqualTo("a year");
	}

	@Test
	void setsYearOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setYear("a year");
		assertThat(id3v1Tag.getYear()).isEqualTo("a year");
	}

	@Test
	void setsYearOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setYear("a year");
		assertThat(id3v2Tag.getYear()).isEqualTo("a year");
	}

	@Test
	void setYearDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setYear("a year");
	}
	//endregion

	//region getGenre
	@Test
	void getGenreReturnsV2TagsGenreBeforeV1TagsGenre() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenre(20);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getGenre()).isEqualTo(20);
	}

	@Test
	void getGenreReturnsV1TagsGenreIfV2TagsGenreIsNegativeOne() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenre(-1);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getGenre()).isEqualTo(10);
	}

	@Test
	void getGenreReturnsV1TagsGenreIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenre(10);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getGenre()).isEqualTo(10);
	}

	@Test
	void getGenreReturnsNegativeOneIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getGenre()).isEqualTo(-1);
	}
	//endregion

	//region setGenre
	@Test
	void setsGenreOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setGenre(22);
		assertThat(id3v1Tag.getGenre()).isEqualTo(22);
		assertThat(id3v2Tag.getGenre()).isEqualTo(22);
	}

	@Test
	void setsGenreOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setGenre(22);
		assertThat(id3v1Tag.getGenre()).isEqualTo(22);
	}

	@Test
	void setsGenreOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setGenre(22);
		assertThat(id3v2Tag.getGenre()).isEqualTo(22);
	}

	@Test
	void setGenreDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setGenre(22);
	}
	//endregion

	//region getGenreDescription
	@Test
	void getGenreDescriptionReturnsV2TagsGenreDescriptionBeforeV1TagsGenreDescription() {
		ID3v1TagForTesting id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenreDescription("V1 GenreDescription");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setGenreDescription("V2 GenreDescription");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getGenreDescription()).isEqualTo("V2 GenreDescription");
	}

	@Test
	void getGenreDescriptionReturnsV1TagsGenreDescriptionIfV2TagDoesNotExist() {
		ID3v1TagForTesting id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setGenreDescription("V1 GenreDescription");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getGenreDescription()).isEqualTo("V1 GenreDescription");
	}

	@Test
	void getGenreDescriptionReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getGenreDescription()).isNull();
	}
	//endregion

	//region getComment
	@Test
	void getCommentReturnsV2TagsCommentBeforeV1TagsComment() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment("V2 Comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getComment()).isEqualTo("V2 Comment");
	}

	@Test
	void getCommentReturnsV1TagsCommentIfV2TagsCommentIsEmpty() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment("");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getComment()).isEqualTo("V1 Comment");
	}

	@Test
	void getCommentReturnsV1TagsCommentIfV2TagsCommentDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComment(null);
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getComment()).isEqualTo("V1 Comment");
	}

	@Test
	void getCommentReturnsV1TagsCommentIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("V1 Comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getComment()).isEqualTo("V1 Comment");
	}

	@Test
	void getCommentReturnsNullIfBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		assertThat(wrapper.getComment()).isNull();
	}
	//endregion

	//region setComment
	@Test
	void setsCommentOnBothV1AndV2Tags() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setComment("a comment");
		assertThat(id3v1Tag.getComment()).isEqualTo("a comment");
		assertThat(id3v2Tag.getComment()).isEqualTo("a comment");
	}

	@Test
	void setsCommentOnV1TagOnly() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setComment("a comment");
		assertThat(id3v1Tag.getComment()).isEqualTo("a comment");
	}

	@Test
	void setsCommentOnV2TagOnly() {
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.setComment("a comment");
		assertThat(id3v2Tag.getComment()).isEqualTo("a comment");
	}

	@Test
	void setCommentDoesNotThrowExceptionWhenBothTagsDoNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.setComment("a comment");
	}
	//endregion

	//region getComposer
	@Test
	void getComposerReturnsV2TagsComposer() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setComposer("V2 Composer");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getComposer()).isEqualTo("V2 Composer");
	}

	@Test
	void getComposerReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getComposer()).isNull();
	}
	//endregion

	//region setComposer
	@Test
	void setsComposerOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setComposer("a composer");
		assertThat(id3v2Tag.getComposer()).isEqualTo("a composer");
	}

	@Test
	void setComposerDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setComposer("a composer");
	}
	//endregion

	//region getOriginalArtist
	@Test
	void getOriginalArtistReturnsV2TagsOriginalArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setOriginalArtist("V2 OriginalArtist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getOriginalArtist()).isEqualTo("V2 OriginalArtist");
	}

	@Test
	void getOriginalArtistReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getOriginalArtist()).isNull();
	}
	//endregion

	//region setOriginalArtist
	@Test
	void setsOriginalArtistOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setOriginalArtist("an original artist");
		assertThat(id3v2Tag.getOriginalArtist()).isEqualTo("an original artist");
	}

	@Test
	void setOriginalArtistDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setOriginalArtist("an original artist");
	}
	//endregion

	//region getAlbumArtist
	@Test
	void getAlbumArtistReturnsV2TagsAlbumArtist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbumArtist("V2 AlbumArtist");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getAlbumArtist()).isEqualTo("V2 AlbumArtist");
	}

	@Test
	void getAlbumArtistReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getAlbumArtist()).isNull();
	}
	//endregion

	//region setAlbumArtist
	@Test
	void setsAlbumArtistOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbumArtist("an album artist");
		assertThat(id3v2Tag.getAlbumArtist()).isEqualTo("an album artist");
	}

	@Test
	void setAlbumArtistDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbumArtist("an album artist");
	}
	//endregion

	//region getCopyright
	@Test
	void getCopyrightReturnsV2TagsCopyright() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setCopyright("V2 Copyright");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getCopyright()).isEqualTo("V2 Copyright");
	}

	@Test
	void getCopyrightReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getCopyright()).isNull();
	}
	//endregion

	//region setCopyright
	@Test
	void setsCopyrightOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setCopyright("a copyright");
		assertThat(id3v2Tag.getCopyright()).isEqualTo("a copyright");
	}

	@Test
	void setCopyrightDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setCopyright("a copyright");
	}
	//endregion

	//region getUrl
	@Test
	void getUrlReturnsV2TagsUrl() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setUrl("V2 Url");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getUrl()).isEqualTo("V2 Url");
	}

	@Test
	void getUrlReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getUrl()).isNull();
	}
	//endregion

	//region setUrl
	@Test
	void setsUrlOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setUrl("a url");
		assertThat(id3v2Tag.getUrl()).isEqualTo("a url");
	}

	@Test
	void setUrlDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setUrl("a url");
	}
	//endregion

	//region getEncoder
	@Test
	void getEncoderReturnsV2TagsEncoder() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setEncoder("V2 Encoder");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getEncoder()).isEqualTo("V2 Encoder");
	}

	@Test
	void getEncoderReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getEncoder()).isNull();
	}
	//endregion

	//region setEncoder
	@Test
	void setsEncoderOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setEncoder("an encoder");
		assertThat(id3v2Tag.getEncoder()).isEqualTo("an encoder");
	}

	@Test
	void setEncoderDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setEncoder("an encoder");
	}
	//endregion

	//region getAlbumImage and getAlbumImageMimeType
	@Test
	void getAlbumImageReturnsV2TagsAlbumImage() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getAlbumImage()).isEqualTo(new byte[]{12, 4, 7});
		assertThat(wrapper.getAlbumImageMimeType()).isEqualTo("mime type");
	}

	@Test
	void getAlbumImageReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getAlbumImage()).isNull();
		assertThat(wrapper.getAlbumImageMimeType()).isNull();
	}
	//endregion

	//region setAlbumImage
	@Test
	void setsAlbumImageOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
		assertThat(id3v2Tag.getAlbumImage()).isEqualTo(new byte[]{12, 4, 7});
		assertThat(id3v2Tag.getAlbumImageMimeType()).isEqualTo("mime type");
	}

	@Test
	void setAlbumImageDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setAlbumImage(new byte[]{12, 4, 7}, "mime type");
	}
	//endregion

	//region getLyrics
	@Test
	void getLyricsReturnsV2TagsLyrics() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.setLyrics("V2 Lyrics");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		assertThat(wrapper.getLyrics()).isEqualTo("V2 Lyrics");
	}

	@Test
	void getLyricsReturnsNullIfV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		assertThat(wrapper.getLyrics()).isNull();
	}
	//endregion

	//region setLyrics
	@Test
	void setsLyricsOnV2Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3v2 id3v2Tag = new ID3v2TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, id3v2Tag);
		wrapper.setLyrics("lyrics");
		assertThat(id3v2Tag.getLyrics()).isEqualTo("lyrics");
	}

	@Test
	void setLyricsDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.setLyrics("lyrics");
	}
	//endregion

	//region clearComment
	@Test
	void clearsCommentOnV1Tag() {
		ID3v1 id3v1Tag = new ID3v1TagForTesting();
		id3v1Tag.setComment("a comment");
		ID3Wrapper wrapper = new ID3Wrapper(id3v1Tag, null);
		wrapper.clearComment();
		assertThat(id3v1Tag.getComment()).isNull();
	}

	@Test
	void clearsCommentFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_COMMENT, new ID3v2FrameSet(AbstractID3v2Tag.ID_COMMENT));
		assertThat(id3v2Tag.getFrameSets()).doesNotContainKey(AbstractID3v2Tag.ID_COMMENT);
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearComment();
		assertThat(id3v2Tag.getFrameSets()).doesNotContainKey(AbstractID3v2Tag.ID_COMMENT);
	}
	//endregion

	//region clearCopyright
	@Test
	void clearsCopyrightFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_COPYRIGHT, new ID3v2FrameSet(AbstractID3v2Tag.ID_COPYRIGHT));
		assertThat(id3v2Tag.getFrameSets()).containsKey(AbstractID3v2Tag.ID_COPYRIGHT);
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearCopyright();
		assertThat(id3v2Tag.getFrameSets()).doesNotContainKey(AbstractID3v2Tag.ID_COPYRIGHT);
	}

	@Test
	void clearCopyrightDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.clearCopyright();
	}
	//endregion

	//region clearCo}pyright
	@Test
	void clearsEncoderFrameOnV2Tag() {
		ID3v2TagForTesting id3v2Tag = new ID3v2TagForTesting();
		id3v2Tag.addFrameSet(AbstractID3v2Tag.ID_ENCODER, new ID3v2FrameSet(AbstractID3v2Tag.ID_ENCODER));
		assertThat(id3v2Tag.getFrameSets()).containsKey(AbstractID3v2Tag.ID_ENCODER);
		ID3Wrapper wrapper = new ID3Wrapper(null, id3v2Tag);
		wrapper.clearEncoder();
		assertThat(id3v2Tag.getFrameSets()).doesNotContainKey(AbstractID3v2Tag.ID_ENCODER);
	}

	@Test
	void clearEncoderDoesNotThrowExceptionWhenV2TagDoesNotExist() {
		ID3Wrapper wrapper = new ID3Wrapper(null, null);
		wrapper.clearEncoder();
	}
	//endregion

	//region ID3v1TagForTesting class
	private static class ID3v1TagForTesting implements ID3v1 {
		private String track;
		private String artist;
		private String title;
		private String album;
		private String year;
		private int genre;
		protected String genreDescription;
		private String comment;

		@Override
		public String getVersion() {
			return null;
		}

		@Override
		public String getTrack() {
			return track;
		}

		@Override
		public void setTrack(String track) {
			this.track = track;
		}

		@Override
		public String getArtist() {
			return artist;
		}

		@Override
		public void setArtist(String artist) {
			this.artist = artist;
		}

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String getAlbum() {
			return album;
		}

		@Override
		public void setAlbum(String album) {
			this.album = album;
		}

		@Override
		public String getYear() {
			return year;
		}

		@Override
		public void setYear(String year) {
			this.year = year;
		}

		@Override
		public int getGenre() {
			return genre;
		}

		@Override
		public void setGenre(int genre) {
			this.genre = genre;
		}

		@Override
		public String getGenreDescription() {
			return genreDescription;
		}

		public void setGenreDescription(String genreDescription) {
			this.genreDescription = genreDescription;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public void setComment(String comment) {
			this.comment = comment;
		}

		@Override
		public byte[] toBytes() throws NotSupportedException {
			return new byte[0];
		}
	}
	//endregion

	//region ID3v2TagForTesting class
	private static class ID3v2TagForTesting extends ID3v1TagForTesting implements ID3v2 {
		private String composer;
		private String originalArtist;
		private String albumArtist;
		private String copyright;
		private String url;
		private String encoder;
		private byte[] albumImage;
		private String albumImageMimeType;
		private String lyrics;
		private Map<String, ID3v2FrameSet> frameSets = new HashMap<>();

		@Override
		public boolean getPadding() {
			return false;
		}

		@Override
		public void setPadding(boolean padding) {

		}

		@Override
		public boolean hasFooter() {
			return false;
		}

		@Override
		public void setFooter(boolean footer) {

		}

		@Override
		public boolean hasUnsynchronisation() {
			return false;
		}

		@Override
		public void setUnsynchronisation(boolean unsynchronisation) {

		}

		@Override
		public int getBPM() {
			return 0;
		}

		@Override
		public void setBPM(int bpm) {

		}

		@Override
		public String getGrouping() {
			return null;
		}

		@Override
		public void setGrouping(String grouping) {

		}

		@Override
		public String getKey() {
			return null;
		}

		@Override
		public void setKey(String key) {

		}

		@Override
		public String getDate() {
			return null;
		}

		@Override
		public void setDate(String date) {

		}

		@Override
		public String getComposer() {
			return composer;
		}

		@Override
		public void setComposer(String composer) {
			this.composer = composer;
		}

		@Override
		public String getPublisher() {
			return null;
		}

		@Override
		public void setPublisher(String publisher) {

		}

		@Override
		public String getOriginalArtist() {
			return originalArtist;
		}

		@Override
		public void setOriginalArtist(String originalArtist) {
			this.originalArtist = originalArtist;
		}

		@Override
		public String getAlbumArtist() {
			return albumArtist;
		}

		@Override
		public void setAlbumArtist(String albumArtist) {
			this.albumArtist = albumArtist;
		}

		@Override
		public String getCopyright() {
			return copyright;
		}

		@Override
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}

		@Override
		public String getArtistUrl() {
			return null;
		}

		@Override
		public void setArtistUrl(String url) {

		}

		@Override
		public String getCommercialUrl() {
			return null;
		}

		@Override
		public void setCommercialUrl(String url) {

		}

		@Override
		public String getCopyrightUrl() {
			return null;
		}

		@Override
		public void setCopyrightUrl(String url) {

		}

		@Override
		public String getAudiofileUrl() {
			return null;
		}

		@Override
		public void setAudiofileUrl(String url) {

		}

		@Override
		public String getAudioSourceUrl() {
			return null;
		}

		@Override
		public void setAudioSourceUrl(String url) {

		}

		@Override
		public String getRadiostationUrl() {
			return null;
		}

		@Override
		public void setRadiostationUrl(String url) {

		}

		@Override
		public String getPaymentUrl() {
			return null;
		}

		@Override
		public void setPaymentUrl(String url) {

		}

		@Override
		public String getPublisherUrl() {
			return null;
		}

		@Override
		public void setPublisherUrl(String url) {

		}

		@Override
		public String getUrl() {
			return url;
		}

		@Override
		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String getPartOfSet() {
			return null;
		}

		@Override
		public void setPartOfSet(String partOfSet) {

		}

		@Override
		public boolean isCompilation() {
			return false;
		}

		@Override
		public void setCompilation(boolean compilation) {

		}

		@Override
		public ArrayList<ID3v2ChapterFrameData> getChapters() {
			return null;
		}

		@Override
		public void setChapters(ArrayList<ID3v2ChapterFrameData> chapters) {

		}

		@Override
		public ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC() {
			return null;
		}

		@Override
		public void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> ctoc) {

		}

		@Override
		public String getEncoder() {
			return encoder;
		}

		@Override
		public void setEncoder(String encoder) {
			this.encoder = encoder;
		}

		@Override
		public byte[] getAlbumImage() {
			return albumImage;
		}

		@Override
		public void setAlbumImage(byte[] albumImage, String mimeType) {
			this.albumImage = albumImage;
			this.albumImageMimeType = mimeType;
		}

		@Override
		public void setAlbumImage(byte[] albumImage, String mimeType, byte imageType, String imageDescription) {

		}

		@Override
		public void clearAlbumImage() {

		}

		@Override
		public String getAlbumImageMimeType() {
			return albumImageMimeType;
		}

		@Override
		public int getWmpRating() {
			return 0;
		}

		@Override
		public void setWmpRating(int rating) {

		}

		@Override
		public String getItunesComment() {
			return null;
		}

		@Override
		public void setItunesComment(String itunesComment) {

		}

		@Override
		public String getLyrics() {
			return lyrics;
		}

		@Override
		public void setLyrics(String lyrics) {
			this.lyrics = lyrics;
		}

		@Override
		public void setGenreDescription(String text) {
			this.genreDescription = text;
		}

		@Override
		public int getDataLength() {
			return 0;
		}

		@Override
		public int getLength() {
			return 0;
		}

		@Override
		public boolean getObseleteFormat() {
			return false;
		}

		@Override
		public Map<String, ID3v2FrameSet> getFrameSets() {
			return frameSets;
		}

		public void addFrameSet(String id, ID3v2FrameSet frameSet) {
			frameSets.put(id, frameSet);
		}

		@Override
		public void clearFrameSet(String id) {
			frameSets.remove(id);
		}
	}
	//endregion
}
