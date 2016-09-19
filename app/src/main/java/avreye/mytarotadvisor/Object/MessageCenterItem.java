package avreye.mytarotadvisor.Object;

/**
 * Created by null on 5/12/2016.
 */
public class MessageCenterItem {
    private String ImageUrl;
    private  String LastSender;
    private  String LastMessage;
    private  String LastMessageTime;

    public MessageCenterItem(String imageUrl, String lastSender, String lastMessage, String lastMessageTime) {
        ImageUrl = imageUrl;
        LastSender = lastSender;
        LastMessage = lastMessage;
        LastMessageTime = lastMessageTime;
    }

    public MessageCenterItem()
    {
        this.ImageUrl = "";
        this.LastSender = "sjk";
        this.LastMessage = "PanamaLeaks";
        this.LastMessageTime = "12-12-2016-05-06";
    }
    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLastSender() {
        return LastSender;
    }

    public void setLastSender(String lastSender) {
        LastSender = lastSender;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return LastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        LastMessageTime = lastMessageTime;
    }


}
