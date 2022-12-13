package com.amazonaws.lambda.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ItemDBtoAPIHandler implements RequestHandler<Event, String> {

    private DynamoDB dynamoDb;
    @Override
    public String handleRequest(Event input, Context context) {
        this.initDynamoDbClient();

        Table table = dynamoDb.getTable("LostItem");//input.device);
        ItemCollection<ScanOutcome> items=null;
        
        long from=0;
        if(input.page!=null&&input.page!="") { //page가 들어왔을때(한 페이지(9개씩))
        	items = table.scan();
        	return getResponse(items,input.page);
        }
        else if(input.time!=null&&input.time!=""){//time이 들어왔을때(하나만)
        	from = Integer.parseInt(input.time);//sdf.parse(input.time).getTime() / 1000;
            try {
            	ScanSpec scanSpec = new ScanSpec()
                        .withFilterExpression("#t = :from").withNameMap(new NameMap().with("#t", "time"))
                        .withValueMap(new ValueMap().withNumber(":from", from));
            	items = table.scan(scanSpec);
            }
            catch (Exception e) {
                System.err.println("Unable to scan the table:");
                System.err.println(e.getMessage());
            }
        	return "{ \"data\": ["+items.iterator().next().toJSON()+"]}";
        }else if(input.page!=null||input.page!=""&&input.time!=null||input.time!="") { //page가 들어왔을때(한 페이지(9개씩))
        	items = table.scan();
        	return getResponse(items,"ALL");
        }
        else {
        	return "Lambda fail!";
        }
    }

    private String getResponse(ItemCollection<ScanOutcome> items,String page) {
        Iterator<Item> iter = items.iterator();
        if(page=="ALL") {
        	String response = "{ \"data\": ["; 
            for (int i =0; iter.hasNext(); i++) {
                if(i!=0)//첫번째는 ,없음
                    response +=",";
                response += iter.next().toJSON();
            }
            response += "]}";
            return response;
        }else {
        	int fin = 9*Integer.valueOf(page);//9,18
            int fir = fin-9;
            String response = "{ \"data\": ["; 
            for (int i =0; iter.hasNext(); i++) {
                if(fin<i)//마지막까지 출력하고 종료
                	break;
                else if(fir>i) { //ex 2page->마지막이 18번 1page는 건너뛰어야함
                	iter.next();
                	continue;
                }else if (i!=fir) //첫번째가 아니면. 페이지의 첫번째 0,9,18
                    response +=",";
                response += iter.next().toJSON();
            }
            response += "]}";
            return response;
        }
    }
    private void initDynamoDbClient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("ap-northeast-2").build();

        this.dynamoDb = new DynamoDB(client);
    }
}

class Event {
    public String time;
    public String page;
}
