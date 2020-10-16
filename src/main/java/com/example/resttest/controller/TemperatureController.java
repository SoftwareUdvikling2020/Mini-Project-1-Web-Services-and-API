package com.example.resttest.controller;

import com.example.resttest.SOAPService.SOAPClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TemperatureController {

    SOAPClient SOAPCLi = new SOAPClient();

    private JSONObject hateoas(){
        JSONObject hateoas = new JSONObject();
        JSONObject ctof = new JSONObject();
        ctof.put("href","http://localhost:8080/getFahr/{double}");
        ctof.put("rel","Convert celsius to fahrenheit");

        JSONObject ftoc = new JSONObject();
        ftoc.put("href","http://localhost:8080/getCel/{double}");
        ftoc.put("rel","Convert fahrenheit to celsius");


        hateoas.append("link",ctof);
        hateoas.append("link",ftoc);
        return hateoas;
    }
    @GetMapping("/")
    public ResponseEntity getHello(){

        return ResponseEntity.ok().body(hateoas().toString());
    }

    @GetMapping("/convert/{value}/{type}")
    public ResponseEntity convert(@PathVariable(value = "value") double fahr,@PathVariable(value = "type") String type) throws IOException {
        try {
            JSONObject returnObj = new JSONObject();
            JSONObject obj = hateoas();
            returnObj.put("links",obj);
            returnObj.put("tempature", SOAPCLi.getTemperatureSOAP(fahr, type));

            return ResponseEntity.ok().body(returnObj.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/getCel/{fahr}")
    public ResponseEntity getCelsius(@PathVariable(value = "fahr") double fahr) throws IOException {
        try {
            JSONObject returnObj = new JSONObject();
            JSONObject obj = hateoas();
            returnObj.put("links",obj);
            returnObj.put("celsius", SOAPCLi.getTemperatureSOAP(fahr, "fahr"));
            return ResponseEntity.ok().body(returnObj.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getFahr/{cel}")
    public ResponseEntity getFahrenheit(@PathVariable(value = "cel") double cel) throws IOException {

        try {
            JSONObject returnObj = new JSONObject();
            JSONObject obj = hateoas();
            returnObj.put("link",obj);
            returnObj.put("Fahrenheit", SOAPCLi.getTemperatureSOAP(cel, "cel"));
            return ResponseEntity.ok().body(returnObj.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

}
