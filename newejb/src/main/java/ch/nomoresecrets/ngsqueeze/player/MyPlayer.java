package ch.nomoresecrets.ngsqueeze.player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ch.nomoresecrets.ngsqueeze.api.IPlayer;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@Stateless
public class MyPlayer implements IPlayer {

	@PersistenceContext
	private EntityManager manager;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void play(long id) throws UnknownHostException, IOException, URISyntaxException {
		Track track = manager.find(Track.class, id);
		String path = track.getLocation();
		path = path.replace("/media/nfs/", "/srv/nfs/public/");
		path = path.replace(" ", "%20");
		Socket s = new Socket("192.168.5.1", 9090);
		
		DataOutputStream output = new DataOutputStream(s.getOutputStream());
		String command = "playlist play " + path + "\n";
		System.err.println("sending command: " + command);
		output.writeBytes(command);
		output.flush();
		s.close();
	}

}
