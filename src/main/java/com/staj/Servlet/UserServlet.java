package com.staj.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.staj.test.servlet.entity;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class UserServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private final UserManager userMan;
    Collection<ApplicationUser> users;
    @ComponentImport
    private final TemplateRenderer templateRenderer;
 
    @ComponentImport
    private final ActiveObjects ao;
    public UserServlet(TemplateRenderer templateRenderer,ActiveObjects ao) {
    	this.templateRenderer = templateRenderer;
		this.ao=ao;
		userMan= ComponentAccessor.getUserManager();
		users =new ArrayList<ApplicationUser>();

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	ArrayList<Object> firstuser = new ArrayList<>();
    	ArrayList<Object> seconduser = new ArrayList<>();
    	
        Map<String, Object> context = new HashMap<>();
        
        ao.executeInTransaction(new TransactionCallback<Void>() // (1)
          {
              @Override
              public Void doInTransaction()
              {
                  for (entity ent : ao.find(entity.class)) // (2)
                  {
                 	 firstuser.add(ent.getUser1());
                 	 seconduser.add(ent.getUser2());
                      
                  }

             	 users =userMan.getAllUsers();
                  return null;
              }
          });

        context.put("user1",firstuser); 
        context.put("user2",seconduser); 
          context.put("users",users); 
          templateRenderer.render("/templates/test2.vm", context,resp.getWriter());
    }

}