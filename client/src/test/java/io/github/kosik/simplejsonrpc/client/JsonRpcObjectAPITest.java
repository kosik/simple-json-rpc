package io.github.kosik.simplejsonrpc.client;

import io.github.kosik.simplejsonrpc.client.domain.Player;
import io.github.kosik.simplejsonrpc.client.domain.Position;
import io.github.kosik.simplejsonrpc.client.domain.Team;
import io.github.kosik.simplejsonrpc.client.exception.JsonRpcException;
import io.github.kosik.simplejsonrpc.client.object.FixedIntegerIdGenerator;
import io.github.kosik.simplejsonrpc.client.object.FixedStringIdGenerator;
import io.github.kosik.simplejsonrpc.client.object.TeamService;
import io.github.kosik.simplejsonrpc.client.util.MapBuilder;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcMethod;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcParam;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcService;
import io.github.kosik.simplejsonrpc.core.domain.ErrorMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.Checksum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Date: 24.08.14
 * Time: 18:06
 */
public class JsonRpcObjectAPITest extends BaseClientTest {

    @Test
    public void testAddPlayer() {
        JsonRpcClient client = initClient("add_player");
        TeamService teamService = client.onDemand(TeamService.class, new FixedStringIdGenerator("asd671"));
        boolean result = teamService.add(new Player("Kevin", "Shattenkirk",
                new Team("St. Louis Blues", "NHL"), 22, Position.DEFENDER,
                Date.from(LocalDate.parse("1989-01-29").atStartOfDay(ZoneOffset.UTC).toInstant()),
                4.25));
        assertThat(result).isTrue();
    }

    @Test
    public void findPlayerByInitials() {
        JsonRpcClient client = initClient("find_player");
        Player player = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(43121)).findByInitials("Steven", "Stamkos");
        assertThat(player).isNotNull();
        assertThat(player.firstName()).isEqualTo("Steven");
        assertThat(player.lastName()).isEqualTo("Stamkos");
    }

    @Test
    public void testPlayerIsNotFound() {
        JsonRpcClient client = initClient("player_is_not_found");
        Player player = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(4111)).findByInitials("Vladimir", "Sobotka");
        assertThat(player).isNull();
    }

    @Test
    public void testOptionalParams() {
        JsonRpcClient client = initClient("optional_params");
        List<Player> players = client.onDemand(TeamService.class, new FixedStringIdGenerator("xar331"))
                .find(null, 91, Optional.of(new Team("St. Louis Blues", "NHL")),
                        null, null, null, Optional.empty());
        Assertions.assertEquals(players.size(), 1);
        Player player = players.get(0);
        assertThat(player.team()).isEqualTo(new Team("St. Louis Blues", "NHL"));
        assertThat(player.number()).isEqualTo(91);
        assertThat(player.firstName()).isEqualTo("Vladimir");
        assertThat(player.lastName()).isEqualTo("Tarasenko");
    }

    @Test
    public void testOptionalArray() {
        JsonRpcClient client = initClient("find_array_null_params");
        List<Player> players = client.onDemand(TeamService.class, ParamsType.ARRAY, new FixedStringIdGenerator("pasd81"))
                .find(null, 19, Optional.of(new Team("St. Louis Blues", "NHL")),
                        null, null, null, Optional.empty());
        Assertions.assertEquals(players.size(), 1);
        Player player = players.get(0);
        assertThat(player.team()).isEqualTo(new Team("St. Louis Blues", "NHL"));
        assertThat(player.number()).isEqualTo(19);
        assertThat(player.firstName()).isEqualTo("Jay");
        assertThat(player.lastName()).isEqualTo("Bouwmeester");
    }

    @Test
    public void testFindArray() {
        JsonRpcClient client = initClient("find_player_array");
        Player player = client.onDemand(TeamService.class, ParamsType.ARRAY, new FixedStringIdGenerator("dsfs1214"))
                .findByInitials("Ben", "Bishop");
        assertThat(player).isNotNull();
        assertThat(player.firstName()).isEqualTo("Ben");
        assertThat(player.lastName()).isEqualTo("Bishop");
    }

    @Test
    public void testReturnList() {
        JsonRpcClient client = initClient("findByBirthYear");
        List<Player> players = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(5621)).findByBirthYear(1990);
        assertThat(players).isNotNull();
        assertThat(players).hasSize(3);
        assertThat(players.get(0).lastName()).isEqualTo("Allen");
        assertThat(players.get(1).lastName()).isEqualTo("Stamkos");
        assertThat(players.get(2).lastName()).isEqualTo("Hedman");
    }

    @Test
    public void testNoParams() {
        JsonRpcClient client = initClient("getPlayers");
        List<Player> players = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(1000)).getPlayers();
        assertThat(players).isNotNull();
        assertThat(players).hasSize(3);
        assertThat(players.get(0).lastName()).isEqualTo("Bishop");
        assertThat(players.get(1).lastName()).isEqualTo("Tarasenko");
        assertThat(players.get(2).lastName()).isEqualTo("Bouwmeester");
    }

    @Test
    public void testReturnVoid() {
        JsonRpcClient client = initClient("logout");
        client.onDemand(TeamService.class, new FixedIntegerIdGenerator(29314)).logout("fgt612");
    }

    @Test
    public void testMap() {
        Map<String, Integer> contractLengths = new MapBuilder<String, Integer>()
                .put("Backes", 4)
                .put("Tarasenko", 3)
                .put("Allen", 2)
                .put("Bouwmeester", 5)
                .put("Stamkos", 8)
                .put("Callahan", 3)
                .put("Bishop", 4)
                .put("Hedman", 2)
                .build();
        JsonRpcClient client = initClient("getContractSums");
        Map<String, Double> contractSums = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(97555))
                .getContractSums(contractLengths);
        assertThat(contractSums).isExactlyInstanceOf(LinkedHashMap.class);
        assertThat(contractSums).isEqualTo(new MapBuilder<String, Double>()
                .put("Backes", 18.0)
                .put("Tarasenko", 2.7)
                .put("Allen", 1.0)
                .put("Bouwmeester", 27.0)
                .put("Stamkos", 60.0)
                .put("Callahan", 17.4)
                .put("Bishop", 9.2)
                .put("Hedman", 8.0)
                .build());
    }

    @Test
    public void testOptional() {
        JsonRpcClient client = initClient("player_is_not_found");
        Optional<Player> optionalPlayer = client.onDemand(TeamService.class, new FixedIntegerIdGenerator(4111))
                .optionalFindByInitials("Vladimir", "Sobotka");
        assertThat(optionalPlayer.isPresent()).isFalse();
    }

    @Test
    public void testJsonRpcError() {
        JsonRpcClient client = initClient("methodNotFound");
        try {
            client.onDemand(TeamService.class, new FixedIntegerIdGenerator(1001)).getPlayer();
            Assertions.fail();
        } catch (JsonRpcException e) {
            e.printStackTrace();
            ErrorMessage errorMessage = e.getErrorMessage();
            assertThat(errorMessage.getCode()).isEqualTo(-32601);
            assertThat(errorMessage.getMessage()).isEqualTo("Method not found");
        }
    }

    @JsonRpcService
    interface BogusTeamService {

        @JsonRpcMethod
        void bogusLogin(String username, @JsonRpcParam("password") String password);
    }

    @Test
    public void testParameterIsNotAnnotated() {
        assertThatIllegalStateException()
                .isThrownBy(() -> fakeClient().onDemand(BogusTeamService.class).bogusLogin("super", "secret"))
                .withMessage("Parameter with index=0 of method 'bogusLogin' is not annotated with @JsonRpcParam");
    }

    @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "ResultOfMethodCallIgnored"})
    @Test
    public void testNotJsonRpcMethod() {
        assertThatIllegalStateException()
                .isThrownBy(() -> fakeClient().onDemand(TeamService.class).equals("Test"))
                .withMessage("Method 'equals' is not JSON-RPC available");
    }

    @JsonRpcService
    public interface MethodIsNotAnnotatedService {

        boolean find(@JsonRpcParam("name") String name);
    }

    @Test
    public void testMethodIsNotAnnotated() {
        assertThatIllegalStateException()
                .isThrownBy(() -> fakeClient().onDemand(MethodIsNotAnnotatedService.class).find("Logan"))
                .withMessage("Method 'find' is not annotated as @JsonRpcMethod");
    }

    @Test
    public void testServiceIsNotAnnotated() {
        assertThatIllegalStateException()
                .isThrownBy(() -> fakeClient().onDemand(Checksum.class).getValue())
                .withMessage("Class 'java.util.zip.Checksum' is not annotated as @JsonRpcService");
    }

    @JsonRpcService
    interface DuplicateParametersService {

        @JsonRpcMethod
        boolean find(@JsonRpcParam("code") String username, @JsonRpcParam("code") String code, @JsonRpcParam("number") int number);
    }

    @Test
    public void testDuplicatedParameterNames() {
        assertThatIllegalStateException()
                .isThrownBy(() -> fakeClient().onDemand(DuplicateParametersService.class).find("Joe", "12", 21))
                .withMessage("Two parameters of method 'find' have the same name 'code'");
    }

}
