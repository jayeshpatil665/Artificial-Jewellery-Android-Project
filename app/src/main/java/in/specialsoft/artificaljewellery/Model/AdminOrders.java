package in.specialsoft.artificaljewellery.Model;

public class AdminOrders {
    private String address,date,name,orderID,phone,state,time,totalAmount,userID;

    public AdminOrders() {
    }

    public AdminOrders(String address, String date, String name, String orderID, String phone, String state, String time, String totalAmount, String userID) {
        this.address = address;
        this.date = date;
        this.name = name;
        this.orderID = orderID;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
