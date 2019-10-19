package com.aaroom.service;

import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public interface CacheService {

    int EXPIRE_MEDIA = 3600 * 25;

    /**
     * 短期缓存，半小时过期
     */
    int EXPIRE_SHORT = 1800;

    /**
     * 公司KEY
     */
    String PRE_STARTUP_VIEW = "SV:";

    /**
     * 用户KEY
     */
    String PRE_USER_VIEW = "U:";

    /**
     * 孵化器前缀
     */
    String PRE_CAMP = "CAMP:";

    /**
     * 项目的地址
     */
    String PRE_ADDRS = "ADDRS:";

    /**
     * 统计信息
     */
    String PROP_STATISTIC = "statistic";

    /**
     * 用户基本信息
     */
    String PROP_BASIC = "basic";

    /**
     * 标签
     */
    String PROP_TAGS = "tags";

    /**
     * 教育经历
     */
    String PROP_COLLEGES = "colleges";

    /**
     * 角色， 挑选顺序： 创始人、 联合创始人、 投资人等。
     */
    String PROP_ROLES = "roles";

    /**
     * 当前角色
     */
    String PROP_CURRENT_ROLE = "cur_role";

    /**
     * 正在领投的项目。 针对领投人
     */
    String PROP_LEADINGS = "leadings";

    /**
     * 运营要求
     */
    String PROP_STAGE_REQ = "stage_req";

    /**
     * 团队教育经历
     */
    String PROP_EDU_REQ = "edu_req";

    /**
     * 团队工作经历
     */
    String PROP_EXP_REQ = "exp_req";

    /**
     * 约谈的项目
     */
    String PROP_INTROS = "intros";

    /**
     * 粉丝
     */
    String PROP_FOLLOWERS = "followers";

    /**
     * 关注的东东
     */
    String PROP_FOLLOWINGS = "followings";

    /**
     * 跟投者
     */
    String PROP_BACKERS = "backers";

    /**
     * 领投者
     */
    String PROP_LEADERS = "leaders";

    /**
     * 用户向哪些投资人提交过项目
     */
    String PROP_REFERED_INVESTORS = "refered_investors";

    String PROP_REFERED_VCFIRMS = "refered_vcfirms";

    // ---------

    String PROP_SPOTS = "spots";

    String PROP_RESERVATION = "reserve";

    String PROP_FUNDING = "funding";

    String PROP_MEMBERS = "members";

    String PROP_FOUNDERS = "founders";

    String PROP_INVESTORS = "investors";

    String PROP_MILESTONES = "milestones";


    String PROP_PLANDOC = "plandoc";

    String PROP_PRODMEDIA = "prodmedia";

    String PROP_PLANMEDIA = "planmedia";

    String PROP_LEADERCLAUSE = "LeaderClause";

    String PROP_DUEMEDIA = "DueMedia";

    String PROP_CUSTOMERS = "customers";

    String PROP_PATTERN = "pattern";

    String PRE_JOB = "J:";

    /**
     *
     */
    String PRE_TAG = "T:";

    String PRE_USER_WEIGHT = "UW:";

    String PRE_TAG_CHILDREN = "TAGC:";

    String PRE_TAG_ANCESTORS = "TAGA:";

    String PRE_ACCESS_TOKEN = "AT:";
    String PRE_TRID = "TR:";
    String PRE_CAPTCHA = "CAPT:";
    String PRE_MEDIA = "MEDIA:";
    String PRE_DEMO = "DEMO:";
    String PRE_PATTERN = "PAT:";
    String PRE_MILESTONE = "MS:";
    String PRE_SPOT = "SP:";
    String PRE_FINANCE = "FIN:";
    String PRE_FINANCE_DETAIL = "FD:";
    String PRE_PRESS = "PRESS:";
    String PRE_STARTUP_WEIGHT = "SW:";

    String PRE_RESOURCE = "RES:";

    /**
     * 失败记录的IP
     */
    String PRE_FAILED_IP = "IPF:";
    /**
     * 检查图形验证码失败。
     */
    String PRE_FAILED_CAPTCHA = "CAPF:";

    /**
     * 用户在手机上的最后活跃时间
     */
    String PRE_LAST_LOG = "LLOG:";

    String PRE_NOTIFY_ALL = "NTF_ALL:";

    String PRE_NOTIFY_NEW = "NTF_NEW:";

    String PRE_COUNT_INVITEES = "CIV:";

    /**
     * 成功案例的id集合
     */
    String PRE_STARTUP_SUCCEED_IDS = "SSIDS:";

    /**
     * Category的统计结果
     */
    String PRE_STAT_CATE = "STAT_CATES";

    /**
     * 用户level的统计结果
     */
    String PRE_STAT_LEVEL = "STAT_LEVEL";

    String PRE_INTRO_USER_LIST = "I_U_L:";

    String PRE_INTRO_USER_CNT = "I_U_C:";

    String PRE_TASK = "TASK:";

    /**
     * 项目成员的Id列表
     */
    String PRE_MEMBERS = "ST_MEMBERS:";

    /**
     * 两个用户之间的共同好友数量
     */
    String REL_SUMMARY_USER = "REL_USER:%d:%d";

    /**
     * 用户与项目之间的人脉关系数量
     */
    String REL_SUMMARY_STARTUP = "REL_ST:%d:%d";

    /**
     * 用户上传的通讯录数量
     */
    String PRE_CONTACT_CNT = "CONTACT_CNT:";

    /**
     * 二度人脉中项目的数量
     */
    String CNT_CONN_STARTUPS = "CNT_CONN_ST:";

    /**
     * 二度人脉中的 投资人数量
     */
    String CNT_CONN_USERS = "CNT_CONN_U:%d:%d";

    /**
     * 一度人脉数量
     */
    String PRE_CNT_DGR1 = "CNT_UREL_DR1:";

    /**
     * 二度人脉数量
     */
    String PRE_CNT_DGR2 = "CNT_UREL_DR2:";

    /**
     * 请求短信验证码的ip地址。
     */
    String PRE_SMS_CAPTCHA_COUNTER = "SMS_COUNTER:";

    /**
     * 发送短信， 图形验证码的全局控制
     */
    String PRE_SMS_CAPTACHA_SWITCHER = "SMS_CAPTCHA_SWITCHER";

    String PRE_IM_PROCESSED = "PIM_MSG:";

    /**
     * 避免往openfire中发送的消息。
     */
    String PRE_IM_AVOID_XMPP = "IM_NX:";
    /**
     * 申请创业资源邀请码的集合。包含了所有已经发出的邀请码，验证邀请码的正确性。
     */
    String PRE_RES_INV_CODES = "RES_INV_CODES";
    /**
     * 申请创业资源已使用邀请码的集合。 包含了所有已经使用过的邀请码的集合。
     */
    String PRE_RES_INV_CODES_USED = "RES_INV_CODES_USED";

    /**
     * 12点发送的队列，发送的是昨天12-24点的任务。
     */
    String PRE_RES_APPLY_AUDIT_SMS_AM_TASKS = "RES_APPLY_SMS_AM";
    /**
     * 18点发送的队列，今天0-12点的任务。
     */
    String PRE_RES_APPLY_AUDIT_SMS_PM_TASKS = "RES_APPLY_SMS_PM";

    /**
     * 用户对项目的跟进记录。
     */
    String PRE_FOLLOW_UP = "FUP:";

    /**
     * 最后一条跟进记录。
     */
    String PROP_FOLLOW_UP_LAST = "LS:";

    /**
     * 用户的跟进的所有项目的ID。
     */
    String PRE_FOLLOW_UP_IDS = "FUPALLIDS:";

    String PRE_USER_PREFER = "USER_PREFER:";

    String PRE_ACCOUNT = "ACCT:";

    /**
     * 项目总指数。第二天0时过期。
     */
    String PROP_INDEX = "index";
    /**
     * 项目指数的缓存存入时间。如果读取时，发现不是今天缓存的数据，则需要重新获取。
     */
    String PROP_INDEX_CACHE_DATE = "index_cd";


    /**
     * 产业图谱相关的统计数据
     */
    String KEY_ATLAS_NUMS = "K_ATLAS_NUMS";


    /**
     * 系统属性, 对应 Prop
     */
    String KEY_PROPS = "K_PROPS";


    /**
     * 系统属性, 对应所有管理员的名字
     */
    String KEY_ALL_MANAGER = "K_ALL_MANAGER";

    /**
     * 导航上的面包屑
     */
    String KEY_TAG_NAVI = "K_NAVI";

    /**
     * 搜索各个种类计数
     */
    String PRE_KEYWORD_CNT = "KEYWORD_CNT";

    /**
     * 前一天所有项目权重值的最高值
     */
    String KEY_GLOBAL_ADSCORE_X = "KEY_GLOBAL_ADSCORE_X";


    String KEY_TODAY_UPDATED_VCFIRM = "KEY_TODAY_UPDATED_VCFIRM";

    /**
     * ai大数据活动使用,作为缓存项目和二级分类对应关系
     */
    String PRE_AIBIGDATA_CATE = "M1195_T:";

    boolean exists(String key);

    boolean hexists(String key, String field);

    /**
     * 批量删除
     *
     * @param key
     * @param fields
     * @return
     */
    boolean hdel(String key, String... fields);

    boolean hset(String key, String field, Object value);

    <T> T hget(String key, String field, Class<T> type);

    <T> T hget(String key, String field, TypeReference<T> type);

    Set<String> hkeys(String key);

    Map<String, String> hgetall(String key);

    Map<String, String> hmget(String key, String... fields);

    // --------------

    boolean put(String key, Object val, int ex);

    boolean put(String key, Object val);

    <T> T get(String key, Type type);

    <T> T get(String key, Class<T> type);

    <T> T get(String key, TypeReference<T> type);

    boolean del(String... key);

    <S, T> Map<S, T> mget(String prefix, S[] keys, Class<T> type);

    long incr(String key, int ex);

    Set<String> keys(String prefix);

    Set<String> smembers(String key);

    Long scard(String key);

    Long sadd(String key, String... members);

    boolean sismember(String key, String member);

    Long lpush(String key, String... members);

    String lpop(String key);

    Long expire(String key, int seconds);

}
