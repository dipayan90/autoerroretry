package com.nordstrom.ds.autoerroretry.config.sqs;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

/**
 * Load AWS Credentials in the order specified.
 *
 */
public final class CustomCredentialsProviderChain extends AWSCredentialsProviderChain {
	/**
	 * Default profile to be used from local box using <b>awscreds</b> application. Used for federated logins.
	 */
	private static final String NORDSTROM_FEDERATED = "nordstrom-federated";

	/**
	 * 
	 */
	public CustomCredentialsProviderChain() {
		super(new ClasspathPropertiesFileCredentialsProvider(), new SystemPropertiesCredentialsProvider(),
				new EnvironmentVariableCredentialsProvider(), new ProfileCredentialsProvider(NORDSTROM_FEDERATED));

	}
}
