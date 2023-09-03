package plugin.mapper;

import com.mybatis.plugin.model.User;

import java.util.List;

/**
 * @author: tr256
 * @time: 2023/4/20
 */
public interface UserMapper {

    List<User> selectUserList();

    User selectUserById(Integer id);
}
