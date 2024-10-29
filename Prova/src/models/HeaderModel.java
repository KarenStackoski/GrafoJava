package models;

public class HeaderModel {
	private String id;
	private String numeroNo;
	private String somaPesos;
	
	public HeaderModel() {
		
	}
	
	public HeaderModel(String id, String numeroNo, String somaPesos) {
		this.id = id;
		this.numeroNo = numeroNo;
		this.somaPesos = somaPesos; 
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroNo() {
		return numeroNo;
	}

	public void setNumeroNo(String numeroNo) {
		this.numeroNo = numeroNo;
	}

	public String getSomaPesos() {
		return somaPesos;
	}

	public void setSomaPesos(String somaPesos) {
		this.somaPesos = somaPesos;
	}
}
