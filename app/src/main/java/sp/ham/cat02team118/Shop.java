package sp.ham.cat02team118;

public class Shop {

    String description,name,photo,address,category;

    public Shop() {
    }

    public Shop(String description, String name, String photo, String address,String category) {
        this.description = description;
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.category = category;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
