package com.wsclient;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.monitors.ws.WebServiceStub;
import com.monitors.ws.WebServiceStub.Frame;
import com.monitors.ws.WebServiceStub.InsertSingleFrameDTOResponse;


public class InvocaServicios {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static String SingleFrameRecorder(String data){
		try {
			WebServiceStub service = new WebServiceStub("http://190.19.175.174:8008/Monitores.WS/services/WebService?wsdl");
			WebServiceStub.InsertSingleFrame req = new WebServiceStub.InsertSingleFrame();
			req.setData(data);			
			WebServiceStub.InsertSingleFrameResponse res = service.insertSingleFrame(req);
			
			String result = res.get_return();
			return result;			
		} catch (Exception e) {
			System.out.println("Service error: "+ e.getMessage());
		}
		
		return "";
	}
	
	public static String SingleFrameDTORecorder(Frame new_frame){
		try {
			WebServiceStub service = new WebServiceStub("http://190.19.175.174:8008/Monitores.WS/services/WebService?wsdl");
			WebServiceStub.InsertSingleFrameDTO req = new WebServiceStub.InsertSingleFrameDTO();
			req.setFrame(new_frame);	
			InsertSingleFrameDTOResponse res = service.insertSingleFrameDTO(req);
			
			String result = res.get_return();
			return result;			
		} catch (Exception e) {
			System.out.println("Service error: "+ e.getMessage());
		}
		
		return "";
	}
	
	
	public static String MultipleFrameRecorder(String data){
		try {
			WebServiceStub service = new WebServiceStub("http://190.19.175.174:8008/Monitores.WS/services/WebService?wsdl");
			WebServiceStub.InsertMultipleFrames req = new WebServiceStub.InsertMultipleFrames();
			req.setData(data);			
			WebServiceStub.InsertMultipleFramesResponse res = service.insertMultipleFrames(req);
			
			String result = res.get_return();
			return result;			
		} catch (Exception e) {
			System.out.println("Service error: "+ e.getMessage());
		}
		
		return "";
	}
	
	//Lo mismo para SingleDeviceRecorder y MultipleDeviceRecorder
	
	public static void main(String[] args){
		//Single Frame example
//		String data1 = "1|2014-07-02 04:05:06|AA12DD001234|AA12DD001234|12|probe_req|android|W|wifi";		
//		String serviceResult = InvocaServicios.SingleFrameRecorder(data1);		
//		System.out.println("Result: "+serviceResult);
		
		
		//MultipleFrameExample		
		//String data2 = "1|2014-07-02 04:05:08|AA12DD001234|AA12DD001234|12|probe_req|android|W|wifi;1|2014-07-02 04:05:09|AA12DD001234|AA12DD001234|12|probe_req|android|W|wifi;1|2014-07-02 04:05:10|AA12DD001234|AA12DD001234|12|probe_req|android|W|wifi;1|2014-07-02 04:05:11|AA12DD001234|AA12DD001234|12|probe_req|android|W|wifi";		
		//String serviceResult = InvocaServicios.MultipleFrameRecorder(data2);		
		//System.out.println("Result: "+serviceResult);
		
		//Usando los DTOs		
		Date datetime = new Date();
		try {
			datetime = sdf.parse("2016-01-02 04:06:08.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar fecha = sdf.getCalendar();
		fecha.setTime(datetime);
		
		Frame new_frame = new Frame();	
		new_frame.setId_scanner(1);
		new_frame.setTimestamp(fecha);
		new_frame.setSource_mac("AA12DD001234");
		new_frame.setDestination_mac("AA12DD001234");
		new_frame.setSignal(12);
		new_frame.setFrame_type("probe_req");
		new_frame.setSource_device_type("android");
		new_frame.setProtocol("B");
		new_frame.setSsid("bt-0");
		
		String serviceResult2 = InvocaServicios.SingleFrameDTORecorder(new_frame);		
		System.out.println(serviceResult2);
	}
}
