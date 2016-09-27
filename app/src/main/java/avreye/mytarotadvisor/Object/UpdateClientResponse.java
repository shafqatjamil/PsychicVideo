package avreye.mytarotadvisor.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateClientResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private Boolean id;

    /**
     * @return The result
     */
    public Integer getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The id
     */
    public Boolean getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Boolean id) {
        this.id = id;
    }

}