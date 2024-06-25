package lk.ijse.demokushan.model.TM;

public class FeedbackTM {
    private String feedbackId;
    private String comment;

    private String appointmentId;



    public FeedbackTM() {

    }

    public FeedbackTM(String feedbackId, String comment,  String appointmentId) {
        this.feedbackId = feedbackId;
        this.comment = comment;
        this.appointmentId = appointmentId;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return "feedbackTM{" +
                "feedbackId='" + feedbackId + '\'' +
                ", comment='" + comment + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                '}';
    }
}
