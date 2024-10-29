package models;

public class ConectionsModel {
	private String id;
	private String noOrigem;
	private String noDestino;
	
	public ConectionsModel() {
		
	}
	
	public ConectionsModel(String id, String noOrigem, String noDestino) {
		this.id = id;
		this.noOrigem = noOrigem;
		this.noDestino = noDestino;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoOrigem() {
		return noOrigem;
	}

	public void setNoOrigem(String noOrigem) {
		this.noOrigem = noOrigem;
	}

	public String getNoDestino() {
		return noDestino;
	}

	public void setNoDestino(String noDestino) {
		this.noDestino = noDestino;
	}
}
