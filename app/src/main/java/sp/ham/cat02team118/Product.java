package sp.ham.cat02team118;

public class Product {

    String description,name,photo,price;

    public Product() {
    }

    public Product(String description, String name, String photo, String price) {
        this.description = description;
        this.name = name;
        this.photo = photo;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
