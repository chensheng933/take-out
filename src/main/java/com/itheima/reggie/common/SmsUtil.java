package com.itheima.reggie.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;

public class SmsUtil {
    //==========================下面四项配置修改成自己的========================
    private static String accessKeyId = "LTAI5tKCnaGQWqasMN6avPbm";//需要替换成自己申请的accessKeyId
    private static String accessKeySecret = "LOpyNJiR0k1TB4co7WCSqN1vqSWhcJ";//需要替换成自己申请的accessKeySecret
    private static String signName = "阿里云短信测试";//使用自己的签名
    private static String templateCode = "SMS_154950909";//使用自己的短信模版
    //====================================END===================================


    public static SendSmsResponse sendSms(String phoneNumbers, String code) throws ClientException {
        return sendSms(phoneNumbers, signName, templateCode, "{\"code\":\"" + code + "\"}");
    }

    /**
     * 发送短信
     * @param phoneNumbers 要发送短信到哪个手机号
     * @param signName     短信签名[必须使用前面申请的]
     * @param templateCode 短信短信模板ID[必须使用前面申请的]
     * @param param        模板中${code}位置传递的内容
     */
    public static SendSmsResponse sendSms(String phoneNumbers, String signName, String templateCode, String param) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //待发送手机号
        request.setPhoneNumbers(phoneNumbers);
        //短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //模板中的变量替换JSON串,如模板内容为"您好,您的验证码为${code}"
        request.setTemplateParam(param);

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println(new Gson().toJson(sendSmsResponse));
        return sendSmsResponse;
    }

    public static void main(String[] args) throws ClientException {
        SendSmsResponse sendSmsResponse = sendSms("13422943068", "6688");
        if (sendSmsResponse.getCode().equalsIgnoreCase("ok")) {
            System.out.println("短信发送成功");
        }

    }
}
