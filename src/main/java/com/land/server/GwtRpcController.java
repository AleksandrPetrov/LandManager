package com.land.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;
import com.land.shared.exception.AnyServiceException;
import com.land.shared.exception.ServiceSecurityException;

@SuppressWarnings("serial")
@RequestMapping("/*/*.rpc")
@org.springframework.stereotype.Controller
public class GwtRpcController extends RemoteServiceServlet implements ServletContextAware {

    private ServletContext servletContext;

    @Autowired
    private RemoteService remoteService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    public String processCall(String payload) throws SerializationException {
        try {
            // First, check for possible XSRF situation
            checkPermutationStrongName();
            try {
                RPCRequest rpcRequest = RPC.decodeRequest(payload, remoteService.getClass(), this);
                onAfterRequestDeserialized(rpcRequest);
                return RPC.invokeAndEncodeResponse(remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy(), rpcRequest.getFlags());
            }
            catch (IncompatibleRemoteServiceException ex) {
                log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
                return RPC.encodeResponseForFailure(null, ex);
            }
        }
        catch (AccessDeniedException ex) {
            // AccessDeniedException
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace(System.out);
            return RPC.encodeResponseForFailure(null, new ServiceSecurityException(ex.getMessage(), ex));
        }
        catch (UnexpectedException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace(System.out);
            if (ex.getCause() instanceof AccessDeniedException) {
                // AccessDeniedException
                return RPC.encodeResponseForFailure(null, new ServiceSecurityException("Доступ к ресурсу запрещён", ex));
            }
            if (ex.getCause() instanceof TransactionSystemException) {
                Throwable e = ((TransactionSystemException) ex.getCause()).getApplicationException();
                if (e instanceof AnyServiceException)
                    return RPC.encodeResponseForFailure(null, e);
                else
                    return RPC.encodeResponseForFailure(null, new ServiceSecurityException(ex.getMessage(), ex));
            }
            else {
                return RPC.encodeResponseForFailure(null, new AnyServiceException(ex.getMessage(), ex));
            }
        }
        catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace(System.out);
            if (ex instanceof AnyServiceException)
                return RPC.encodeResponseForFailure(null, ex);
            else
                return RPC.encodeResponseForFailure(null, new AnyServiceException(ex.getMessage(), ex));
        }

    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public void setRemoteService(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

}
