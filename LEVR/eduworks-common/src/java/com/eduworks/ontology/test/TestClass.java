package com.eduworks.ontology.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyClass;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

public class TestClass {

	public static String idChar = Ontology.idCharacter;
	
	public static final String localDirectory = "/Users/djunker/Java/etc/test-java-competencies/tdb";
	
	public static String testOntologyName = "test-class";
	
	public static String domainClassId = "domain_class";
	public static String rangeClassId = "range_class";
	
	public static String existingClassId = "existing_class";
	
	public static String updatedClassId = "updated_class";
	
	public static String deletedClassId = "deleted_class";
	
	public static String soloParentClassId = "solo_parent_class";
	public static String parentClassId = "parent_class";
	public static String siblingClassId = "sibling_class";
	
	public static String noDomainNoRangeDataPropId = "nodomain_norange_data_property";
	public static String noDomainNoRangeObjectPropId = "nodomain_norange_object_property";
	
	public static String noDomainDataPropId = "nodomain_data_property";
	public static String noDomainObjectPropId = "nodomain_object_property";
	
	public static String noRangeDataPropId = "norange_data_property";
	public static String noRangeObjectPropId = "norange_object_property";
	
	public static String domainRangeDataPropId = "domain_range_data_property";
	public static String domainRangeObjectPropId = "domain_range_object_property";
	
	public static Dataset ds;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		ds = Ontology.setTDBLocation(localDirectory);
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont;
			try
			{
				ont = Ontology.createOntology(ds, testOntologyName);
				
			}
			catch (RuntimeException e)
			{
				ont = Ontology.loadOntology(ds, testOntologyName);
				
				ont = Ontology.createOntology(ds, testOntologyName);
			}
			
			JSONObject values = new JSONObject();
			
			ont.createClass(domainClassId, values);
			ont.createClass(rangeClassId, values);
			
			ont.createClass(existingClassId, values);
			ont.createClass(updatedClassId, values);
			ont.createClass(deletedClassId, values);
			
			ont.createClass(soloParentClassId, values);
			ont.createClass(parentClassId, values);
			
			values.put("subclassOf", idChar+parentClassId);
			ont.createClass(siblingClassId, values);
			
			
			values = new JSONObject();
			ont.createDataProperty(noDomainNoRangeDataPropId, values);
			
			values.put("domain", idChar+domainClassId);
			ont.createDataProperty(noRangeDataPropId, values);
			
			values.put("range", "xsd:string");
			ont.createDataProperty(domainRangeDataPropId, values);
			
			values.remove("domain");
			ont.createDataProperty(noDomainDataPropId, values);
			
			
			values = new JSONObject();
			ont.createObjectProperty(noDomainNoRangeObjectPropId, values);
			
			values.put("domain", idChar+domainClassId);
			ont.createObjectProperty(noRangeObjectPropId, values);
			
			values.put("range", idChar+rangeClassId);
			ont.createObjectProperty(domainRangeObjectPropId, values);
			
			values.remove("domain");
			ont.createObjectProperty(noDomainObjectPropId, values);
			
			ds.commit();
		}
		finally
		{
			
		}
			
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			Set<String> classIds = ont.getClassIdList();
			Set<String> dataPropIds = ont.getDataPropertyIdList();
			Set<String> objPropIds = ont.getObjectPropertyIdList();
			
			assertTrue(classIds.contains(idChar+domainClassId));
			assertTrue(classIds.contains(idChar+rangeClassId));
			assertTrue(classIds.contains(idChar+soloParentClassId));
			
			assertTrue(classIds.contains(idChar+existingClassId));
			assertTrue(classIds.contains(idChar+updatedClassId));
			assertTrue(classIds.contains(idChar+deletedClassId));
			
			assertTrue(classIds.contains(idChar+parentClassId));
			assertTrue(classIds.contains(idChar+siblingClassId));
			
			
			assertTrue(dataPropIds.contains(idChar+noDomainNoRangeDataPropId));
			assertTrue(dataPropIds.contains(idChar+noDomainDataPropId));
			assertTrue(dataPropIds.contains(idChar+domainRangeDataPropId));
			
			assertTrue(objPropIds.contains(idChar+noDomainNoRangeObjectPropId));
			assertTrue(objPropIds.contains(idChar+noDomainObjectPropId));
			assertTrue(objPropIds.contains(idChar+domainRangeObjectPropId));
			
			ds.commit();
		
		}
		catch(RuntimeException e){
			ds.abort();
		}
		finally
		{
			ds.end();
		}
		
		//ds.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	
		
	}

	/**
	 * FAILING TESTS
	 */
	@Test (expected = RuntimeException.class)
	public void test_CreateDuplicate(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			String classId = existingClassId;
			
			JSONObject values = new JSONObject();
			
			JSONObject requirements = new JSONObject();
			JSONArray requiredVals = new JSONArray();
			try{
				requiredVals.put(idChar+rangeClassId);
				requirements.put(idChar+noDomainNoRangeObjectPropId, requiredVals);
				
				values.put("requirements", requirements);
			}catch(JSONException e){}
			
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONObject actualRestrictions = cls.getJSONRepresentation().optJSONObject("requirements");
			
			System.out.println(actualRestrictions);
			
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
	
	
	/**
	 * PASSING TESTS
	 */
	
	// CREATE
	
	@Test
	public void test_CreateSimple() {
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			String classId = "simple_class";
			
			OntologyClass cls = ont.createClass(classId, new JSONObject());
			
			Set<String> allClassIds = ont.getClassIdList();
			
			assertTrue("", allClassIds.contains(idChar+classId));
			
			helper_testSimpleClass(cls);
			
			ds.commit();
		}
		finally{
			ds.end();
		}
		
	}
	
	public void helper_testSimpleClass(OntologyClass cls){
		Set<OntologyClass> superClasses = cls.getSuperClasses();
		
		assertTrue("Simple Class has Standard Super Classes ", superClasses.size() == 2);
		
		JSONObject rep = cls.getJSONRepresentation();
		
		assertTrue("restrictions: "+rep.optJSONObject("restrictions"), rep.has("restrictions") && rep.optJSONObject("restrictions").length() == 0);
		assertTrue("requirements: "+rep.optJSONObject("requirements"), rep.has("requirements") && rep.optJSONObject("requirements").length() == 0);
		assertTrue("subclasses: "+rep.optJSONObject("subclasses"), rep.has("subclasses") && rep.optJSONObject("subclasses").length() == 0);
		assertTrue("superclasses: "+rep.optJSONObject("superclasses"), rep.has("superclasses") && rep.optJSONArray("superclasses").length() == 2);
		assertTrue("no disjoints in simple class", rep.has("disjoints"));
	}
	
	@Test
	public void test_CreateWithObjectPropertyRestriction(){
		String classId = "object_property_restriction_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			
			JSONObject values = new JSONObject();
			JSONObject restrictions = new JSONObject();
			JSONArray restrictedVals = new JSONArray();
			
			restrictedVals.put(idChar+rangeClassId);
			restrictions.put(idChar+noDomainNoRangeObjectPropId, restrictedVals);
			
			values.put("restrictions", restrictions);
			
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONObject actualRestrictions = cls.getJSONRepresentation().optJSONObject("restrictions");
			
			assertTrue("", actualRestrictions.has(idChar+noDomainNoRangeObjectPropId));
			assertTrue("", actualRestrictions.optJSONArray(idChar+noDomainNoRangeObjectPropId).optString(0).equals(idChar+rangeClassId));
			
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_CreateWithDataPropertyRestriction(){
		String classId = "data_property_restriction_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			
			JSONObject values = new JSONObject();
			JSONObject restrictions = new JSONObject();
			JSONArray restrictedVals = new JSONArray();
			
			restrictedVals.put("xsd:integer");
			restrictions.put(idChar+noDomainNoRangeDataPropId, restrictedVals);
			
			values.put("restrictions", restrictions);
			
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONObject actualRestrictions =  cls.getJSONRepresentation().optJSONObject("restrictions");
			
			assertTrue("", actualRestrictions.has(idChar+noDomainNoRangeDataPropId));
			assertTrue("", actualRestrictions.optJSONArray(idChar+noDomainNoRangeDataPropId).optString(0).equals("xsd:integer"));
			
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_CreateWithObjectPropertyRequirement(){
		String classId = "object_property_requirement_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			JSONObject values = new JSONObject();
			JSONObject requirements = new JSONObject();
			JSONArray requiredVals = new JSONArray();
		
			requiredVals.put(idChar+rangeClassId);
			requirements.put(idChar+noDomainNoRangeObjectPropId, requiredVals);
			
			values.put("requirements", requirements);
			
			
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONObject actualRequirements = cls.getJSONRepresentation().optJSONObject("requirements");
			
			assertTrue("", actualRequirements.has(idChar+noDomainNoRangeObjectPropId));
			assertTrue("", actualRequirements.optJSONArray(idChar+noDomainNoRangeObjectPropId).optString(0).equals(idChar+rangeClassId));

			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_CreateWithDataPropertyRequirement(){
		String classId = "data_property_requirement_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			JSONObject values = new JSONObject();
			JSONObject requirements = new JSONObject();
			JSONArray requiredVals = new JSONArray();
			
			requiredVals.put("xsd:integer");
			requirements.put(idChar+noDomainNoRangeDataPropId, requiredVals);
			
			values.put("requirements", requirements);
		
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONObject actualRequirements = cls.getJSONRepresentation().optJSONObject("requirements");
			
			assertTrue("", actualRequirements.has(idChar+noDomainNoRangeDataPropId));
			assertTrue("", actualRequirements.optJSONArray(idChar+noDomainNoRangeDataPropId).optString(0).equals("xsd:integer"));
		
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_CreateSubclass(){
		String classId = "child_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			JSONObject values = new JSONObject();
			
			JSONArray superclasses = new JSONArray();
			
			superclasses.put(idChar+soloParentClassId);
			
			values.put("subclassOf", superclasses);
		
			OntologyClass cls = ont.createClass(classId, values);
			
			boolean hasSuper = false;
			for(OntologyClass sup : cls.getSuperClasses()){
				if(sup.getId().equals(idChar+soloParentClassId)){
					hasSuper = true;
				}
			}
			
			assertTrue("", hasSuper);
			
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_CreateDisjointSiblingclass(){
		String classId = "disjoint_child_class";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			JSONObject values = new JSONObject();
			
			JSONArray superclasses = new JSONArray();
		
			superclasses.put(idChar+parentClassId);
			
			values.put("subclassOf", superclasses);
		
			OntologyClass cls = ont.createClass(classId, values);
			
			JSONArray disjointClasses = cls.getJSONRepresentation().optJSONArray("disjoints");
			
			assertTrue(disjointClasses.length() == 1);
			assertTrue(disjointClasses.optString(0).equals(idChar+siblingClassId));
		
			ds.commit();
		}
		catch(JSONException e)
		{
			ds.abort();
		}
		finally
		{
			ds.end();	
		}
	}
	
	// READ
	
	@Test
	public void test_ReadSimple(){
		String classId = existingClassId;
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyClass cls = ont.getClass(classId);
			
			helper_testSimpleClass(cls);
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_ReadSuperclass(){
		String classId = parentClassId;
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyClass cls = ont.getClass(classId);
			
			JSONObject subclasses = cls.getJSONRepresentation().optJSONObject("subclasses");
			
			assertTrue("Parent doesn't have expected child ("+idChar+siblingClassId+")", subclasses.has(idChar+siblingClassId));
			
			JSONObject siblingJSON = ont.getClass(siblingClassId).getJSONRepresentation();
			JSONObject childJSON = subclasses.optJSONObject(idChar+siblingClassId);
			
			//assertTrue("child ("+childJSON+") doesn't match expected ("+siblingJSON+")", childJSON.equals(siblingJSON));
		}
		finally
		{
			ds.end();	
		}
	}
	
	@Test
	public void test_ReadSubclass(){
		String classId = siblingClassId;
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyClass cls = ont.getClass(classId);
			
			JSONArray superclasses =  cls.getJSONRepresentation().optJSONArray("superclasses");
			
			boolean hasParent = false;
			for(int i = 0; i < superclasses.length(); i++){
				if(superclasses.opt(i).equals(idChar+parentClassId)){
					hasParent = true;
				}
			}
			
			assertTrue("", hasParent);
		}
		finally
		{
			ds.end();	
		}
	}
	
	// UPDATE
	@Test
	public void test_UpdateRequirement(){
		String classId = updatedClassId;
		
		ds.begin(ReadWrite.WRITE);
		try
		{
		
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyClass cls = ont.getClass(classId);
			
			JSONObject oldRep = cls.getJSONRepresentation();
			
			JSONObject req = oldRep.optJSONObject("requirements");
			
			assertTrue("class to update ("+classId+") already has requirement",req.length() == 0);
			
			// Add 2 Requirements
			JSONObject newVals = new JSONObject();
			JSONObject newReq = new JSONObject();
		
			newReq.put(idChar+noDomainNoRangeDataPropId, new JSONArray("['xsd:string']"));
			newReq.put(idChar+noDomainNoRangeObjectPropId, new JSONArray("['"+idChar+rangeClassId+"']"));
			newVals.put("requirements", newReq);
	
			cls.update(newVals);
			
			JSONObject newRep = cls.getJSONRepresentation();
			
			req = newRep.optJSONObject("requirements");
			
			assertTrue("Requirements not added : "+req, req.length() == 2 && newReq.toString().equals(req.toString()));
		
			oldRep.put("requirements", newReq);
		
		
		
			// TODO: Go from 2 requirements down to 1
			
			// Remove Requirement
			newVals = new JSONObject();
			newReq = new JSONObject();
		
			newVals.put("requirements", newReq);
		
		
			cls.update(newVals);
			
			req = cls.getJSONRepresentation().optJSONObject("requirements");
		
			// TODO: COMPARE Entire JSON Object to ensure no other changes
		}
		catch(JSONException e)
		{ 
			ds.abort();
			
			fail("error with JSON Object: "+e.getMessage());
		}
		finally
		{
			ds.end();	
		}
	}
	
	//@Test
	public void test_UpdateRestriction(){
		fail("Not Yet Implemented");
	}
	
	//@Test
	public void test_UpdateSuperClass(){
		fail("Not Yet Implemented");
	}
	
	
	// DELETE
	@Test
	public void test_DeleteClass(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			OntologyClass cls = ont.getClass(deletedClassId);
			cls.delete();
			
			assertTrue("Property Doesn't Exist", !ont.classExists(deletedClassId));
			
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
			ont.getClass(deletedClassId);
			fail("Getting Deleted Class should throw Exception");
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
