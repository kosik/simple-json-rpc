package io.github.kosik.simplejsonrpc.client.object;

import io.github.kosik.simplejsonrpc.client.JsonRpcParams;
import io.github.kosik.simplejsonrpc.client.ParamsType;
import io.github.kosik.simplejsonrpc.client.domain.Player;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcMethod;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcParam;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcService;

import java.util.List;

/**
 * Date: 11/17/14
 * Time: 9:59 PM
 */
@JsonRpcService
public interface BaseService {

    @JsonRpcMethod
    @JsonRpcParams(ParamsType.ARRAY)
    List<Player> getPlayers();

    @JsonRpcMethod
    @JsonRpcParams(ParamsType.ARRAY)
    Player getPlayer();

    @JsonRpcMethod
    long login(@JsonRpcParam("login") String login, @JsonRpcParam("password") String password);

    @JsonRpcMethod
    void logout(@JsonRpcParam("token") String token);
}
