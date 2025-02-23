package io.github.kosik.simplejsonrpc.server.simple.service;

import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcMethod;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcOptional;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcParam;
import io.github.kosik.simplejsonrpc.core.annotation.JsonRpcService;
import io.github.kosik.simplejsonrpc.server.simple.domain.Player;
import io.github.kosik.simplejsonrpc.server.simple.domain.Position;
import io.github.kosik.simplejsonrpc.server.simple.domain.Team;
import io.github.kosik.simplejsonrpc.server.simple.exception.EmptyMessageTeamServiceException;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithDataField;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithDataGetter;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithDataMultipleFields;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithDataMultipleGetters;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithDataMultipleMixed;
import io.github.kosik.simplejsonrpc.server.simple.exception.ExceptionWithWrongMethods;
import io.github.kosik.simplejsonrpc.server.simple.exception.TeamServiceAuthException;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 7/27/14
 * Time: 8:46 PM
 */
@JsonRpcService
public class TeamService extends BaseService {

    private final Team stLouis = new Team("St. Louis Blues", "NHL");
    private final Team tampa = new Team("Tampa Bay Lightning", "NHL");
    private final List<Player> players = Stream.of(
                    new Player("David", "Backes", stLouis, 42, Position.CENTER, date("1984-05-01"), 4.5),
                    new Player("Vladimir", "Tarasenko", stLouis, 91, Position.RIGHT_WINGER, date("1991-12-13"), 0.9),
                    new Player("Jack", "Allen", stLouis, 34, Position.GOALTENDER, date("1990-08-07"), 0.5),
                    new Player("Jay", "Bouwmeester", stLouis, 19, Position.DEFENDER, date("1985-08-07"), 5.4),
                    new Player("Steven", "Stamkos", tampa, 91, Position.CENTER, date("1990-02-07"), 7.5),
                    new Player("Ryan", "Callahan", tampa, 24, Position.RIGHT_WINGER, date("1985-03-21"), 5.8),
                    new Player("Ben", "Bishop", tampa, 30, Position.GOALTENDER, date("1986-11-21"), 2.3),
                    new Player("Victor", "Hedman", tampa, 77, Position.DEFENDER, date("1990-12-18"), 4.0))
            .collect(Collectors.toList());

    @JsonRpcMethod
    public boolean add(@JsonRpcParam("player") Player s) {
        return players.add(s);
    }

    @JsonRpcMethod("find_by_birth_year")
    public List<Player> findByBirthYear(@JsonRpcParam("birth_year") final int birthYear) {
        return players.stream().filter(player -> {
            int year = LocalDate.ofInstant(player.birthDate().toInstant(), ZoneOffset.UTC).getYear();
            return year == birthYear;
        }).collect(Collectors.toList());
    }

    @JsonRpcMethod
    public Player findByInitials(@JsonRpcParam("firstName") final String firstName,
                                 @JsonRpcParam("lastName") final String lastName) {
        return players.stream()
                .filter(player -> player.firstName().equals(firstName) && player.lastName().equals(lastName))
                .findAny()
                .orElse(null);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @JsonRpcMethod
    public List<Player> find(@JsonRpcOptional @JsonRpcParam("position") @Nullable Position position,
                             @JsonRpcOptional @JsonRpcParam("number") int number,
                             @JsonRpcOptional @JsonRpcParam("team") Optional<Team> team,
                             @JsonRpcOptional @JsonRpcParam("firstName") @Nullable String firstName,
                             @JsonRpcOptional @JsonRpcParam("lastName") @Nullable String lastName,
                             @JsonRpcOptional @JsonRpcParam("birthDate") @Nullable Date birthDate,
                             @JsonRpcOptional @JsonRpcParam("capHit") Optional<Double> capHit) {
        return players.stream().filter(player -> {
            if (position != null && !player.position().equals(position)) return false;
            if (number != 0 && player.number() != number) return false;
            if (team.isPresent() && !player.team().equals(team.get())) return false;
            if (firstName != null && !player.firstName().equals(firstName)) return false;
            if (lastName != null && !player.lastName().equals(lastName)) return false;
            if (birthDate != null && !player.birthDate().equals(birthDate)) return false;
            if (capHit.isPresent() && player.capHit() != capHit.get()) return false;
            return true;
        }).collect(Collectors.toList());
    }

    @JsonRpcMethod
    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> bogusGetPlayers() {
        return players;
    }

    @JsonRpcMethod
    private List<Player> privateGetPlayers() {
        return players;
    }

    @JsonRpcMethod
    public Player bogusFindByInitials(@JsonRpcParam("firstName") String firstName, String lastName) {
        return findByInitials(firstName, lastName);
    }

    @JsonRpcMethod
    public Player findByCapHit(@JsonRpcParam("cap") double capHit) {
        throw new IllegalStateException("Not implemented");
    }

    @JsonRpcMethod
    public long login(@JsonRpcParam("login") String login, @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new TeamServiceAuthException("Not authorized");
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long bogusMessageLogin(@JsonRpcParam("login") String login, @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new EmptyMessageTeamServiceException("Not authorized");
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataFieldLogin(@JsonRpcParam("login") String login, @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithDataField("Detailed message", new String[]{"Data 1", "Data 2"});
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataGetterLogin(@JsonRpcParam("login") String login, @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithDataGetter("Detailed message", new String[]{"Data 1", "Data 2"});
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataMultipleFieldsLogin(@JsonRpcParam("login") String login,
                                             @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithDataMultipleFields(
                    "Detailed message",
                    new String[]{"Data 1", "Data 2"},
                    "AnotherData");
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataMultipleGettersLogin(@JsonRpcParam("login") String login,
                                              @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithDataMultipleGetters(
                    "Detailed message",
                    new String[]{"Data 1", "Data 2"},
                    "AnotherData");
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataMultipleMixedLogin(@JsonRpcParam("login") String login,
                                            @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithDataMultipleMixed(
                    "Detailed message",
                    new String[]{"Data 1", "Data 2"});
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public long errorDataWrongMethodsLogin(@JsonRpcParam("login") String login,
                                           @JsonRpcParam("password") String password) {
        if (!login.equals("CAFE") && !password.equals("BABE")) {
            throw new ExceptionWithWrongMethods(
                    "Detailed message",
                    new String[]{"Data 1", "Data 2"});
        }
        return 0xCAFEBABE;
    }

    @JsonRpcMethod
    public Player bogusFind(@JsonRpcParam("firstName") String firstName,
                            @JsonRpcParam("firstName") String lastName,
                            @JsonRpcParam("age") int age) {
        return null;
    }

    @JsonRpcMethod
    public List<Player> findPlayersByFirstNames(@JsonRpcParam("names") final List<String> names) {
        return players.stream().filter(player -> names.contains(player.firstName())).collect(Collectors.toList());
    }

    @JsonRpcMethod
    public List<Player> findPlayersByNumbers(@JsonRpcParam("numbers") final int... numbers) {
        return players.stream().filter(player -> {
            for (int number : numbers) {
                if (player.number() == number) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    @SafeVarargs
    @JsonRpcMethod
    public final <T> List<Player> genericFindPlayersByNumbers(@JsonRpcParam("numbers") final T... numbers) {
        return players.stream().filter(player -> {
            for (T number : numbers) {
                if (String.valueOf(player.number()).equals(number.toString())) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    @JsonRpcMethod
    public Map<String, Double> getContractSums(@JsonRpcParam("contractLengths") Map<String, ? extends Number> contractLengths) {
        Map<String, Double> playerContractSums = new LinkedHashMap<>();
        for (Player player : players) {
            playerContractSums.put(player.lastName(),
                    player.capHit() * contractLengths.get(player.lastName()).intValue());
        }
        return playerContractSums;
    }


    @JsonRpcMethod
    public static Date date(@JsonRpcParam("textDate") String textDate) {
        return Date.from(LocalDate.parse(textDate).atStartOfDay(ZoneOffset.UTC).toInstant());
    }

}
