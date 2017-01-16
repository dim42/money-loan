package pack.loan.api;

public interface CountryService {
    String GEO_IP_SERVICE = "geoIpService";

    String getCountryCode(String ipAddress);
}
