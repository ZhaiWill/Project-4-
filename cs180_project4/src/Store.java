import java.io.Serializable;

public class Store implements Serializable {
    String storeName;
    String OwnerUserName;

    public Store(String name, String ownerUserName) {
        this.storeName = name;
        this.OwnerUserName = ownerUserName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getownerUserName() {
        return OwnerUserName;
    }


    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", OwnerUserName='" + OwnerUserName + '\'' +
                '}';
    }
}
