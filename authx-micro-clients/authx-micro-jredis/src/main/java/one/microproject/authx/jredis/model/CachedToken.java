package one.microproject.authx.jredis.model;

import one.microproject.authx.common.dto.GrantType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash("cached-token")
public class CachedToken {

    @Id
    private String id;
    private String token;
    private String projectId;
    private String x509Certificate;

    @Indexed
    private String relatedTokenId;
    private String typ;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeToLive;
    private GrantType grantType;

    public CachedToken() {
    }

    public CachedToken(String id, String token, String projectId, String x509Certificate, String relatedTokenId, String typ, Long timeToLive, GrantType grantType) {
        this.id = id;
        this.token = token;
        this.projectId = projectId;
        this.x509Certificate = x509Certificate;
        this.relatedTokenId = relatedTokenId;
        this.typ = typ;
        this.timeToLive = timeToLive;
        this.grantType = grantType;
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

    public String getProjectId() {
        return projectId;
    }

    public String getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(String x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public String getRelatedTokenId() {
        return relatedTokenId;
    }

    public void setRelatedTokenId(String relatedTokenId) {
        this.relatedTokenId = relatedTokenId;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }
}
