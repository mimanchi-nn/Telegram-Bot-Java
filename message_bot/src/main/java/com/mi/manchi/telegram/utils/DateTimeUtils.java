package com.mi.manchi.telegram.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateTimeUtils {

	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	public static final DateTimeFormatter TIME_FULL_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static LocalDate parseLocalDate(String dateStr) {
		return LocalDate.parse(dateStr, DATE_FORMATTER);
	}

	public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
		return LocalDateTime.parse(dateTimeStr, DATE_FORMATTER);
	}

	public static LocalDateTime parseLocalDateTimeHms(String dateTimeStr) {
		return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
	}

	public static LocalDateTime parseLocalDateTimeYYYYMMDD(String dateTimeStr) {
		LocalDate localDate = parseLocalDate(dateTimeStr);
		return localDate.atTime(0, 0, 0);
	}

	public static LocalTime parseLocalTime(String timeStr) {
		return LocalTime.parse(timeStr, TIME_FORMATTER);
	}

	public static String formatLocalDate(LocalDate date) {
		return date.format(DATE_FORMATTER);
	}

	public static String formatLocalDateTime(LocalDateTime datetime) {
		return datetime.format(DATETIME_FORMATTER);
	}

	/**
	 * @param dateTime
	 * @return yyyy-mm-dd 2020-01-01
	 */
	public static String formatLocalDateTimeYYYYMMDD(LocalDateTime dateTime) {
		return dateTime.format(DATE_FORMATTER);
	}

	public static String formatLocalTime(LocalTime time) {
		return time.format(TIME_FORMATTER);
	}

	/**
	 * 日期相隔天数(只计算一个月内)
	 * @param startDateInclusive
	 * @param endDateExclusive
	 * @return
	 */
	public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return Period.between(startDateInclusive, endDateExclusive).getDays();
	}

	/**
	 * 日期相隔天数(精确到毫秒)
	 * @param startDateInclusive
	 * @param endDateExclusive
	 * @return
	 */
	public static Long apartDays(Long startDateInclusive, Long endDateExclusive) {
		return (endDateExclusive - startDateInclusive) / (60 * 60 * 24 * 1000);
	}

	/**
	 * 日期相隔小时
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toHours();
	}

	/**
	 * 日期相隔分钟
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMinutes();
	}

	/**
	 * 日期相隔毫秒数
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMillis();
	}

	/**
	 * 返回当前的日期
	 * @return
	 */
	public static LocalDate getCurrentLocalDate() {
		return LocalDate.now();
	}

	/**
	 * 返回当前时间
	 * @return
	 */
	public static LocalTime getCurrentLocalTime() {
		return LocalTime.now();
	}

	public static LocalDateTime get() {
		return LocalDateTime.now();
	}

	public static int getYear() {
		return get().getYear();
	}

	public static LocalDateTime withYear(int year) {
		return get().withYear(year);
	}

	public static int getMonth() {
		return get().getMonthValue();
	}

	public static LocalDateTime firstDayOfThisYear(int year) {
		return withYear(year).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
	}

	/**
	 * 获取昨日
	 * @return 昨日
	 */
	public static LocalDateTime yesterday() {
		return LocalDateTime.now().minusDays(1);
	}

	/**
	 * 获取昨日
	 * @return 昨日
	 */
	public static LocalDate yesterdayLocalDate() {
		return LocalDate.now().minusDays(1);
	}

	/**
	 * 获取某个月的第一天
	 * @param dateStr eg: 2021-01
	 * @return
	 */
	public static LocalDateTime getFirstDayOfMonth(String dateStr) {
		String[] split = dateStr.split("-");
		int year = Integer.valueOf(split[0]);
		int month = Integer.valueOf(split[1]);
		LocalDateTime firstDay = LocalDateTime.of(year, month, 01, 00, 00, 00);
		return firstDay;
	}

	/**
	 * @param year
	 * @return String
	 * @Title: getFirstDayOfThisYear
	 * @Description: 获取设置所属年最初时间
	 */
	public static String getFirstDayOfThisYear(int year) {
		LocalDateTime firstDayOfThisYear = firstDayOfThisYear(year);
		return DATETIME_FORMATTER.format(firstDayOfThisYear);
	}

	public static LocalDateTime lastDayOfThisYear(int year) {
		return withYear(year).with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
	}

	/**
	 * @param year
	 * @return String
	 * @Title: getLastDayOfThisYear
	 * @Description: 获取设置所属年最后时间
	 */
	public static String getLastDayOfThisYear(int year) {
		LocalDateTime lastDayOfThisYear = lastDayOfThisYear(year);
		return DATETIME_FORMATTER.format(lastDayOfThisYear);
	}

	/**
	 * @return String
	 * @Title: getFirstDayOfThisMonth
	 * @Description: 获取本月的第一天
	 */
	public static String getFirstDayOfThisMonth() {
		LocalDateTime firstDayOfThisYear = get().with(TemporalAdjusters.firstDayOfMonth());
		return DATETIME_FORMATTER.format(firstDayOfThisYear);
	}

	/**
	 * @return String
	 * @Title: getFirstDayOfThisMonth
	 * @Description: 获取本月的最末天
	 */
	public static String getLastDayOfThisMonth() {
		LocalDateTime firstDayOfThisYear = get().with(TemporalAdjusters.lastDayOfMonth());
		return DATETIME_FORMATTER.format(firstDayOfThisYear);
	}

	/**
	 * @param days
	 * @return LocalDateTime
	 * @Title: plusDays
	 * @Description: 当前日期向后推多少天
	 */
	public static LocalDateTime plusDays(int days) {
		return get().plusDays(days);
	}

	/**
	 * @param year
	 * @param month
	 * @return LocalDateTime
	 * @Title: firstDayOfWeekInYearMonth
	 * @Description: 获取指定年月的第一个周一
	 */
	public static LocalDateTime firstDayOfWeekInYearMonth(int year, int month) {
		return get().withYear(year).withMonth(month).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
	}

	/**
	 * @return LocalDateTime
	 * @Title: todayStart
	 * @Description: 当天开始时间
	 */
	public static LocalDateTime todayStart() {
		return LocalDateTime.of(getCurrentLocalDate(), LocalTime.MIN);
	}

	/**
	 * @return LocalDateTime
	 * @Title: todayEnd
	 * @Description: 当天结束时间
	 */
	public static LocalDateTime todayEnd() {
		return LocalDateTime.of(getCurrentLocalDate(), LocalTime.MAX);
	}

	/**
	 * 获取给定日期的开始时间
	 * @param start
	 * @return
	 */
	public static LocalDateTime dayStart(LocalDateTime start) {
		return LocalDateTime.of(start.toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 获取给定日期的结束时间
	 * @param end
	 * @return
	 */
	public static LocalDateTime dayEnd(LocalDateTime end) {
		return LocalDateTime.of(end.toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 昨日开始时间
	 * @return
	 */
	public static LocalDateTime yesterdayStart() {
		return LocalDateTime.of(getCurrentLocalDate().minusDays(1), LocalTime.MIN);
	}

	/**
	 * 昨日结束时间
	 * @return
	 */
	public static LocalDateTime yesterdayEnd() {
		return LocalDateTime.of(getCurrentLocalDate().minusDays(1), LocalTime.MAX);
	}

	/**
	 * @return String
	 * @Title: getStartDayOfWeekToString
	 * @Description: 获取周第一天
	 */
	public static String getStartDayOfWeekToString() {
		return formatLocalDate(getStartDayOfWeek());
	}

	public static LocalDate getStartDayOfWeek() {
		TemporalAdjuster FIRST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(
				localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
		return getCurrentLocalDate().with(FIRST_OF_WEEK);
	}

	/**
	 * @return String
	 * @Title: getEndDayOfWeekToString
	 * @Description: 获取周最后一天
	 */
	public static String getEndDayOfWeekToString() {
		return formatLocalDate(getEndDayOfWeek());
	}

	/**
	 * @return LocalDateTime
	 * @Description: 当前开始时间
	 */
	public static LocalDateTime getNow() {
		return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
	}

	/**
	 * @return LocalDateTime
	 * @Description: 指定时间添加一周
	 */
	public static LocalDateTime minusOneWeek(LocalDateTime now) {
		return now.minus(1, ChronoUnit.WEEKS);
	}

	public static LocalDate getEndDayOfWeek() {
		TemporalAdjuster LAST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(
				localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
		return getCurrentLocalDate().with(LAST_OF_WEEK);
	}

	public static LocalDateTime date2LocalDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 时间戳转LocalDateTime
	 * @param timestamp
	 * @return
	 */
	public static LocalDateTime timestamp2LocalDateTime(Long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
	}

	/**
	 * 返回某天时间的开始时间 即2020-10-12 12：26：56 -> 2020-10-12 00:00:00
	 * @param someDay
	 * @return
	 */
	public static LocalDateTime someDayStartLocalDateTime(LocalDateTime someDay) {
		return someDay.toLocalDate().atStartOfDay();
	}

	public static LocalDate getYesTerDayDate() {
		return LocalDate.now().minusDays(1);
	}

	/**
	 * 返回最近7天的日期字符串 2020-02-01 ... 2020-02-06 2020-02-07
	 * @return
	 */
	public static List<String> lastSevenDay() {
		ArrayList<String> dayList = new ArrayList();
		for (int i = 1; i <= 7; i++) {
			String format = LocalDateTime.now().minusDays(i).format(DATE_FORMATTER);
			dayList.add(format);
		}
		return dayList;
	}

	/**
	 * 参数是带有时分秒格式的字符串
	 * @param beginDay 开始时间
	 * @param endDay 结束时间
	 * @return "yyyy-mm-dd" str 数组
	 */
	public static List<String> rangeDay(String beginDay, String endDay) {
		ArrayList<String> dayList = new ArrayList();
		LocalDateTime beginTime = parseLocalDateTime(beginDay);
		LocalDateTime endTime = parseLocalDateTime(endDay);
		int daysNum = (int) (endTime.toLocalDate().toEpochDay() - beginTime.toLocalDate().toEpochDay());
		for (int i = 0; i <= daysNum; i++) {
			String format = endTime.minusDays(i).format(DATE_FORMATTER);
			dayList.add(format);
		}
		return dayList;
	}

	public static List<String> rangeDay(LocalDateTime beginDay, LocalDateTime endDay) {
		ArrayList<String> dayList = new ArrayList();
		int daysNum = (int) (endDay.toLocalDate().toEpochDay() - beginDay.toLocalDate().toEpochDay());
		for (int i = 0; i <= daysNum; i++) {
			String format = endDay.minusDays(i).format(DATE_FORMATTER);
			dayList.add(format);
		}
		return dayList;
	}

	/**
	 * 参数是带有Y-m-d格式的字符串
	 * @param beginDay 开始时间
	 * @param endDay 结束时间
	 * @return "yyyy-mm-dd" str 数组
	 */
	public static List<String> rangeDayParamWithYMD(String beginDay, String endDay) {
		ArrayList<String> dayList = new ArrayList<>();
		LocalDate beginTime = parseLocalDate(beginDay);
		LocalDate endTime = parseLocalDate(endDay);
		int daysNum = (int) (endTime.toEpochDay() - beginTime.toEpochDay());
		for (int i = 0; i <= daysNum; i++) {
			String format = endTime.minusDays(i).format(DATE_FORMATTER);
			dayList.add(format);
		}
		return dayList;
	}

	/**
	 * @return
	 */
	public static Map<String, String> range30Day() {
		HashMap<String, String> map = new HashMap<>();
		String beginDay = get().minusDays(30).format(DATETIME_FORMATTER);
		String endDay = get().minusDays(1).format(DATETIME_FORMATTER);
		map.put("beginDay", beginDay);
		map.put("endDay", endDay);
		return map;
	}

	/**
	 * @return
	 */
	public static Map<String, String> range7Day() {
		HashMap<String, String> map = new HashMap<>();
		String beginDay = get().minusDays(7).format(DATETIME_FORMATTER);
		String endDay = get().minusDays(1).format(DATETIME_FORMATTER);
		map.put("beginDay", beginDay);
		map.put("endDay", endDay);
		return map;
	}

	/**
	 * 返回当前时间及前7天日期
	 * @return
	 */
	public static Map<String, String> range7DayToNow() {
		HashMap<String, String> map = new HashMap<>();
		String beginDay = get().minusDays(6).format(DATETIME_FORMATTER);
		String endDay = get().minusDays(0).format(DATETIME_FORMATTER);
		map.put("beginDay", beginDay);
		map.put("endDay", endDay);
		return map;
	}

	public static List<LocalDateTime> monthProcess(int year, int month, int days) {
		ArrayList<LocalDateTime> dateList = new ArrayList();
		for (int i = 1; i <= days; i++) {
			LocalDateTime dateTime = LocalDateTime.of(year, month, i, 00, 00, 00);
			// 只返回昨日及以前的数据
			if (dateTime.isBefore(LocalDateTime.now().minusDays(1))) {
				dateList.add(dateTime);
			}
			else {
				break;
			}
		}
		return dateList;
	}

	// Date转LocalDate
	public static LocalDate date2LocalDate(Date date) {
		if (null == date) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * 生成随机localDate
	 * @param min 开始位置
	 * @param max 结束位置
	 * @return 随机localDate
	 */
	public static LocalDate randomLocalDate(String min, String max) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateMin;
		Date dateMax;
		try {
			dateMin = sdf.parse(min);
			dateMax = sdf.parse(max);
			long timeMin = dateMin.getTime();// 获取日期所对应的数字
			long timeMax = dateMax.getTime();
			double random = Math.random(); // [0,1)
			long digit = (long) (random * (timeMax - timeMin + 1) + timeMin);
			Date date = new Date(digit);
			return date2LocalDate(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把一天按小时分割为24个区间
	 * @param day
	 * @return
	 */
	public static List<LocalDateTime> splitDay2Hours(LocalDateTime day) {
		ArrayList<LocalDateTime> rst = new ArrayList();
		LocalDateTime dayStart = dayStart(day);
		LocalDateTime dayEnd = dayEnd(day);
		for (int i = 0; i < 24; i++) {
			rst.add(dayStart.withHour(i));
			rst.add(dayEnd.withHour(i));
		}
		return rst;
	}

	/**
	 * 判断是否是周末
	 * @param timeStr ‘yyyy-mm-dd’
	 * @return true 是周末 false 不是
	 */
	public static Boolean isWeekEnd(String timeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date myDate = sdf.parse(timeStr);
			Calendar myCalendar = Calendar.getInstance();
			myCalendar.setTime(myDate);
			int i = myCalendar.get(Calendar.DAY_OF_WEEK);
			if (i == 1 || i == 7) {
				return Boolean.TRUE;
			}
			else {
				return Boolean.FALSE;
			}
		}
		catch (Exception ex) {
			return Boolean.FALSE;
		}
	}

	/**
	 * 判断是周几
	 * @param timeStr ‘yyyy-mm-dd’
	 * @return 周一 2 周二 3 周三4 周四5 周六 7 周日1
	 */
	public static Integer isWeekEndByLocalDateTime(String timeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date myDate = sdf.parse(timeStr);
			Calendar myCalendar = Calendar.getInstance();

			myCalendar.setTime(myDate);
			return myCalendar.get(Calendar.DAY_OF_WEEK);

		}
		catch (Exception ex) {
			return 0;
		}

	}

	/**
	 * 获取当前时间戳 秒级
	 * @return
	 */
	public static long getTimestampBySecond() {
		return get().toEpochSecond(ZoneOffset.of("+8"));
	}

	public static long getTimestampBySecond(LocalDateTime time) {
		return get().toEpochSecond(ZoneOffset.of("+8"));
	}

	/**
	 * 获取当前时间戳 毫秒级
	 * @return
	 */
	public static long getTimestampByMilliSecond() {
		return get().toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}

	public static long getTimestampByMilliSecond(LocalDateTime time) {
		return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}

	public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * 比较aTime时间是否在bTime之前
	 * @param aTime LocalDateTime
	 * @param bTime LocalDateTime
	 * @return true/false
	 */
	public static boolean isBefore(LocalDateTime aTime, LocalDateTime bTime) {
		return bTime.isBefore(aTime);
	}

}
