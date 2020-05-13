package app.entities;

public class Message {
    private final int id;
    private final int senderId;
    private final int receiverId;
    private final String context;
    private final String messageDate;

    public Message(int id, int senderId, int receiverId, String context, String messageDate) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.context = context;
        this.messageDate = messageDate;
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getContext() {
        return context;
    }

    public String getMessageDate() {
        return messageDate;
    }

    @Override
    public String toString() {
        return String.format("Message{id=%d, senderId=%d, receiverId=%d, context='%s', messageDate='%s'}", id, senderId, receiverId, context, messageDate);
    }
}
