package combibet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import combibet.service.TwilioService;
import combibet.twilio.SmsRequest;

//@RestController
//@RequestMapping("api/v1/sms")
//public class TwilioController {
//
//    private final TwilioService service;
//
//    @Autowired
//    public TwilioController(TwilioService service) {
//        this.service = service;
//    }
//
////    @PostMapping
////    public void sendSms(@Valid @RequestBody SmsRequest smsRequest) {
////        service.sendSms(smsRequest);
////    }
//    
//    @PostMapping
//    public void sendSms(SmsRequest smsRequest) {
//        service.sendSms(smsRequest);
//    }
//}