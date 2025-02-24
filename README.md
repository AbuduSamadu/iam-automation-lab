# IAM Automation Lab

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
4. [Setup and Installation](#setup-and-installation)
5. [Architecture](#architecture)
6. [Usage](#usage)
7. [Contributing](#contributing)
8. [License](#license)

---

## Overview

The **IAM Automation Lab** is a project designed to automate the creation of IAM users, groups, and permissions using AWS CloudFormation and Java-based controllers. It also integrates with AWS services such as Secrets Manager, Parameter Store, EventBridge, and Lambda to handle notifications for newly created users.

This project demonstrates how to:
- Create IAM users and assign them to groups programmatically.
- Generate temporary passwords and store them securely.
- Notify new users via email with their credentials.
- Automate infrastructure provisioning using AWS CloudFormation.

---

## Features

- **Automated IAM User Creation**: Use a `LoginController` to create IAM users and assign them to predefined groups.
- **Secure Password Management**: Temporary passwords are generated and stored in AWS Secrets Manager.
- **User Information Storage**: User emails are stored in AWS Systems Manager (SSM) Parameter Store.
- **Event-Driven Notifications**: An EventBridge rule triggers a Lambda function to notify new users via logs or email.
- **Infrastructure as Code**: All resources are provisioned using AWS CloudFormation.

---

## Prerequisites

Before running this project, ensure you have the following:

- **AWS Account**: Access to an AWS account with sufficient permissions.
- **Java Development Kit (JDK)**: JDK 11 or later.
- **Maven**: For building and packaging the Java code.
- **AWS CLI**: Installed and configured with appropriate credentials.
- **Git**: To clone the repository and manage version control.

---

## Setup and Installation

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/AbuduSamadu/iam-automation-lab.git
cd iam-automation-lab

```

### 2. Build the Project

Build the project using Maven:

```bash 
mvn clean install
```

### 3. Configure AWS CLI

Ensure your AWS CLI is configured with the appropriate credentials:

```bash
aws configure
```

### 4. Deploy the CloudFormation Stack

Deploy the CloudFormation stack using the provided template:

```bash
aws cloudformation deploy \
    --template-file cloudformation-template.yaml \
    --stack-name iam-automation-stack \
    --parameter-overrides S3BucketName=my-s3-bucket-name EC2Region=us-east-1 \
    --capabilities CAPABILITY_NAMED_IAM
```

### Architecture

The architecture of the **IAM Automation Lab** consists of the following components:
```bash
The project follows a modular architecture:
CloudFormation Template :
Defines IAM groups, users, policies, and other AWS resources.
Sets up EventBridge rules and Lambda functions for event-driven notifications.
LoginController :
A Java-based controller that creates IAM users, assigns them to groups, generates passwords, and stores user information.
NotificationLambda :
A Lambda function triggered by EventBridge when a new IAM user is created.
Retrieves user details from Parameter Store and Secrets Manager and logs the information.
Secrets Manager :
Stores temporary passwords securely.
Parameter Store :
Stores user email addresses for future reference.
EventBridge :
Detects CreateUser events from IAM and triggers the Lambda function.
```

- **IAM Users
- **IAM Groups
- **AWS Secrets Manager
- **AWS Systems Manager (SSM) Parameter Store
- **AWS EventBridge
- **AWS Lambda
- **AWS CloudFormation
- **Amazon S3
- **Amazon EC2



### References

- [AWS SDK for Java Documentation](https://docs.aws.amazon.com/sdk-for-java/index.html)
- [AWS CloudFormation User Guide](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/Welcome.html)
- [AWS Secrets Manager Documentation](https://docs.aws.amazon.com/secretsmanager/latest/userguide/intro.html)
- [AWS Systems Manager Parameter Store Documentation](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html)
- [AWS EventBridge User Guide](https://docs.aws.amazon.com/eventbridge/latest/userguide/what-is-amazon-eventbridge.html)
- [AWS Lambda Developer Guide](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html)
- [AWS SDK for Java API Reference](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/index.html)
- [AWS CloudFormation Template Reference](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-reference.html)
- [AWS CloudFormation Resource Types Reference](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html)
- [AWS CloudFormation Function Reference](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference.html)