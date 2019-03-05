public class Transaction {
    private String sender;
    private String content;

    // Setters
    public void setSender(String sender) {
        if (isValidSender(sender)) {
            this.sender = sender;
        }
    }

    public void setContent(String content) {
        if (isValidContent((content))) {
            this.content = content;
        }
    }

    // Getters
    public String getSender() { return sender; }
    public String getContent() { return content; }

    public String toString() {
        return String.format("|%s|%70s|\n", sender, content);
    }

    // Helper functions
    private boolean isValidSender(String sender) {
        return sender != null && sender.matches("[a-z]{4}[0-9]{4}");
    }

    private boolean isValidContent(String content) {
        return content.length() <= 70 && !content.contains("|");
    }
}
