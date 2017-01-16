package pack.loan.service;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pack.loan.api.CountryService;

import static java.lang.String.format;
import static pack.loan.api.CountryService.GEO_IP_SERVICE;

/*
  Country service classes have been generated with: wsimport http://www.webservicex.net/geoipservice.asmx?WSDL -keep
 */
@Component(GEO_IP_SERVICE)
public class CountryServiceGeoIp implements CountryService {
    private static final Logger log = LoggerFactory.getLogger(CountryServiceGeoIp.class);
    private static final String DEFAULT_COUNTRY_CODE = "lv";

    private final GeoIPService geoIPService = new GeoIPService();

    @Override
    public String getCountryCode(String ipAddress) {
        try {
            GeoIP geoIP = geoIPService.getGeoIPServiceSoap().getGeoIP(ipAddress);
            String countryCode = geoIP.getCountryCode();
            if (countryCode == null || countryCode.trim().isEmpty()) {
                return DEFAULT_COUNTRY_CODE;
            }
            return countryCode;
        } catch (Exception e) {
            log.error(format("Getting country error for ip: %s", ipAddress), e);
            return DEFAULT_COUNTRY_CODE;
        }
    }
}
