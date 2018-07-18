package com.joeysin.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joeysin.App;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
public class AjaxController {

//    @Autowired
//    private Cache localCache;

    @RequestMapping("")
    public ModelAndView welcomePage(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping("/random_ajax")
    public Map<String, List<String>> getData() {
        Map<String, List<String>> map = Maps.newHashMapWithExpectedSize(2);
        List<String> x = Lists.newArrayList();
        List<String> y = Lists.newArrayList();
        for (Map.Entry entry : App.localCache().entrySet()) {
            x.add(String.valueOf(entry.getKey()));
            y.add(String.valueOf(entry.getValue()));
        }
        map.put("x", x);
        map.put("y", y);
        return map;
    }
}
