package sg.edu.nus.pafworkshop21.workshop21.models;

public class FirebaseNotification {
    private String title;
    private String message;
    private String target;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
}
