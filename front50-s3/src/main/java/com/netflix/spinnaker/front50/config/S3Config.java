package com.netflix.spinnaker.front50.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.netflix.spinnaker.front50.model.*;
import com.netflix.spinnaker.front50.plugins.PluginBinaryStorageService;
import com.netflix.spinnaker.front50.plugins.S3PluginBinaryStorageService;
import com.netflix.spinnaker.kork.aws.bastion.BastionConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty("spinnaker.s3.enabled")
@Import(BastionConfig.class)
@EnableConfigurationProperties({S3PluginStorageProperties.class})
public class S3Config {
  @Bean
  @ConditionalOnProperty("spinnaker.s3.plugin-storage.enabled")
  public AmazonS3 awsS3PluginClient(
      AWSCredentialsProvider awsCredentialsProvider, S3PluginStorageProperties s3Properties) {
    return S3ClientFactory.create(awsCredentialsProvider, s3Properties);
  }

  @Bean
  @ConditionalOnProperty("spinnaker.s3.plugin-storage.enabled")
  PluginBinaryStorageService pluginBinaryStorageService(
      AmazonS3 awsS3PluginClient, S3PluginStorageProperties properties) {
    return new S3PluginBinaryStorageService(awsS3PluginClient, properties);
  }
}
