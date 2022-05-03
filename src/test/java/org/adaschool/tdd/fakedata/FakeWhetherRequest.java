package org.adaschool.tdd.fakedata;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;

import java.util.Date;

public class FakeWhetherRequest {
    private static final String reporter ="tester";
    private static final double lat = 4.7110;
    private static final double lng = 74.0721;
    private static final double temperature=35f;
    private static final double humidity=22f;

    public static WeatherReportDto getRequestWeatherDto(){
        return new WeatherReportDto( new GeoLocation( lat, lng ),
                temperature, humidity, reporter, new Date() );
    }

    public static WeatherReport getWeatherReport(){
        return new WeatherReport( new GeoLocation( lat, lng ),
                temperature, humidity, reporter, new Date() );
    }
}
