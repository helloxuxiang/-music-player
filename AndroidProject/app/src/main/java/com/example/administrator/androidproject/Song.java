package com.example.administrator.androidproject;

import java.io.Serializable;

public class Song implements Serializable{
	private String song;		//歌名
	private String filename;//歌手-歌名
	private String singer;	//歌曲的歌手名
	private String path;		//歌曲文件的全路径
	private int duration;	//歌曲的总播放时长
	private long size;		//歌曲文件的大小

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Song() {
	}

	public Song(String song, String filename, String singer, String path, int duration, long size) {
        this.song = song;
        this.filename = filename;
        this.singer = singer;
        this.path = path;
        this.duration = duration;
        this.size = size;
    }

	@Override
	public String toString() {
		return "Song{" +
				"song='" + song + '\'' +
				", filename='" + filename + '\'' +
				", singer='" + singer + '\'' +
				", path='" + path + '\'' +
				", duration=" + duration +
				", size=" + size +
				'}';
	}
}
