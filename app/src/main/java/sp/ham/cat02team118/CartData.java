package sp.ham.cat02team118;

public class CartData {
    private String ItemName;
    private String ItemPrice;
    private String ItemVaration;
    private String NumofItem;
    private Integer itemImage;

    public CartData(String itemName, String itemPrice, String itemVaration, String numofItem, Integer itemImage) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemVaration = itemVaration;
        NumofItem = numofItem;
        this.itemImage = itemImage;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public void setItemVaration(String itemVaration) {
        ItemVaration = itemVaration;
    }

    public void setNumofItem(String numofItem) {
        NumofItem = numofItem;
    }

    public void setMovieImage(Integer itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public String getItemVaration() {
        return ItemVaration;
    }

    public String getNumofItem() {
        return NumofItem;
    }

    public Integer getItemImage() {
        return itemImage;
    }
}
