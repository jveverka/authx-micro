package one.microproject.authx.jclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.microproject.authx.common.dto.*;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXOAuth2Client;

import java.io.IOException;

import static one.microproject.authx.common.Constants.BEARER_PREFIX;
import static one.microproject.authx.common.Urls.DELIMITER;
import static one.microproject.authx.common.Urls.SERVICES_ADMIN_AUTHX;
import static one.microproject.authx.common.Urls.SERVICES_ADMIN_PROJECTS;
import static one.microproject.authx.common.Urls.SERVICES_SYSTEM;
import static one.microproject.authx.jclient.impl.AuthXOAuth2ClientImpl.APPLICATION_JSON;
import static one.microproject.authx.jclient.impl.AuthXOAuth2ClientImpl.AUTHORIZATION;

public class AuthXClientImpl implements AuthXClient {

    private final String baseUrl;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public AuthXClientImpl(String baseUrl, OkHttpClient client, ObjectMapper mapper) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public AuthXResponse<AuthXInfo, Void> getAuthXInfo() {
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + SERVICES_SYSTEM + DELIMITER + "info")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                AuthXInfo authxInfo = mapper.readValue(response.body().string(), AuthXInfo.class);
                return AuthXResponse.ok(authxInfo, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXOAuth2Client getAuthXOAuth2Client(String projectId) {
        return new AuthXOAuth2ClientImpl(baseUrl, projectId, client, mapper);
    }

    @Override
    public AuthXResponse<String, ErrorMessage> buildProject(String token, BuildProjectRequest buildProjectRequest) {
        try {
            String jsonBody = mapper.writeValueAsString(buildProjectRequest);
            Request request = new Request.Builder()
                    .addHeader(AUTHORIZATION, BEARER_PREFIX + token)
                    .url(baseUrl + SERVICES_ADMIN_AUTHX + "/projects/build")
                    .put(RequestBody.create(jsonBody, MediaType.parse(APPLICATION_JSON)))
                    .build();
            Response response = client.newCall(request).execute();
            ResponseMessage responseMessage = mapper.readValue(response.body().string(), ResponseMessage.class);
            if (response.code() == 200) {
                return AuthXResponse.ok(responseMessage, response.code());
            } else {
                return AuthXResponse.error(responseMessage, response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<String, ErrorMessage> deleteProject(String token, String projectId) {
        try {
            Request request = new Request.Builder()
                    .addHeader(AUTHORIZATION, BEARER_PREFIX + token)
                    .url(baseUrl + SERVICES_ADMIN_AUTHX + "/projects/" + projectId)
                    .delete()
                    .build();
            Response response = client.newCall(request).execute();
            ResponseMessage responseMessage = mapper.readValue(response.body().string(), ResponseMessage.class);
            if (response.code() == 200) {
                return AuthXResponse.ok(responseMessage, response.code());
            } else {
                return AuthXResponse.error(responseMessage, response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<ProjectReportDto, Void> getProjectReport(String token, String projectId) {
        try {
            Request request = new Request.Builder()
                    .addHeader(AUTHORIZATION, BEARER_PREFIX + token)
                    .url(baseUrl + SERVICES_ADMIN_PROJECTS + DELIMITER + projectId)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                ProjectReportDto projectReportDto = mapper.readValue(response.body().string(), ProjectReportDto.class);
                return AuthXResponse.ok(projectReportDto, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<String, ErrorMessage> update(String token, UpdateProjectRequest updateProjectRequest) {
        try {
            String jsonBody = mapper.writeValueAsString(updateProjectRequest);
            Request request = new Request.Builder()
                    .addHeader(AUTHORIZATION, BEARER_PREFIX + token)
                    .url(baseUrl + SERVICES_ADMIN_PROJECTS + DELIMITER + updateProjectRequest.id())
                    .post(RequestBody.create(jsonBody, MediaType.parse(APPLICATION_JSON)))
                    .build();
            Response response = client.newCall(request).execute();
            ResponseMessage responseMessage = mapper.readValue(response.body().string(), ResponseMessage.class);
            if (response.code() == 200) {
                return AuthXResponse.ok(responseMessage, response.code());
            } else {
                return AuthXResponse.error(responseMessage, response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

}
