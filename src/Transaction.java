public class Transaction {
    private String sender;
    private String content;

    // Getters
    public String getSender() { return sender; }
    public String getContent() { return content; }

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

    public String toString() {
        return String.format("|%s|%70s|\n", sender, content);
    }

    // Checks whether the sender is valid
    private boolean isValidSender(String sender) {
        return sender != null && sender.matches("[a-z]{4}[0-9]{4}");
    }

    // Checks whether the content is valid
    private boolean isValidContent(String content) {
        return content.length() <= 70 && !content.contains("|");
    }
}
