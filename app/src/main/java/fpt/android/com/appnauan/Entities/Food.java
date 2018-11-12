package fpt.android.com.appnauan.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    protected Food(Parcel in) {
        name = in.readString();
        meat = in.readString();
        vegetable = in.readString();
        recipe = in.readString();
        type = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(meat);
        parcel.writeString(vegetable);
        parcel.writeString(recipe);
        parcel.writeString(type);
        parcel.writeString(imageUrl);
    }

    public enum Type{
        MAN("Món mặn"),
        CHAY("Món chay"),
        TRANG_MIENG("Tráng miệng");

        private String value;

        Type(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String name;
    private String meat;
    private String vegetable;
    private String recipe;
    private String type;
    private String imageUrl;

    public Food() {
    }

    public Food(String name, String meat, String vegetable, String recipe, String type, String imageUrl) {
        this.name = name;
        this.meat = meat;
        this.vegetable = vegetable;
        this.recipe = recipe;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }

    public String getVegetable() {
        return vegetable;
    }

    public void setVegetable(String vegetable) {
        this.vegetable = vegetable;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", meat='" + meat + '\'' +
                ", vegetable='" + vegetable + '\'' +
                ", recipe='" + recipe + '\'' +
                ", type='" + type + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
