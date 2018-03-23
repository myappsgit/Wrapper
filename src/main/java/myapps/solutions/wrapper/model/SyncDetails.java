package myapps.solutions.wrapper.model;

public class SyncDetails {

	boolean tcps;
	boolean appVersion;
	boolean offers;
	String messages;
	String name;
	
	public SyncDetails() {

	}

	public SyncDetails(boolean tcps, boolean appVersion, boolean offers, String messages, String name) {
		this.tcps = tcps;
		this.appVersion = appVersion;
		this.offers = offers;
		this.messages = messages;
		this.name = name;
	}

	public boolean isTcps() {
		return tcps;
	}

	public void setTcps(boolean tcps) {
		this.tcps = tcps;
	}

	public boolean isAppVersion() {
		return appVersion;
	}

	public void setAppVersion(boolean appVersion) {
		this.appVersion = appVersion;
	}

	public boolean isOffers() {
		return offers;
	}

	public void setOffers(boolean offers) {
		this.offers = offers;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
