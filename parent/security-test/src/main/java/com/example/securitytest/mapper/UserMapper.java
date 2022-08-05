package com.example.securitytest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.securitytest.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author daxue0929
 * @date 2022/7/27
 */

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
