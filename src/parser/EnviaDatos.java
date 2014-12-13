package parser;

import com.monitors.ws.WebServiceStub;

public class EnviaDatos {

	private static EnviaDatos instancia = null;

	private EnviaDatos(){

	}

	public static EnviaDatos getInstancia(){
		if (instancia == null){
			instancia = new EnviaDatos();
		}
		return instancia;
	}


	public String MultipleFrameRecorder(String datos){
		String salida = "";
		try {
			WebServiceStub servicio = new WebServiceStub("http://190.19.175.174:8008/Monitores.WS/services/WebService?wsdl");
			WebServiceStub.InsertMultipleFrames request = new WebServiceStub.InsertMultipleFrames();
			request.setData(datos);			
			WebServiceStub.InsertMultipleFramesResponse res = servicio.insertMultipleFrames(request);
			salida = res.get_return();

		} catch (Exception e) {
			System.out.println("Error del servicio: "+ e.getMessage());
		}

		return salida;
	}


	public String enviarDatos(String datos){
		//Se envian datos y se devuelve el resultado de la transacci�n
		return MultipleFrameRecorder(datos);		
	}

}
