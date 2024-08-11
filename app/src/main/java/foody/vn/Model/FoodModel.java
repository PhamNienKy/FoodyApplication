package foody.vn.Model;

public class FoodModel {
    String food_id, food_name, image_food;
    long price;


    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getImage_food() {
        return image_food;
    }

    public void setImage_food(String image_food) {
        this.image_food = image_food;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
