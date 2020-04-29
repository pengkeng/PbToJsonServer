package com.example.ucas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Hashtable;


/**
 * @author pqc
 */
@CrossOrigin
@Controller
@ResponseBody
public class MainController {
    @RequestMapping(value = "transPBToJson", method = RequestMethod.POST)
    String getJson(@RequestParam("PBSchema") String schema, @RequestParam("PBData") String data) {
        TransUtils.getJsonFromPB();
        return "1";
    }
}
