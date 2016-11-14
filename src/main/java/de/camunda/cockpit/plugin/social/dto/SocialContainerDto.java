package de.camunda.cockpit.plugin.social.dto;

public class SocialContainerDto {

	private Integer id;
	private String tagName;
    private String defId;
    private String user;

	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

    public String getDefId() {
        return defId;
    }
    public void setDefId(String defId) {
        this.defId = defId;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

}