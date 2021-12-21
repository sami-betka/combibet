//package combibet.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import combibet.twilio.SmsRequest;
//import combibet.twilio.SmsSender;
//import combibet.twilio.TwilioSmsSender;
//
//@org.springframework.stereotype.Service
//public class TwilioService {
//
//    private final SmsSender smsSender;
//
//    @Autowired
//    public TwilioService(@Qualifier("twilio") TwilioSmsSender smsSender) {
//        this.smsSender = smsSender;
//    }
//
//    public void sendSms(SmsRequest smsRequest) {
//        smsSender.sendSms(smsRequest);
//    }
//}