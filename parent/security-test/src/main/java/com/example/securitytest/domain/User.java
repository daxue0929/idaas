package com.example.securitytest.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author daxue0929
 * @date 2022/7/27
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName("user")
public class User {
    Integer id;
    String name;
    String password;
}
