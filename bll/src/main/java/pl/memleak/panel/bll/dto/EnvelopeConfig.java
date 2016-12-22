package pl.memleak.panel.bll.dto;

public class EnvelopeConfig {
    private final String sender;
    private final String createUserSubject;

    public EnvelopeConfig(String sender, String createUserSubject) {
        this.sender = sender;
        this.createUserSubject = createUserSubject;
    }

    public String getSender() {
        return sender;
    }

    public String getCreateUserSubject() {
        return createUserSubject;
    }
}
