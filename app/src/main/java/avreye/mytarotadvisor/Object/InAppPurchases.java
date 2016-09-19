package avreye.mytarotadvisor.Object;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InAppPurchases {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private ArrayList<Message> message = new ArrayList<Message>();

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
    public ArrayList<Message> getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(ArrayList<Message> message) {
        this.message = message;
    }

    public static class Message {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("credits")
        @Expose
        private String credits;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("description")
        @Expose
        private String description;

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
         * @return The productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         * @param productId The product_id
         */
        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         * @return The credits
         */
        public String getCredits() {
            return credits;
        }

        /**
         * @param credits The credits
         */
        public void setCredits(String credits) {
            this.credits = credits;
        }

        /**
         * @return The price
         */
        public String getPrice() {
            return price;
        }

        /**
         * @param price The price
         */
        public void setPrice(String price) {
            this.price = price;
        }

        /**
         * @return The description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description The description
         */
        public void setDescription(String description) {
            this.description = description;
        }

    }
}