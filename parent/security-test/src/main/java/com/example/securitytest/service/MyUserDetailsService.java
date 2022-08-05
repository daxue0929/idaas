package com.example.securitytest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.securitytest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author daxue0929
 * @date 2022/7/27
 */

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {


    final UserMapper userMapper;

    @Autowired
    public MyUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        final List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");

        final QueryWrapper<com.example.securitytest.domain.User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        final com.example.securitytest.domain.User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }


        return new User(user.getName(), new BCryptPasswordEncoder().encode(user.getPassword()), auths);
    }

}
