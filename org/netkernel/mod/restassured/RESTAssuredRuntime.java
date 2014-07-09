package org.netkernel.mod.restassured;

import org.netkernel.layer0.nkf.INKFRequest;
import org.netkernel.layer0.nkf.INKFRequestContext;
import org.netkernel.layer0.nkf.INKFRequestReadOnly;
import org.netkernel.layer0.nkf.INKFResponseReadOnly;
import org.netkernel.layer1.representation.IDeterminateStringRepresentation;
import org.netkernel.module.standard.endpoint.StandardAccessorImpl;

public class RESTAssuredRuntime extends StandardAccessorImpl
{
	public RESTAssuredRuntime()
	{	this.declareThreadSafe();	
	}

	@Override
	public void onSource(INKFRequestContext context) throws Exception
	{
		IDeterminateStringRepresentation dstring=context.source("arg:operator", IDeterminateStringRepresentation.class);
		
		StringBuilder b=new StringBuilder();
		b.append("import static com.jayway.restassured.RestAssured.*;\n");
		b.append("import static com.jayway.restassured.matcher.RestAssuredMatchers.*;\n");
		b.append("import static org.hamcrest.Matchers.*;\n");
		b.append("import static com.jayway.restassured.module.jsv.JsonSchemaValidator.*;\n");
		b.append(dstring.getString());
		
		INKFRequest req=context.createRequest("active:groovy");
		req.addArgumentByValue("operator", b.toString());
		//Relay any varargs
		INKFRequestReadOnly outerReq=context.getThisRequest();
		for(int i=0; i<outerReq.getArgumentCount(); i++)
		{	String name=outerReq.getArgumentName(i);
			if(!name.equals("operator"))
			{	req.addArgument(name, outerReq.getArgumentValue(i));				
			}
		}
		INKFResponseReadOnly resp=context.issueRequestForResponse(req);
		context.createResponseFrom(resp);
	}
}