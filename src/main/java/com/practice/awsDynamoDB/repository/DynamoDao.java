package com.practice.awsDynamoDB.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.awsDynamoDB.model.SongsPojo;

@Repository
public class DynamoDao {

	@Autowired
	private DynamoDBMapper mapper;

	@Autowired
	private DynamoDB db;

	@Autowired
	ObjectMapper objMapper;
	
	public String addMusic(SongsPojo song) throws Exception {
		System.out.println("Dao called");
		mapper.save(song);
		return "success";
	}

	public List<SongsPojo> getMusicfromId(String songId) throws Exception {
		System.out.println("Dao called");
		List<SongsPojo> songsList = new ArrayList<>();
		Table table = db.getTable("songs");
		QuerySpec spec = new QuerySpec().withKeyConditionExpression("songId = :id")
				.withValueMap(new ValueMap().withString(":id",songId));

		ItemCollection<QueryOutcome> items = table.query(spec);

		Iterator<Item> iterator = items.iterator();

		while (iterator.hasNext()) {
			songsList.add(objMapper.readValue(iterator.next().toJSON(),SongsPojo.class));
		}
		return songsList;
	}
	
	public List<SongsPojo> getMusicfromName(String songName) throws Exception {
		System.out.println("Dao called");
		List<SongsPojo> songsList=new ArrayList<>();
		HashMap<String,String> nameMap = new HashMap<String,String>();
		nameMap.put("#songName", "songName");
		
		HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":name", songName);
        
		Table table = db.getTable("songs");
		Index index = table.getIndex("songName");
		QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("#songName = :name")
				.withNameMap(nameMap)
				.withValueMap(valueMap);

		ItemCollection<QueryOutcome> items = index.query(spec);

		Iterator<Item> iterator = items.iterator();

		while (iterator.hasNext()) {
			songsList.add(objMapper.readValue(iterator.next().toJSON(),SongsPojo.class));
		}
		return songsList;
	}

	public void updateMusicbyId(SongsPojo song) {
		try {
			mapper.save(song,buildDynamoDBSaveExpression(song));	
		}catch (ConditionalCheckFailedException e) {
			e.printStackTrace();
		}	
	}

	private DynamoDBSaveExpression buildDynamoDBSaveExpression(SongsPojo song) {
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expected = new HashMap<>();
		expected.put("songId", new ExpectedAttributeValue(new AttributeValue(song.getSongId()))
				.withComparisonOperator(ComparisonOperator.EQ));
		saveExpression.setExpected(expected);
		return saveExpression;
	}

}
