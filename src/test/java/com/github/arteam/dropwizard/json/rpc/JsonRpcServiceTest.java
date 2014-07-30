package com.github.arteam.dropwizard.json.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.github.arteam.dropwizard.json.rpc.protocol.controller.JsonRpcServer;
import com.github.arteam.dropwizard.json.rpc.service.TeamService;
import com.github.arteam.dropwizard.json.rpc.util.RequestResponse;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 7/28/14
 * Time: 10:29 PM
 *
 * @author Artem Prigoda
 */
public class JsonRpcServiceTest {

    private static ObjectMapper userMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private static Map<String, RequestResponse> testData;

    private JsonRpcServer rpcServer = new JsonRpcServer(userMapper);
    private TeamService teamService = new TeamService();

    @BeforeClass
    public static void init() throws Exception {
        testData = new ObjectMapper().readValue(
                Resources.toString(JsonRpcServiceTest.class.getResource("/test_data.json"), Charsets.UTF_8),
                MapType.construct(Map.class,
                        SimpleType.construct(String.class),
                        SimpleType.construct(RequestResponse.class)));
    }

    /**
     * Test additional of a complex object
     */
    @Test
    public void testAddPlayer() {
        test("add_player");
        test("find_shattenkirk");
    }

    /**
     * Test usual case (request and complex response)
     */
    @Test
    public void testFindPlayer() {
        test("find_player");
    }

    /**
     * Test null as a result
     */
    @Test
    public void testPlayerIsNotFound() {
        test("player_is_not_found");
    }

    /**
     * Test params are set as array
     */
    @Test
    public void testFindPlayerWithArrayParams() {
        test("find_player_array");
    }

    /**
     * Test optional fields
     */
    @Test
    public void testFind() {
        test("find");
    }

    /**
     * Test overridden method name and response as a list of objects
     */
    @Test
    public void testFindByBirthYear() {
        test("findByBirthYear");
    }

    /**
     * Test optional fields in array params
     */
    @Test
    public void testFindWithArrayNullParams() {
        test("find_array_null_params");
    }

    /**
     * Test calling a method from super-class and method without parameters
     */
    @Test
    public void testIsAlive() {
        test("isAlive");
    }

    private void test(String testName) {
        try {
            RequestResponse requestResponse = testData.get(testName);
            String textRequest = userMapper.writeValueAsString(requestResponse.request);

            String actual = rpcServer.handle(textRequest, teamService);
            assertThat(requestResponse.response).isEqualTo(userMapper.readTree(actual));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
