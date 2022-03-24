package top.sogrey.encryption.algorithm;

/**
 * Simple object that keep private and public key
 */
public class PPKeys {
    private String privateKey;
    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivatekey() {
        return privateKey;
    }

    public void setPrivatekey(String privatekey) {
        this.privateKey = privatekey;
    }
}