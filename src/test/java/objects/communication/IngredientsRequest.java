package objects.communication;


import com.google.gson.annotations.SerializedName;

public class IngredientsRequest {

    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }


}
