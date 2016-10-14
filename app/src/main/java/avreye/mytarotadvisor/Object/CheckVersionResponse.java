package avreye.mytarotadvisor.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckVersionResponse {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("backend")
    @Expose
    private List<Backend> backend = new ArrayList<Backend>();

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
     * @return The backend
     */
    public List<Backend> getBackend() {
        return backend;
    }

    /**
     * @param backend The backend
     */
    public void setBackend(List<Backend> backend) {
        this.backend = backend;
    }

    public class Backend {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("version")
        @Expose
        private String version;

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
         * @return The version
         */
        public String getVersion() {
            return version;
        }

        /**
         * @param version The version
         */
        public void setVersion(String version) {
            this.version = version;
        }

    }

}