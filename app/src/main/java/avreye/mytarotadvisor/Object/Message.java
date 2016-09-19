package avreye.mytarotadvisor.Object;

/**
 * Created by null on 4/29/2016.
 */
public class Message{
    private  int  id;
    private  String sender_id;
    private  String sender_display_name;
    private  String reciever_id;
    private  String reciever_display_name;
    private  String text;
    private  String date;
    private  int status;
    private  String url;
    private  String type;
    private  String sender_type;
    private  String reciever_type;
    private  String review_status;
    private  String message_review_id;
    private String rid;
    public Message()
    {
        this.id = 0;
        this.sender_id = "";
        this.sender_display_name = "";
        this.reciever_id = "";
        this.reciever_display_name = "";
        this.text = "";
        this.date = "";
        this.status = 0;
        this.url = "";
        this.type = "";
        this.sender_type = "";
        this.reciever_type = "";
        this.review_status = "";
        this.message_review_id = "";
        rid = "";
    }
    public Message(int id, String sender_id, String sender_display_name, String reciever_id,
                   String reciever_display_name, String text, String date, int status, String url,
                   String type, String sender_type, String reciever_type, String review_status,
                   String message_review_id, String rid)
    {
        this.id = id;
        this.sender_id = sender_id;
        this.sender_display_name = sender_display_name;
        this.reciever_id = reciever_id;
        this.reciever_display_name = reciever_display_name;
        this.text = text;
        this.date = date;
        this.status = status;
        this.url = url;
        this.type = type;
        this.sender_type = sender_type;
        this.reciever_type = reciever_type;
        this.review_status = review_status;
        this.message_review_id = message_review_id;
        this.rid = rid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setMessage_review_id(String message_review_id) {
        this.message_review_id = message_review_id;
    }
    public String getMessage_review_id() {
        return message_review_id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_display_name() {
        return sender_display_name;
    }

    public void setSender_display_name(String sender_display_name) {
        this.sender_display_name = sender_display_name;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getReciever_display_name() {
        return reciever_display_name;
    }

    public void setReciever_display_name(String reciever_display_name) {
        this.reciever_display_name = reciever_display_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getReciever_type() {
        return reciever_type;
    }

    public void setReciever_type(String reciever_type) {
        this.reciever_type = reciever_type;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

   /* public JSONObject getJsonFromMessage(Message message){
        Bundle b = new Bundle();
        b.putString();
    }*/
}
