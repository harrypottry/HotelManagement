package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.service.*;
import com.aaroom.utils.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @className ScriptController
 * @Description 这个类主要是干 脚本录入 暂用App测试数据 添加数据用（布草信息、相对应的关联表修改增加、emp、hotel）
 * @Author 张赢
 * @Date 2018/12/18 0018下午 14:20
 * @Version 1.0
 **/
@RestController
public class ScriptController {

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionTypeService missionTypeService;

    @Autowired
    private HotelEmployeeService hotelEmployeeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ClothService clothService;

    @Autowired
    private ClothLogService clothLogService;

    @Autowired
    private RelClothTypeService relClothTypeService;

    @Autowired
    private RoomClothService roomClothService;

    @Autowired
    private ClothPriceService clothPriceService;

    @RequestMapping(value = "/console/api/AddCloth2", method = RequestMethod.POST)
    //增加对应房型的布草 传入：1，cloth_id 2，room_name  目前是批量
    public Object AddClothforTest2(HttpServletRequest request,
                                @RequestParam(value = "cloth_idlist" ,required = false, defaultValue = "") List<Integer> cloth_idlist2,
                                @RequestParam("room_name") String room_name) throws Exception {

        //ArrayList list=new ArrayList();
//        for (int i = 1600; i < 1775; i++) {
//            cloth_idlist2.add(i);
//        }

        for (int i = 1775; i < 2000; i++) {
            cloth_idlist2.add(i);
        }

        for (int i = 0; i < 225; i = i + 9) {
            List<Integer> ListSub9 = cloth_idlist2.subList(i, i + 9);

            //得到发布者name
            Integer empid = (Integer) request.getSession().getAttribute("employee_id");
            HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);
            //根据【酒店员工】对象得到酒店id
            int hotel_id = hotelEmployee.getHotel_id();


                //查room表 根据 room_name和hotel_id 得到room对象后得到room_type_id
                Room room = roomService.getRoomByname(room_name, hotel_id);
                Integer room_type_id = room.getRoom_type_id();

                //查room_cloth表 根据room_type_id 查到对应的cloth_type_id list
               List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);
                //新建list存cloth_type_id数组
                List<int[]> cloth_type_idList = new ArrayList();
                //循环roomClothList对象取出 cloth_type_id数组
                for (RoomCloth roomCloth :
                        roomClothList) {
                    //按照room_cloth表中的number数字进行 循环忘cloth_type_idList中添加int数组（clot类型的）
                    for (int j = 0; j < roomCloth.getNumber(); j++) {
                        String cloth_type_id = roomCloth.getCloth_type_id();
                        //分割字符串成string数组 后传为int数组
                        String[] splitcloth_type_id = cloth_type_id.split(",");
                        int[] IntSplitcloth_type_id = new int[splitcloth_type_id.length];
                        for (int k = 0; k < splitcloth_type_id.length; k++) {
                            IntSplitcloth_type_id[k] = Integer.parseInt(splitcloth_type_id[k]);
                        }
                        cloth_type_idList.add(IntSplitcloth_type_id);
                    }

                }
                    //在cloth表 插入一个cloth新对象
                    for (Integer cloth_id :
                            ListSub9) {
                        Cloth cloth = new Cloth();
                        cloth.setId(cloth_id);
                        cloth.setHotel_id(hotel_id);
                        clothService.insertSelective(cloth);
                    }

                    //在rel_cloth_type表 插入数据
                    for (int l = 0; l < ListSub9.size(); l++) {
                        for (int m = l; m < cloth_type_idList.size(); ++m) {
                            for (int cloth_type_idR :
                                    cloth_type_idList.get(m)) {
                                RelClothType relClothType = new RelClothType();
                                relClothType.setCloth_id(ListSub9.get(l));
                                relClothType.setCloth_type_id(cloth_type_idR);
                                Integer savenum = relClothTypeService.save(relClothType);
                            }
                            break;
                        }
                    }

            }
        return new RestError("成功");
    }



    //单独增加布草 不对应任何房型或者clothtype  传入：1，cloth_id
    @RequestMapping(value = "/console/api/AddClothAlone", method = RequestMethod.POST)
    public Object AddClothAlone(HttpServletRequest request,
                           @RequestParam("cloth_idlist") List<Integer> cloth_idlist) throws Exception {
        //得到发布者name
        Integer empid = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);
        //根据【酒店员工】对象得到酒店id
        int hotel_id = hotelEmployee.getHotel_id();
        //判断cloth表中是否已经存在你要插入的clothid了
        List<Integer> ClothIdAllList = clothService.getClothid();
        ClothIdAllList.retainAll(cloth_idlist);
        if(ClothIdAllList.isEmpty()) {
            //在cloth表 插入一个cloth新对象
            for (Integer cloth_id:
                    cloth_idlist) {
                Cloth cloth =new Cloth();
                cloth.setId(cloth_id);
                cloth.setHotel_id(hotel_id);
                clothService.insertSelective(cloth);
            }
            return new RestError("您已经插入了"+cloth_idlist.size()+"条数据");
        }else {
            return "您输入的这些clothid，其中"+ClothIdAllList+
                    "已经存在,请输入不与现表中重复的clothid，以免覆盖原数据，然后再重写调用此接口，此时没有插入任何数据";
        }

    }

    //增加log 用于测试
    @RequestMapping(value = "/console/api/AddClothLogForTest", method = RequestMethod.POST)
    public Object AddClothLogForTest(HttpServletRequest request,
                                @RequestParam("cloth_idlist") List<Integer> cloth_idlist,
                                     @RequestParam(value = "mission_id" ,required = false) Integer mission_id,
                                     @RequestParam("谁出(类型)") Integer possessor_type_out,
                                     @RequestParam("谁出(id)") Integer possessor_id_out,
                                     @RequestParam("谁入(类型)") Integer possessor_type_in,
                                     @RequestParam("谁入(id)") Integer possessor_id_in) throws Exception {
        //得到发布者name
        Integer empid = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);
        //根据【酒店员工】对象得到酒店id
        Integer hotel_id = hotelEmployee.getHotel_id();

        //possessor_type :   0.房间。1.员工 2.库房(hotel) 3.洗衣厂(emp)
        //direction : 0.出 1.入
        for (Integer cloth_id:
        cloth_idlist) {
            Cloth clothByid = clothService.getClothByid(cloth_id);
            clothLogService.InsertDoubleClothLog(hotel_id,cloth_id,mission_id,possessor_type_out,possessor_id_out,
                    possessor_type_in,possessor_id_in,clothByid.getStatus());
        }
            return new RestError("您已经插入了"+cloth_idlist.size()+"条数据");
        }


    //增加房间 用于测试
    @RequestMapping(value = "/console/api/AddRoomForTest", method = RequestMethod.POST)
    public Object AddRoomForTest(HttpServletRequest request,
                                     @RequestParam(value = "id", required = false) Integer id,
                                     @RequestParam(value = "room_name", required = false) String room_name,
                                     @RequestParam(value = "hotel_id", required = false) Integer hotel_id,
                                     @RequestParam(value = "room_type_id", required = false) Integer room_type_id,
                                     @RequestParam(value = "floor", required = false) Integer floor,
                                     @RequestParam(value = "status", required = false) Integer status
                                     ) throws Exception {

        Room room =new Room();

        room.setId(id);
        room.setRoom_name(room_name);
        room.setHotel_id(hotel_id);
        room.setRoom_type_id(room_type_id);
        room.setFloor(floor);
        room.setStatus(status);

        Integer InsertNum = roomService.insert(room);


        return new RestError("您已经插入了"+InsertNum+"条数据");
    }


    //增加对应房型的布草 PSVM本类调用 传入：1，cloth_id 2，room_name
    public  Object AddClothforPSVM(Integer hotel_id,List<Integer> cloth_idlist,String room_name) throws Exception {

        //查room表 根据 room_name和hotel_id 得到room对象后得到room_type_id
        Room room = roomService.getRoomByname(room_name, hotel_id);
        Integer room_type_id=room.getRoom_type_id();

        //查room_cloth表 根据room_type_id 查到对应的cloth_type_id list
        List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);
        //新建list存cloth_type_id数组
        List<int[]> cloth_type_idList=new ArrayList();
        //循环roomClothList对象取出 cloth_type_id数组
        for (RoomCloth roomCloth:
                roomClothList) {
            //按照room_cloth表中的number数字进行 循环忘cloth_type_idList中添加int数组（clot类型的）
            for (int j = 0; j <roomCloth.getNumber() ; j++) {
                String cloth_type_id = roomCloth.getCloth_type_id();
                //分割字符串成string数组 后传为int数组
                String[] splitcloth_type_id = cloth_type_id.split(",");
                int[] IntSplitcloth_type_id=new int[splitcloth_type_id.length];
                for (int i = 0; i <splitcloth_type_id.length ; i++) {
                    IntSplitcloth_type_id[i]=Integer.parseInt(splitcloth_type_id[i]);
                }
                cloth_type_idList.add(IntSplitcloth_type_id);
            }

        }
        //判断输入的是几个clothid与房型所需要的个数想不相符，如果相符进来 不相符返回字符串提示
        if(cloth_idlist.size()==cloth_type_idList.size()){

            //在cloth表 插入一个cloth新对象
            for (Integer cloth_id:
                    cloth_idlist) {
                Cloth cloth =new Cloth();
                cloth.setId(cloth_id);
                cloth.setHotel_id(hotel_id);
                clothService.insertSelective(cloth);
            }

            //在rel_cloth_type表 插入数据
            int num=0;

            for (int i = 0; i <cloth_idlist.size() ; i++) {
                for (int j = i; j <cloth_type_idList.size() ; ++j) {
                    for (int cloth_type_idR:
                            cloth_type_idList.get(j)) {
                        RelClothType relClothType = new RelClothType();
                        relClothType.setCloth_id(cloth_idlist.get(i));
                        relClothType.setCloth_type_id(cloth_type_idR);
                        Integer savenum = relClothTypeService.save(relClothType);
                        num = num + savenum;
                    }
                    break;
                }
            }
            return "success!一共增加了 ： "+num+"个rel_cloth_type对象,增加了 ： "+cloth_idlist.size()+"个布草";
        }else{
            return "输入的布草数量与当前房型所需数量不相符,该房型需要 ： "+cloth_type_idList.size()+"个布草";
        }
//        }else {
//            return "你输入的这些clothid，其中 ： "+ClothIdAllList+"已经存在,请输入不存在的clothid";
//        }
    }

//    public static void main(String[] args) throws Exception {
//        ArrayList list=new ArrayList();
//        for (int i = 600; i <775 ; i++) {
//            list.add(i);
//        }
//        ScriptController sc=new ScriptController();
//        for (int i = 0; i <175 ; i=i+7) {
//            List ListSub7 = list.subList(i, i + 7);
//            sc.AddClothforPSVM(2,ListSub7,"101");
//        }
//    }



//接口调用 根据传入布草id和房间name生成对应 cloth 和 rel_clothtype
    @RequestMapping(value = "/console/api/AddCloth", method = RequestMethod.POST)
    //增加对应房型的布草 传入：1，cloth_id 2，room_name
    public Object AddCloth(HttpServletRequest request,
                           @RequestParam("cloth_idlist") List<Integer> cloth_idlist,
                           @RequestParam("room_name") String room_name) throws Exception {
        //得到发布者name
        Integer empid = (Integer)request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);
        //根据【酒店员工】对象得到酒店id
        int hotel_id = hotelEmployee.getHotel_id();
        //判断cloth表中是否已经存在你要插入的clothid了
        List<Integer> ClothIdAllList = clothService.getClothid();
        //取交集 retainAll用法
        ClothIdAllList.retainAll(cloth_idlist);
        if(ClothIdAllList.isEmpty()){
            //查room表 根据 room_name和hotel_id 得到room对象后得到room_type_id
            Room room = roomService.getRoomByname(room_name, hotel_id);
            Integer room_type_id=room.getRoom_type_id();

            //查room_cloth表 根据room_type_id 查到对应的roomCloth对象的list
            List<RoomCloth> roomClothList = roomClothService.getRoomClothByHotelIdAndRoomId(room_type_id);
            //新建list存cloth_type_id数组
            List<int[]> cloth_type_idList=new ArrayList();
            //循环roomClothList对象取出 cloth_type_id数组
            for (RoomCloth roomCloth:
                    roomClothList) {
                //按照room_cloth表中的number数字进行 循环向cloth_type_idList中添加int数组（clot类型的）
                // TODO: 2019/1/22 0022  这个getNumber 有几套就循环几次存进去
                for (int j = 0; j <roomCloth.getNumber() ; j++) {
                    String cloth_type_id = roomCloth.getCloth_type_id();
                    //分割字符串成string数组 后传为int数组
                    String[] splitcloth_type_id = cloth_type_id.split(",");
                    int[] IntSplitcloth_type_id=new int[splitcloth_type_id.length];
                    for (int i = 0; i <splitcloth_type_id.length ; i++) {
                        IntSplitcloth_type_id[i]=Integer.parseInt(splitcloth_type_id[i]);
                    }
                    // TODO: 2019/1/22 0022 现在 cloth_type_idList 就变成了 存有多个int型clothtypeid数组的 list集合
                    cloth_type_idList.add(IntSplitcloth_type_id);
                }

            }
            //判断输入的是几个clothid与房型所需要的个数想不相符，如果相符进来 不相符返回字符串提示
            if(cloth_idlist.size()==cloth_type_idList.size()){

                //在cloth表 插入一个cloth新对象
                for (Integer cloth_id:
                        cloth_idlist) {
                    Cloth cloth =new Cloth();
                    cloth.setId(cloth_id);
                    cloth.setHotel_id(hotel_id);
                    clothService.insertSelective(cloth);
                }

                //在rel_cloth_type表 插入数据
                int num=0;

                for (int i = 0; i <cloth_idlist.size() ; i++) {
                    //cloth_type_idList是内含多cloth_type_id数组的list
                    for (int j = i; j <cloth_type_idList.size() ; ++j) {
                        //其中一个数组循环
                        for (int cloth_type_idR:
                                cloth_type_idList.get(j)) {
                           //数组中一个数字
                            RelClothType relClothType = new RelClothType();
                            relClothType.setCloth_id(cloth_idlist.get(i));
                            relClothType.setCloth_type_id(cloth_type_idR);
                            Integer savenum = relClothTypeService.save(relClothType);
                            num = num + savenum;
                        }
                        break;
                    }
                }
                return "success!一共增加了 ： "+num+"个rel_cloth_type对象,增加了 ： "+cloth_idlist.size()+"个布草";
            }else{
                return "输入的布草数量与当前房型所需数量不相符,该房型需要 ： "+cloth_type_idList.size()+"个布草";
            }
        }else {
            return "你输入的这些clothid，其中 ： "+ClothIdAllList+"已经存在,请输入不存在的clothid";
        }
    }


    //根据传入clothid 开始的id与结束的id 插入cloth_price表数据
    @RequestMapping(value = "/console/api/AddClothPrice", method = RequestMethod.POST)
    public Object AddClothPrice(HttpServletRequest request,
                           @RequestParam("clothIdStart") Integer clothIdStart,
                           @RequestParam("clothIdEnd") Integer clothIdEnd) throws Exception {
        //得到发布者name
        Integer empid = (Integer) request.getSession().getAttribute("employee_id");
        HotelEmployee hotelEmployee = hotelEmployeeService.getHotelEmployeeByEmployeeIdSoloParam(empid);
        //根据【酒店员工】对象得到酒店id
        int hotel_id = hotelEmployee.getHotel_id();

        List<Integer> cloth_idlist = new ArrayList();
        for (int i = clothIdStart; i < clothIdEnd + 1; i++) {
            cloth_idlist.add(i);
        }
        Double cloth_price = 0.00;
        Double rental_price = 0.00;
        Double pay_wash_price = 0.00;
        Double collect_wash_price = 0.00;
        int num = 0;
        for (Integer cloth_id :
                cloth_idlist) {
            List<Integer> idsByClothId = relClothTypeService.getIdsByClothId(cloth_id);

            if (idsByClothId.contains(1)) {
                cloth_price = 100.00;
                rental_price=0.50;
                pay_wash_price=1.40;
                collect_wash_price=1.40;
            } else if (idsByClothId.contains(5)) {
                cloth_price = 200.00;
                rental_price=0.85;
                pay_wash_price=2.20;
                collect_wash_price=2.20;
            } else if (idsByClothId.contains(4)) {
                cloth_price = 50.00;
                rental_price=0.20;
                pay_wash_price=0.60;
                collect_wash_price=0.60;
            } else if (idsByClothId.contains(6)) {
                cloth_price = 40.00;
                rental_price=0.45;
                pay_wash_price=1.00;
                collect_wash_price=1.00;
            } else if (idsByClothId.contains(19)) {
                cloth_price = 30.00;
                rental_price=0.20;
                pay_wash_price=0.60;
                collect_wash_price=0.60;
            } else if (idsByClothId.contains(20)) {
                cloth_price = 20.00;
                rental_price=0.30;
                pay_wash_price=0.70;
                collect_wash_price=0.70;
            }
            //new对象 set值
            ClothPrice clothPrice = new ClothPrice();
            clothPrice.setRental_price(rental_price);
            clothPrice.setPay_wash_price(pay_wash_price);
            clothPrice.setCollect_wash_price(collect_wash_price);
            clothPrice.setCloth_id(cloth_id);
            clothPrice.setCloth_price(cloth_price);
            clothPrice.setCloth_type_id(StringUtils.strip(idsByClothId.toString(),"[]"));
            clothPrice.setHotel_id(hotel_id);

            //插入对象
            int insertnum = clothPriceService.insert(clothPrice);
            num = num + insertnum;
        }

        return "success!一共增加了 ： " + num + "个cloth_price对象 ";
    }
}
