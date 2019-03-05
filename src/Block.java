import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Block {
    private Block previousBlock;
    private byte[] previousHash;
    private ArrayList<Transaction> transactions;

    public Block() { transactions = new ArrayList<>(); }

    // Getters
    public Block getPreviousBlock() { return previousBlock; }
    public byte[] getPreviousHash() { return previousHash; }
    public ArrayList<Transaction> getTransactions() { return transactions; }

    // Setters
    public void setPreviousBlock(Block previousBlock) { this.previousBlock = previousBlock; }
    public void setPreviousHash(byte[] previousHash) { this.previousHash = previousHash; }
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String toString() {
        String cutOffRule = new String(new char[81]).replace("\0", "-") + "\n";
        String prevHashString = String.format("|PreviousHash:|%65s|\n", encodeHash(previousHash));
        String hashString = String.format("|CurrentHash:|%66s|\n", encodeHash(calculateHash()));
        String transactionsString = "";
        for (Transaction tx : transactions) {
            transactionsString += tx.toString();
        }
        return "Block:\n"
                + cutOffRule
                + hashString
                + cutOffRule
                + transactionsString
                + cutOffRule
                + prevHashString
                + cutOffRule;
    }

    // Calculates the hash of current block
    public byte[] calculateHash() {
        byte[] hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            // Writes previous block's hash value
            dos.write(previousHash);

            // Writes transactions
            for (Transaction tx : transactions) {
                dos.writeUTF(String.format("tx|%s|%s", tx.getSender(), tx.getContent()));
            }

            byte[] bytes = baos.toByteArray();
            hash = digest.digest(bytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return hash;
    }

    /**
     * This function encodes a hash value to a string.
     * @param hash is the hash to encode.
     * @return a string which is the encoded hash.
     */
    private String encodeHash(byte[] hash) {
        return Base64.getEncoder().encodeToString(hash);
    }
}