package com.aaroom.restcontroller;

import com.aaroom.model.ClothErrorView;
import com.aaroom.service.ClothErrorService;
import com.aaroom.utils.ExcelUtils;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 布草报损
 * Created by liyp on 2018/12/25 0025.
 */
@RestController
public class ClothErrorController implements Serializable{
    private static final long serialVersionUID = -1877506533728174981L;
    @Autowired
    private ClothErrorService clothErrorService;
    //获取报损信息
    @GetMapping("/console/api/getClothErrorList")
    public Map getErrorClothListInfo( @RequestParam(required = false) Integer hotelId,
                                      @RequestParam(required = false) String beginTime,
                                      @RequestParam(required = false) String endTime,
                                      @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                      @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo< 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        Integer index = (pageNo - 1) * pageSize;
        //获取报损布草信息(分页)
        Map<String,Object> map = new HashMap<>();
        List<ClothErrorView> errorClothListInfo = null;
        //查询总条数
        Long count = 0l;
        //统计总金额
        List<ClothErrorView> totalList = null;
        Page<ClothErrorView> page = PageUtils.getPageList(totalList, pageNo, pageSize);
        double total = 0;
        double pagetotal = 0;
        try {
            errorClothListInfo = clothErrorService.getErrorClothListInfo(hotelId, beginTime, endTime,pageSize,index);
            totalList = clothErrorService.getClothErrorListSum(hotelId,beginTime,endTime);
            page = PageUtils.getPageList(totalList, pageNo, pageSize);
            count = clothErrorService.getErrorClothListCount(hotelId,beginTime,endTime);
            for (ClothErrorView cev : errorClothListInfo) {
                pagetotal += cev.getSuggest_pay();
            }
            for (ClothErrorView cev : totalList) {
                total += cev.getSuggest_pay();
            }
            map.put("data", page);
            map.put("count",count);
            map.put("total", total);
            map.put("pagetotal",pagetotal);
        } catch (Exception e) {
            map.put("data", page);
            map.put("count",count);
            map.put("total", total);
            map.put("pagetotal",pagetotal);
        }
        return map;
    }
    //导出报损信息
    @GetMapping("/console/api/exportClothErrorList")
    public void exportErrorClothListInfo(HttpServletResponse response,
                                         @RequestParam(required = false) Integer hotelId,
                                         @RequestParam(required = false) String beginTime,
                                         @RequestParam(required = false) String endTime){
        List<ClothErrorView> list = null;
        try {
            list = clothErrorService.getErrorClothListInfo(hotelId, beginTime, endTime, null, null);
        } catch (Exception e) {

        }
        Map errorClothListInfo = getErrorClothListInfo(hotelId, beginTime, endTime, 1, 1);
        ClothErrorView clothErrorView = new ClothErrorView("总计", (Double) errorClothListInfo.get("total"));
        list.add(clothErrorView);
        String[] listTitle = {"布草名称","尺寸","布草材质","布草ID","报损类型","流转次数","责任人","报损时间","需赔付金额"};
        String sheetName = null;
        if(beginTime != null  && endTime != null){
            sheetName = "布草报损对账" + beginTime.replace("-", "") + "-" + endTime.replace("-", "") + ".xls";
        }else {
             sheetName = "布草报损对账.xls";
        }
        Object content[][] = new Object[listTitle.length][listTitle.length];
        for (int i = 0; i < list.size(); i++) {
            ClothErrorView obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                content[i][0] = obj.getCloth_type();
                content[i][1] = obj.getCloth_size();
                content[i][2] = obj.getCloth_material();
                if (null !=obj.getCloth_id() ){
                    content[i][3] = obj.getCloth_id()+"";
                }
                if (null !=obj.getError_type() ){
                    content[i][4] = obj.getError_type()+"";
                }
                if (null !=obj.getRecycle_num() ){
                    content[i][5] = obj.getRecycle_num()+"";
                }
                content[i][6] = obj.getHotel_name();
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date error_time = obj.getError_time();
                if (null !=error_time ){
                    content[i][7] = sim.format(error_time);
                }
                content[i][8] = obj.getSuggest_pay()+"";
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb, sheetName, response);
    }
}
