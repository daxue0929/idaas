package com.daxue.auth.controller;

import com.daxue.auth.enums.ResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daxue0929
 * @date 2022/07/23
 **/
public class BaseController {

    public Map<String,Object> sendBaseNormalMap(Map<String,Object> map){
        if (map==null){
            map= new HashMap<>();
        }
        map.put("return_code", ResultCode.SUCCESS.getCode());
        map.put("return_msg",ResultCode.SUCCESS.getMessage());
        return map;
    }

    public Map<String,Object> sendBaseNormalMap(){
        Map<String,Object> map = map= new HashMap<>();
        map.put("return_code",ResultCode.SUCCESS.getCode());
        map.put("return_msg",ResultCode.SUCCESS.getMessage());
        return map;
    }

    public Map<String,Object> sendBaseNormalMap(ResultCode result){
        Map<String,Object> map = map= new HashMap<>();
        map.put("return_code",result.getCode());
        map.put("return_msg",result.getMessage());
        return map;
    }


    public Map<String,Object> sendBaseNormalMap(Map<String,Object> map,ResultCode result){
        if (map==null){
            map= new HashMap<>();
        }
        map.put("return_code",result.getCode());
        map.put("return_msg",result.getMessage());
        return map;
    }

    public Map<String,Object> sendBaseErrorMap(Map<String,Object> map, ResultCode result){
        if (map==null){
            map= new HashMap<>();
        }
        map.put("return_code",result.getCode());
        map.put("return_msg",result.getMessage());
        return map;
    }

    public Map<String,Object> sendBaseErrorMap(ResultCode result){
        Map<String,Object> map = map= new HashMap<>();
        map.put("return_code",result.getCode());
        map.put("return_msg",result.getMessage());
        return map;
    }


}
