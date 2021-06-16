package sp.ham.cat02team118;

public class CartData {
    private String ItemName, ItemPrice, ItemVaration, NumofItem, ItemImage;

    public CartData(String itemName, String itemPrice, String itemVaration, String numofItem, String itemImage) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemVaration = itemVaration;
        NumofItem = numofItem;
        ItemImage = itemImage;
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

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
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

    public String getItemImage() {
        return ItemImage;
    }
}
