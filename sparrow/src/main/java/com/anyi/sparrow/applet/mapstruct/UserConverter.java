package com.anyi.sparrow.applet.mapstruct;

import com.anyi.common.user.domain.UserAccount;
import com.anyi.sparrow.applet.user.vo.UserInfoVO;
import com.anyi.sparrow.base.security.LoginUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author peng can
 * @date 2022/12/4 12:20
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(target = "userId",source = "id"),
            @Mapping(target = "nickname",source = "name")
    })
    UserInfoVO logUser2InfoVO(LoginUser loginUser);

    @Mappings({
            @Mapping(target = "userId",source = "id")
    })
    UserInfoVO user2InfoVO(UserAccount userAccount);
}
