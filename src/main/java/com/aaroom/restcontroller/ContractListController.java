package com.aaroom.restcontroller;

import com.aaroom.service.ContractListService;
import com.aaroom.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 温建成 on 2019/2/13.
 */
@RestController
public class ContractListController {

    @Autowired
    private ContractListService contractListService;

    //合同列表页面的查询
    @GetMapping(value = "/console/api/exportGetContractListList")
    public Object exportAccountList(HttpServletResponse response,
                                                @RequestParam(value = "contractType",required = false ) Integer contractType, //合同类型
                                                @RequestParam(value = "contractRecipientId",required = false ) Integer contractRecipientId, //领用人
                                                @RequestParam(value = "contractSignatoryId",required = false ) Integer contractSignatoryId,
                                                @RequestParam(value = "contractStatus",required = false ) Integer contractStatus,
                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        Map<String, Object> map = contractListService.exportGetContractListList(  contractType, contractRecipientId, contractSignatoryId, contractStatus, pageSize, pageNo);
        Object data = map.get("data");
        return data;
    }
    //获取领用人列表
    @GetMapping(value = "/console/api/exportemployeeList")
    public List exportemployeeList(){
        Map<String, Object> map = contractListService.exportemployeeList(19);
        List list = (List) map.get("contractList");
        return list;
    }
    //合同列表页面的导出
    @GetMapping(value = "/console/api/exportGetContractListExtel")
    public void exportmissionbytimeandhotelidlist(HttpServletResponse response,
                                                  @RequestParam(value = "contractType",required = false ) Integer contractType, //合同类型
                                                  @RequestParam(value = "contractRecipientId",required = false ) Integer contractRecipientId, //领用人
                                                  @RequestParam(value = "contractSignatoryId",required = false ) Integer contractSignatoryId,
                                                  @RequestParam(value = "contractStatus",required = false ) Integer contractStatus) throws Exception {
        //获取查询结果放到集合中
        Map<String, Object> map = contractListService.exportGetContractListList(  contractType, contractRecipientId, contractSignatoryId, contractStatus, 1, 1);
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        String[] listTitle = {"合同编号","合同类型","对应服务协议","领用人","领用时间","签约方","签约方ID","合同状态","备注"};
        String[] listKey = {"contractNumber","contractTypeName","contractNumberTouch","contractRecipientName","contractReceiveTime","contractSignatoryName","contractSignatoryId","contractStatusName","comments"};
        String sheetName = "合同列表"+time+".xls";
        List<Map> list = (List) map.get("list");
        Object content[][] = new Object[list.size()][listTitle.length];
        for (int i = 0; i < list.size(); i++) {
            Map obj = list.get(i);
            for (int j = 0; j < listTitle.length; j++) {
                if(obj.get(listKey[j])!=null){
                    content[i][j] = obj.get(listKey[j])+"";
                }else{
                    content[i][j] = "";
                }
            }
        }
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, listTitle, content, null);
        ExcelUtils.exportExcel(wb,sheetName,response);
    }
    //添加服务协议
    @GetMapping(value = "/console/api/insertContract")
    public Map<String,String> insertContract(HttpServletResponse response,
                                    @RequestParam(value = "contractNumber",required = false ) String contractNumber, //合同编号
                                    @RequestParam(value = "number", defaultValue = "1" ) Integer number) throws Exception {
        Map<String,String> map = new HashMap();
        try {
            Integer integer = contractListService.insertContract(contractNumber,number,null,1);
            if(integer==1){
                map.put("status","error");
                map.put("msg","合同编号有重复");
            }else{
                map.put("status","succeed");
                map.put("msg","添加成功");
            }
        }catch (Exception e){
            map.put("status","error");
            map.put("msg","合同编号有重复");
        }
        return map;
    }
    //添加补充协议
    @GetMapping(value = "/console/api/insertContractTouch")
    public Map<String,String> insertContractTouch(HttpServletResponse response,
                                             @RequestParam(value = "contractNumber",required = false ) String contractNumber, //合同类型
                                             @RequestParam(value = "contractNumberTouch",required = false ) String contractNumberTouch) throws Exception {
        Map<String,String> map = new HashMap();
        try {
            Integer integer = contractListService.insertContract(contractNumber,1,contractNumberTouch,2);
            if(integer==1){
                map.put("status","error");
                map.put("msg","合同编号有重复");
            }else{
                map.put("status","succeed");
                map.put("msg","添加成功");
            }
        }catch (Exception e){
            map.put("status","error");
            map.put("msg","合同编号有重复");
        }
        return map;
    }
    //合同列表的领用
    @GetMapping(value = "/console/api/contractReceive")
    public Map<String,String> contractReceive(HttpServletResponse response,
                                              @RequestParam(value = "id",required = false ) Integer id , //领用人id
                                              @RequestParam(value = "contractRecipientId",required = false ) Integer contractRecipientId //领用人id
                                                ) throws Exception {
        Map<String,String> map = new HashMap();
        try {
            Integer integer = contractListService.contractReceive(id,contractRecipientId);
            if(integer!=1){
                map.put("status","error");
                map.put("msg","分发失败");
            }else{
                map.put("status","succeed");
                map.put("msg","分发成功");
            }
        }catch (Exception e){
            map.put("status","error");
            map.put("msg","分发失败");
        }
        return map;
    }
    //合同列表页面的丢失和作废
    @GetMapping(value = "/console/api/contractDrop")
    public Map<String,String> contractDrop(HttpServletResponse response,
                                              @RequestParam(value = "id",required = false ) Integer id , //领用人id
                                              @RequestParam(value = "contractStatus",required = false ) Integer contractStatus, //状态
                                              @RequestParam(value = "comments",required = false ) String comments //备注
    ) throws Exception {
        Map<String,String> map = new HashMap();
        try {
            Integer integer = contractListService.contractDrop(id,contractStatus,comments);
            if(integer!=1){
                map.put("status","error");
                map.put("msg","变更失败");
            }else{
                map.put("status","succeed");
                map.put("msg","变更成功");
            }
        }catch (Exception e){
            map.put("status","error");
            map.put("msg","变更失败");
        }
        return map;
    }
    //合同列表页面的归档
    @GetMapping(value = "/console/api/contractPigeonhole")
    public Map<String,String> contractPigeonhole(HttpServletResponse response,
                                              @RequestParam(value = "id",required = false ) Integer id  //领用人id
    ) throws Exception {
        Map<String, String> map = new HashMap();
        try {
            Integer integer = contractListService.contractPigeonhole(id,6);
            if (integer != 1) {
                map.put("status", "error");
                map.put("msg", "归档失败");
            } else {
                map.put("status", "succeed");
                map.put("msg", "归档成功");
            }
        } catch (Exception e) {
            map.put("status", "error");
            map.put("msg", "归档失败");
        }
        return map;
    }
    //拓展-我的合同
    @GetMapping(value = "/console/api/exportGetContractListByRecipient")
    public Object exportGetContractListByRecipient(HttpServletResponse response,
                                                  HttpServletRequest request,
                                                 @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
                                    ) throws Exception {
        Integer contractRecipientId = (Integer) request.getSession().getAttribute("employee_id");
        Map<String, Object> map = contractListService.exportGetContractListList(  0, contractRecipientId, 0, 0, pageSize, pageNo);
        Object data =  map.get("data");
        return data;
    }
    //我的合同-接收,拒收，归档
    @GetMapping(value = "/console/api/contractByReceive")
    public Map<String,String> contractByReceive(HttpServletResponse response,
                                                 @RequestParam(value = "id",required = false ) Integer id,  //领用人id
                                                 @RequestParam(value = "contractStatus",required = false ) Integer contractStatus
    ) throws Exception {
        String[] error = {"","","","接收失败","","提交失败","","","","拒收失败"};
        String[] succeed = {"","","","接收成功","","提交成功","","","","拒收成功"};
        Map<String, String> map = new HashMap();
        try {
            Integer integer = contractListService.contractPigeonhole(id,contractStatus);
            if (integer != 1) {
                map.put("status", "error");
                map.put("msg", error[contractStatus]);
            } else {
                map.put("status", "succeed");
                map.put("msg", succeed[contractStatus]);
            }
        } catch (Exception e) {
            map.put("status", "error");
            map.put("msg", error[contractStatus]);
        }
        return map;
    }
}
