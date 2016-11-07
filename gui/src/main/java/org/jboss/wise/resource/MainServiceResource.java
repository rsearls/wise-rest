package org.jboss.wise.resource;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.jboss.wise.core.exception.WiseAuthenticationException;
import org.jboss.wise.core.exception.WiseProcessingException;
import org.jboss.wise.core.exception.WiseURLException;
import org.jboss.wise.core.exception.WiseWebServiceException;
import org.jboss.wise.gwt.shared.Service;
import org.jboss.wise.gwt.shared.WsdlAddress;
import org.jboss.wise.gwt.shared.WsdlInfo;
import org.jboss.wise.gwt.shared.tree.element.RequestResponse;
import org.jboss.wise.gwt.shared.tree.element.TreeElement;
import org.jboss.wise.shared.GWTClientConversationBean;

/**
 * User: rsearls
 * Date: 11/4/16
 */
@Path("")
public class MainServiceResource {

    private GWTClientConversationBean gwtClientConversationBean = new GWTClientConversationBean();

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Response helloTest() {
        String value = "Hello World";
        return Response.status(200).entity(value).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAddressDetails() {
        ArrayList<WsdlAddress> wsdlAddress = new ArrayList<WsdlAddress>();
        List<String> wsdlList = gwtClientConversationBean.getWsdlList();
        // rls test data
        //List<String>  wsdlList = new ArrayList<>();
        //wsdlList.add("http://localhost:8080/wise-test-datatypes?wsdl");

        for (int i = 0; i < wsdlList.size(); ++i) {
            WsdlAddress detail = new WsdlAddress(String.valueOf(i), wsdlList.get(i));
            wsdlAddress.add(detail);
        }
        sortAddressDetails(wsdlAddress);
        return Response.status(200).entity(wsdlAddress).build();
    }
    /**
    public ArrayList<WsdlAddress> getAddressDetails() {
        ArrayList<WsdlAddress> wsdlAddress = new ArrayList<WsdlAddress>();
        List<String> wsdlList = gwtClientConversationBean.getWsdlList();
        for (int i = 0; i < wsdlList.size(); ++i) {
            WsdlAddress detail = new WsdlAddress(String.valueOf(i), wsdlList.get(i));
            wsdlAddress.add(detail);
        }
        return sortAddressDetails(wsdlAddress);
    }
    **/
    @POST
    @Path("endpoints")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEndpoints(WsdlInfo wsdlInfo) throws WiseProcessingException {

        ArrayList<Service> endpointList = new ArrayList<Service>();
        if (wsdlInfo != null) {
            gwtClientConversationBean.setWsdlUser(wsdlInfo.getUser());
            gwtClientConversationBean.setWsdlPwd(wsdlInfo.getPassword());
            gwtClientConversationBean.setWsdlUrl(wsdlInfo.getWsdl());
            gwtClientConversationBean.readWsdl();
            List<Service> serviceList = gwtClientConversationBean.getServices();
            endpointList.addAll(serviceList);
        }
        return Response.status(200).entity(endpointList).build();
    }

    /** rls testing only **/
    /***
    @GET
    @Path("getservices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEndpointsTEST() throws WiseProcessingException {
        // rls test data
        WsdlInfo wsdlInfo = new WsdlInfo();
        wsdlInfo.setWsdl("http://localhost:8080/wise-test-datatypes?wsdl");
        ArrayList<Service> endpointList = new ArrayList<Service>();
        if (wsdlInfo != null) {
            gwtClientConversationBean.setWsdlUser(wsdlInfo.getUser());
            gwtClientConversationBean.setWsdlPwd(wsdlInfo.getPassword());
            gwtClientConversationBean.setWsdlUrl(wsdlInfo.getWsdl());
            gwtClientConversationBean.readWsdl();
            List<Service> serviceList = gwtClientConversationBean.getServices();
            endpointList.addAll(serviceList);
        }
        return Response.status(200).entity(endpointList).build();
    }
    ***/
    /** rls test data only **/
    @GET
    @Path("wsdlinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWsdlInfo() throws WiseProcessingException {
        // rls test data
        WsdlInfo wsdlInfo = new WsdlInfo();
        wsdlInfo.setWsdl("http://localhost:8080/wise-test-datatypes?wsdl");
        return Response.status(200).entity(wsdlInfo).build();
    }

    @POST
    @Path("config")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public RequestResponse getEndpointReflection(String id) {

        if (id != null) {
            gwtClientConversationBean.setCurrentOperation(id);
            return gwtClientConversationBean.parseOperationParameters(id);
        }
        return null;
    }

    @POST
    @Path("preview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String getRequestPreview(TreeElement rootTreeElement) {

        return gwtClientConversationBean.generateRequestPreview(rootTreeElement);
    }

    @POST
    @Path("invoke")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RequestResponse getPerformInvocationOutputTree(TreeElement rootTreeElement, WsdlInfo wsdlInfo)
        throws WiseWebServiceException, WiseProcessingException, WiseAuthenticationException {

        gwtClientConversationBean.setInvocationUrl(wsdlInfo.getWsdl());
        gwtClientConversationBean.setInvocationUser(wsdlInfo.getUser());
        gwtClientConversationBean.setInvocationPwd(wsdlInfo.getPassword());

        return gwtClientConversationBean.performInvocation(rootTreeElement);
    }

    @POST
    @Path("validate")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean isValidURL(String url) throws WiseURLException {
        return gwtClientConversationBean.isValidURL(url);
    }


    private ArrayList<WsdlAddress> sortAddressDetails(ArrayList<WsdlAddress> wsdlAddress) {

        for (int i = 0; i < wsdlAddress.size(); ++i) {
            for (int j = 0; j < wsdlAddress.size() - 1; ++j) {
                if (wsdlAddress.get(j).getDisplayName().compareToIgnoreCase(wsdlAddress.get(j + 1).getDisplayName()) >= 0) {
                    WsdlAddress tmp = wsdlAddress.get(j);
                    wsdlAddress.set(j, wsdlAddress.get(j + 1));
                    wsdlAddress.set(j + 1, tmp);
                }
            }
        }
        return wsdlAddress;
    }
}
