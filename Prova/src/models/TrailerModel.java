package models;

public class TrailerModel {
	private String id;
	private String numeroConexao;
	private String numeroPesos;
	private String somaNos;
	
	public TrailerModel() {
		
	}
	
	public TrailerModel(String id, String numeroConexao, String numeroPesos, String somaNos) {
		this.id = id;
		this.numeroConexao = numeroConexao;
		this.numeroPesos = numeroPesos;
		this.somaNos = somaNos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroConexao() {
		return numeroConexao;
	}

	public void setNumeroConexao(String numeroConexao) {
		this.numeroConexao = numeroConexao;
	}

	public String getNumeroPesos() {
		return numeroPesos;
	}

	public void setNumeroPesos(String numeroPesos) {
		this.numeroPesos = numeroPesos;
	}

	public String getSomaNos() {
		return somaNos;
	}

	public void setSomaNos(String somaNos) {
		this.somaNos = somaNos;
	}
}
