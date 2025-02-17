package com.mascot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.regions.Region;
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
            String email = retrieveUserEmail(username, context);
            if (email == null) {
                context.getLogger().log(String.format(" [%s] Skipping notification for user %s due to missing email.", getCurrentTime(), username));
                return "Notification skipped due to missing email.";
            }

            // Retrieve OTP from Secrets Manager
            String password = retrieveUserPassword(username, context);
            if (password == null) {
                context.getLogger().log(String.format(" [%s] Skipping notification for user %s due to missing password.", getCurrentTime(), username));
                return "Notification skipped due to missing password.";
            }

            // Log the information
            context.getLogger().log(String.format(" [%s] New user created: %s", getCurrentTime(), username));
            context.getLogger().log(String.format(" [%s] Email: %s", getCurrentTime(), email));
            context.getLogger().log(String.format(" [%s] Temporary Password: %s", getCurrentTime(), password));

            return "Notification processed successfully!";
        } catch (Exception e) {
            context.getLogger().log(String.format(" [%s] Error processing event: %s", getCurrentTime(), e.getMessage()));
            return "Error processing event.";
        }
    }

    private String getUsernameFromEvent(Map<String, Object> event) {
        Map<String, Object> detail = (Map<String, Object>) event.get("detail");
        if (detail == null || !(detail instanceof Map)) {
            throw new IllegalArgumentException(String.format(" [%s] Invalid event structure: 'detail' is not a map.", getCurrentTime()));
        }

        Map<String, Object> requestParams = (Map<String, Object>) detail.get("requestParameters");
        if (requestParams == null || !requestParams.containsKey("userName")) {
            throw new IllegalArgumentException(String.format(" [%s] Invalid event structure: 'userName' not found.", getCurrentTime()));
        }

        return requestParams.get("userName").toString();
    }

    private String retrieveUserEmail(String username, Context context) {
        SsmClient ssmClient = SsmClient.builder()
                .region(Region.US_EAST_1)
                .build();
        try {
            return ssmClient.getParameter(request -> request.name("/user/" + username + "/email")).parameter().value();
        } catch (Exception e) {
            context.getLogger().log(String.format(" [%s] Error retrieving email for user %s: %s", getCurrentTime(), username, e.getMessage()));
            return null;
        }
    }

    private String retrieveUserPassword(String username, Context context) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.US_EAST_1)
                .build();
        try {
            return secretsManagerClient.getSecretValue(request -> request.secretId(username + "-password")).secretString();
        } catch (Exception e) {
            context.getLogger().log(String.format(" [%s] Error retrieving password for user %s: %s", getCurrentTime(), username, e.getMessage()));
            return null;
        }
    }

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().toString();
    }
}