package avreye.mytarotadvisor.helper;
import avreye.mytarotadvisor.Object.AdvisorProfilePictureResponse;
import avreye.mytarotadvisor.Object.GetAdvisorStatusReponse;
import avreye.mytarotadvisor.Object.GetMyCreditsResponse;
import avreye.mytarotadvisor.Object.GetTarotResponse;
import avreye.mytarotadvisor.Object.InAppPurchases;
import avreye.mytarotadvisor.Object.LoginResponse;
import avreye.mytarotadvisor.Object.Message;
import avreye.mytarotadvisor.Object.MessageHistoryResponse;
import avreye.mytarotadvisor.Object.RegistrationResponse;
import avreye.mytarotadvisor.Object.SignUpBonusResponse;
import avreye.mytarotadvisor.Object.UpdateStatusResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by null on 4/21/2016.
 */
public interface APIService {
    @FormUrlEncoded
    @POST("createClient")
    Call<RegistrationResponse> RegisterUser(@Field("username") String name, @Field("password")
    String password, @Field("email") String email,
                                            @Field("udid") String udid,
                                            @Field("total_credit") String totalcredit,
                                            @Field("total_orders") String totalorders,
                                            @Field("dob") String dob);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> LoginUser(@Field("email") String name, @Field("password")String password);

    @FormUrlEncoded
    @POST("updateStatus")
    Call<UpdateStatusResponse> UpdateStatus(@Field("id") String id, @Field("status")String status);

    @GET("tarotcard")
    Call<GetTarotResponse> getTarot();


    @FormUrlEncoded
    @POST("getStatus")
    Call<GetAdvisorStatusReponse> getStatus(@Field("id") String id);

    @FormUrlEncoded
    @POST("forgetPassword")
    Call<GetAdvisorStatusReponse> ForgotPassword(@Field("email") String email);

    @GET("getSignUpBonus")
    Call<SignUpBonusResponse> getSignUpBonus();

    @FormUrlEncoded
    @POST("getAdvisorProfilePicture")
    Call<AdvisorProfilePictureResponse> getAdvisorPicture(@Field("id") String id);


    @GET("getCredit")
    Call<InAppPurchases> GetCreditsDetail();

    @FormUrlEncoded
    @POST("history")
    Call<MessageHistoryResponse> getHistory(@Field("id") String id, @Field("type") String type);

    @FormUrlEncoded
    @POST("updateCredits")
    Call<MessageHistoryResponse> updateCredits(@Field("id") String id, @Field("total_credit") String credits);

    @FormUrlEncoded
    @POST("getCredits")
    Call<GetMyCreditsResponse> getMyCredits(@Field("id") String id);

    @FormUrlEncoded
    @POST("createReview")
    Call<GetMyCreditsResponse> postReview(@Field("advisor_id") String advisor_id,
                                          @Field("client_id") String client_id,
                                          @Field("review") String review,
                                          @Field("comment") String comment,
                                          @Field("message_review_id") String message_review_id);


}
