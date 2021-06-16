package sp.ham.cat02team118;

public class ShopProdData {
    private String shopname, productname, productprice, productdescription, productimage;

    public ShopProdData(String shopname, String productname, String productprice, String productdescription, String productimage) {
        this.shopname = shopname;
        this.productname = productname;
        this.productprice = productprice;
        this.productdescription = productdescription;
        this.productimage = productimage;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }
}
