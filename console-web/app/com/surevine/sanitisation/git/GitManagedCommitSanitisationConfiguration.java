package com.surevine.sanitisation.git;

import java.io.File;
import java.util.List;

import models.Repository;

import org.apache.commons.codec.binary.Base64;

/**
 * Special configuration for git 'commit' sanitisations
 *
 * @author jonnyheavey
 */
public class GitManagedCommitSanitisationConfiguration extends GitManagedSanitisationConfiguration {

	private String commitMessage;

	public GitManagedCommitSanitisationConfiguration(final File archive, final Repository repository,
			final String sanitisationIdentifier, final String commitMessage) {
		super(archive, repository, sanitisationIdentifier);
		this.setCommitMessage(commitMessage);
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(final String commitMessage) {
		this.commitMessage = commitMessage;
	}

	@Override
	public List<String> toSanitisationArguments() {
		final List<String> args = super.toSanitisationArguments();
		final byte[] encodedCommitMessage = Base64.encodeBase64(getCommitMessage().getBytes());
		args.add("\"" + new String(encodedCommitMessage) + "\"");
		return args;
	}

}
