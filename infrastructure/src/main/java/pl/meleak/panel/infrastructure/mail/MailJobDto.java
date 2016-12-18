package pl.meleak.panel.infrastructure.mail;

/**
 * Created by maxmati on 12/18/16
 */
public class MailJobDto {
    private boolean sent;
    private String from;
    private String to;
    private String subject;
    private String body;
    private Integer id;

    public MailJobDto() {
    }

    public MailJobDto(Integer id, boolean sent, String from, String to, String subject, String body) {
        this.id = id;
        this.sent = sent;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public boolean getSent() {
        return sent;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
