package com.grepp.synapse4.app.model.auth.token;

import com.grepp.synapse4.app.model.auth.token.entity.UserBlackList;
import org.springframework.data.repository.CrudRepository;

public interface UserBlackListRepository extends CrudRepository<UserBlackList, String> {

}
