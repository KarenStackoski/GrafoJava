package models;

public class WeightModel {
	private String id;
	private String noOrigem;
	private String noAdjacente;
	
	public WeightModel() {
		
	}
	
	public WeightModel(String id, String noOrigem, String noAdjacente) {
		this.id = id;
		this.noOrigem = noOrigem;
		this.noAdjacente = noAdjacente;
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

	public String getNoAdjacente() {
		return noAdjacente;
	}

	public void setNoAdjacente(String noAdjacente) {
		this.noAdjacente = noAdjacente;
	}
}
