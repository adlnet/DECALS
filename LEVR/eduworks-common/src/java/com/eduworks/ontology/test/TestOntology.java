package com.eduworks.ontology.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyClass;
import com.eduworks.ontology.OntologyInstance;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

public class TestOntology extends OntologyTestHarness { 
	
	public static final String localDirectory = "/Users/djunker/Java/etc/test-java-competencies/tdb";
	
	public static String identifier;
	
	public static String idChar = ":";
	
	public static String createdOntId = "simple-create";
	public static String loadedOntId = "simple-load";
	public static String deletedOntId = "simple-delete";
	
	public static String structureOntId = "http://www.competency.services/structure-competency";
	public static String structuredOntId = "test-structured";
	public static String secondStructuredOntId = "test-structured-II";
	
	public static Dataset ds;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		ds = Ontology.setTDBLocation(localDirectory);
		
		HashSet<String> toDelete = new HashSet<String>();
		
		toDelete.add(createdOntId);
		toDelete.add(loadedOntId);
		toDelete.add(deletedOntId);
		toDelete.add(structuredOntId);
		toDelete.add(secondStructuredOntId);
		
		for (String ontId : toDelete)
		{
			ds.begin(ReadWrite.WRITE);
			try
			{
				Ontology ont = Ontology.loadOntology(ds, ontId);
				
				ds.commit();
			}
			catch (RuntimeException e)
			{
				ds.abort();
			}
			finally{
				ds.end();
			}
		}
		
		ds.begin(ReadWrite.READ);
		try
		{
			for (String s : Ontology.listModelIdentifiers(ds))
			{
				System.out.println(s);
			}
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.WRITE);
		try{
			Ontology.createOntology(ds, loadedOntId);
			Ontology.createOntology(ds, deletedOntId);
			Ontology.createOntology(ds, structuredOntId);
			Ontology.createOntology(ds, secondStructuredOntId);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
	}

	@AfterClass
	public static void tearDownAfterClass(){
		
	}

	/**
	 * FAILING TESTS
	 */
	
	// CREATE
	
	@Test (expected = RuntimeException.class)
	public void test_CreateWithEmptyString() {
		identifier = "";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.createOntology(ds, identifier);
			ds.commit();
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test (expected = NullPointerException.class)
	public void test_CreateWithNullString(){
		identifier = null;
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.createOntology(ds, identifier);
			ds.commit();
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
	}

	// LOAD
	
	@Test (expected = RuntimeException.class)
	public void test_LoadWithEmptyString(){
		identifier = "";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds,identifier);
			ds.commit();
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test (expected = NullPointerException.class)
	public void test_LoadWithNullString(){
		identifier = null;
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, identifier);
			ds.commit();
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test (expected = RuntimeException.class)
	public void test_LoadNonExistent(){
		identifier = "doesnt-exist";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, identifier);
			ds.commit();
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test (expected = RuntimeException.class)
	public void test_createInstanceWithoutClass(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, structuredOntId);
	
			JSONObject values = new JSONObject();
			
			values.put(idChar+"competencyTitle", "test_competency");
			values.put(idChar+"CompetencyLevels", new JSONArray("["+idChar+"true, "+idChar+"false]"));
			
			ont.createInstance(idChar+"Competency", values);
			
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			throw new RuntimeException("Couldn't put values in Value Object");
		}
		catch (RuntimeException e)
		{
			ds.abort();
			throw e;
		}
		finally
		{
			ds.end();
		}
		
	}
	
	/**
	 * PASSING TESTS
	 */
	
	// CREATE
	
	@Test
	public void test_CreateSimple(){

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.createOntology(ds, createdOntId);
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.READ);
		assertTrue(ds.containsNamedModel(createdOntId));
		ds.end();
	}
	
	// LOAD
	
	@Test
	public void test_LoadSimple(){
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds,loadedOntId);
			
			assertNotNull(ont);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}
	
	// DELETE
	
	@Test
	public void test_DeleteSimple(){
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, deletedOntId);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.READ);
		assertFalse(ds.containsNamedModel(deletedOntId));
		ds.end();
	}
	
	@Test
	public void test_LoadExternalStructure(){	

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, structuredOntId);
			
			ont.addOntology(ds, structureOntId);
			ds.commit();
		}
		finally
		{
			ds.end();
		}
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, structuredOntId);
			
			JSONObject values = new JSONObject();
			
			values.put(idChar+"competencyTitle", "test_competency");
			values.put(idChar+"competencyLevels", new JSONArray("['"+idChar+"true', '"+idChar+"false']"));
			
			OntologyInstance in = ont.createInstance(idChar+"Competency", values);
			JSONObject oldRep = in.getJSONRepresentation();
		
			String id = in.getId();
			
			ds.commit();
			
			ds.begin(ReadWrite.READ);
			ont = Ontology.loadOntology(ds, structuredOntId);
			
			in = ont.getInstance(id);
			JSONObject newRep = in.getJSONRepresentation();
			
			assertTrue("Instance has different representation after cache reload (old: "+oldRep+") (new: "+newRep+")", compareObjects(oldRep, newRep));
		
		}
		catch (JSONException e)
		{
			ds.abort();
			throw new RuntimeException("Couldn't put values in Value Object");
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_LoadExternalSeparately(){
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ext1 = Ontology.loadOntology(ds, structuredOntId);
			
			ext1.addOntology(ds, structureOntId);
			
			Ontology ext2 = Ontology.loadOntology(ds, secondStructuredOntId);
			
			ext2.addOntology(ds, structureOntId);
			
			ds.commit();
			
			OntologyClass cls1 = ext1.getClass(idChar+"Competency");
			
			OntologyClass cls2 = ext2.getClass(idChar+"Competency");
			
			assertTrue("Different Class Representation for Ontologies that have class structure from same external ontology \n "+cls1.getJSONRepresentation()+"\n"+cls2.getJSONRepresentation(), compareObjects(cls1.getJSONRepresentation(), cls2.getJSONRepresentation()));
		}
		finally
		{
			ds.end();
		}
	}
	
	//@Test
	public void test_QueryWithExternalStructure(){
		Ontology ont = Ontology.loadOntology(ds, secondStructuredOntId);
		
		ont.addOntology(ds, structureOntId);
		
		JSONObject values = new JSONObject();
		
		try{
			values.put(idChar+"competencyTitle", "required_competency");
			values.put(idChar+"competencyLevels", new JSONArray("['"+idChar+"true', '"+idChar+"false']"));
		}catch(JSONException e){
			throw new RuntimeException("Couldn't put values in Value Object");
		}
		
		OntologyInstance required = ont.createInstance(idChar+"Competency", values);
		
		values = new JSONObject();
		
		try{
			values.put(idChar+"competencyTitle", "requiring_competency");
			values.put(idChar+"competencyLevels", new JSONArray("['"+idChar+"true', '"+idChar+"false']"));
			values.put(idChar+"requires", required.getId());
		}catch(JSONException e){
			throw new RuntimeException("Couldn't put values in Value Object");
		}
		
		OntologyInstance requiring = ont.createInstance(idChar+"Competency", values);
		
		assertTrue(requiring.getJSONRepresentation().has(idChar+"Requires"));
		
		String query = "PREFIX comp: <http://www.eduworks.com/competencies#> "+
			 			"SELECT ?required ?requiring "+
			 			"WHERE { "+ 
			 					"?id comp:competencyTitle ?required ." +
			 					"?id comp:requiredBy ?reqId ." +
			 					"?reqId comp:competencyTitle ?requiring" +
			 			"}";
		
		JSONObject result = ont.query(query, false);

		result = result.optJSONArray("result").optJSONObject(0);
		
		assertTrue(result.has("requiring") && result.optString("requiring").equals("requiring_competency"));
		assertTrue(result.has("required") && result.optString("required").equals("required_competency"));
			 					
	}

	
	
}
