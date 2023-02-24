package org.example.model;

public class Audio {
	private int id;
	private String artistName;
	private String trackTitle;
	private String albumTitle;
	private int trackNumber;
	private int year;
	private int reviewsNum;
	private int copiesSoldNum;
	
	public Audio() {
	}

	public Audio(int id,String artistName, String trackTitle, String albumTitle, int trackNumber, int year, int reviewsNum,
			int copiesSoldNum) {
		this.id = id;
		this.artistName = artistName;
		this.trackTitle = trackTitle;
		this.albumTitle = albumTitle;
		this.trackNumber = trackNumber;
		this.year = year;
		this.reviewsNum = reviewsNum;
		this.copiesSoldNum = copiesSoldNum;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getTrackTitle() {
		return trackTitle;
	}
	public void setTrackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
	}
	public String getAlbumTitle() {
		return albumTitle;
	}
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	public long getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getReviewsNum() {
		return reviewsNum;
	}
	public void setReviewsNum(int reviewsNum) {
		this.reviewsNum = reviewsNum;
	}
	public int getCopiesSoldNum() {
		return copiesSoldNum;
	}
	public void setCopiesSoldNum(int copiesSoldNum) {
		this.copiesSoldNum = copiesSoldNum;
	}

	@Override
	public String toString() {
		return "Audio [id=" + id + ", artistName=" + artistName + ", trackTitle=" + trackTitle + ", albumTitle="
				+ albumTitle + ", trackNumber=" + trackNumber + ", year=" + year + ", reviewsNum=" + reviewsNum
				+ ", copiesSoldNum=" + copiesSoldNum + "]";
	}
	
	
}
