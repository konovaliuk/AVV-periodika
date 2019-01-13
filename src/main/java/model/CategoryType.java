package model;

public enum CategoryType {
    NEWSPAPER(1),
    MAGAZINE(2);
    private int id;

    CategoryType(int id) {
        this.id = id;
    }

    public static CategoryType findById(int id) {
        CategoryType[] values = values();
        for (CategoryType v : values) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
