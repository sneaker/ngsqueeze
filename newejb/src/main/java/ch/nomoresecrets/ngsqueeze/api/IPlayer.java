package ch.nomoresecrets.ngsqueeze.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public interface IPlayer {
	public void play(long id) throws UnknownHostException, IOException, URISyntaxException;
}
