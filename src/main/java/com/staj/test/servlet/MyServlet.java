package com.staj.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.templaterenderer.TemplateRenderer;

import net.java.ao.EntityManager;
import net.java.ao.Query;

@Scanned
public class MyServlet extends HttpServlet{

	private static final Logger log = LoggerFactory.getLogger(MyServlet.class);

    @ComponentImport
    private final TemplateRenderer templateRenderer;
 
    @ComponentImport
    private final ActiveObjects ao;
    
    public MyServlet(TemplateRenderer templateRenderer,ActiveObjects ao) {
		this.templateRenderer = templateRenderer;
		this.ao=ao;
	}
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	
    	Map<String, Object> context = new HashMap<>();
    	context.put("message", "List Name");
    	
    	ArrayList<Object> lists = new ArrayList<>();

   	

       ao.executeInTransaction(new TransactionCallback<Void>() // (1)
         {
             @Override
             public Void doInTransaction()
             {
                 for (entity ent : ao.find(entity.class)) // (2)
                 {
                     lists.add(ent);
                     
                 }
                 return null;
             }
         });
         context.put("lists",lists);
         templateRenderer.render("/templates/test.vm", context,resp.getWriter());

    	
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	 
    	if(req.getParameter("add")!=null) {
    		 final String name= req.getParameter("name");
    		 ao.executeInTransaction(new TransactionCallback<entity>() 
         {
    			 @Override
                 public entity doInTransaction()
                 {
//                     entity[] entities = ao.find(entity.class, Query.select().where("NAME = ?", name));
//                     if(entities[0].equals(null)) {
                    	 final entity ent = ao.create(entity.class); 
                    	 ent.setName(name);
                    	 ent.save(); 
                    	 return ent;
//                     }
//                     else {
//                    	 return null;
//                     }
//                     
//                     return null;
                 }
         });
    	}
    	else if(req.getParameter("delete")!=null) {
    		 final String name= req.getParameter("delete");
    		 ao.executeInTransaction(new TransactionCallback<entity>() 
    	        {
    			 @Override
                 public entity doInTransaction()
                 {
                     entity[] entities = ao.find(entity.class, Query.select().where("NAME = ?", name));
                     if(entities[0].equals(null)) {
                    	 final entity ent = ao.create(entity.class); 
                    	 ent.setName(name);
                    	 ent.save(); 
                    	 return ent;
                     }
                     
                     return null;
                 }
    	        });
    	}
    	else if(req.getParameter("save")!=null) {
    		
	   		 final String name=req.getParameter("save");
	   		  String user1= req.getParameter("first");
	   		 final String user2=req.getParameter("second");
	   		 
	   		 entity[] entities = ao.find(entity.class, Query.select().where("NAME = ?", name));
	   		 entities[0].setUser1(user1);
	       	 entities[0].setUser2(user2);
   	         entities[0].save();
            
   	 
   }
         resp.sendRedirect(req.getContextPath() + "/plugins/servlet/myservlet");
        
    }
    

}