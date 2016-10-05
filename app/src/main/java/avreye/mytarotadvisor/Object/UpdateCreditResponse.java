package avreye.mytarotadvisor.Object;

/**
 * Created by shafqat on 10/3/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCreditResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;

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

}