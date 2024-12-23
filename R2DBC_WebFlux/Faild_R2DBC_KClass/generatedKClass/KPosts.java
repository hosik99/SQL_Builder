package com.kclass.generated;

import com.kclass.generated.k.KClass; 

public class KPosts implements KClass {
    public String createdAt = "created_at";
    public String isPublic = "is_public";
    public String id = "id";
    public String state = "state";
    public String authorId = "author_id";
    public String title = "title";
    public String category = "category_id";
    public String table = "posts";
    public String content = "content";
    public String thumbnailUrl = "thumbnail_url";
    public String updatedAt = "updated_at";
	public String nick = null;

    public KPosts withNick(String nick) {
        this.createdAt = nick + "." + this.createdAt;
        this.isPublic = nick + "." + this.isPublic;
        this.id = nick + "." + this.id;
        this.state = nick + "." + this.state;
        this.authorId = nick + "." + this.authorId;
        this.title = nick + "." + this.title;
        this.category = nick + "." + this.category;
        this.table = nick + "." + this.table;
        this.content = nick + "." + this.content;
        this.thumbnailUrl = nick + "." + this.thumbnailUrl;
        this.updatedAt = nick + "." + this.updatedAt;
		this.nick = nick;
        return this;
    }
}
