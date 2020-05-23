package entity;

public class Billboard {

    private String billboardID;
    private int inf; // influence
    private int price; // price

    public Billboard(String billboardID, int inf, int price) {
        this.billboardID = billboardID;
        this.inf = inf;
        this.price = price;
    }

    public String getBillboardID() {
        return billboardID;
    }

    public int getInf() {
        return inf;
    }

    public int getPrice() {
        return price;
    }

    public double getMarginalInf(){
        return (double)inf/price;
    }
}
