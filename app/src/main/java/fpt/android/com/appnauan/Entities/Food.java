package fpt.android.com.appnauan.Entities;

public class Food {

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

    public Food() {
    }

    public Food(String foodName, String meat, String vegetable, String recipe, String type) {
        this.name = foodName;
        this.meat = meat;
        this.vegetable = vegetable;
        this.recipe = recipe;
        this.type = type;
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

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", meat='" + meat + '\'' +
                ", vegetable='" + vegetable + '\'' +
                ", recipe='" + recipe + '\'' +
                ", type=" + type +
                '}';
    }
}
