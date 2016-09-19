package avreye.mytarotadvisor.Object;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by null on 4/26/2016.
 */
public class AdvisorInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("bio_data")
    @Expose
    private String bioData;
    @SerializedName("display_picture")
    @Expose
    private String displayPicture;
    @SerializedName("intro_video")
    @Expose
    private String introVideo;
    @SerializedName("video_thumb")
    @Expose
    private String videoThumb;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_psychic")
    @Expose
    private String isPsychic;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("reviews")
    @Expose
    private ArrayList<Review> reviews = new ArrayList<Review>();

    @SerializedName("user_rating")
    @Expose
    private float rating;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;

    @SerializedName("display_order")
    @Expose
    private String displayorder;
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
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     * The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     * The bioData
     */
    public String getBioData() {
        return bioData;
    }

    /**
     *
     * @param bioData
     * The bio_data
     */
    public void setBioData(String bioData) {
        this.bioData = bioData;
    }

    /**
     *
     * @return
     * The displayPicture
     */
    public String getDisplayPicture() {
        return displayPicture;
    }

    /**
     *
     * @param displayPicture
     * The display_picture
     */
    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    /**
     *
     * @return
     * The introVideo
     */
    public String getIntroVideo() {
        return introVideo;
    }

    /**
     *
     * @param introVideo
     * The intro_video
     */
    public void setIntroVideo(String introVideo) {
        this.introVideo = introVideo;
    }

    /**
     *
     * @return
     * The videoThumb
     */
    public String getVideoThumb() {
        return videoThumb;
    }

    /**
     *
     * @param videoThumb
     * The video_thumb
     */
    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
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
     * The isPsychic
     */
    public String getIsPsychic() {
        return isPsychic;
    }

    /**
     *
     * @param isPsychic
     * The is_psychic
     */
    public void setIsPsychic(String isPsychic) {
        this.isPsychic = isPsychic;
    }

    /**
     *
     * @return
     * The shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     *
     * @param shortDescription
     * The short_description
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     *
     * @return
     * The instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     *
     * @param instructions
     * The instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     *
     * @return
     * The reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     *
     * @param reviews
     * The reviews
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
    /**
     *
     * @return
     * The rating
     */
    public float getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    public class Review {

        @SerializedName("client_username")
        @Expose
        private String clientUsername;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        /**
         *
         * @return
         * The clientUsername
         */
        public String getClientUsername() {
            return clientUsername;
        }

        /**
         *
         * @param clientUsername
         * The client_username
         */
        public void setClientUsername(String clientUsername) {
            this.clientUsername = clientUsername;
        }

        /**
         *
         * @return
         * The review
         */
        public String getReview() {
            return review;
        }

        /**
         *
         * @param review
         * The review
         */
        public void setReview(String review) {
            this.review = review;
        }

        /**
         *
         * @return
         * The comment
         */
        public String getComment() {
            return comment;
        }

        /**
         *
         * @param comment
         * The comment
         */
        public void setComment(String comment) {
            this.comment = comment;
        }

        /**
         *
         * @return
         * The createdAt
         */
        public String getCreatedAt() {
            return createdAt;
        }

        /**
         *
         * @param createdAt
         * The created_at
         */
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }


    /**
     *
     * @return
     * The status
     */
    public String getDeactivated() {
        return deactivated;
    }

    /**
     *
     * @param deactivated
     * The status
     */
    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }





    /**
     *
     * @return
     * The status
     */
    public String getDisplayorder() {
        return displayorder;
    }

    /**
     *
     * @param displayorder
     * The status
     */
    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

}