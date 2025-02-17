package com.mascot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import java.util.Map;

public class NotificationLambda implements RequestHandler<Map<String, Object>, String> {
    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        try {
            // Extract the username from the event
            String username = getUsernameFromEvent(input);

            // Retrieve user email from Parameter Store
            String email = retrieveUserEmail(username);
            if ("Email not found".equals(email)) {
                context.getLogger().log("Warning: Email not found for user " + username);
            }

            // Retrieve OTP from Secrets Manager
            String password = retrieveUserPassword(username);
            if ("Password not found".equals(password)) {
                context.getLogger().log("Warning: Password not found for user " + username);
            }

            // Log the information
            context.getLogger().log("New user created: " + username);
            context.getLogger().log("Email: " + email);
            context.getLogger().log("Temporary Password: " + password);

            return "Notification processed successfully!";
        } catch (Exception e) {
            context.getLogger().log("Error processing event: " + e.getMessage());
            return "Error processing event.";
        }
    }

    private String getUsernameFromEvent(Map<String, Object> event) {
        Map<String, Object> detail = (Map<String, Object>) event.get("detail");
        if (detail == null || !(detail instanceof Map)) {
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
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.create();
        try {
            return secretsManagerClient.getSecretValue(request -> request.secretId(username + "-password")).secretString();
        } catch (Exception e) {
            return "Password not found";
        }
    }
}