package com.staj.test.servlet;

import java.util.Collection;
import java.util.Set;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;

import net.java.ao.Entity;

public interface entity extends Entity{
	
	String getName();
	void setName(String name);
	String getUser1();
 	void setUser1(String user1);
 	String getUser2();
 	void setUser2(String user2);
}