package one.microproject.authx.common.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {

    private final String sub;

    @JsonCreator
    public UserInfoResponse(@JsonProperty("sub") String sub) {
        this.sub = sub;
    }

    public String getSub() {
        return sub;
    }

}
