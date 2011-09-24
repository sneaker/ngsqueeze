package ch.nomoresecrets.ngsqueeze.editor;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_3;
import org.farng.mp3.id3.ID3v2_4;

public class ID3Editor {

	public void editTrack(String filename, String name, String artistname) 
			throws IOException, TagException {
		System.out.println("changing id3 tags");
		MP3File mpfile = new MP3File(new File(filename));
		if (mpfile.hasID3v1Tag()) {
			ID3v1 id3v1tag = mpfile.getID3v1Tag();
			setInformation(name, artistname, id3v1tag);
			mpfile.setID3v1Tag(id3v1tag);
			mpfile.save();
		}
		if (mpfile.hasID3v2Tag()) {
			ID3v2_3 id3v2Tag = (ID3v2_3) mpfile.getID3v2Tag();
			ID3v2_4 newid3v2Tag = new ID3v2_4(id3v2Tag);
			setInformation(name, artistname, id3v2Tag);
			mpfile.setID3v2Tag(newid3v2Tag);
			mpfile.save();
		}
	}

	private void setInformation(String name, String artistname, AbstractID3 id3) {
		id3.setSongTitle(name);
		id3.setLeadArtist(artistname);
		System.out.println("saving: " + artistname);
	}
}
