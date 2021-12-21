package one.microproject.authx.jclient;

import one.microproject.authx.common.dto.AuthxInfo;

public interface AuthXClient {

    AuthxInfo getAuthxInfo();

    AuthXOAuth2Client getAuthXOAuth2Client(String projectId);

}
