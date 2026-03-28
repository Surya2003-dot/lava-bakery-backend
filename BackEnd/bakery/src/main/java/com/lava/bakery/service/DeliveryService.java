







//---------------------------------------NOT IN USE


package com.lava.bakery.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService {

    //  Shop Location
    private static final double SHOP_LAT = 11.516458151348942;
    private static final double SHOP_LNG = 79.3318844269849;

    // MAIN METHOD
    public double calculateDeliveryCharge(String address){

        double[] coords = getLatLng(address);

        //  if location fetch fail
        if(coords[0] == -1){
            return -1;
        }

        double userLat = coords[0];
        double userLng = coords[1];

        double distance = haversine(SHOP_LAT, SHOP_LNG, userLat, userLng);

        System.out.println("Distance: " + distance + " KM");

        if(distance <= 5){
            return 30;
        }else if(distance <= 10){
            return 50;
        }else if(distance <= 15){
            return 80;
        }else{
            return -1;
        }
    }

    //  GET LAT/LNG FROM ADDRESS
    private double[] getLatLng(String address){

        try{

            String url = "https://nominatim.openstreetmap.org/search?q="
                    + URLEncoder.encode(address, "UTF-8")
                    + "&format=json&limit=1";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "lava-bakery-app (surya.developer15@gmail.com)");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            List body = response.getBody();

            //  invalid address
            if(body == null || body.isEmpty()){
                System.out.println("Invalid address ❌");
                return new double[]{-1, -1};
            }

            Map data = (Map) body.get(0);

            double lat = Double.parseDouble((String) data.get("lat"));
            double lon = Double.parseDouble((String) data.get("lon"));

            System.out.println("LAT: " + lat + " LNG: " + lon);

            return new double[]{lat, lon};

        }catch(Exception e){
            System.out.println("Location fetch failed ❌");
            return new double[]{-1, -1};
        }
    }

    //  DISTANCE CALCULATION
    private double haversine(double lat1, double lon1, double lat2, double lon2){

        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) *
                        Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }
}