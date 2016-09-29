package avreye.mytarotadvisor.Object;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoCodeReponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("credit")
    @Expose
    private String credit;

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return The credit
     */
    public String getCredit() {
        return credit;
    }

    /**
     * @param credit The credit
     */
    public void setCredit(String credit) {
        this.credit = credit;
    }

}