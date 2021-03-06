package org.neo4j.jdbc.bolt;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.jdbc.bolt.data.StatementData;
import org.neo4j.jdbc.bolt.utils.JdbcConnectionTestUtils;

import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for the setParameter for Date, Time and Timestamp
 * @since 3.4
 */
public class BoltNeo4jSetParameterDateIT {
    @ClassRule
    public static Neo4jBoltRule neo4j = new Neo4jBoltRule();

    static Connection connection;

    @Before
    public void cleanDB() throws SQLException {
        neo4j.getGraphDatabase().execute(StatementData.STATEMENT_CLEAR_DB);
        connection = JdbcConnectionTestUtils.verifyConnection(connection, neo4j);
    }

    @AfterClass
    public static void tearDown(){
        JdbcConnectionTestUtils.closeConnection(connection);
    }

    /*
    =============================
            TIMESTAMP
    =============================
    */

    @Test
    public void shouldSetFieldTimestamp() throws SQLException {

        LocalDateTime ldt = LocalDateTime.now();
        long epochMilli = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Timestamp now = new Timestamp(epochMilli);

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldTimestamp' }) RETURN e AS event");
        preparedStatement.setTimestamp(1,now);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldTimestamp' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type", whenObj instanceof LocalDateTime);

        LocalDateTime when = (LocalDateTime) whenObj;

        assertEquals("Wrong data",ldt, when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }

    @Test
    public void shouldSetFieldTimestampAndCalendar() throws SQLException {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        ZonedDateTime zdt = Instant.ofEpochMilli(cal.getTimeInMillis()).atZone(ZoneId.of("America/New_York"));

        Timestamp now = new Timestamp(cal.getTimeInMillis());

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldTimestampAndCalendar' }) RETURN e AS event");
        preparedStatement.setTimestamp(1,now, cal);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldTimestampAndCalendar' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type", whenObj instanceof ZonedDateTime);

        ZonedDateTime when = (ZonedDateTime) whenObj;

        assertEquals("Wrong data",zdt, when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }

    /*
    =============================
            DATE
    =============================
    */

    @Test
    public void shouldSetFieldDate() throws SQLException {

        LocalDateTime ldt = LocalDateTime.now();
        long epochMilli = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Date date = new Date(epochMilli);

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldDate' }) RETURN e AS event");
        preparedStatement.setDate(1,date);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldDate' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type",whenObj instanceof LocalDate);

        LocalDate when = (LocalDate) whenObj;

        assertEquals("Wrong data",ldt.toLocalDate(), when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }

    @Test
    public void shouldSetFieldDateAndCalendar() throws SQLException {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        ZonedDateTime zdt = Instant.ofEpochMilli(cal.getTimeInMillis()).atZone(ZoneId.of("America/New_York"));
        long epochMilli = zdt.toInstant().toEpochMilli();

        Date date = new Date(epochMilli);

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldDateAndCalendar' }) RETURN e AS event");
        preparedStatement.setDate(1,date, cal);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldDateAndCalendar' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type",whenObj instanceof ZonedDateTime);

        ZonedDateTime when = (ZonedDateTime) whenObj;

        assertEquals("Wrong data",zdt, when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }


    /*
    =============================
            TIME
    =============================
    */


    @Test
    public void shouldSetFieldTime() throws SQLException {

        LocalDateTime ldt = LocalDateTime.now();
        long epochMilli = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Time time = new Time(epochMilli);

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldTime' }) RETURN e AS event");
        preparedStatement.setTime(1,time);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldTime' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type", whenObj instanceof LocalTime);

        LocalTime when = (LocalTime) whenObj;

        assertEquals("Wrong data",ldt.toLocalTime(), when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }

    @Test
    public void shouldSetFieldTimeAndCalendar() throws SQLException {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        ZonedDateTime zdt = Instant.ofEpochMilli(cal.getTimeInMillis()).atZone(ZoneId.of("America/New_York"));
        OffsetTime offsetTime = zdt.toOffsetDateTime().toOffsetTime();
        long epochMilli = zdt.toInstant().toEpochMilli();

        Time time = new Time(epochMilli);

        PreparedStatement preparedStatement = connection.prepareStatement("CREATE (e:Event {when: ?, test: 'shouldSetFieldTimeAndCalendar' }) RETURN e AS event");
        preparedStatement.setTime(1,time,cal);
        preparedStatement.execute();

        Result result = neo4j.getGraphDatabase().execute("MATCH (e:Event) WHERE e.test = 'shouldSetFieldTimeAndCalendar' RETURN e.when as when");

        assertTrue("Node not found",result.hasNext());

        Map<String, Object> next = result.next();

        assertTrue("Result not found",next.containsKey("when"));

        Object whenObj = next.get("when");

        assertTrue("Wrong type", whenObj instanceof OffsetTime);

        OffsetTime when = (OffsetTime) whenObj;

        assertEquals("Wrong data",offsetTime, when);

        JdbcConnectionTestUtils.closeStatement(preparedStatement);
    }
}
