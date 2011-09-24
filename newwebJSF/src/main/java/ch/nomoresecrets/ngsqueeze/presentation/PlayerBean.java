package ch.nomoresecrets.ngsqueeze.presentation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import ch.nomoresecrets.ngsqueeze.player.IPlayer;

@ManagedBean(name = "playBean")
@RequestScoped
public class PlayerBean {

	@EJB
	private IPlayer player;
	
	@ManagedProperty("#{param.id}")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String play() throws UnknownHostException, IOException, URISyntaxException {
		System.out.println("played");
		player.play(id);
		return "play";
	}
}
