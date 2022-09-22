package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :王冰冰
 * @date : 2022/9/4
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
