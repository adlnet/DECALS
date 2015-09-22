package com.eduworks.cruncher.service.paypal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;

public class CruncherPaypalPay extends CruncherPaypal
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Map<String, String> stuntedParameters = new HashMap<String, String>();
		stuntedParameters.put("acct1.UserName",getAsString("apiUsername",c,parameters, dataStreams));
		stuntedParameters.put("acct1.Password",getAsString("apiPassword",c,parameters, dataStreams));
		stuntedParameters.put("acct1.Signature",getAsString("apiSignature",c,parameters, dataStreams));
		stuntedParameters.put("acct1.AppId",getAsString("apiAppId",c,parameters, dataStreams));
		stuntedParameters.put("mode",getAsString("mode",c,parameters, dataStreams));
		stuntedParameters.put("service.EndPoint",getAsString("endpoint",c,parameters, dataStreams));

		RequestEnvelope requestEnvelope = new RequestEnvelope();
		requestEnvelope.setErrorLanguage("en_US");
		
		PayRequest payRequest=  new PayRequest(requestEnvelope, getAsString("actionType",c,parameters, dataStreams),
				getAsString("cancelUrl",c,parameters, dataStreams), getAsString("currencyCode",c,parameters, dataStreams), null,
				getAsString("acceptUrl",c,parameters, dataStreams));
		payRequest.setSenderEmail(getAsString("senderEmail",c,parameters, dataStreams));
		payRequest.setFeesPayer(getAsString("feesPayer",c,parameters, dataStreams));
		
		PayResponse payResponse;

		// Amount to be credited to the receiver's account
		Receiver receiver1 = new Receiver(Double.parseDouble(getAsString("amount",c,parameters, dataStreams)));

		// A receiver's email address
		receiver1.setEmail(getAsString("email",c,parameters, dataStreams));
		List<Receiver> receiverLst = new ArrayList<Receiver>();
		receiverLst.add(receiver1);

		ReceiverList receiverList = new ReceiverList(receiverLst);
		payRequest.setReceiverList(receiverList);
		payResponse = makeAPICall(payRequest,stuntedParameters);
		return payResponse.getPayErrorList();
	}

	@Override
	public String getDescription()
	{
		return "Attempts to pay a paypal customer. See internal code for details and document this. (I don't remember!)";
	}

	@Override
	public String getReturn()
	{
		return "PayErrorList";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("apiUsername","String","apiPassword","String","apiSignature","String","apiAppId","String","mode","String","endpoint","String","actionType","String","cancelUrl","String","acceptUrl","String","currencyCode","String","senderEmail","String","feesPayer","String","amount","Number","email","String");
	}

}
