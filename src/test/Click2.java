package test;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class Click2 {
	static long apidelay;
	static final long customDelay = 775L; // 서버 지연시간등을 생각한 
	static final long allowDelay = 150L; //api 지연시간으로 인한 밀리초 단위 오차를 최소화하기위해 이변수값 이상 지연발생시 재측정
	
    public static void main(String[] args) {
        try {
            // 1. 서버 시간 가져오기
            LocalTime serverTime = getServerTime();
            System.out.println("서버 시간: " + serverTime);

            // 2. 특정 시간에 클릭 이벤트 트리거
            LocalTime targetTime = LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern("HH:mm:ss"));
            scheduleClickEvent(serverTime,targetTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //서버 시간 측정 
    private static LocalTime getServerTime() throws Exception {
        String url = "https://faneventapi.weverse.io/api/v1/events/4042/register";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.connect();
        
        // 응답 헤더에서 Date 정보 가져오기
        String dateHeader = conn.getHeaderField("Date");
        System.out.println("최초 측정 시간 =" + dateHeader);
        String dateHeader2 = conn.getHeaderField("Date");
        conn.disconnect();
        if (dateHeader == null) {
            throw new Exception("서버로부터 날짜 정보를 가져올 수 없습니다.");
        }
        
        while (dateHeader.equals(dateHeader2)) { //밀리초 단위 오차 최소화를 위해 응답 시간 초가 넘어가는 순간 시간으로 서버시간 적용 
        	LocalTime now = LocalTime.now();
        	conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.connect();
            dateHeader2 = conn.getHeaderField("Date");
            LocalTime now2 = LocalTime.now();
            apidelay = java.time.Duration.between(now, now2).toMillis();
            System.out.println("지연시간 = " + apidelay);
            System.out.println("추가 측정시간 =" +dateHeader2);
            if(apidelay > allowDelay) {             	//지연시간 길시 재측정
            	dateHeader = conn.getHeaderField("Date");

            }
		}
        // Date 헤더를 파싱하여 LocalTime 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalTime serverTime = LocalTime.parse(dateHeader2, formatter);
        return serverTime;
    }
    // 마우스클릭 스케쥴링 
    private static void scheduleClickEvent(LocalTime serverTime, LocalTime targetTime) {
        long delay = java.time.Duration.between(serverTime, targetTime).toMillis();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                performClick();
            }
        }, delay-apidelay-customDelay);
    }
    //마우스 를릭 이벤트 
    private static void performClick() {
    	try {
    		Robot robot = new Robot();
        	robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        	robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);	
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	System.out.println("클릭 이벤트 발생!");
        // 실제 클릭 이벤트를 트리거하는 코드가 들어갈 부분
    }
}