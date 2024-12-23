package com.kclass.generated;

import com.kclass.generated.k.KClass; 

public class KPostCategory implements KClass {
    public String id = "id";
    public String category = "category";
    public String table = "post_category";
	public String nick = null;

    public KPostCategory withNick(String nick) {
        this.id = nick + "." + this.id;
        this.category = nick + "." + this.category;
        this.table = nick + "." + this.table;
		this.nick = nick;
        return this;
    }
}
