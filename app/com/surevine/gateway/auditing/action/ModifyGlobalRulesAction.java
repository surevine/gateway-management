package com.surevine.gateway.auditing.action;

public class ModifyGlobalRulesAction implements AuditAction {

	private String ruleFile;
	private String ruleFileContents;

	public ModifyGlobalRulesAction(String ruleFile, String ruleFileContents) {
		this.ruleFile = ruleFile;
		this.ruleFileContents = ruleFileContents;
	}

	@Override
	public String getDescription() {
		return String.format("Updated global %s rules.", ruleFile);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
