package isd.aims.main.entity.invoice;


import isd.aims.main.entity.order.Order;

public class Invoice {

    private Order order;
    private int amount;

    public Invoice(Order order){
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void saveInvoice(){
        
    }
}
