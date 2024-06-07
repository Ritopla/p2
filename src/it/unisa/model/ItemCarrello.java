package it.unisa.model;

public class ItemCarrello {

	public ItemCarrello(ProdottoBean prodotto){
		this.prodotto = prodotto;
		quantityCarrello = 1;
	}
	
	public ProdottoBean getProdotto() {
		return prodotto;
	}
	
	public void setProdotto(ProdottoBean prodotto) {
		this.prodotto = prodotto;
	}
	
	public int getQuantityCarrello() {
		return quantityCarrello;
	}
	
	public void setQuantityCarrello(int quantity) {
		this.quantityCarrello = quantity;
	}
	
	public int getId() {
		return prodotto.getIdProdotto();
	}
	
	public double getTotalPrice() {
		return quantityCarrello * prodotto.getPrezzo();
		

	}
	
	public String getDescription() {
		return prodotto.getDescrizione();
	}
	
	public void incrementa() {
		if(quantityCarrello < prodotto.getQuantity() )
			quantityCarrello = quantityCarrello + 1;
	}
	
	public void decrementa() {
		if( quantityCarrello > 1)
			quantityCarrello = quantityCarrello - 1;
	}
	
	private ProdottoBean prodotto;
	private int quantityCarrello;
}