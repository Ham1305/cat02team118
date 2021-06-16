package sp.ham.cat02team118;

public class ShopProdData {
    private String Shopname, Productname, Productprice, Productdescription, Productimage;

    public ShopProdData(String shopname, String productname, String productprice, String productdescription, String productimage) {
        this.Shopname = shopname;
        this.Productname = productname;
        this.Productprice = productprice;
        this.Productdescription = productdescription;
        this.Productimage = productimage;
    }

    public String getShopname() {
        return Shopname;
    }

    public void setShopname(String shopname) {
        this.Shopname = shopname;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        this.Productname = productname;
    }

    public String getProductprice() {
        return Productprice;
    }

    public void setProductprice(String productprice) {
        this.Productprice = productprice;
    }

    public String getProductdescription() {
        return Productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.Productdescription = productdescription;
    }

    public String getProductimage() {
        return Productimage;
    }

    public void setProductimage(String productimage) {
        this.Productimage = productimage;
    }
}
