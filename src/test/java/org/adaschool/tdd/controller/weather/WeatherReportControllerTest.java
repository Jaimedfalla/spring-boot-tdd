package org.adaschool.tdd.controller.weather;

import com.flextrade.jfixture.JFixture;
import org.adaschool.tdd.controller.weather.dto.NearByWeatherReportsQueryDto;
import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.fakedata.FakeWhetherRequest;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.adaschool.tdd.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class WeatherReportControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private WeatherService weatherService;

    @Test
    void create_Test_Returns_Equals_Expected()  {
        String uri = "http://localhost:" + port +"/v1/weather";
        WeatherReportDto request = FakeWhetherRequest.getRequestWeatherDto();
        WeatherReport expected = new WeatherReport(
                request.getGeoLocation(),
                request.getTemperature(),
                request.getHumidity(),
                request.getReporter(),
                request.getCreated()
        );
        HttpEntity<WeatherReportDto> body = new HttpEntity<WeatherReportDto>(request);
        when(weatherService.report(any(WeatherReportDto.class))).thenReturn(expected);
        WeatherReport response = this.restTemplate.postForObject(uri,body,WeatherReport.class);

        assertEquals(expected,response);
    }

    @Test
    void findById_Test_Returns_Equals_Expected(){
        String uri = "http://localhost:" + port +"/v1/weather/8fgsffsghghd5g5461g6fg5";
        WeatherReport expected = FakeWhetherRequest.getWeatherReport();
        when(weatherService.findById(anyString())).thenReturn(expected);
        WeatherReport response = this.restTemplate.getForObject(uri,WeatherReport.class);

        assertEquals(expected,response);
    }

    @Test
    void findNearByReports_Test_Returns_Data(){
        String uri = String.format("http://localhost:%s/v1/weather/nearby",port);
        JFixture fixture = new JFixture();
        NearByWeatherReportsQueryDto request = fixture.create(NearByWeatherReportsQueryDto.class);
        HttpEntity<NearByWeatherReportsQueryDto> body = new HttpEntity<NearByWeatherReportsQueryDto>(request);
        when(weatherService.findNearLocation(any(GeoLocation.class),anyFloat()))
                .thenReturn((List<WeatherReport>)fixture.collections().createCollection(WeatherReport.class,3));

        List<WeatherReport> response = this.restTemplate.postForObject(uri,body,new ArrayList<WeatherReport>().getClass());

        assertNotNull(response);
        assertTrue(response.size()>0);
        assertEquals(response.size(),3);
    }

    @Test
    void findByReporterId_Test_Returns_Data(){
        String uri = String.format("http://localhost:%s/v1/weather/reporter/kakdjfakdjflkad",port);
        JFixture fixture = new JFixture();
        when(weatherService.findWeatherReportsByName(anyString()))
                .thenReturn((List<WeatherReport>) fixture.collections().createCollection(WeatherReport.class,3));

        List<WeatherReport> response = this.restTemplate.getForObject(uri,new ArrayList<WeatherReport>().getClass());

        assertNotNull(response);
        assertTrue(response.size()>0);
        assertEquals(response.size(),3);
    }
}