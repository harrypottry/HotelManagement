package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import com.aaroom.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.shangmei.util.DateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aaroom.utils.Constants.ClothErrorType.Broken;
import static com.aaroom.utils.Constants.ClothErrorType.Lost;
import static com.aaroom.utils.Constants.ClothErrorType.QRBroken;
import static com.aaroom.utils.Constants.ClothKind.Material;
import static com.aaroom.utils.Constants.ClothKind.Size;
import static com.aaroom.utils.Constants.ClothKind.Type;

/**
 * @className StoreController
 * @Description 这个类主要是干【库存管理】
 * @Author 张赢
 * @Date 2018/12/3 0003下午 15:05
 * @Version 1.0
 **/
@RestController
public class StoreController {

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private ClothService clothService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClothTypeService clothTypeService;

    @Autowired
    private ClothErrorService clothErrorService;

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CentralWarehouseEmployeeService centralWarehouseEmployeeService;
    //得到所有布草的瞬时状态对象集合
    @RequestMapping(value = "/wx/console/api/getAllStoreCloth", method = RequestMethod.GET)
    public Object getAllStoreCloth(HttpServletRequest request,
                                   @RequestParam(value = "possessor_type", required = false) Integer possessor_type,
                                   @RequestParam(value = "comment", required = false) Integer comment,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {


        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo < 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        Integer index = (pageNo - 1) * pageSize;

        if (possessor_type == 4) {
            possessor_type = null;
        }

        if (comment != null && comment == 1) {
            comment = null;
        }
        //根据hotel_id得到对应酒店下的所有log对象  comment 1是全部 2是有备注
        List<ClothLog> clothLogList = clothLogService.getClothLogListBypossessor_type_comment(hotel_id, possessor_type, comment, index, pageSize);

        //一共有多少条 公共变量
        Integer count = clothLogService.downgetClothLogListcount(hotel_id, possessor_type, comment);

        //new一个当前时间
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatCurrentTime = sdf.format(currentTime);


        //新建list集合存储viewweb对象用
        List clothViewWebList = new ArrayList<>();

        //循环遍历所得clothloglist 把新值存进去
        for (ClothLog clothLog :
                clothLogList) {
            //先键一个view对象给前端展示使用
            ClothViewWeb clothViewWeb = new ClothViewWeb();
            clothViewWeb.setCloth_id(clothLog.getCloth_id());
            //relclothtye表+clothtype表 两表联合查询得出 根据clothid查rel_cloth_type
            List<Integer> descByClothId = relClothTypeService.getDescByclothid(clothLog.getCloth_id());
            clothViewWeb.setCloth_type_brand_size_material(descByClothId);

            //根据possessor_type与possessor_id双条件查询具体的中文显示
            Integer clothLogPossessor_type = clothLog.getPossessor_type();
            Integer clothLogPossessor_id = clothLog.getPossessor_id();
            switch (clothLogPossessor_type) {
                case 0:
                    //是房间直接存房间号
                    Room room = roomService.getRoomById(clothLogPossessor_id);
                    clothViewWeb.setPosition(room.getRoom_name());
                    break;
                case 1:
                    //根据员工id得到对应的中文名字
                    String employeename = employeeService.getEmployeenamebyid(clothLogPossessor_id);
                    clothViewWeb.setPosition(employeename);
                    break;
                case 2:
                    //是库房直接存库房
                    clothViewWeb.setPosition("库房");
                    break;
                case 3:
                    //是洗衣厂就存洗衣厂名字
                    String WashingFactoryName = employeeService.getEmployeenamebyid(clothLogPossessor_id);
                    clothViewWeb.setPosition(WashingFactoryName);
                    break;
            }
            //从新对象中取status数字后进行判断 0为干净 1为脏
            Integer status = clothLog.getStatus();
            if (status == 0) {
                clothViewWeb.setStatus("干净");
            } else if (status == 1) {
                clothViewWeb.setStatus("脏");
            }
            //存流转次数 是从cloth表查出 查一个cloth对象 根据clothid查cloth表
            Cloth clothByid = clothService.getClothByid(clothLog.getCloth_id());
            if (clothByid != null) {
                clothViewWeb.setRecycle_num(clothByid.getRecycle_num());
                //备注 根据cloth表查出
                clothViewWeb.setComment(clothByid.getComment());
            }
            //【总】存入list当中
            clothViewWebList.add(clothViewWeb);
        }
        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
        ret.put("CurrentTime", formatCurrentTime);
        ret.put("data", clothViewWebList);
        //判断返回集合
        if (ret.size() > 0) {
            return ret;
        } else {
            return RestError.E_CLOTHPOSSESSORTYPE_FAIL;
        }
    }


    //导出查询数据到excel表格
    @RequestMapping(value = "/wx/console/api/downloadlogexcel", method = RequestMethod.POST)
    public Object downloadlogexcel(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "possessor_type", defaultValue = "") Integer possessor_type,
                                   @RequestParam(value = "comment", defaultValue = "") Integer comment) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");
//        String state = new String(re.getBytes("ISO-8859-1"),"UTF-8");
//        String address = new String(ad.getBytes("ISO-8859-1"),"UTF-8");
//        String netname = new String(n.getBytes("ISO-8859-1"),"UTF-8");

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();
        if (possessor_type == 4) {
            possessor_type = null;
        }
        //根据hotel_id得到对应酒店下的所有log对象
        List<ClothLog> clothLogList = clothLogService.downgetClothLogList(hotel_id, possessor_type, comment);


        //新建list集合存储viewweb对象用
        //前台要7个中文显示 1：布草id 2：布草名称 3：规格 4：所在位置 5：洁净程度 6：流转次数 7：备注
        List<ClothViewWeb> clothViewWebList = new ArrayList<>();

        //循环遍历所得clothloglist 把新值存进去
        for (ClothLog clothLog :
                clothLogList) {
            //先键一个view对象给前端展示使用
            ClothViewWeb clothViewWeb = new ClothViewWeb();
            clothViewWeb.setCloth_id(clothLog.getCloth_id());
            //relclothtye表+clothtype表 两表联合查询得出 根据clothid查cloth_type_brand
            //存类型和品牌
            List cloth_type_brand = relClothTypeService.getclothtype_brand(clothLog.getCloth_id());
            clothViewWeb.setCloth_type_brand(cloth_type_brand);
            //存规格
            String cloth_size = relClothTypeService.getcloth_size(clothLog.getCloth_id());
            clothViewWeb.setCloth_size(cloth_size);

            //根据possessor_type与possessor_id双条件查询具体的中文显示
            Integer clothLogPossessor_type = clothLog.getPossessor_type();
            Integer clothLogPossessor_id = clothLog.getPossessor_id();
            switch (clothLogPossessor_type) {
                case 0:
                    //是房间直接存房间号
                    //根据id得name
                    Room room = roomService.getRoomById(clothLogPossessor_id);
                    clothViewWeb.setPosition(room.getRoom_name());
                    break;
                case 1:
                    //根据员工id得到对应的中文名字
                    String employeename = employeeService.getEmployeenamebyid(clothLogPossessor_id);
                    clothViewWeb.setPosition(employeename);
                    break;
                case 2:
                    //是库房直接存库房
                    clothViewWeb.setPosition("库房");
                    break;
                case 3:
                    //是洗衣厂就存洗衣厂名字
                    String WashingFactoryName = employeeService.getEmployeenamebyid(clothLogPossessor_id);
                    clothViewWeb.setPosition(WashingFactoryName);
                    break;
            }
            //从新对象中取status数字后进行判断 0为干净 1为脏
            Integer status = clothLog.getStatus();
            if (status == 0) {
                clothViewWeb.setStatus("干净");
            } else if (status == 1) {
                clothViewWeb.setStatus("脏");
            }
            //存流转次数 是从cloth表查出 查一个cloth对象 根据clothid查cloth表
            Cloth clothByid = clothService.getClothByid(clothLog.getCloth_id());
            if (clothByid != null) {
                clothViewWeb.setRecycle_num(clothByid.getRecycle_num());
                //备注 根据cloth表查出
                clothViewWeb.setComment(clothByid.getComment());
            }
            //【总】存入list当中
            clothViewWebList.add(clothViewWeb);
        }


        List<String> title = new ArrayList<String>();
        title.add("布草id");
        title.add("布草名称");
        title.add("规格");
        title.add("所在位置");
        title.add("洁净程度");
        title.add("流转次数");
        title.add("备注");
        createExcel(request, response, clothViewWebList, "库存列表导出Excel", title);
        //判断返回结果
        if (clothViewWebList.size() > 0) {
            return RestError.E_OK;
        } else {
            return RestError.E_WORKING_FAIL;
        }
    }

    public void createExcel(HttpServletRequest request, HttpServletResponse response,
                            List<ClothViewWeb> clothViewWebList, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < clothViewWebList.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (clothViewWebList.get(j).getCloth_id() != null) {// 第一列布草id
                                cell.setCellValue(clothViewWebList.get(j).getCloth_id());
                            }
                            break;
                        case 1:
                            if (clothViewWebList.get(j).getCloth_type_brand() != null) {//第二列布草类型+品牌
                                List<String> cloth_type_brandlist = clothViewWebList.get(j).getCloth_type_brand();
                                String str = "";
                                for (int i = 0; i < cloth_type_brandlist.size(); i++) {
                                    str += cloth_type_brandlist.get(i) + ",";
                                }
                                cell.setCellValue(str);
                            }
                            break;
                        case 2:
                            if (clothViewWebList.get(j).getCloth_size() != null) {//第三列规格
                                cell.setCellValue(clothViewWebList.get(j).getCloth_size());
                            }
                            break;
                        case 3:
                            if (clothViewWebList.get(j).getPosition() != null) {//第四列所在位置
                                cell.setCellValue(clothViewWebList.get(j).getPosition());
                            }
                            break;
                        case 4:
                            if (clothViewWebList.get(j).getStatus() != null) {//第五列洁净程度
                                cell.setCellValue(clothViewWebList.get(j).getStatus());
                            }
                            break;
                        case 5:
                            if (clothViewWebList.get(j).getRecycle_num() != null) {//第六列流转次数
                                cell.setCellValue(clothViewWebList.get(j).getRecycle_num());
                            }
                            break;
                        case 6:
                            if (clothViewWebList.get(j).getComment() != null) {//第七列备注
                                cell.setCellValue(clothViewWebList.get(j).getComment());
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //app 库管端  布草发放 H-1-3【选择阿姨】
    //获得本酒店标记为上班状态的保洁员inworking 0下班 1上班
    @RequestMapping(value = "/app/console/api/GetEmployeeNameInWorking", method = RequestMethod.POST)
    public Object GetEmployeeNameInWorking(HttpServletRequest request) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        int hotel_id = hotelEmployee.getHotel_id();
        List employeelist = hotelEmployeeService.getemloyeeinworking(hotel_id);
        if (employeelist.size() > 0) {
            return new RestError(employeelist);
        } else {
            return RestError.E_DATA_FAIL;
        }
    }

    //布草发放 H-1-3【获取cloth详情】 根据传入clothid得到对应详情  扫码rfid
    @RequestMapping(value = "/app/console/api/GetClothDetails", method = RequestMethod.POST)
    public Object GetClothDetails(HttpServletRequest request,
                                  @RequestParam String ClothIdList) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        int hotel_id = hotelEmployee.getHotel_id();

        List<String> ClothIdListjson = JSON.parseArray(ClothIdList, String.class);
        List clothViewList = new ArrayList();
        //判空 ClothIdList 如果这里面有库中没有的clothid 就直接返回worong提示

        for (String clothId :
                ClothIdListjson) {
            //先根据这个id查到当前布草的干净状态
            Cloth clothByid = clothService.getClothByid(Integer.parseInt(clothId));
            if (clothByid != null) {
                Integer status = clothByid.getStatus();
                ClothView clothView = new ClothView();
                String clothTypeIds = clothLogService.getClothIdsByRedis(Integer.parseInt(clothId));
                String[] clothTypeIdsArray = clothTypeIds.split(",");
                clothView.setClothTypeIds(clothTypeIds);
                clothView.setId(Integer.parseInt(clothId));
                List<ClothType> clothType = clothTypeService.getClothTypeDescByIds(clothTypeIdsArray);
                clothView.setClothTypes_name(clothType);
                clothView.setHotel_id(hotel_id);
                clothView.setStatus(status);
                clothViewList.add(clothView);
            } else {
                return RestError.E_CLOTHIDMISS_FAIL;
            }
        }

        if (clothViewList.size() > 0) {
            return new RestError(clothViewList);
        } else {
            return RestError.E_DATA_FAIL;
        }
    }

    //新【获取cloth详情】rfid与二维码id 都有可能传
    @RequestMapping(value = "/app/console/api/GetClothDetailsNew", method = RequestMethod.POST)
    public Object GetClothDetailsNew(HttpServletRequest request,
                                     @RequestParam(value = "QRCodeList", required = false, defaultValue = "") List<String> QRCodeListUrl,
                                     @RequestParam(value = "RfidList", required = false, defaultValue = "") List<String> RfidList) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        int hotel_id = hotelEmployee.getHotel_id();

        List<Integer> QRCodeList=new ArrayList<>();
        for (String  QRCode:
             QRCodeListUrl) {
            String[] split = QRCode.split("/");
            Integer cloth_id = Integer.parseInt(split[split.length - 1]);
            QRCodeList.add(cloth_id);
        }

        List<Integer> ClothIdList = null;
        //1：如果二维码id与rfid都有值，暂时解决办法就是按二维码id执行
        if (QRCodeList != null && QRCodeList.size() > 0) {
            ClothIdList = QRCodeList;
            // 2：如果有Rfid 没有二维码id走这个 直接赋值
        } else if (RfidList != null && !RfidList.equals("")) {
            ClothIdList = clothService.getClothidListByRfidList(RfidList).get("ClothIdList");
            // 3：如果什么都没传就直接提示
        } else {
            return RestError.E_DATA_INVALID;//您并没有传入任何数据
        }

        List clothViewList = new ArrayList();
        //判空 ClothIdList 如果这里面有库中没有的clothid 就直接返回worong提示

        for (Integer clothId :
                ClothIdList) {
            //先根据这个id查到当前布草的干净状态
            Cloth clothByid = clothService.getClothByid(clothId);
            if (clothByid != null) {
                Integer status = clothByid.getStatus();
                String rfID = clothByid.getRfID();
                ClothView clothView = new ClothView();
                String clothTypeIds = clothLogService.getClothIdsByRedis(clothId);
                String[] clothTypeIdsArray = clothTypeIds.split(",");
                clothView.setClothTypeIds(clothTypeIds);
                clothView.setId(clothId);
                clothView.setRfID(rfID);
                List<ClothType> clothType = clothTypeService.getClothTypeDescByIds(clothTypeIdsArray);
                clothView.setClothTypes_name(clothType);
                clothView.setHotel_id(hotel_id);
                clothView.setStatus(status);
                clothViewList.add(clothView);
            } else {
                return RestError.E_CLOTHIDMISS_FAIL;
            }
        }
        List RfidMissList = clothService.getClothidListByRfidList(RfidList).get("RfidMissList");
        Map RfidMissListMap = new HashMap();
        RfidMissListMap.put("RfidMissList", RfidMissList);

        RfidMissListMap.put("clothViewList", clothViewList);


        if (RfidMissListMap.size() > 0) {
            return new RestError(RfidMissListMap);
        } else {
            return RestError.E_DATA_FAIL;
        }
    }

    //布草发放 H-1-3【确认发放】确认发放  扫描领取布草 增加双事务日志【张赢】酒店库房出 保洁员入
    @RequestMapping(value = "/app/console/api/scanClothCent", method = RequestMethod.POST)
    public Object scanClothCent(HttpServletRequest request,
                                @RequestParam(value = "ClothIdList", required = false, defaultValue = "") List<Integer> ClothIdList,
                                @RequestParam(value = "employee_id", required = false) Integer employee_id) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();


        //调事务service层开启事务同时保洁员增加log和酒店（仓库）减少log 都带酒店id
//        Map<String, List> clothidListAndmissRfidListMap = clothService.getClothidListByRfidList(RfidList);
//        List<Integer> ClothidList = clothidListAndmissRfidListMap.get("ClothIdList");
//        List<Integer> RfidMissList = clothidListAndmissRfidListMap.get("RfidMissList");

        for (Integer Clothid :
                ClothIdList) {
            Cloth cloth = clothService.getClothByid(Clothid);
            clothLogService.InsertDoubleClothLog(hotel_id, Clothid, null, 2, hotel_id, 1, employee_id, cloth.getStatus());
        }

        return new RestError("一共扫描" + ClothIdList.size() + "个布草，已经成功发放给id为：" + employee_id + "号的保洁员");
    }

    //库管端H-1 总页面 三数字 当前时间本酒店布草总数 今日送洗数 洗归数
    @RequestMapping(value = "/app/console/api/ClothTotal", method = RequestMethod.POST)
    public Object ClothTotal(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idCurrent = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idCurrent);
        //根据hotelEmployee关系对象得到hotel_id
        int hotel_id = hotelEmployee.getHotel_id();

        //获取当前时间
        Date CurrentTime = new Date();
        //获取当天0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date ZeroTime = calendar.getTime();

        //1,得到当前酒店下 当前时间所有布草 （不包含去了洗衣厂的）
        Integer countByhotelId = clothLogService.getCountByhotelId(hotel_id);
        //2,得到当前酒店下 今日(0点)到当前时间已经送去洗衣厂的布草总数
        Integer countByWashing = clothLogService.getCountByhotelIdAndWashFactory(hotel_id, 3, 1, ZeroTime, CurrentTime);
        //2,得到当前酒店下 今日(0点)到当前时间已经洗完送回来的布草总数
        Integer countByWashOver = clothLogService.getCountByhotelIdAndWashFactory(hotel_id, 3, 0, ZeroTime, CurrentTime);

        //新建view对象存进去返回
        ClothCountView clothCountView = new ClothCountView();
        clothCountView.setTotal_count(countByhotelId);
        clothCountView.setWashing_count(countByWashing);
        clothCountView.setWashover_count(countByWashOver);
        if (clothCountView != null) {
            return new RestError(clothCountView);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }

    //库管端  H-1-2【布草确认回收】确认回收 增加双事务日志【张赢】 员工出 库房入
    @RequestMapping(value = "/app/console/api/ClothLogStoreTakeBack", method = RequestMethod.POST)
    public Object ClothLogStoreTakeBack(HttpServletRequest request,
                                        @RequestParam(value = "ClothIdList", required = false, defaultValue = "") List<Integer> ClothIdList) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();


        //调事务service层开启事务
        for (Integer ClothId :
                ClothIdList) {
            Cloth cloth = clothService.getClothByid(ClothId);
            List<ClothLog> clothLoglist = clothLogService.getClothLogByClothIdAndHotelId(hotel_id, ClothId);
            for (ClothLog clothLog :
                    clothLoglist) {
//                if (clothLog.getPossessor_type() == 1) {
                    clothLogService.InsertDoubleClothLog(hotel_id, ClothId, null, 1, clothLog.getPossessor_id(), 2, hotel_id, cloth.getStatus());
//                } else {
//                    return RestError.E_RETURN_FAIL;
//                }
            }
        }
        //todo不管在谁手里都可以入库操作
        return new RestError("已成功从保洁入本酒店库" + ClothIdList.size() + "个布草");
    }

    //库管端 H-4 报损列表【张赢】
    @RequestMapping(value = "/app/console/api/GetClothErrorList", method = RequestMethod.POST)
    public Object GetClothErrorList(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

        //获取当前时间
        Date CurrentTime = new Date();
        //获取当月1日0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date MonthFirstTime = calendar.getTime();

        //查找cloth_error表找到本月的保存列表(自然月)
        List<ClothError> ClothErrorList = clothErrorService.getClothErrorByDuringTime(hotel_id, MonthFirstTime, CurrentTime);

        List clothErrorViewList = new ArrayList();
        //遍历ClothErrorList取出
        for (ClothError clothError :
                ClothErrorList) {

            //新建ClothError对象存储查询出的数据准备返给前端。
            ClothErrorView clothErrorView = new ClothErrorView();

            clothErrorView.setStatus(clothError.getStatus());//处理情况
            clothErrorView.setComment(clothError.getComment());//备注
            clothErrorView.setType(clothError.getType());//报损类型 枚举 损坏 丢失 二维码损坏
            clothErrorView.setCreate_time(clothError.getCreate_time());//报损日期
            if (clothError.getCloth_id() != null) {
                clothErrorView.setCloth_id(clothError.getCloth_id());//id
                String clothTypeIds = clothLogService.getClothIdsByRedis(clothError.getCloth_id());//根据clothid查clothTypeIds
                String[] clothTypeIdsArray = clothTypeIds.split(",");//根据,拆分 clothType类型数组
                clothErrorView.setClothTypeIds(clothTypeIds);//存ids
                List<ClothType> clothType = clothTypeService.getClothTypeDescByIds(clothTypeIdsArray);//根据拆分完的clothTypeIds得到中文list
                clothErrorView.setClothTypes_name(clothType);
            }

            clothErrorViewList.add(clothErrorView);
        }
        if (clothErrorViewList.size() > 0) {
            return new RestError(clothErrorViewList);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }

    //库管端   H-4-1【新报损】A 确认报损 +添加照片【张赢】
    @RequestMapping(value = "/app/console/api/MarkBroken", method = RequestMethod.POST)
    public Object MarkBroken(HttpServletRequest request,
                             @RequestParam(value = "cloth_id", required = false) Integer cloth_id,
                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                             @RequestParam(value = "type") String type,
                             @RequestParam(value = "comment", required = false) String comment,
                             @RequestParam(value = "hotel", required = false) Integer hotel,
                             @RequestParam(value = "washFactory_id", required = false) Integer washFactory_id) throws Exception {

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();

        if (type != null) {
            Integer InsertSum = 0;
            List<String> pathArray = new ArrayList();
            if (files != null && files.length > 0) {
                for (MultipartFile multipartFile : files) {
                    String[] fileName = multipartFile.getOriginalFilename().split("\\.");
                    File temp = File.createTempFile(fileName[0], fileName[1]);
                    multipartFile.transferTo(temp);
                    String destFileName = UUID.randomUUID().toString()+fileName[1];
                    storageService.uploadFile(temp, destFileName);
                    pathArray.add(destFileName);
                }

                for (String path : pathArray) {
                    //新建clotherror对象
                    ClothError clothError = new ClothError();
                    clothError.setCloth_id(cloth_id);
                    clothError.setHotel_id(hotel_id);
                    clothError.setReporter_id((Integer) employee_id);
                    clothError.setType(Constants.ClothErrorType.valueOf(type));
                    clothError.setComment(comment);
                    if (hotel != null) {
                        clothError.setResponser_type(2);
                        clothError.setResponser_id(hotel_id);
                    } else if (washFactory_id != null) {
                        clothError.setResponser_type(3);
                        clothError.setResponser_id(washFactory_id);
                    }
                    clothError.setPic(path);
                    //进行新增insert操作
                    Integer InsertNum = clothErrorService.insert(clothError);
                    InsertSum = InsertSum + InsertNum;
                }
            } else {
                //新建clotherror对象
                ClothError clothError = new ClothError();
                clothError.setCloth_id(cloth_id);
                clothError.setHotel_id(hotel_id);
                clothError.setReporter_id((Integer) employee_id);
                clothError.setType(Constants.ClothErrorType.valueOf(type));
                clothError.setComment(comment);
                if (hotel != null) {
                    clothError.setResponser_type(2);
                    clothError.setResponser_id(hotel_id);
                } else if (washFactory_id != null) {
                    clothError.setResponser_type(3);
                    clothError.setResponser_id(washFactory_id);
                }
                //进行新增insert操作
                Integer InsertNum = clothErrorService.insert(clothError);
                InsertSum = InsertSum + InsertNum;
            }
            if (InsertSum > 0) {
                return new RestError("已经成功报损" + InsertSum + "个布草");
            } else {
                return RestError.E_CLOTHERROR_FAIL;
            }

        } else {
            return RestError.E_CLOTHIERRORTYPE_FAIL;
        }


    }

    //库管端   H-4-1【新报损】C 报损类型接口【张赢】
    @RequestMapping(value = "/app/console/api/GetClothErrorType", method = RequestMethod.POST)
    public Object GetClothErrorType(HttpServletRequest request) throws Exception {
        Constants.ClothErrorType[] ClothErrorValues = Constants.ClothErrorType.values();
        List clothErrorlist = new ArrayList();
//        ClothErrorView clothErrorView=new ClothErrorView();
        for (Constants.ClothErrorType ClothErrortype :
                ClothErrorValues) {
            if (ClothErrortype.toString().equals("Lost")) {
//                clothErrorView.setLost("丢失");
                Map clothErrormap = new HashMap();
                clothErrormap.put("clotherrortype", ClothErrortype);
                clothErrormap.put("clotherrordesc", "丢失");
                clothErrorlist.add(clothErrormap);
            } else if (ClothErrortype.toString().equals("Broken")) {
//                clothErrorView.setBroken("损坏");
                Map clothErrormap = new HashMap();
                clothErrormap.put("clotherrortype", ClothErrortype);
                clothErrormap.put("clotherrordesc", "损坏");
                clothErrorlist.add(clothErrormap);
            } else if (ClothErrortype.toString().equals("QRBroken")) {
//                clothErrorView.setQRBroken("二维码损坏");
                Map clothErrormap = new HashMap();
                clothErrormap.put("clotherrortype", ClothErrortype);
                clothErrormap.put("clotherrordesc", "二维码损坏");
                clothErrorlist.add(clothErrormap);
            }
        }
        return new RestError(clothErrorlist);
    }

    //库管端   H-4-1【新报损】责任人接口B【张赢】
    @RequestMapping(value = "/app/console/api/Responser", method = RequestMethod.POST)
    public Object Responser(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

        HotelBase hotelBase = hotelBaseService.getById(hotel_id);

        //新建hotelview类存对象
        HotelView hotelView = new HotelView();
        hotelView.setHotel_id(hotel_id);
        hotelView.setHotel_name(hotelBase.getHotel_name());

        List<Employee> employeeList = employeeService.getEmployeeWashFactoryListByHotelId(hotel_id);
        hotelView.setWashFactoryList(employeeList);

        if (hotelView != null) {
            return new RestError(hotelView);
        } else {
            return RestError.E_DATA_FAIL;
        }
    }

    //app 库管端  阿姨布草 H-1-4【选择阿姨】  获得本酒店所有保洁员 【张赢】
    @RequestMapping(value = "/app/console/api/GetEmployeeName", method = RequestMethod.GET)
    public Object GetEmployeeName(HttpServletRequest request) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        Integer hotel_id = hotelEmployee.getHotel_id();

        List employeelist = hotelEmployeeService.GetEmloyee(hotel_id);
        if (employeelist.size() > 0) {
            return new RestError(employeelist);
        } else {
            return RestError.E_DATA_FAIL;
        }
    }

    //app 库管端  阿姨布草 H-1-4【阿姨身上的布草】  获得本酒店保洁员名下的所有布草 【张赢】
    @RequestMapping(value = "/app/console/api/GetCleanerCloth", method = RequestMethod.POST)
    public Object GetCleanerCloth(HttpServletRequest request,
                                  @RequestParam(value = "id") Integer id) throws Exception {
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        Integer hotel_id = hotelEmployee.getHotel_id();


        List<ClothLog> clothLogList = clothLogService.getLogListByEmpId(hotel_id, id, 1, null);

        Map clothViewMapDetail = clothLogService.getClothViewMapDetail(clothLogList);

        Iterator<Map.Entry<String, Map<String, Object>>> entries = clothViewMapDetail.entrySet().iterator();
        Integer Sum = 0;
        while (entries.hasNext()) {
            Map.Entry<String, Map<String, Object>> entry = entries.next();
            int OneTimeAll = Integer.parseInt(entry.getValue().get("all").toString());
            Sum = Sum + OneTimeAll;
        }

        Map clothViewMap = new HashMap();
        clothViewMap.put("Sum", Sum);
        List clothviewlist = new ArrayList();
        clothviewlist.add(clothViewMap);
        clothviewlist.add(clothViewMapDetail);

        return new RestError(clothviewlist);

    }

    //库管端  H-1-1【确认洗归】 增加双事务日志【张赢】 洗衣厂出 酒店入
    @RequestMapping(value = "/app/console/api/ClothLogWashBack", method = RequestMethod.POST)
    public Object ClothLogWashBack(HttpServletRequest request,
                                   @RequestParam(value = "ClothIdList", required = false, defaultValue = "") List<Integer> ClothIdList) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();


        //调事务service层开启事务
        for (Integer ClothId :
                ClothIdList) {
            //根据ClothId 查到最大的clothid 用以确定从后从到那个洗衣厂
            //List<ClothLog> maxClothLogList = clothLogService.getMaxClothLogByClothId(ClothId);
            //todo 暂时都出自113东直门洗衣厂
//            for (ClothLog clothLog :
//                    maxClothLogList) {
//                if (clothLog.getPossessor_type() == 3) {
                    clothLogService.InsertDoubleClothLog(hotel_id, ClothId, null, 3, 113, 2, hotel_id, 0);
            Cloth cloth = clothService.getClothByid(ClothId);
            cloth.setStatus(0);
            clothService.update(cloth);
// }
//                else {
//                    return RestError.E_WASHFACTORY_FAIL;
//                }
//            }
        }
        return new RestError("已成功从东直门洗衣厂入本酒店" + ClothIdList.size() + "个布草");

    }

    //库管端  H-5【布草送洗】 增加双事务日志【张赢】 酒店库房出 洗衣厂入
    @RequestMapping(value = "/app/console/api/ClothLogGoWash", method = RequestMethod.POST)
    public Object ClothLogGoWash(HttpServletRequest request,
                                 @RequestParam(value = "ClothIdList", required = false, defaultValue = "") List<Integer> ClothIdList) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();

        //List<Integer> ClothIdListjson = JSON.parseArray(ClothIdList, Integer.class);

        //调事务service层开启事务
        for (Integer ClothId :
                ClothIdList) {
            //根据ClothId 查到最大的clothid 用以确定从后从到那个洗衣厂
//            List<ClothLog> maxClothLogList = clothLogService.getMaxClothLogByClothId(ClothId);
//
//            for (ClothLog clothLog :
//                    maxClothLogList) {
//                if (clothLog.getPossessor_type() == 2) {
                    clothLogService.InsertDoubleClothLog(hotel_id, ClothId, null,
                            2, hotel_id, 3, 113, 1);
//                } else {
//                    return RestError.E_STOREHOUSE_FAIL;
//                }
                //todo先写死 东直门洗衣厂
            }
//        }
        return new RestError("已成功从本酒店送到走去洗衣厂" + ClothIdList.size() + "个布草去送洗");

    }

    //洗涤租赁信息 H-6 每月洗涤的具体信息和个数 +展示每日的送洗具体详情
    @RequestMapping(value = "/app/console/api/GetWashMessagePerMonth", method = RequestMethod.POST)
    public Object GetWashMessagePerMonth(HttpServletRequest request) throws Exception {
        //根据登录人的request 查出hotel_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        Integer hotel_id = hotelEmployee.getHotel_id();

        //获取当前时间
        Date CurrentTime = new Date();
        //获取当月1日0点时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date MonthFirstTime = calendar.getTime();

        //获取当前月的所有ClothLog对象list
        List<ClothLog> clothLogListByCurrentMonth = clothLogService.getClothLogListByCurrentMonth(MonthFirstTime,
                CurrentTime, hotel_id, 1, 3);

        //总数量+时间累加
        Map clothViewMapDetail = clothLogService.getClothViewMapIncludeTime(clothLogListByCurrentMonth);

        //新建Map存总数 和 每一天的详细信息
        Map Viewlistmap = new LinkedHashMap();
        Viewlistmap.put("ByMonth", clothViewMapDetail);

        Map Viewlistmapday = new LinkedHashMap();
        //循环日期 进行每日查询送洗log
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(MonthFirstTime);//设置日期起始时间
        while (dd.getTime().before(CurrentTime)) {//判断是否到结束日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
            String formatTimeCurrent = sdf.format(dd.getTime()) + "%";
            //Date time = dd.getTime();
            //查询当日log
            List<ClothLog> clothLogListByDay = clothLogService.getClothLogListByDay(formatTimeCurrent, hotel_id, 1, 3);
            //每日总数量+时间累加
            Map clothViewMapByDay = clothLogService.getClothViewMapIncludeTime(clothLogListByDay);
            Viewlistmapday.put(sdf.format(dd.getTime()), clothViewMapByDay);
            dd.add(Calendar.DAY_OF_MONTH, 1);//进行当前日期月份加1
        }
        Viewlistmap.put("ByDay", Viewlistmapday);
        if (Viewlistmap.size() > 0) {
            return new RestError(Viewlistmap);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }

    //库管端  房间布草查询H-2请选择楼层 房间
    @RequestMapping(value = "/app/console/api/GetHotelFloorAndRoom", method = RequestMethod.POST)
    public Object GetHotelFloorAndRoom(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();

        //根据hotelid得到不重复的floor集合
        List<Integer> floorlist = roomService.getFloorlistByHotelId(hotel_id);

        List list = new ArrayList();
        for (int i = 0; i < floorlist.size(); i++) {
            List<Room> floorroomlist = roomService.getFloorroomHotelId(hotel_id, floorlist.get(i));
            Map mapfloorroom = new HashMap();
            mapfloorroom.put("floor_id", floorlist.get(i));
            mapfloorroom.put("floor_room", floorroomlist);
            list.add(mapfloorroom);
        }

        if (list.size() > 0) {
            return new RestError(list);
        } else {
            return RestError.E_DATA_INVALID;
        }
    }


    //库管端  H-2-2 按照房间查看布草详情
    @RequestMapping(value = "/app/console/api/SearchClothByRoom", method = RequestMethod.POST)
    public Object SearchClothByRoom(HttpServletRequest request,
                                    @RequestParam(value = "room_name") String room_name) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();

        //根据roomname取roomid
        Room room = roomService.getRoomByname(room_name, hotel_id);

        //根据roomid查询当前房间下的布草id
        List<ClothLog> logList = clothLogService.getLogListByEmpId(hotel_id, room.getId(), 0, null);
        //循环clothid得到对应的 clothview 新建clothview
        List clothViewList = new ArrayList();
        //判空 ClothIdList 如果这里面有库中没有的clothid 就直接返回worong提示
        for (ClothLog clothLog :
                logList) {
            Cloth clothByid = clothService.getClothByid(clothLog.getCloth_id());
            if (clothByid != null) {
                Integer status = clothByid.getStatus();
                ClothView clothView = new ClothView();
                String clothTypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                String[] clothTypeIdsArray = clothTypeIds.split(",");
                clothView.setClothTypeIds(clothTypeIds);
                clothView.setId(clothLog.getCloth_id());
                List<ClothType> clothType = clothTypeService.getClothTypeDescByIds(clothTypeIdsArray);
                clothView.setClothTypes_name(clothType);
                clothView.setHotel_id(hotel_id);
                clothView.setStatus(status);
                clothViewList.add(clothView);
            } else {
                return RestError.E_CLOTHIDMISS_FAIL;
            }
        }
        Map SumAllmap = new HashMap();
        SumAllmap.put("SumAll", logList.size());

        List sumlist = new ArrayList();
        sumlist.add(clothViewList);
        sumlist.add(SumAllmap);
        if (sumlist.size() > 0) {
            return new RestError(sumlist);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }


    //库管端  H-2-1 按照楼层查看布草详情 SumAll
    @RequestMapping(value = "/app/console/api/SearchClothByFloor", method = RequestMethod.POST)
    public Object SearchClothByFloor(HttpServletRequest request,
                                     @RequestParam(value = "floor") Integer floor) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();


        List clothViewList = new ArrayList();
        //判空 ClothIdList 如果这里面有库中没有的clothid 就直接返回worong提示

        List<Room> roomList = roomService.getRoomByfloor(floor, hotel_id);

        //叠加总数
        Integer SumAll = 0;
        for (Room room :
                roomList) {
//根据roomid查询当前房间下的布草id
            List<ClothLog> logList = clothLogService.getLogListByEmpId(hotel_id, room.getId(), 0, null);
            //得到对应 详情 规格 计数
            Map<String, Map<String, Object>> clothViewMapDetail = clothLogService.getClothViewMapDetail(logList);
            clothViewList.add(clothViewMapDetail);

            //遍历map集合取出all相加
            for (String key : clothViewMapDetail.keySet()) {
                Map<String, Object> ValueMap = clothViewMapDetail.get(key);
                //for (String keyTwo : ValueMap.keySet()) {
                SumAll = SumAll + Integer.parseInt(ValueMap.get("all").toString());
                //}
            }
        }
        Map SumMap = new HashMap();
        SumMap.put("SumAll", SumAll);

        List sumlist = new ArrayList();
        sumlist.add(clothViewList);
        sumlist.add(SumMap);

        if (sumlist.size() > 0) {
            return new RestError(sumlist);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }


    //web端 库管 遍历布草类型名称接口
    @RequestMapping(value = "/console/api/ShowClothType", method = RequestMethod.GET)
    public Object ShowClothType(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_idadmin);
        //根据hotelEmployee关系对象得到hotel_id属性
        Integer hotel_id = hotelEmployee.getHotel_id();

        List<ClothType> clothTypeByKind = clothTypeService.getClothTypeByKind(Type);

        List clothTypelist=new ArrayList<>();
        for (ClothType clothType:
             clothTypeByKind) {
            Map clothTypemap=new HashMap();
            clothTypemap.put("id",clothType.getId());
            clothTypemap.put("desc",clothType.getDesc());
            clothTypelist.add(clothTypemap);
        }
        if (clothTypelist.size() > 0) {
            return new RestError(clothTypelist);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }

    //web端 库管 遍历布草规格 接口
    @RequestMapping(value = "/console/api/ShowClothTypeSize", method = RequestMethod.GET)
    public Object ShowClothTypeSize(HttpServletRequest request) throws Exception {

        List<ClothType> clothTypeByKind = clothTypeService.getClothTypeByKind(Size);

        List clothTypelist=new ArrayList<>();
        for (ClothType clothType:
                clothTypeByKind) {
            Map clothTypemap=new HashMap();
            clothTypemap.put("id",clothType.getId());
            clothTypemap.put("desc",clothType.getDesc());
            clothTypelist.add(clothTypemap);
        }
        if (clothTypelist.size() > 0) {
            return new RestError(clothTypelist);
        } else {
            return RestError.E_DATA_FAIL;
        }

    }

    //得到所有布草的瞬时状态对象集合 frontDesk 【库存查询】
    @RequestMapping(value = "/wx/console/api/getAllStoreClothOrder", method = RequestMethod.POST)
    public Object getAllStoreClothOrder(HttpServletRequest request,
                                   @RequestParam(value = "cloth_typeType", required = false) Integer cloth_typeType,
                                   @RequestParam(value = "cloth_typeSize", required = false) Integer cloth_typeSize,
                                   @RequestParam(value = "status", required = false) Integer status,
                                   @RequestParam(value = "possessor_type", required = false) Integer possessor_type,
                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {


        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo < 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        Integer index = (pageNo - 1) * pageSize;

        //根据hotel_id得到对应酒店下的所有log对象  comment 1是全部 2是有备注
        List<ClothLog> clothLogList = clothLogService.getClothLogListByPossessor_typeAndStutasAndTypeAndSize(hotel_id, possessor_type, status, index, pageSize);

        //一共有多少条 公共变量
        Integer count = clothLogService.getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimit(hotel_id, possessor_type, status);
        List clothErrorViewList = new ArrayList<>();

            //针对报损情况 与报损处理 用clotherror表查一下 看看这个clothid包含不包含 如果有此循环clothid则set两属性 如果没有就跳过null
            List<Integer> errorClothidList = clothErrorService.getErrorClothidByHotelId(hotel_id);

            if(cloth_typeType == null && cloth_typeSize == null){
//新建list集合存储viewweb对象用
                //循环遍历所得clothloglist 把新值存进去
                for (ClothLog clothLog :
                        clothLogList) {
                    //先键一个view对象给前端展示使用
                    ClothErrorView clothErrorView =new ClothErrorView();
                    clothErrorView.setCloth_id(clothLog.getCloth_id());
                    clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                    clothErrorView.setCleanStatus(clothLog.getStatus());
                    clothErrorView.setUpdate_time(clothLog.getCreate_time());
                    //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                    if(errorClothidList.contains(clothLog.getCloth_id())){
                        List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                        clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                        clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                    }
                    //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                    String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                        if(clothtypeIds.contains(",")){
                            String[] clothIdssplit = clothtypeIds.split(",");
                            List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                            clothErrorView.setClothTypeList(clothTypeDescByIds);
                        }else {
                            String[] clothtypearr=new String[]{clothtypeIds};
                            List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothtypearr);
                            clothErrorView.setClothTypeList(clothTypeDescByIds);
                        }
                    clothErrorViewList.add(clothErrorView);
                }
            }else{
                for (ClothLog clothLog:
                clothLogList) {
                    if(cloth_typeType != null){
                        //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                        String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                        String[] clothIdssplit = clothtypeIds.split(",");
                        List<Integer> clothidslist=new ArrayList<>();
                        for (String clothId:
                        clothIdssplit) {
                            clothidslist.add(Integer.parseInt(clothId));
                        }
                        //如果这个ids包含要求的typeid就进行set操作
                        if(clothidslist.contains(cloth_typeType)){
                            if(cloth_typeSize  != null){
                                if(clothidslist.contains(cloth_typeSize)){
                                    //先键一个view对象给前端展示使用
                                    ClothErrorView clothErrorView =new ClothErrorView();
                                    clothErrorView.setCloth_id(clothLog.getCloth_id());
                                    clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                                    clothErrorView.setCleanStatus(clothLog.getStatus());
                                    clothErrorView.setUpdate_time(clothLog.getCreate_time());
                                    //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                                    if(errorClothidList.contains(clothLog.getCloth_id())){
                                        List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                                        clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                                        clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                                    }
                                    //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                                    List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                                    clothErrorView.setClothTypeList(clothTypeDescByIds);

                                    clothErrorViewList.add(clothErrorView);
                                }
                            }
                            //先键一个view对象给前端展示使用
                            ClothErrorView clothErrorView =new ClothErrorView();
                            clothErrorView.setCloth_id(clothLog.getCloth_id());
                            clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                            clothErrorView.setCleanStatus(clothLog.getStatus());
                            clothErrorView.setUpdate_time(clothLog.getCreate_time());
                            //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                            if(errorClothidList.contains(clothLog.getCloth_id())){
                                List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                                clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                                clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                            }
                            //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                            List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                            clothErrorView.setClothTypeList(clothTypeDescByIds);

                            clothErrorViewList.add(clothErrorView);
                        }

                    }else {
                        //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                        String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                        String[] clothIdssplit = clothtypeIds.split(",");
                        List<Integer> clothidslist=new ArrayList<>();
                        for (String clothId:
                                clothIdssplit) {
                            clothidslist.add(Integer.parseInt(clothId));
                        }
                        //如果这个ids包含要求的typeid就进行set操作
                        if(clothidslist.contains(cloth_typeSize)){
                            //先键一个view对象给前端展示使用
                            ClothErrorView clothErrorView =new ClothErrorView();
                            clothErrorView.setCloth_id(clothLog.getCloth_id());
                            clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                            clothErrorView.setCleanStatus(clothLog.getStatus());
                            clothErrorView.setUpdate_time(clothLog.getCreate_time());
                            //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                            if(errorClothidList.contains(clothLog.getCloth_id())){
                                List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                                clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                                clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                            }
                            //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                            List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                            clothErrorView.setClothTypeList(clothTypeDescByIds);

                            clothErrorViewList.add(clothErrorView);

                        }

                    }

                }

            }
        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
       // ret.put("CurrentTime", formatCurrentTime);
        ret.put("data", clothErrorViewList);
        //判断返回集合
        if (ret.size() > 0) {
            return ret;
        } else {
            return RestError.E_CLOTHPOSSESSORTYPE_FAIL;
        }
    }


    //web端得到所有报损布草相信信息
    @RequestMapping(value = "/wx/console/api/GetWebErrorCloth", method = RequestMethod.POST)
    public Object GetWebErrorCloth(HttpServletRequest request,
                                        @RequestParam(value = "type", required = false) String type,
                                        @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) throws Exception {

        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();

        //第几页 如果当前页小于1就让它等于1 也就是从第一页开始显示
        if (pageNo < 1) {
            pageNo = 1;
        }
        //每页显示多少行 默认20行
        //limit（m,n）分页m是下标index n是pageSize 还有一个变量是当前页pageNo
        // 他们存在函数关系 pageNo -1 * pagesize=index
        Integer index = (pageNo - 1) * pageSize;

        //查询clotherror表 分页按照全查询还是有有type查询
        List<ClothErrorView> errorClothListByHotelidOrType = clothErrorService.getErrorClothListByHotelidOrType(hotel_id,type, pageSize, index);

        //一共有多少条 公共变量
        Integer count =  clothErrorService.getErrorClothListByHotelidOrTypeNolimit(hotel_id, type);

        Map<String, Object> ret = new HashMap<>();
        ret.put("paging", new Paging(pageNo, pageSize, count));
        ret.put("data", errorClothListByHotelidOrType);
        //判断返回集合
        if (ret.size() > 0) {
            return ret;
        } else {
            return RestError.E_CLOTHPOSSESSORTYPE_FAIL;
        }
    }


    //【frontDesk】【布草人力管理-库存查询倒出Excel】 根据前台所选选项得到相应查询结果 下載這個列表 不分頁
    @RequestMapping(value = "/wx/console/api/DownLoadAllStoreClothExcel", method = RequestMethod.POST)
    public String DownLoadAllStoreClothExcel(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam(value = "cloth_typeType", required = false) Integer cloth_typeType,
                                             @RequestParam(value = "cloth_typeSize", required = false) Integer cloth_typeSize,
                                             @RequestParam(value = "status", required = false) Integer status,
                                             @RequestParam(value = "possessor_type", required = false) Integer possessor_type) throws Exception {
        request.setCharacterEncoding("utf-8");
        String re = request.getParameter("state");
        String ad = request.getParameter("ad");
        String n = request.getParameter("n");


        //根据登录人的session找到对应的employee_id
        Object employee_id = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 hotelEmployee关系对象
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam((Integer) employee_id);
        //根据hotelEmployee关系对象得到hotel_id属性
        int hotel_id = hotelEmployee.getHotel_id();



        //根据hotel_id得到对应酒店下的所有log对象  comment 1是全部 2是有备注
        List<ClothLog> clothLogList = clothLogService.getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimitDownExcel(hotel_id, possessor_type, status);

        //一共有多少条 公共变量
        Integer count = clothLogService.getClothLogListByPossessor_typeAndStutasAndTypeAndSizeNoLimit(hotel_id, possessor_type, status);
        List clothErrorViewList = new ArrayList<>();

        //针对报损情况 与报损处理 用clotherror表查一下 看看这个clothid包含不包含 如果有此循环clothid则set两属性 如果没有就跳过null
        List<Integer> errorClothidList = clothErrorService.getErrorClothidByHotelId(hotel_id);

        if(cloth_typeType == null && cloth_typeSize == null){
//新建list集合存储viewweb对象用
            //循环遍历所得clothloglist 把新值存进去
            for (ClothLog clothLog :
                    clothLogList) {
                //先键一个view对象给前端展示使用
                ClothErrorView clothErrorView =new ClothErrorView();
                clothErrorView.setCloth_id(clothLog.getCloth_id());
                clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                clothErrorView.setCleanStatus(clothLog.getStatus());
                clothErrorView.setUpdate_time(clothLog.getCreate_time());
                //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                if(errorClothidList.contains(clothLog.getCloth_id())){
                    List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                    clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                    clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                }
                //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                if(clothtypeIds.contains(",")){
                    String[] clothIdssplit = clothtypeIds.split(",");
                    List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                    clothErrorView.setClothTypeList(clothTypeDescByIds);
                }else {
                    String[] clothtypearr=new String[]{clothtypeIds};
                    List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothtypearr);
                    clothErrorView.setClothTypeList(clothTypeDescByIds);
                }
                clothErrorViewList.add(clothErrorView);
            }
        }else{
            for (ClothLog clothLog:
                    clothLogList) {
                if(cloth_typeType != null){
                    //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                    String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                    String[] clothIdssplit = clothtypeIds.split(",");
                    List<Integer> clothidslist=new ArrayList<>();
                    for (String clothId:
                            clothIdssplit) {
                        clothidslist.add(Integer.parseInt(clothId));
                    }
                    //如果这个ids包含要求的typeid就进行set操作
                    if(clothidslist.contains(cloth_typeType)){
                        if(cloth_typeSize  != null){
                            if(clothidslist.contains(cloth_typeSize)){
                                //先键一个view对象给前端展示使用
                                ClothErrorView clothErrorView =new ClothErrorView();
                                clothErrorView.setCloth_id(clothLog.getCloth_id());
                                clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                                clothErrorView.setCleanStatus(clothLog.getStatus());
                                clothErrorView.setUpdate_time(clothLog.getCreate_time());
                                //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                                if(errorClothidList.contains(clothLog.getCloth_id())){
                                    List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                                    clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                                    clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                                }
                                //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                                List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                                clothErrorView.setClothTypeList(clothTypeDescByIds);

                                clothErrorViewList.add(clothErrorView);
                            }
                        }
                        //先键一个view对象给前端展示使用
                        ClothErrorView clothErrorView =new ClothErrorView();
                        clothErrorView.setCloth_id(clothLog.getCloth_id());
                        clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                        clothErrorView.setCleanStatus(clothLog.getStatus());
                        clothErrorView.setUpdate_time(clothLog.getCreate_time());
                        //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                        if(errorClothidList.contains(clothLog.getCloth_id())){
                            List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                            clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                            clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                        }
                        //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                        List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                        clothErrorView.setClothTypeList(clothTypeDescByIds);

                        clothErrorViewList.add(clothErrorView);
                    }

                }else {
                    //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                    String clothtypeIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());
                    String[] clothIdssplit = clothtypeIds.split(",");
                    List<Integer> clothidslist=new ArrayList<>();
                    for (String clothId:
                            clothIdssplit) {
                        clothidslist.add(Integer.parseInt(clothId));
                    }
                    //如果这个ids包含要求的typeid就进行set操作
                    if(clothidslist.contains(cloth_typeSize)){
                        //先键一个view对象给前端展示使用
                        ClothErrorView clothErrorView =new ClothErrorView();
                        clothErrorView.setCloth_id(clothLog.getCloth_id());
                        clothErrorView.setPossessor_type(clothLog.getPossessor_type());
                        clothErrorView.setCleanStatus(clothLog.getStatus());
                        clothErrorView.setUpdate_time(clothLog.getCreate_time());
                        //判断该clothid是否存在报损情况,如果存在就set 报损的两个属性，如果不存在就跳过 直接null
                        if(errorClothidList.contains(clothLog.getCloth_id())){
                            List<ClothError> clothErrorsByHotelIdAndClothId = clothErrorService.getClothErrorsByHotelIdAndClothId(hotel_id, clothLog.getCloth_id());
                            clothErrorView.setType(clothErrorsByHotelIdAndClothId.get(0).getType());
                            clothErrorView.setStatus(clothErrorsByHotelIdAndClothId.get(0).getStatus());
                        }
                        //根据clothid 查的ids 然后根据ids查得relclothtype对象存进去
                        List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothIdssplit);
                        clothErrorView.setClothTypeList(clothTypeDescByIds);

                        clothErrorViewList.add(clothErrorView);

                    }

                }

            }

        }
        List<String> title = new ArrayList<String>();
        title.add("布草名称");
        title.add("规格");
        title.add("布草材质");
        title.add("布草ID");
        title.add("所在位置");
        title.add("清洁程度");
        title.add("报损情况");
        title.add("报损处理");
        title.add("最新更新时间");
        AllStoreClothCreateExcel(request, response, clothErrorViewList, "库存列表导出Excel", title);
        return null;
    }

    public void AllStoreClothCreateExcel(HttpServletRequest request, HttpServletResponse response,
                                          List<ClothErrorView> clothErrorViewList, String fileName, List<String> title) {
        try {
// 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            // 字体增粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.size(); i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.get(i));
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
            for (int j = 0; j < clothErrorViewList.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 1);
                for (int k = 0; k < title.size(); k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (clothErrorViewList.get(j).getClothTypeList().get(0) != null) {// 第一列布草名称
                                for (ClothType clothType:
                                clothErrorViewList.get(j).getClothTypeList()) {
                                    if(clothType.getCloth_kind().equals(Type)){
                                        cell.setCellValue(clothType.getDesc());
                                    }
                                }

                            }
                            break;
                        case 1:
                            if (clothErrorViewList.get(j).getClothTypeList().get(0) != null) {//第二列规格
                                for (ClothType clothType:
                                        clothErrorViewList.get(j).getClothTypeList()) {
                                    if(clothType.getCloth_kind().equals(Size)){
                                        cell.setCellValue(clothType.getDesc());
                                    }
                                }
                            }
                            break;
                        case 2:
                            if (clothErrorViewList.get(j).getClothTypeList().get(0) != null) {//第三列布草材质
                                for (ClothType clothType:
                                        clothErrorViewList.get(j).getClothTypeList()) {
                                    if(clothType.getCloth_kind().equals(Material)){
                                        cell.setCellValue(clothType.getDesc());
                                    }
                                }
                            }
                            break;
                        case 3:
                            if (clothErrorViewList.get(j).getCloth_id() != null) {//第四列布草ID
                                cell.setCellValue(clothErrorViewList.get(j).getCloth_id());
                            }
                            break;
                        case 4:
                            if (clothErrorViewList.get(j).getPossessor_type() != null) {//第五列所在位置
                                switch (clothErrorViewList.get(j).getPossessor_type()){
                                    case 0 : cell.setCellValue("房间");
                                        break;
                                    case 1 : cell.setCellValue("员工");
                                        break;
                                    case 2 : cell.setCellValue("库房");
                                        break;
                                    case 3 : cell.setCellValue("洗衣厂");
                                        break;
                                }
                            }
                            break;
                        case 5:
                            if (clothErrorViewList.get(j).getCleanStatus() != null) {//第六列清洁程度
                                switch (clothErrorViewList.get(j).getCleanStatus()){
                                    case 0 : cell.setCellValue("干净");
                                        break;
                                    case 1 : cell.setCellValue("脏");
                                        break;
                                }
                            }
                            break;
                        case 6:
                            if (clothErrorViewList.get(j).getType() != null) {//第七列报损情况
                                if(clothErrorViewList.get(j).getType().equals(Lost)){
                                    cell.setCellValue("丢失");
                                }else if (clothErrorViewList.get(j).getType().equals(Broken)){
                                    cell.setCellValue("损坏");
                                }else if (clothErrorViewList.get(j).getType().equals(QRBroken)){
                                    cell.setCellValue("二维码损坏");
                                }
                            }
                            break;
                        case 7:
                            if (clothErrorViewList.get(j).getStatus() != null) {//第8列报损处理
                                switch (clothErrorViewList.get(j).getStatus()){
                                    case 0 : cell.setCellValue("已报损");
                                        break;
                                    case 1 : cell.setCellValue("已处理");
                                        break;
                                }
                            }
                            break;
                        case 8:
                            if (clothErrorViewList.get(j).getUpdate_time() != null) {//第9列报损处理
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                cell.setCellValue(df.format(clothErrorViewList.get(j).getUpdate_time()));
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //1.6 总仓入库 洗衣厂出 总仓入 或者 第一次 新布草直接入库  或者 酒店出 总仓入
    @RequestMapping(value = "/app/console/api/IntoStorage", method = RequestMethod.POST)
    public Object IntoStorage(HttpServletRequest request,
                                   @RequestParam(value = "ClothIdList", required = false, defaultValue = "") List<Integer> ClothIdList) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 centralWarehouseEmployeee关系对象
        CentralWarehouseEmployee centralWarehouseEmployee = centralWarehouseEmployeeService.getById((Integer) employee_idadmin);
        //根据centralWarehouseEmployee关系对象得到中央仓库id
        Integer Central_warehouse_id = centralWarehouseEmployee.getCentral_warehouse_id();


        //调事务service层开启事务
        for (Integer ClothId :
                ClothIdList) {
            //根据ClothId 查到最大的clothid 用以确定从后从到那个洗衣厂
            List<ClothLog> maxClothLogList = clothLogService.getMaxClothLogByClothId(ClothId);

            //判断是不是第一次入库的新布草，有没有之前的时间log点  如果有loglist 说明 不是从洗衣厂来的就是从酒店来的
            if(maxClothLogList.size()>0){
                for (ClothLog clothLog :
                        maxClothLogList) {
                    //=3就是从洗衣厂来的
                    if(clothLog.getPossessor_type()==3){
                        clothLogService.InsertDoubleClothLog(null, ClothId, null, 3, clothLog.getPossessor_id(), 4, Central_warehouse_id, 0);
                        Cloth cloth = clothService.getClothByid(ClothId);
                        cloth.setStatus(0);
                        clothService.update(cloth);
                    }else {
                        //从其他地方来的 酒店 或者 司机  来的都是脏的（因为只有洗衣厂来的才是干净的）
                        clothLogService.InsertDoubleClothLog(clothLog.getHotel_id(), ClothId, null, clothLog.getPossessor_type(), clothLog.getPossessor_id(), 4, Central_warehouse_id, 1);
                        Cloth cloth = clothService.getClothByid(ClothId);
                        cloth.setStatus(1);
                        clothService.update(cloth);
                    }
            }
            }else{//第一次入库的新布草
                clothLogService.InsertDoubleClothLog(null, ClothId, null, null, null, 4, Central_warehouse_id, 0);
            }
        }
        return new RestError("已成功从洗衣厂入本总仓" + ClothIdList.size() + "个布草");

    }


    /*
1.6版APP 总仓 停滞布草
*/
    @RequestMapping(value = "/app/console/api/StandStillCloth", method = RequestMethod.POST)
    public Object StandStillCloth(HttpServletRequest request) throws Exception {
        //根据登录人的session找到对应的employee_id
        Object employee_idadmin = request.getSession().getAttribute("employee_id");
        //根据employee_id 找到对应的 centralWarehouseEmployeee关系对象
        CentralWarehouseEmployee centralWarehouseEmployee = centralWarehouseEmployeeService.getById((Integer) employee_idadmin);
        //根据centralWarehouseEmployee关系对象得到中央仓库id
        Integer Central_warehouse_id = centralWarehouseEmployee.getCentral_warehouse_id();

        //先查一下 目前所有在本总仓内的布草id list
        List<ClothLog> maxClothLogList = clothLogService.getMaxClothLogByPossessor_typeAndPossessor_id(Central_warehouse_id,4);
        //新建list存clothview对象返回值
        List clothViewList=new ArrayList();
        //获取当前时间
        Date CurrentTime = new Date();


        if(maxClothLogList.size()>0){
            for (ClothLog clothLog:
                    maxClothLogList) {
                ClothView clothView=new ClothView();
                clothView.setId(clothLog.getCloth_id());//获取cothid
                String clothIds = clothLogService.getClothIdsByRedis(clothLog.getCloth_id());//获取clothids
                clothView.setClothTypeIds(clothIds);
                Date create_time = clothLog.getCreate_time();//获取log抵达时间
                //下面计算停留时间
                long distime=CurrentTime.getTime()-create_time.getTime();
                long StayTimeDay=distime/(24*60*60*1000);
                if(StayTimeDay<1){
                    clothView.setStayTimeDay( Long.valueOf(1));
                }else{
                    clothView.setStayTimeDay(StayTimeDay);//存停留时间
                }


                String[] clothTypeIdsArray = clothIds.split(",");
                List<ClothType> clothTypeDescByIds = clothTypeService.getClothTypeDescByIds(clothTypeIdsArray);
                clothView.setClothTypes(clothTypeDescByIds);
                clothViewList.add(clothView);
            }
            Collections.sort(clothViewList, new Comparator<ClothView>(){
                @Override
                public int compare(ClothView o1, ClothView o2) {
                    return o1.getClothTypeIds().compareTo(o2.getClothTypeIds());
                }
            });
            return new RestError(clothViewList);
        }else {
            return RestError.E_DATA_FAIL;
        }

    }

}