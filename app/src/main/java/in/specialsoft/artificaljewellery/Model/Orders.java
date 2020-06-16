package in.specialsoft.artificaljewellery.Model;

public class Orders {
    private String orderID,state;

    public Orders() {
    }

    public Orders(String orderID, String state) {
        this.orderID = orderID;
        this.state = state;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
