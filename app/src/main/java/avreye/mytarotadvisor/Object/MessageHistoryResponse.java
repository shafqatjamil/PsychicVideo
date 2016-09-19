package avreye.mytarotadvisor.Object;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;
        import java.util.List;


public class MessageHistoryResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private ArrayList<Message> message = new ArrayList<Message>();

    /**
     *
     * @return
     * The result
     */
    public Integer getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The message
     */
    public ArrayList<Message> getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(ArrayList<Message> message) {
        this.message = message;
    }
    public class Message {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("message_review_id")
        @Expose
        private String messageReviewId;
        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("sender_displayname")
        @Expose
        private String senderDisplayname;
        @SerializedName("sender_type")
        @Expose
        private String senderType;
        @SerializedName("receiver_id")
        @Expose
        private String receiverId;
        @SerializedName("receiver_displayname")
        @Expose
        private String receiverDisplayname;
        @SerializedName("receiver_type")
        @Expose
        private String receiverType;
        @SerializedName("message_type")
        @Expose
        private String messageType;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("review_status")
        @Expose
        private String reviewStatus;
        @SerializedName("video_url")
        @Expose
        private String videoUrl;
        @SerializedName("response_time")
        @Expose
        private String responseTime;
        @SerializedName("msg_text")
        @Expose
        private String msgText;
        @SerializedName("msg_date")
        @Expose
        private String msgDate;
        @SerializedName("client_dob")
        @Expose
        private String clientDob;

        /**
         *
         * @return
         * The id
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @param id
         * The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         *
         * @return
         * The messageReviewId
         */
        public String getMessageReviewId() {
            return messageReviewId;
        }

        /**
         *
         * @param messageReviewId
         * The message_review_id
         */
        public void setMessageReviewId(String messageReviewId) {
            this.messageReviewId = messageReviewId;
        }

        /**
         *
         * @return
         * The senderId
         */
        public String getSenderId() {
            return senderId;
        }

        /**
         *
         * @param senderId
         * The sender_id
         */
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        /**
         *
         * @return
         * The senderDisplayname
         */
        public String getSenderDisplayname() {
            return senderDisplayname;
        }

        /**
         *
         * @param senderDisplayname
         * The sender_displayname
         */
        public void setSenderDisplayname(String senderDisplayname) {
            this.senderDisplayname = senderDisplayname;
        }

        /**
         *
         * @return
         * The senderType
         */
        public String getSenderType() {
            return senderType;
        }

        /**
         *
         * @param senderType
         * The sender_type
         */
        public void setSenderType(String senderType) {
            this.senderType = senderType;
        }

        /**
         *
         * @return
         * The receiverId
         */
        public String getReceiverId() {
            return receiverId;
        }

        /**
         *
         * @param receiverId
         * The receiver_id
         */
        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        /**
         *
         * @return
         * The receiverDisplayname
         */
        public String getReceiverDisplayname() {
            return receiverDisplayname;
        }

        /**
         *
         * @param receiverDisplayname
         * The receiver_displayname
         */
        public void setReceiverDisplayname(String receiverDisplayname) {
            this.receiverDisplayname = receiverDisplayname;
        }

        /**
         *
         * @return
         * The receiverType
         */
        public String getReceiverType() {
            return receiverType;
        }

        /**
         *
         * @param receiverType
         * The receiver_type
         */
        public void setReceiverType(String receiverType) {
            this.receiverType = receiverType;
        }

        /**
         *
         * @return
         * The messageType
         */
        public String getMessageType() {
            return messageType;
        }

        /**
         *
         * @param messageType
         * The message_type
         */
        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The reviewStatus
         */
        public String getReviewStatus() {
            return reviewStatus;
        }

        /**
         *
         * @param reviewStatus
         * The review_status
         */
        public void setReviewStatus(String reviewStatus) {
            this.reviewStatus = reviewStatus;
        }

        /**
         *
         * @return
         * The videoUrl
         */
        public String getVideoUrl() {
            return videoUrl;
        }

        /**
         *
         * @param videoUrl
         * The video_url
         */
        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        /**
         *
         * @return
         * The responseTime
         */
        public String getResponseTime() {
            return responseTime;
        }

        /**
         *
         * @param responseTime
         * The response_time
         */
        public void setResponseTime(String responseTime) {
            this.responseTime = responseTime;
        }

        /**
         *
         * @return
         * The msgText
         */
        public String getMsgText() {
            return msgText;
        }

        /**
         *
         * @param msgText
         * The msg_text
         */
        public void setMsgText(String msgText) {
            this.msgText = msgText;
        }

        /**
         *
         * @return
         * The msgDate
         */
        public String getMsgDate() {
            return msgDate;
        }

        /**
         *
         * @param msgDate
         * The msg_date
         */
        public void setMsgDate(String msgDate) {
            this.msgDate = msgDate;
        }

        /**
         *
         * @return
         * The clientDob
         */
        public String getClientDob() {
            return clientDob;
        }

        /**
         *
         * @param clientDob
         * The client_dob
         */
        public void setClientDob(String clientDob) {
            this.clientDob = clientDob;
        }

    }

}