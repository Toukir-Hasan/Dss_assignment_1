package org.example.controller;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.example.model.Audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@WebServlet(urlPatterns = "/audios/*", asyncSupported = true)
public class ResourceServlet extends HttpServlet {

	private BlockingQueue<AsyncContext> acs = new LinkedBlockingQueue<>();
	private final Executor executor = Executors.newFixedThreadPool(10);
	
	private static final long serialVersionUID = 1L;
	ConcurrentHashMap<String, Object> audioDB = new ConcurrentHashMap<>();
	Gson gson = new Gson();
	
	@Override
	 public void init() throws ServletException {
		audioDB.put("audio_1", new Audio(1,"50 cents","Candy Shop","Candy Shop",12,1991,13,10));
		audioDB.put("audio_2", new Audio(2,"dfd","dfd","dafd",12,1991,12,20));
		audioDB.put("audio_3", new Audio(3,"dfd","dfd","dafd",12,1991,15,30));
		audioDB.put("totalCopiesSoldNum", 60);
		
        new Thread(() -> {
            while (true) {
                try {
                    AsyncContext context = acs.take();

                    executor.execute(new MyService(context, audioDB));
                } catch (InterruptedException e) {
                    log(e.getMessage());
                }
            }
        }).start();
	 }
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext ac = request.startAsync();
//        ac.addListener(new AsyncListener() {
//            @Override
//            public void onComplete(AsyncEvent event) throws IOException {
//                log("ASYNC complete");
//            }
//
//            @Override
//            public void onTimeout(AsyncEvent event) throws IOException {
//                log("ASYNC timout");
//            }
//
//            @Override
//            public void onError(AsyncEvent event) throws IOException {
//                log("ASYNC error" + event.getThrowable());
//            }
//
//            @Override
//            public void onStartAsync(AsyncEvent event) throws IOException {
//            	log("ASYNC timout");
//
//            }
//        });
		acs.add(ac);
//		String pathInfo = request.getPathInfo();
//		if (pathInfo == null) {
//			List<Object> audios = audioDB.entrySet().stream()
//									.filter(entry -> entry.getKey().matches("audio.*"))
//									.map(Map.Entry::getValue)
//									.collect(Collectors.toList());
//			
//		    JsonElement element = gson.toJsonTree(audios);
//		    
//			PrintWriter out = response.getWriter();
//	        response.setContentType("application/json");
//	        response.setCharacterEncoding("UTF-8");
//	        
//	        out.println(element.toString());
//	        out.flush(); 
//        } else {
//        	// url: audios/{audioID}/propertyValues/{propertyName}
//        	String[] pathParams = pathInfo.split("/"); 
// 	        String audioIDString = pathParams[1];
// 	        String propertyName = pathParams[3]; 
//     		Audio audio = (Audio) audioDB.get("audio_"+audioIDString);
//     		String propertyValue = "";
//     		
//      		if("id".equals(propertyName)) propertyValue = Integer.toString(audio.getId());
//     		else if("artistName".equals(propertyName)) propertyValue = audio.getArtistName();
//     		else if("trackTitle".equals(propertyName)) propertyValue = audio.getTrackTitle();
//     		else if("albumTitle".equals(propertyName)) propertyValue = audio.getAlbumTitle();
//     		else if("trackNumber".equals(propertyName)) propertyValue = Long.toString(audio.getTrackNumber());
//     		else if("year".equals(propertyName)) propertyValue = Long.toString(audio.getYear());
//     		else if("reviewsNum".equals(propertyName)) propertyValue = Long.toString(audio.getReviewsNum());
//     		else if("copiesSoldNum".equals(propertyName)) propertyValue = Long.toString(audio.getCopiesSoldNum());
//
//     		PrintWriter out = response.getWriter();
//     		response.setContentType("application/json");
//     		response.setStatus(HttpServletResponse.SC_OK);
//     		response.setCharacterEncoding("UTF-8");
//            
//     		JsonObject jsonObject = new JsonObject();
//        	jsonObject.addProperty("propertyValue",propertyValue);
//        	String jsonString = gson.toJson(jsonObject);
//        	
//	        out.print(jsonString);
//            out.flush();
//        }
	}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	AsyncContext ac = request.startAsync();
//        ac.addListener(new AsyncListener() {
//            @Override
//            public void onComplete(AsyncEvent event) throws IOException {
//                log("ASYNC complete");
//            }
//
//            @Override
//            public void onTimeout(AsyncEvent event) throws IOException {
//                log("ASYNC timout");
//            }
//
//            @Override
//            public void onError(AsyncEvent event) throws IOException {
//                log("ASYNC error" + event.getThrowable());
//            }
//
//            @Override
//            public void onStartAsync(AsyncEvent event) throws IOException {
//            	log("ASYNC timout");
//
//            }
//        });
    	acs.add(ac);
//    	ObjectMapper mapper = new ObjectMapper();
//	    Audio audio = mapper.readValue(request.getInputStream(), Audio.class);
//	    audioDB.put("audio_"+ audio.getId(), audio);
//	    audioDB.compute("totalCopiesSoldNum", (key, value) -> (int)value + audio.getCopiesSoldNum());
//	    response.setStatus(HttpServletResponse.SC_OK);
//    	response.getOutputStream().println("Audio with an ID of " + audio.getId() + " is added to the database.");
    }
}

class MyService implements Runnable {
    private AsyncContext ac;
    ConcurrentHashMap<String, Object> audioDB;
    public MyService(AsyncContext ac, ConcurrentHashMap<String, Object> audioDB) {
        this.ac = ac;
        this.audioDB = audioDB;
    }

    Gson gson = new Gson();
    @Override
    public void run() {
    	 // Check if request is a GET request
    	HttpServletRequest request = (HttpServletRequest) ac.getRequest();
    	HttpServletResponse response =  (HttpServletResponse) ac.getResponse();
        if(request.getMethod().equals("GET")){
            // Do something for GET requests
        	try {
//	    		/coen6731/audios
	    		String[] pathParts = request.getRequestURI().split("/");
	    		if (pathParts[pathParts.length-1].equals("audios")) {
	    			List<Object> audios = audioDB.entrySet().stream()
	    									.filter(entry -> entry.getKey().matches("audio.*"))
	    									.map(Map.Entry::getValue)
	    									.collect(Collectors.toList());
	    			
	    		    JsonElement element = gson.toJsonTree(audios);
					PrintWriter out = response.getWriter();
	    	        response.setContentType("application/json");
	    	        response.setCharacterEncoding("UTF-8");
	    	        
	    	        out.println(element.toString());
	    	        out.flush(); 
	            } else {
	            	// url: /coen6731/audios/{audioID}/propertyValues/{propertyName}
	                String audioIDString = pathParts[pathParts.length - 3];
	                String propertyName = pathParts[pathParts.length - 1];
	         		Audio audio = (Audio) audioDB.get("audio_"+audioIDString);
	         		String propertyValue = "";
	         		
	          		if("id".equals(propertyName)) propertyValue = Integer.toString(audio.getId());
	         		else if("artistName".equals(propertyName)) propertyValue = audio.getArtistName();
	         		else if("trackTitle".equals(propertyName)) propertyValue = audio.getTrackTitle();
	         		else if("albumTitle".equals(propertyName)) propertyValue = audio.getAlbumTitle();
	         		else if("trackNumber".equals(propertyName)) propertyValue = Long.toString(audio.getTrackNumber());
	         		else if("year".equals(propertyName)) propertyValue = Long.toString(audio.getYear());
	         		else if("reviewsNum".equals(propertyName)) propertyValue = Long.toString(audio.getReviewsNum());
	         		else if("copiesSoldNum".equals(propertyName)) propertyValue = Long.toString(audio.getCopiesSoldNum());
	    
	         		PrintWriter out = response.getWriter();
	         		response.setContentType("application/json");
	         		response.setStatus(HttpServletResponse.SC_OK);
	         		response.setCharacterEncoding("UTF-8");
	                
	         		JsonObject jsonObject = new JsonObject();
	            	jsonObject.addProperty("propertyValue",propertyValue);
	            	String jsonString = gson.toJson(jsonObject);
	            	
	    	        out.print(jsonString);
	                out.flush();
	            }
	    		ac.complete();
    		}
    		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	// Post requests
        	try {
	        	ObjectMapper mapper = new ObjectMapper();
	    	    Audio audio = mapper.readValue(request.getInputStream(), Audio.class);
					// TODO Auto-generated catch block
	    	    audioDB.put("audio_"+ audio.getId(), audio);
	    	    audioDB.compute("totalCopiesSoldNum", (key, value) -> (int)value + audio.getCopiesSoldNum());
	    	    response.setStatus(HttpServletResponse.SC_OK);
	        	response.getOutputStream().println("Audio with an ID of " + audio.getId() + " is added to the database.");
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
        	ac.complete();
        }
    }
}


