package avreye.mytarotadvisor.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payload {

    @SerializedName("message_type")
    @Expose
    private String messageType;
    @SerializedName("msg_date")
    @Expose
    private String msgDate;
    @SerializedName("msg_text")
    @Expose
    private String msgText;
    @SerializedName("pn_apns")
    @Expose
    private PnApns pnApns;


    @SerializedName("pn_gcm")
    @Expose
    private PnGcm pnGcm;


    @SerializedName("receiver_displayname")
    @Expose
    private String receiverDisplayname;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;
    @SerializedName("receiver_type")
    @Expose
    private String receiverType;
    @SerializedName("sender_displayname")
    @Expose
    private String senderDisplayname;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("sender_type")
    @Expose
    private String senderType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("review_status")
    @Expose
    private String reviewStatus;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("message_review_id")
    @Expose
    private String reviewId;

    @SerializedName("response_time")
    @Expose
    private String reponseTime;

    @SerializedName("client_dob")
    @Expose
    private String dob;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getReponseTime() {
        return reponseTime;
    }

    public void setReponseTime(String reponseTime) {
        this.reponseTime = reponseTime;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message_type\":\""   +   messageType + '\"' +
                ", \"msg_date\":\"" + msgDate + '\"' +
                ", \"msg_text\":\"" + msgText + '\"' +
                ", \"pn_apns\":" + pnApns +
                ", \"pn_gcm\":" + pnGcm +
                ", \"receiver_displayname\":\"" + receiverDisplayname + '\"' +
                ", \"receiver_id\":\"" + receiverId + '\"' +
                ", \"receiver_type\":\"" + receiverType + '\"' +
                ", \"sender_displayname\":\"" + senderDisplayname + '\"' +
                ", \"sender_id\":\"" + senderId + '\"' +
                ", \"sender_type\":\"" + senderType + '\"' +
                ", \"status\":\"" + status + '\"' +
                ", \"review_status\":\"" + reviewStatus + '\"' +
                ", \"video_url\":\"" + videoUrl + '\"' +
                ", \"message_review_id\":\"" + reviewId + '\"' +
                ", \"response_time\":\"" + reponseTime + '\"' +
                ", \"client_dob\":\"" + dob + '\"' +
                '}';
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
     * The pnApns
     */
    public PnApns getPnApns() {
        return pnApns;
    }

    /**
     *
     * @param pnApns
     * The pn_apns
     */
    public void setPnApns(PnApns pnApns) {
        this.pnApns = pnApns;
    }
    public PnGcm getPnGcm() {
        return pnGcm;
    }

    /**
     *
     * @param pnGcm
     * The pn_apns
     */
    public void setPnGcm(PnGcm pnGcm) {
        this.pnGcm = pnGcm;
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

    public static class PnApns {

        @SerializedName("aps")
        @Expose
        private Aps aps;

        @Override
        public String toString() {
            return "{" +
                    "\"aps\":" + aps +
                    '}';
        }

        /**
         *
         * @return
         * The aps
         */
        public Aps getAps() {
            return aps;
        }

        /**
         *
         * @param aps
         * The aps
         */
        public void setAps(Aps aps) {
            this.aps = aps;
        }

    }

    public static class Aps implements Serializable{

        @SerializedName("alert")
        @Expose
        private String alert;
        @SerializedName("appType")
        @Expose
        private String appType;
        @SerializedName("badge")
        @Expose
        private Integer badge;
        @SerializedName("sender_Id")
        @Expose
        private String senderId;
        @SerializedName("sender_displayname")
        @Expose
        private String senderDisplayname;
        @SerializedName("sound")
        @Expose
        private String sound;

        @Override
        public String toString() {
            return "{" +
                    "\"alert\":\"" + alert + '\"' +
                    ", \"appType\":\"" + appType + '\"' +
                    ", \"badge\":\"" + badge + '\"' +
                    ", \"sender_Id\":\"" + senderId + '\"' +
                    ", \"sender_displayname\":\"" + senderDisplayname + '\"' +
                    ", \"sound\":\"" + sound + '\"' +
                    '}';
        }

        public String getAlert() {
            return alert;
        }
        public void setAlert(String alert) {
            this.alert = alert;
        }
        public String getAppType() {
            return appType;
        }
        public void setAppType(String appType) {
            this.appType = appType;
        }
        public Integer getBadge() {
            return badge;
        }
        public void setBadge(Integer badge) {
            this.badge = badge;
        }
        public String getSenderId() {
            return senderId;
        }
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
        public String getSenderDisplayname() {
            return senderDisplayname;
        }
        public void setSenderDisplayname(String senderDisplayname) {
            this.senderDisplayname = senderDisplayname;
        }
        public String getSound() {
            return sound;
        }
        public void setSound(String sound) {
            this.sound = sound;
        }

    }

    public static class PnGcm {

        @SerializedName("data")
        @Expose
        private Data data;

        @Override
        public String toString() {
            return "{" +
                    "\"data\":" + data +
                    '}';
        }

        /**
         *
         * @return
         * The aps
         */
        public Data getData() {
            return data;
        }

        /**
         *
         * @param data
         * The aps
         */
        public void setData(Data data) {
            this.data = data;
        }

    }
    public static class Data implements Serializable{

        @SerializedName("alert")
        @Expose
        private String alert;
        @SerializedName("appType")
        @Expose
        private String appType;
        @SerializedName("badge")
        @Expose
        private Integer badge;
        @SerializedName("sender_Id")
        @Expose
        private String senderId;
        @SerializedName("sender_displayname")
        @Expose
        private String senderDisplayname;
        @SerializedName("sound")
        @Expose
        private String sound;

        @Override
        public String toString() {
            return "{" +
                    "\"alert\":\"" + alert + '\"' +
                    ", \"appType\":\"" + appType + '\"' +
                    ", \"badge\":\"" + badge + '\"' +
                    ", \"sender_Id\":\"" + senderId + '\"' +
                    ", \"sender_displayname\":\"" + senderDisplayname + '\"' +
                    ", \"sound\":\"" + sound + '\"' +
                    '}';
        }

        public String getAlert() {
            return alert;
        }
        public void setAlert(String alert) {
            this.alert = alert;
        }
        public String getAppType() {
            return appType;
        }
        public void setAppType(String appType) {
            this.appType = appType;
        }
        public Integer getBadge() {
            return badge;
        }
        public void setBadge(Integer badge) {
            this.badge = badge;
        }
        public String getSenderId() {
            return senderId;
        }
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
        public String getSenderDisplayname() {
            return senderDisplayname;
        }
        public void setSenderDisplayname(String senderDisplayname) {
            this.senderDisplayname = senderDisplayname;
        }
        public String getSound() {
            return sound;
        }
        public void setSound(String sound) {
            this.sound = sound;
        }

    }
}
