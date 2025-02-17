package com.mascot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import java.util.Map;

import static software.amazon.awssdk.services.secretsmanager.SecretsManagerClient.*;

public class NotificationLambda implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        try {
            context.getLogger().log("Received event: " + input.toString());

            // Extract the username from the event
            String username = getUsernameFromEvent(input);

            // Retrieve user email from Parameter Store
            String email = retrieveUserEmail(username);

            // Retrieve OTP from Secrets Manager
            String password = retrieveUserPassword(username);

            // Log the information
            context.getLogger().log("New user created: " + username);
            context.getLogger().log("Email: " + email + ", Temporary Password: " + password);

            return "Notification processed successfully!";
        } catch (Exception e) {
            context.getLogger().log("Error processing event: " + e.getMessage());
            return "Error processing event.";
        }
    }

    private String getUsernameFromEvent(Map<String, Object> event) {
        Map<String, Object> detail = (Map<String, Object>) event.get("detail");
        if (detail == null) {
            throw new IllegalArgumentException("Invalid event structure: 'detail' is not a map.");
        }

        Map<String, Object> requestParams = (Map<String, Object>) detail.get("requestParameters");
        if (requestParams == null || !requestParams.containsKey("userName")) {
            throw new IllegalArgumentException("Invalid event structure: 'userName' not found.");
        }

        return requestParams.get("userName").toString();
    }

    private String retrieveUserEmail(String username) {
        SsmClient ssmClient = SsmClient.create();
        try {
            return ssmClient.getParameter(request -> request.name("/user/" + username + "/email")).parameter().value();
        } catch (Exception e) {
            return "Email not found";
        }
    }

    private String retrieveUserPassword(String username) {
        SecretsManagerClient secretsManagerClient = create();
        try {
            return secretsManagerClient.getSecretValue(request -> request.secretId(username + "-password")).secretString();
        } catch (Exception e) {
            return "Password not found";
        }
    }
}