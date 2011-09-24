package ch.nomoresecrets.ngsqueeze.api;

import javax.ejb.Remote;

@Remote
public interface IMyIndexer {
	public void indexDir(String directory);
}
