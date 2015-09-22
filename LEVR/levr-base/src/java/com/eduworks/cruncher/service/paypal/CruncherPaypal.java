package com.eduworks.cruncher.service.paypal;

import java.util.Map;
import java.util.logging.Logger;

import com.eduworks.resolver.Cruncher;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;

public abstract class CruncherPaypal extends Cruncher
{

	protected PayResponse makeAPICall(PayRequest payRequest, Map<String, String> parameters)
	{

		Logger logger = Logger.getLogger(this.getClass().toString());

		// ## Creating service wrapper object
		// Creating service wrapper object to make API call and loading
		// configuration file for your credentials and endpoint
		AdaptivePaymentsService service = null;
		service = new AdaptivePaymentsService(parameters);
		
		PayResponse payResponse = null;
		try
		{

			// ## Making API call
			// Invoke the appropriate method corresponding to API in service
			// wrapper object
			payResponse = service.pay(payRequest);
		}
		catch (Exception e)
		{
			logger.severe("Error Message : " + e.getMessage());
		}

		// ## Accessing response parameters
		// You can access the response parameters using getter methods in
		// response object as shown below
		// ### Success values
		if (payResponse.getResponseEnvelope().getAck().getValue().equalsIgnoreCase("Success"))
		{

			// The pay key, which is a token you use in other Adaptive
			// Payment APIs (such as the Refund Method) to identify this
			// payment. The pay key is valid for 3 hours; the payment must
			// be approved while the pay key is valid.
			logger.info("Pay Key : " + payResponse.getPayKey());

			// Once you get success response, user has to redirect to PayPal
			// for the payment. Construct redirectURL as follows,
			// `redirectURL=https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey="
			// + payResponse.getPayKey();`
		}
		// ### Error Values
		// Access error values from error list using getter methods
		else
		{
			logger.severe("API Error Message : " + payResponse.getError().get(0).getMessage());
		}
		return payResponse;

	}
}
