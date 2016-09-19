package avreye.mytarotadvisor.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class SignUpBonusResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private List<Message> message = new ArrayList<Message>();

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
    public List<Message> getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public class Message {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("bonus")
        @Expose
        private String bonus;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The bonus
         */
        public String getBonus() {
            return bonus;
        }

        /**
         * @param bonus The bonus
         */
        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

    }
}