package com.itheima.reggie.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GKKRZkaZk3FxcP9fiuZ", "rkPgyyNqCFmXraP0xJTf4hB19zSQwC");
		IAcsClient client = new DefaultAcsClient(profile);

		SendSmsRequest request = new SendSmsRequest();
		request.setSysRegionId("cn-hangzhou");
		request.setPhoneNumbers(phoneNumbers);
		request.setSignName(signName);
		request.setTemplateCode(templateCode);
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		try {
			SendSmsResponse response = client.getAcsResponse(request);
			System.out.println("短信发送成功:"+response.getMessage());
		}catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//1.生成一个4位随机数字验证码
		String code = ValidateCodeUtils.generateValidateCode4String(4);
		//打印验证码
		System.out.println("验证码："+code);

		//2.发送短信
		SMSUtils.sendMessage("黑马旅游网","SMS_198917972","18620624601",code);
	}

}
