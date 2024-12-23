package com.kclass.generated;

import com.kclass.generated.k.KClass; 

public class KMembers implements KClass {
    public String createdAt = "created_at";
    public String password = "password";
    public String role = "role";
    public String name = "name";
    public String id = "id";
    public String table = "members";
    public String status = "status";
    public String updatedAt = "updated_at";
	public String nick = null;

    public KMembers withNick(String nick) {
        this.createdAt = nick + "." + this.createdAt;
        this.password = nick + "." + this.password;
        this.role = nick + "." + this.role;
        this.name = nick + "." + this.name;
        this.id = nick + "." + this.id;
        this.table = nick + "." + this.table;
        this.status = nick + "." + this.status;
        this.updatedAt = nick + "." + this.updatedAt;
		this.nick = nick;
        return this;
    }
}
