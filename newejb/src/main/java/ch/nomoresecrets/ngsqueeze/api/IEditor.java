package ch.nomoresecrets.ngsqueeze.api;

import java.io.IOException;

import org.farng.mp3.TagException;

public interface IEditor {

		void updateArtist(long id, String name, boolean merge) throws IOException, TagException, Exception;

		void updateTrack(long id, String name) throws IOException, TagException;
}
