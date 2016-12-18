package pl.memleak.panel.dal.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by maxmati on 12/18/16
 */
@Entity
public class DbMail {

    @Id
    @GeneratedValue
    private Integer id;
    private String sender;
    private String recipient;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;
    private boolean sent;

    public DbMail() {
    }

    public DbMail(Integer id, String sender, String recipient, String subject, String body, boolean sent) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "DbMail{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sent=" + sent +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
