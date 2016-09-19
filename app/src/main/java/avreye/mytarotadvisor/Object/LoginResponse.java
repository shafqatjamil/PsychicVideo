package avreye.mytarotadvisor.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user")
    @Expose
    private User user;

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
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
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
        @SerializedName("display_order")
        @Expose
        private String displayOrder;
        @SerializedName("profile_picture")
        @Expose
        private String profilePicture;
        @SerializedName("deactivated")
        @Expose
        private String deactivated;
        @SerializedName("udid")
        @Expose
        private String udid;
        @SerializedName("total_credit")
        @Expose
        private String totalCredit;
        @SerializedName("total_orders")
        @Expose
        private String totalOrders;
        @SerializedName("dob")
        @Expose
        private String dob;

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
         * @return The email
         */
        public String getEmail() {
            return email;
        }

        /**
         * @param email The email
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @return The username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username The username
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return The password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password The password
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * @return The firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @param firstName The first_name
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * @return The lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName The last_name
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         * @return The bioData
         */
        public String getBioData() {
            return bioData;
        }

        /**
         * @param bioData The bio_data
         */
        public void setBioData(String bioData) {
            this.bioData = bioData;
        }

        /**
         * @return The displayPicture
         */
        public String getDisplayPicture() {
            return displayPicture;
        }

        /**
         * @param displayPicture The display_picture
         */
        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        /**
         * @return The introVideo
         */
        public String getIntroVideo() {
            return introVideo;
        }

        /**
         * @param introVideo The intro_video
         */
        public void setIntroVideo(String introVideo) {
            this.introVideo = introVideo;
        }

        /**
         * @return The videoThumb
         */
        public String getVideoThumb() {
            return videoThumb;
        }

        /**
         * @param videoThumb The video_thumb
         */
        public void setVideoThumb(String videoThumb) {
            this.videoThumb = videoThumb;
        }

        /**
         * @return The status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * @return The isPsychic
         */
        public String getIsPsychic() {
            return isPsychic;
        }

        /**
         * @param isPsychic The is_psychic
         */
        public void setIsPsychic(String isPsychic) {
            this.isPsychic = isPsychic;
        }

        /**
         * @return The shortDescription
         */
        public String getShortDescription() {
            return shortDescription;
        }

        /**
         * @param shortDescription The short_description
         */
        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        /**
         * @return The instructions
         */
        public String getInstructions() {
            return instructions;
        }

        /**
         * @param instructions The instructions
         */
        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        /**
         * @return The displayOrder
         */
        public String getDisplayOrder() {
            return displayOrder;
        }

        /**
         * @param displayOrder The display_order
         */
        public void setDisplayOrder(String displayOrder) {
            this.displayOrder = displayOrder;
        }

        /**
         * @return The profilePicture
         */
        public String getProfilePicture() {
            return profilePicture;
        }

        /**
         * @param profilePicture The profile_picture
         */
        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        /**
         * @return The deactivated
         */
        public String getDeactivated() {
            return deactivated;
        }

        /**
         * @param deactivated The deactivated
         */
        public void setDeactivated(String deactivated) {
            this.deactivated = deactivated;
        }

        /**
         * @return The udid
         */
        public String getUdid() {
            return udid;
        }

        /**
         * @param udid The udid
         */
        public void setUdid(String udid) {
            this.udid = udid;
        }

        /**
         * @return The totalCredit
         */
        public String getTotalCredit() {
            return totalCredit;
        }

        /**
         * @param totalCredit The total_credit
         */
        public void setTotalCredit(String totalCredit) {
            this.totalCredit = totalCredit;
        }

        /**
         * @return The totalOrders
         */
        public String getTotalOrders() {
            return totalOrders;
        }

        /**
         * @param totalOrders The total_orders
         */
        public void setTotalOrders(String totalOrders) {
            this.totalOrders = totalOrders;
        }

        /**
         * @return The dob
         */
        public String getDob() {
            return dob;
        }

        /**
         * @param dob The dob
         */
        public void setDob(String dob) {
            this.dob = dob;
        }

    }
}

