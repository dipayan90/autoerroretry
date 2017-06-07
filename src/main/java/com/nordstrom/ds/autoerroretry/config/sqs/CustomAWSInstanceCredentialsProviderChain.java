package com.nordstrom.ds.autoerroretry.config.sqs;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;

/**
 * Load AWS Credentials for an IAM role attached to AWS Resources (Eg: EC2)
 *
 */
public final class CustomAWSInstanceCredentialsProviderChain extends AWSCredentialsProviderChain {
	/**
	 * Load AWS Credentials on EC2 Instances. If no appropriate role is
	 * attached, the aws-sdk retires call to the AWS service to fetch the roles.
	 * This credentials call works with AWS and cannot be invoked from within
	 * the Local Development Environment.
	 */
	public CustomAWSInstanceCredentialsProviderChain() {
		super(new InstanceProfileCredentialsProvider(true));

	}

}
