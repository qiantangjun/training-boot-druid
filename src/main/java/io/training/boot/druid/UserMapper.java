package io.training.boot.druid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    //@Select("select * from user where id = #{id}")
    User findUser(@Param("id") Integer id);

    Integer create(User user);
}
