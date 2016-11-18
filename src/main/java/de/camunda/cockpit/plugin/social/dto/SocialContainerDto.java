package de.camunda.cockpit.plugin.social.dto;

import java.util.Date;

public class SocialContainerDto {

	private Integer id;
	private String tagName;
    private String defId;
    private String user;
    private String post;
    private Date time;
    private String caption;
    private Integer userAmount;

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

    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }

    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getUserAmount() {
        return userAmount;
    }
    public void setUserAmount(Integer userAmount) {
        this.userAmount = userAmount;
    }

}