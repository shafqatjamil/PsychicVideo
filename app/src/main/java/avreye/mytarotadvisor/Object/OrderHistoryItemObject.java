package avreye.mytarotadvisor.Object;

/**
 * Created by shafqat on 9/27/16.
 */

public class OrderHistoryItemObject {

    String OrderDate;
    String AdvisorName;
    String Deliverd;

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getAdvisorName() {
        return AdvisorName;
    }

    public void setAdvisorName(String advisorName) {
        AdvisorName = advisorName;
    }

    public String getDeliverd() {
        return Deliverd;
    }

    public void setDeliverd(String deliverd) {
        Deliverd = deliverd;
    }
}
