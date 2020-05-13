package app.entities;

public class Like {
    private final int id;
    private final int senderId;
    private final int receiverId;

    public Like(int id, int senderId, int receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

    @Override
    public String toString() {
        return String.format("Like{id=%d, senderId=%d, receiverId=%d}", id, senderId, receiverId);
    }
}
