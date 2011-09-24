package ch.nomoresecrets.ngsqueeze.presentation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

import ch.nomoresecrets.ngsqueeze.api.IMyIndexer;

@ManagedBean(name="indexBean")
@RequestScoped
public class IndexerBean {
	
	@EJB
	private IMyIndexer indexer;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void indexDir() {
		if (path != null) {
			indexer.indexDir(path);
		}
	}
}
