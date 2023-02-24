package org.example.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.example.model.Audio;


class AudioClientTest {
	
	void runConcurrentRequestsFromMultipleClients(int num_clients, int ratio) throws Exception {
//		final String uri = "http://155.248.230.86:8080/audios";
		final String uri = "http://localhost:9090/coen6731/audios";
		
		ExecutorService executor = Executors.newFixedThreadPool(num_clients);
		HttpClient client = new HttpClient();
		client.start();
		
		List<Long> roundTimes = new ArrayList<>();
		long totalStartTime = System.currentTimeMillis();
		for(int i = 0; i < num_clients; i++) {
			int clientID = i + 1;
			executor.execute(()->{
				// get request
				for (int j = 0; j < ratio; j++) {
					try {
						// calculate round-time
						long startTime = System.currentTimeMillis();
						
						ContentResponse res = client.GET(uri);
						assertThat(res.getStatus(), equalTo(200));
//						System.out.println(res.getContentAsString());
						
						long roundTime = System.currentTimeMillis() - startTime;
						roundTimes.add(roundTime);
						
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						System.out.print(e.getMessage());
					}
				}
			
				// post request
				ObjectMapper mapper = new ObjectMapper();
				Audio audio = new Audio(3 + clientID, "pefadfe","dafdf","Candy Shop",12,1991,11,10);
				try {
					String jsonString = mapper.writeValueAsString(audio);
					
					// calculate round-time
					long startTime = System.currentTimeMillis();
					
					ContentResponse res = client.POST(uri)
							.content(new StringContentProvider(jsonString), "application/json")
							.send();
					assertThat(res.getStatus(), equalTo(200));
//					System.out.println(res.getContentAsString());
					
					long roundTime = System.currentTimeMillis() - startTime;
					roundTimes.add(roundTime);
					
				} catch (InterruptedException | TimeoutException | ExecutionException | JsonProcessingException e) {
					System.out.print(e.getMessage());
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		long totalRoundTime = System.currentTimeMillis() - totalStartTime;
		System.out.println("Round-trip time list(ms): "+ roundTimes);
		System.out.println("The number of clients: " + num_clients + ", ratio: "+ ratio + ":1, total round-trip time: " + totalRoundTime + "ms");	
//		long totalRoundTime = roundTimes.stream().mapToLong(Long::longValue).sum();
//		System.out.println("The number of clients: " + num_clients + ", ratio: " + ratio + ":1, round-trip time: " + totalRoundTime + "ms");
	}
	
	@Test
	void testAudios10Clients2ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(10,2);
	}
	@Test
	void testAudios50Clients2ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(50,2);
	}
	@Test
	void testAudios100Clients2ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(100,2);
	}
	@Test
	void testAudios10Clients5ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(10,5);
	}
	@Test
	void testAudios50Clients5ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(50,5);
	}
	@Test
	void testAudios100Clients5ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(100,5);
	}
	@Test
	void testAudios10Clients10ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(10,10);
	}
	@Test
	void testAudios50Clients10ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(50,10);
	}
	@Test
	void testAudios100Clients10ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(100,10);
	}
	@Test
	void testAudios10Clients20ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(10,20);
	}
	@Test
	void testAudios50Clients20ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(50,20);
	}
	@Test
	void testAudios100Clients20ratio() throws Exception {
		runConcurrentRequestsFromMultipleClients(100,20);
	}

}






