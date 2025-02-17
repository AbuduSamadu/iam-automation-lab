package com.mascot;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.mascot.lambda.NotificationLambda;

import java.util.HashMap;
import java.util.Map;

public class NotificationLambdaTest {

    public static void main(String[] args) {
        // Simulate the Lambda context (minimal implementation for testing)
        Context context = new Context() {
            @Override
            public String getAwsRequestId() {
                return "test-request-id";
            }

            @Override
            public String getLogGroupName() {
                return "test-log-group";
            }

            @Override
            public String getLogStreamName() {
                return "test-log-stream";
            }

            @Override
            public String getFunctionName() {
                return "NotificationLambdaFunction";
            }

            @Override
            public String getFunctionVersion() {
                return "$LATEST";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "arn:aws:lambda:us-east-1:123456789012:function:NotificationLambdaFunction";
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 10000; // Simulate 10 seconds remaining
            }

            @Override
            public int getMemoryLimitInMB() {
                return 128; // Simulate 128 MB memory limit
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String message) {
                        System.out.println(message);
                    }

                    @Override
                    public void log(byte[] bytes) {

                    }
                };
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null; // Return null for testing
            }

            @Override
            public ClientContext getClientContext() {
                return null; // Return null for testing
            }
        };

        // Create a simplified test event
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> detail = new HashMap<>();
        Map<String, Object> requestParameters = new HashMap<>();

        requestParameters.put("userName", "test-user");
        detail.put("requestParameters", requestParameters);
        event.put("detail", detail);

        // Instantiate the Lambda function and invoke it
        NotificationLambda lambdaFunction = new NotificationLambda();
        String result = lambdaFunction.handleRequest(event, context);

        // Print the result
        System.out.println("Lambda Function Result: " + result);
    }
}