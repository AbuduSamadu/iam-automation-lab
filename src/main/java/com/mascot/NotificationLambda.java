//package com.mascot;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.CloudWatchEvent;
//import software.amazon.awssdk.services.ssm.SsmClient;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//
//import java.util.Map;
//
//public class NotificationLambda implements RequestHandler<CloudWatchEvent, String> {
//
//    @Override
//    public String handleRequest(CloudWatchEvent input, Context context) {
//        try {
//            // Extract the username from the event
//            String username = getUsernameFromEvent(input);
//
//            // Retrieve user email from Parameter Store
//            String email = retrieveUserEmail(username);
//
//            // Retrieve OTP from Secrets Manager
//            String password = retrieveUserPassword(username);
//
//            // Log the information
//            context.getLogger().log("New user created: " + username);
//            context.getLogger().log("Email: " + email + ", Temporary Password: " + password);
//
//            return "Notification processed successfully!";
//        } catch (Exception e) {
//            context.getLogger().log("Error processing event: " + e.getMessage());
//            return "Error processing event.";
//        }
//    }
//
//    private String getUsernameFromEvent(CloudWatchEvent event) {
//        // Safely extract the username from the event
//        Object detail = event.getDetail();
//        if (detail instanceof Map) {
//            Map<String, Object> detailMap = (Map<String, Object>) detail;
//            Map<String, Object> requestParams = (Map<String, Object>) detailMap.get("requestParameters");
//            if (requestParams != null && requestParams.containsKey("userName")) {
//                return requestParams.get("userName").toString();
//            }
//        }
//        throw new IllegalArgumentException("Invalid event structure: 'userName' not found.");
//    }
//
//    private String retrieveUserEmail(String username) {
//        SsmClient ssmClient = SsmClient.create();
//        return ssmClient.getParameter(request -> request.name("/user/" + username + "/email")).parameter().value();
//    }
//
//    private String retrieveUserPassword(String username) {
//        SecretsManagerClient secretsManagerClient = SecretsManagerClient.create();
//        return secretsManagerClient.getSecretValue(request -> request.secretId(username + "-password")).secretString();
//    }
//}