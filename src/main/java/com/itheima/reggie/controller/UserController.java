package com.itheima.reggie.controller;
import cn.hutool.core.util.RandomUtil;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.util.SMSUtils;
import com.itheima.reggie.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.Map;

import static com.itheima.reggie.common.ConstantUtil.LOGINCODE;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 发送短信
     * @param user
     * @param session
     * @return
     */
    @PostMapping("sendMsg")
    public R sendMsg(@RequestBody User user, HttpSession session){
        //1.获取手机号
        String phone = user.getPhone();

        //2.生成随机数字验证码
        String code = RandomUtil.randomNumbers(6);
        System.out.println(code);

        //TODO 等项目上线了 把这行代码注释掉
        code = "1234";

        //4.若发送成功,将手机验证码存入session,方便登陆校验
        //session.setAttribute(phone,code);

        //4.若发送成功,将手机验证码存入redis且设置时效,方便登陆校验
        redisTemplate.opsForValue().set(LOGINCODE+phone,code, Duration.ofMinutes(5));

        //5.返回r
        return R.success(null);

        //3.调用工具类发送手机验证码
        //TODO 以后上线了,在把这段代码放开
//        try {
//            SendSmsResponse sendSmsResponse = SmsUtil.sendSms(phone, code);
//            if (sendSmsResponse.getCode().equalsIgnoreCase("ok")) {
//                //4.若发送成功,将手机验证码存入session,方便登陆校验
//                session.setAttribute(phone,code);
//            }
//            return R.success(null);
//        } catch (ClientException e) {
//            e.printStackTrace();
//            return R.error("短信发送失败");
//        }
    }

    /**
     * app用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("login")
    public R login(@RequestBody Map<String,String> map,HttpSession session){
        //1.获取用户手机号和验证码
        String phone = map.get("phone");
        String requestCode = map.get("code");

        /*
        //2.获取session中验证码
        String sessionCode = (String) session.getAttribute(phone);
        */

        //2.获取redis中验证码
        String sessionCode = (String) redisTemplate.opsForValue().get(LOGINCODE+phone);

        //3.调用userService中login方法完成登陆操作
        R<User> r = userService.login(phone,requestCode,sessionCode);

        //4.判断用户是否登陆成功,若登陆成功,将用户的id存入session(记录登陆状态)
        if (r.getCode() == 1) {
            session.setAttribute("user",r.getData().getId());

            /*//移除session中验证码
            session.removeAttribute(phone);*/

            //移除redis中验证码
            redisTemplate.delete(LOGINCODE+phone);
        }

        //5.返回R
        return r;
    }

    /**
     * 作用： 发送短信
     * @param user
     * @return
     */
    @RequestMapping("/sendMsg")
    public R<String> sendMsg2(@RequestBody  User user, HttpSession session){
        //1. 获取手机号
        String phone = user.getPhone();

        //2. 生成四位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        //3. 调用阿里的工具类去发送短信(备注： 每位同学发两次就要注释了)
        SMSUtils.sendMessage("黑马旅游网","SMS_205126318",phone,code);
        //log.info("=======本次验证码：========="+ code);
        System.out.println("=======本次验证码：========="+ code);
        //4. 把验证码存储到session中。
        session.setAttribute(phone,code); //不好控制有效时间

        //5. 返回信息
        return R.success("发送成功");
    }

    /**
     * 作用： 登陆校验（宇哥）
     * @param param  存储phone与用户输入的code
     * @return
     */
    @RequestMapping("/login")
    public R<User> login2(@RequestBody Map<String,String> param, HttpSession session){
        //1. 取出参数
        String phone = param.get("phone");
        String code = param.get("code"); //用户输入的验证码
        String codeInSession = (String) session.getAttribute(phone);
        //2. 把参数交给Service,返回当前登陆者对象
        User user =  userService.login2(phone,code,codeInSession);
        //登录成功要做一个登录成功标记
        if(user!=null){
            session.setAttribute("user",user.getId());
        }else{
            return R.error("登陆失败");
        }
        //3. 返回响应
        return R.success(user);
    }
}
