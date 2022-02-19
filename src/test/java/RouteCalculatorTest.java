import calculator.RouteCalculator;
import calculator.StationIndex;
import core.Line;
import core.Station;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RouteCalculatorTest {

    public static final double DELTA = 0.01;
    List<Station> rout;
    StationIndex stationIndex = new StationIndex();
    RouteCalculator routeCalculator = new RouteCalculator(stationIndex);
    List<List<Station>> connection = new ArrayList<>();
    Line line1 = new Line(1, "Первая");
    Line line2 = new Line(2, "Вторая");
    Line line3 = new Line(3, "Третья");
    Station testStation1 = new Station("Test1", line1);
    Station testStation2 = new Station("Test2", line1);
    Station testStation3 = new Station("Test3", line1);
    Station testStation4 = new Station("Test4", line2);
    Station testStation5 = new Station("Test5", line2);
    Station testStation6 = new Station("Test6", line3);
    Station testStation7 = new Station("Test7", line3);

    @Before
    public void setUp() throws Exception {
        line1.addStation(testStation1);
        line1.addStation(testStation2);
        line1.addStation(testStation3);
        line2.addStation(testStation4);
        line2.addStation(testStation5);
        line3.addStation(testStation6);
        line3.addStation(testStation7);
        rout = new ArrayList<>();

        rout.add(testStation1);
        rout.add(testStation2);
        rout.add(testStation3);
        rout.add(testStation4);
        rout.add(testStation5);

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);
        stationIndex.addStation(testStation1);
        stationIndex.addStation(testStation2);
        stationIndex.addStation(testStation3);
        stationIndex.addStation(testStation4);
        stationIndex.addStation(testStation5);
        stationIndex.addStation(testStation6);
        stationIndex.addStation(testStation7);

        connection.add(new ArrayList<>(List.of(testStation2, testStation4)));
        connection.add(new ArrayList<>(List.of(testStation5, testStation6)));

        stationIndex.addConnection(connection.get(0));
        stationIndex.addConnection(connection.get(1));
    }

    @After
    public void tearDown() throws Exception {
        rout.clear();
    }

    @Test
    @DisplayName("Проверка переходов с 1 линии на другую")
    public void isConnected() {
        Set<Station> setTrue = stationIndex.getConnectedStations(testStation2);
        Set<Station> setFalse = stationIndex.getConnectedStations(testStation6);
        boolean actualTrue = setTrue.contains(testStation4);
        boolean actualFalse = setFalse.contains(testStation2);
        assertFalse(actualFalse);
        assertTrue(actualTrue);
    }

    @Test
    @DisplayName("Проверка пути по двум линиям")
    public void getShortestRouteWithTwoConnectionAndRoutViaConnectedLine() {
        List<Station> actual = routeCalculator.getShortestRoute(testStation1, testStation7);
        List<Station> expected = new ArrayList<>();
        expected.add(testStation1);
        expected.add(testStation2);
        expected.add(testStation4);
        expected.add(testStation5);
        expected.add(testStation6);
        expected.add(testStation7);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверка пути c 1 переходом на другую линию")
    public void getShortestRouteWithOneConnection() {
        List<Station> actual = routeCalculator.getShortestRoute(testStation1, testStation5);
        List<Station> expected = new ArrayList<>();
        expected.add(testStation1);
        expected.add(testStation2);
        expected.add(testStation4);
        expected.add(testStation5);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверка пути по одной линии")
    public void getShortestRouteOnTheLine() {
        List<Station> actual = routeCalculator.getShortestRoute(testStation1, testStation3);
        List<Station> expected = new ArrayList<>();
        expected.add(testStation1);
        expected.add(testStation2);
        expected.add(testStation3);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Калькулятор времени на путь")
    public void calculateDuration() {
        double actual = RouteCalculator.calculateDuration(rout);
        double expected = 11.0;
        assertEquals(expected, actual, DELTA);
    }
}