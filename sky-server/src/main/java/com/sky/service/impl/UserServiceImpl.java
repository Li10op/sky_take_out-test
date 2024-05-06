package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;



    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */

    public User wxLogin(UserLoginDTO userLoginDTO) throws LoginException {
        String openId = getOpenid(userLoginDTO.getCode());


//        判断openid是否为空,为空抛出异常：登录失败
        if(openId == null || openId.equals("")) {
            throw new LoginException(MessageConstant.LOGIN_FAILED);
        }

//        判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openId);
        if(user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }
//        新用户自动完成注册
        return user;




    }


    /**
     * 调用微信接口服务获取openid信息
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        //        调用微信接口服务获取openid信息

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid", weChatProperties.getAppid());
        hashMap.put("secret", weChatProperties.getSecret());
        hashMap.put("js_code", code);
        hashMap.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, hashMap);

        JSONObject jsonObject = JSON.parseObject(json);
        String openId = jsonObject.getString("openid");
        return openId;

    }
}
