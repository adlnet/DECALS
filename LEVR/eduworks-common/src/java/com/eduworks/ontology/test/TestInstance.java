package com.eduworks.ontology.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyInstance;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

public class TestInstance {

	
	public static String idChar = Ontology.idCharacter;
	
	public static final String localDirectory = "/Users/djunker/Java/etc/test-java-competencies/tdb";
	
	static String testOntologyName = "test-instance";
	
	static String dataPropId = "data_prop";
	static String objectPropId = "obj_prop";
	
	static String simpleClassId = "simple_class";
	
	static String restrictedDataClassId = "restricted_data_class";
	static String restrictedObjectClassId = "restricted_object_class";
	
	static String requiredDataClassId = "required_data_class";
	static String requiredObjectClassId = "required_object_class";
	
	
	static String existingInstanceId;
	static String deleteInstanceId;
	
	public static Dataset ds;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
		ds = Ontology.setTDBLocation(localDirectory);
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont;
			
			try{
				ont = Ontology.createOntology(ds, testOntologyName);
			}catch(RuntimeException e){
				ont = Ontology.loadOntology(ds, testOntologyName);
				ont = Ontology.createOntology(ds, testOntologyName);
			}
		
			JSONObject values = new JSONObject();
			
			ont.createDataProperty(dataPropId, values);
			ont.createObjectProperty(objectPropId, values);
			
			ont.createClass(simpleClassId, values);
			
			existingInstanceId = ont.createInstance(simpleClassId, values).getId();
			deleteInstanceId = ont.createInstance(simpleClassId, values).getId();
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			assertTrue(ont.propertyExists(dataPropId));
			assertTrue(ont.propertyExists(objectPropId));
			
			assertTrue(ont.classExists(simpleClassId));
		}
		finally
		{
			ds.end();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	
		
	}
	
	/**
	 * FAILING TESTS
	 */
	
	
	/**
	 * PASSING TESTS
	 */
	
	// CREATE 
	
	@Test
	public void test_CreateSimple() {
		String classId = simpleClassId;
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
			
			OntologyInstance instance = ont.createInstance(classId, values);
			
			JSONObject rep = instance.getJSONRepresentation();
			
			assertTrue(rep.toString(), rep.length() == values.length());
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_CreateSimpleWithDataProp(){
		String classId = simpleClassId;
		
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
			
			String[] propValue = new String[1];
			propValue[0] = "test"; 
		
			values.put(idChar+dataPropId, new JSONArray(propValue));
		
			OntologyInstance instance = ont.createInstance(classId, values);
			
			JSONObject rep = instance.getJSONRepresentation();
			
			assertTrue("", rep.length() == values.length());
			assertTrue("Instance does not have expected data property and value: "+rep, rep.has(idChar+dataPropId) 
																					&& rep.optJSONArray(idChar+dataPropId).length() == 1 
																					&& rep.optJSONArray(idChar+dataPropId).optString(0).equals(propValue[0]));
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
			fail(e.getMessage()); 
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_CreateSimpleWithObjectProp(){
		String classId = simpleClassId;
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject values = new JSONObject();
			
			String[] propValue = new String[1];
			propValue[0] = existingInstanceId; 
			values.put(idChar+objectPropId, new JSONArray(propValue));
		
			OntologyInstance instance = ont.createInstance(classId, values);
			
			JSONObject rep = instance.getJSONRepresentation();
			
			assertTrue("JSON Rep: "+rep, rep.length() == values.length());
			assertTrue("Instance does not have expected data property and value: "+rep, rep.has(idChar+objectPropId) 
																					&& rep.optJSONArray(idChar+objectPropId).length() == 1 
																					&& rep.optJSONArray(idChar+objectPropId).optString(0).equals(propValue[0]));
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
			fail(e.getMessage()); 
		}
		finally
		{
			ds.end();
		}
	}
	
	// READ
	
	@Test
	public void test_ReadAllInstances(){
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			Map<String, OntologyInstance> instanceMap = ont.getClass(simpleClassId).getAllInstances();
			
			assertTrue(instanceMap.containsKey(existingInstanceId));
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void testRead(){
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyInstance instance = ont.getInstance(existingInstanceId);
			
			JSONObject rep = instance.getJSONRepresentation();
			
			assertTrue("", rep.length() == 0);
		}
		finally
		{
			ds.end();
		}
	}
	
	// UPDATE
	
	@Test
	public void testUpdate(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
			
			OntologyInstance instance = ont.createInstance(simpleClassId, values);
			
			JSONObject rep = instance.getJSONRepresentation();
			
			assertTrue(rep.length() == 0);
			
			// Add DataProperty
		
			values.put(idChar+dataPropId, new JSONArray("[test]"));
			
			instance.update(values);
			
			rep = instance.getJSONRepresentation();
			
			assertTrue(rep.length() == 1);
			assertTrue(rep.toString().equals(values.toString()));
			
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
			fail(e.getMessage()); 
		}
		finally
		{
			ds.end();
		}
	}
	
	// DELETE
	
	@Test
	public void testDelete(){
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyInstance instance = ont.getInstance(deleteInstanceId);
			
			instance.delete();
			
			assertTrue("", instance.getJSONRepresentation().length() == 0);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.READ);
		try{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			ont.getInstance(deleteInstanceId);
			
			fail("Should throw RuntimeException when getting deleted Instance");
		}
		catch(RuntimeException e)
		{
		}
		finally
		{
			ds.end();
		}
	}
	

}
