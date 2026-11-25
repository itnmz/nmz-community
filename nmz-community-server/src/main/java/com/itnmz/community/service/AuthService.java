package com.itnmz.community.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itnmz.community.common.exception.BusinessException;
import com.itnmz.community.config.SmsCodeStore;
import com.itnmz.community.mapper.UserMapper;
import com.itnmz.community.model.dto.LoginDTO;
import com.itnmz.community.model.dto.RegisterDTO;
import com.itnmz.community.model.entity.User;
import com.itnmz.community.model.vo.LoginVO;
import com.itnmz.community.model.vo.UserVO;
import com.itnmz.community.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * 认证业务：发验证码、注册、登录。
 */
@Slf4j
@Service
@RequiredArgsConstructor  //通过构造器注入依赖
public class AuthService {

    /** 验证码有效期 */
    private static final Duration CODE_TTL = Duration.ofMinutes(5);

    /** SecureRandom 而不是 Math.random：后者是可预测的伪随机，验证码能被推算 */
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SmsCodeStore smsCodeStore;
    private final SmsService smsService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 1、生成并发送验证码。
     * 手机号格式已由 SendCodeDTO 上的 @Pattern 拦过，方法内不再重复校验。
     */
    public void sendCode(String phone) {
        //format()：’%06d‘：格式化符（d十进制，6六位，0不足六位时左边补0）
        //RANDOM.nextInt(1_000_000)：生成[0, 999999]的整数（包含 0，不包含 1000000），也就是 0～999999
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));

        //写入redis中
        smsCodeStore.save(phone, code, CODE_TTL);
        //把验证码打印到控制台
        smsService.sendVerificationCode(phone, code);

        log.info("验证码已生成，phone={}", phone);
    }


    /**
     * 2、注册：校验验证码 → 查重 → 加密密码 → 落库 → 签发 token。
     *
     * 加 @Transactional：现在只有一次 insert，但以后加「送新人积分」「建默认收藏夹」
     * 就是多表写入了，提前把事务边界定好，避免以后改结构。
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterDTO dto) {
        String phone = dto.getPhone();

        // ① 验证码校验
        String cachedCode = smsCodeStore.get(phone);
        if (cachedCode == null) {
            throw new BusinessException("验证码已失效，请重新获取");
        }
        if (!cachedCode.equals(dto.getCode())) {
            throw new BusinessException("验证码错误");
        }

        // ② 一次性消费：校验通过后立刻删除，同一个码不能用第二次
        smsCodeStore.delete(phone);

        // ③ 根据手机号获取用户 查重
        if (findByPhone(phone) != null) {
            throw new BusinessException("该手机号已注册，请直接登录");
        }

        // ④ 组装用户
//        User user = new User();
//        user.setPhone(phone);
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setNickname(resolveNickname(dto.getNickname(), phone));
//        user.setAvatar("");
//        user.setRole("USER");
//        user.setStatus(1);

        //使用builder()构建对象（建造者模式）
        User user = User.builder()
                .phone(phone)
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(resolveNickname(dto.getNickname(), phone))
                .avatar("")
                .role("USER")
                .status(1)
                .build();

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            // 并发兜底：两个请求同时通过查重时，uk_phone 会让其中一个插入失败
            throw new BusinessException("该手机号已注册，请直接登录");
        }

        log.info("新用户注册成功, userId={}, phone={}", user.getId(), phone);
        return buildLoginVO(user);
    }


    /**
     * 3、登录：手机号 + 密码。
     */
    public LoginVO login(LoginDTO dto) {
        //获取用户
        User user = findByPhone(dto.getPhone());

        //1、判断有无
        if (user == null) {
            throw new BusinessException(404, "该手机号尚未注册，请先注册");
        }
        //2、判断用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用，请联系管理员");
        }
        //3、passwordEncoder.matches()：第一个参数(原密码)、第二个参数(加密后)，true 表示密码一致，false 表示密码错误。
        if (user.getPassword() == null || !passwordEncoder.matches(/*原密码*/dto.getPassword(), /*加密后*/user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        return buildLoginVO(user);
    }




    /** 获取根据手机号获取用户 */
    private User findByPhone(String phone) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));
    }

    /** 昵称为空时的默认值：用户 + 手机号后四位 */
    private String resolveNickname(String nickname, String phone) {
        if (nickname != null && !nickname.isBlank()) { //str.isBlank()不判断null，只判断""和" "
            //作用：去掉字符串首尾的空白字符（空格、制表符、换行、回车），中间空格不动
            return nickname.trim();
        }
        return "用户" + phone.substring(phone.length() - 4); //substring()方法左闭右开
    }

    /** 生成jwt返回给前端 */
    private LoginVO buildLoginVO(User user) {
        LoginVO vo = new LoginVO();

        // subject 存 userId，username claim 现在存的是手机号（表里已无 username 字段）
        //生成token
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getPhone()));
        //封装返回前端
        vo.setUserInfo(UserVO.from(user));
        return vo;
    }
}
