package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {
    /**
     * 获取当前秒级时间戳
     *
     * @return
     */
    public static Integer getCurrentSecondTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getSecond();
    }

    /**
     * 根据Date对象生成当前秒级时间戳
     *
     * @param date
     * @return
     */
    public static Integer getCurrentSecondTimeFromDate(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }

    /**
     * 日期转为String,"yyyy-MM-DD"
     *
     * @param date
     * @return
     */
    public static String date2YYYYMMDHMS(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * 日期转为String,"yyyy-MM-DD"
     *
     * @param date
     * @return
     */
    public static String date2YYYYMMDHMS(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    /**
     * 日期转为String,"yyyy-MM-DD"
     *
     * @param date
     * @return
     */
    public static String date2YYYYMMD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }


    public static String date2YYYYMMD(LocalDateTime createTime) {
        if (createTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return createTime.format(formatter);
    }

    /**
     * 日期转为String,"MM-DD"
     *
     * @param date
     * @return
     */
    public static String date2MMD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(date);
    }

    public static Date YMD2Date(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(str);
    }

    /**
     * 秒转换为分钟
     *
     * @param d
     * @return
     */
    public static String s2m(double d) {
        double m = d / 60.00;
        String a[] = String.valueOf(m).split("\\.");
        a[1] = a[1] + "00";
        return a[0] + ":" + a[1].substring(0, 2);
    }

    /**
     * 分钟转换为秒
     *
     * @param s
     * @return
     */
    public static Integer m2s(String s) {
        int index = s.indexOf(":");
        int mi = Integer.parseInt(s.substring(0, index));
        int ss = Integer.parseInt(s.substring(index + 1));
        return mi * 60 + ss;
    }

    /**
     * 获取参数日期相对当前日期的描述
     *
     * @param date
     * @return
     */
    public static String date2CurrentDesc(Date date) {
        Date nowDate = new Date();
        Long secDiff = (nowDate.getTime() - date.getTime()) / 1000;
        return convertSecToDesc(secDiff);
    }

    public static String date2CurrentDesc(LocalDateTime date) {
        Long nowSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long dateSecond = date.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long secDiff = (nowSecond - dateSecond) / 1000;
        return convertSecToDesc(secDiff);
    }

    /**
     * 秒转为描述
     *
     * @param secDiff
     * @return
     */
    public static String convertSecToDesc(Long secDiff) {
        Long res = 0L;
        String desc = "";
        if (secDiff < 86400) {
            if (secDiff < 3600) {
                if (secDiff < 60) {
                    res = secDiff;
                    desc = "秒前";
                } else {
                    res = secDiff / 60;
                    desc = "分钟前";
                }
            } else {
                res = secDiff / 3600;
                desc = "小时前";
            }
        } else {

            Long dayDiff = secDiff / 86400;
            if (dayDiff < 7) {
                res = dayDiff;
                desc = "天前";
            }

            if (dayDiff >= 7 && dayDiff < 30) {
                res = dayDiff / 7;
                desc = "周前";
            }

            if (dayDiff >= 30 && dayDiff < 365) {
                res = dayDiff / 30;
                desc = "月前";
            }

            if (dayDiff >= 365) {
                res = dayDiff / 365;
                desc = "年前";
            }
        }
        return res.toString() + desc;
    }
}
