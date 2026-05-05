package com.example.demo.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@CrossOrigin(origins = {"http://localhost:3000"})

@RestController
public class PaymentController {

		@Value("${razorpay.key_id}")
		private String keyId;
		
		@Value("razorpay.key_secret")
		private String keySecret;
	
	@PostMapping("/api/create-order")
	public ResponseEntity<?>createRazorpayOrder(@RequestBody Map<String ,Object>data){
		try {
			int amount =(int)data.get("amount");
			
			RazorpayClient client =new RazorpayClient(keyId,keySecret);
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount",amount*100);
			orderRequest.put("currency","INR");
			orderRequest.put("recipt","txn_12345");
			
			Order order=client.orders.create(orderRequest);
			return new ResponseEntity(Map.of("orderId",order.get("id"),"key","keyId"),HttpStatus.OK);
			
		}catch(Exception e){
			
			return new ResponseEntity<>("Error:"+e.getMessage(),HttpStatus.BAD_REQUEST);
			
		}
		
	}
}
