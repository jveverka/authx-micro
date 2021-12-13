package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("cached-token")
public class CachedToken {

    @Id
    private String id;
    private String token;
    private String kid;
    private String x509Certificate;

    public CachedToken() {
    }

    public CachedToken(String id, String token, String kid, String x509Certificate) {
        this.id = id;
        this.token = token;
        this.kid = kid;
        this.x509Certificate = x509Certificate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(String x509Certificate) {
        this.x509Certificate = x509Certificate;
    }
}
