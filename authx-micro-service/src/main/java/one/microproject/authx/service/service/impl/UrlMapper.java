package one.microproject.authx.service.service.impl;

public class UrlMapper {

    private final String internalUrl;
    private final String externalUrl;

    public UrlMapper(String internalUrl, String externalUrl) {
        this.internalUrl = internalUrl;
        this.externalUrl = externalUrl;
    }

    public String map(String requestUrl) {
        if (internalUrl.equals(requestUrl)) {
            return externalUrl;
        } else if (requestUrl.startsWith(internalUrl)) {
            return externalUrl + requestUrl.substring(internalUrl.length());
        } else {
            return requestUrl;
        }
    }

}
