package com.sh.wxapp.controller;

import com.sh.wxapp.dto.user.UserInfoDTO;
import com.sh.wxapp.jwt.TokenUtils;
import com.sh.wxapp.req.user.UserInfoUpdateRequest;
import com.sh.wxapp.rop.JsonResponse;
import com.sh.wxapp.service.UserService;
import com.sh.wxapp.util.CookieUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: carorder
 * @description:
 * @author: xuqie
 * @create: 2019-02-23 18:26
 **/
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(HttpServletRequest request,String username, String password) {
        UserInfoDTO userInfoDTO = userService.getUser(username, password);
        CookieUtils.removeCookie(request,"token");
        Map map = new HashMap();
        map.put(TokenUtils.EXPIRETIME, System.currentTimeMillis() + TokenUtils.TOKEN_VALID_TIME);
        map.put(TokenUtils.USERID, userInfoDTO.getUserId());
        String token = TokenUtils.createToken(map);
        request.getSession().setAttribute("userInfo",userInfoDTO);
        Map mp = new HashMap();
        mp.put("token", token);
        return JsonResponse.success("登陆成功", mp);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation("注册")
    public JsonResponse register(String username, String password) {
        Long userId = userService.register(username, password);
        Map map = new HashMap();
        map.put("userId", userId);
        return JsonResponse.success("注册成功", map);
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("更新用户信息")
    public JsonResponse register(@RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfoUpdateRequest, userInfoDTO);
        userService.updateUserInfo(userInfoUpdateRequest.getUserId(), userInfoDTO);
        return JsonResponse.success("更新信息成功", null);
    }


}
