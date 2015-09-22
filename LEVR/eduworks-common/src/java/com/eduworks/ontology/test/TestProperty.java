package com.eduworks.ontology.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyProperty;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

public class TestProperty {

	public static final String localDirectory = "/Users/djunker/Java/etc/test-java-competencies/tdb";
	
	public static String idChar = Ontology.idCharacter;
	
	// Ontology to hold properties we are testing
	public static String testOntologyName = "test-property";
	
	public static String domainClassId = "domain_class";
	public static String intersectingDomainClassId = "interesecting_domain_class";
	
	public static String rangeClassId = "range_class";
	
	public static String existingDataPropertyId = "existing_data_property";
	public static String existingDataSubPropertyId = "existing_data_subproperty";
	public static String existingObjectPropertyId = "existing_object_property";
	public static String existingObjectSubPropertyId = "existing_object_subproperty";

	public static String existingInversePropertyId = "existing_inverse_property";
	
	public static String updateDataPropertyId = "update_data_property";
	public static String updateObjectPropertyId = "update_object_property";
	
	public static String deletePropertyId = "delete_property";
	
	public static String nonexistentPropertyId = "nonexistent_property";
	
	// Object Properties with unspecified domain/range will always have domain/range of Thing and Resource
	public static int baseObjectDomainLength = 2;
	public static int baseObjectRangeLength = 2;
	
	public static Dataset ds;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
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
			
			// Create Classes for Domains and Object Properties
			ont.createClass(domainClassId, new JSONObject());
			ont.createClass(intersectingDomainClassId, new JSONObject());
			ont.createClass(rangeClassId, new JSONObject());
			
			assertTrue(ont.getClassIdList().contains(idChar+domainClassId));
			assertTrue(ont.getClassIdList().contains(idChar+intersectingDomainClassId));
			assertTrue(ont.getClassIdList().contains(idChar+rangeClassId));
			
			// Create Properties to be used during tests
			ont.createDataProperty(existingDataPropertyId, new JSONObject());
			ont.createDataProperty(updateDataPropertyId, new JSONObject());
			ont.createDataProperty(deletePropertyId, new JSONObject());
			
			ont.createObjectProperty(existingObjectPropertyId, new JSONObject());
			ont.createObjectProperty(updateObjectPropertyId, new JSONObject());
			ont.createObjectProperty(existingInversePropertyId, new JSONObject());
			
			JSONObject values = new JSONObject();
			
			try{
				values.put("subpropertyOf", idChar+existingDataPropertyId);
			}catch(JSONException e){}
			OntologyProperty dataSubProp = ont.createDataProperty(existingDataSubPropertyId, values);
			
			try{
				values.put("subpropertyOf", idChar+existingObjectPropertyId);
			}catch(JSONException e){}
			OntologyProperty objectSubProp = ont.createObjectProperty(existingObjectSubPropertyId, values);
			
			assertTrue(dataSubProp.getJSONRepresentation().optJSONArray("superproperties").toString().contains(existingDataPropertyId));
			assertTrue(objectSubProp.getJSONRepresentation().optJSONArray("superproperties").toString().contains(existingObjectPropertyId));
			
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
			
			assertTrue(ont.getDataPropertyIdList().contains(idChar+existingDataPropertyId));
			assertTrue(ont.getDataPropertyIdList().contains(idChar+existingDataSubPropertyId));
			assertTrue(ont.getObjectPropertyIdList().contains(idChar+existingInversePropertyId));
			assertTrue(ont.getObjectPropertyIdList().contains(idChar+existingObjectPropertyId));
			assertTrue(ont.getObjectPropertyIdList().contains(idChar+existingObjectSubPropertyId));
		}
		finally
		{
			ds.end();
		}
		
	}

	@AfterClass
	public static void tearDownAfterClass() {
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			System.out.println("Updated Data Property Values: ");
			System.out.println(ont.getProperty(updateDataPropertyId).getJSONRepresentation());
			System.out.println();
			
			System.out.println("Updated Object Property Values: ");
			System.out.println(ont.getProperty(updateObjectPropertyId).getJSONRepresentation());
		}
		finally
		{
			ds.end();
		}
		
		// Removed this so I could inspect the ontology after the test, may want to leave commented out 
		//ont.delete();
	}

	/**
	 * FAILING TESTS
	 */
	
	
	/**
	 * PASSING TESTS
	 */
	
	// CREATE
	// " " " DATA PROPS
	
	@Test
	public void test_CreateSimpleDataProperty(){
		String propId = "data_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			OntologyProperty prop = ont.createDataProperty(propId, new JSONObject());
			
			Set<String> dataPropIds = ont.getDataPropertyIdList();
			
			assertTrue("Check Contained in Data Property Id List", dataPropIds.contains(idChar + propId));
			
			helper_testSimpleDataProperty(prop);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}
	
	public void helper_testSimpleDataProperty(OntologyProperty prop){
		assertTrue("Check Domain", prop.getDomain().length() == 0);
		assertTrue("Check Range", prop.getRange().length() == 0);
		
		JSONObject representation = prop.getJSONRepresentation();
		
		assertTrue("Data Property JSON Representation has Domain", representation.has("domain"));
		assertTrue("Data Property JSON Representation has Range", representation.has("range"));
		assertTrue("Data Property JSON Representation has Superproperties", representation.has("superproperties"));
		assertTrue("Data Property JSON Representation has Subproperties", representation.has("subproperties"));
		assertTrue("Data Property JSON Representation has Functional Flag", representation.has("functional") && !representation.optBoolean("functional"));
	}
	
	@Test
	public void test_CreateDomainedDataProperty(){
		String propId = "domained_data_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject values = new JSONObject();
		
			values.put("domain", domainClassId);
	
			OntologyProperty prop = ont.createDataProperty(propId, values);
			
			assertTrue("Data Property has Domain_Class in Domain", prop.getDomain().toString().contains(domainClassId));
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateIntersectDomainedDataProperty(){
		String propId = "intersected_domained_data_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject values = new JSONObject();
		
			values.append("domain", domainClassId);
			values.append("domain", intersectingDomainClassId);
			
			
			OntologyProperty prop = ont.createDataProperty(propId, values);
			
			assertTrue("Data Property has Domain_Class in Domain", prop.getDomain().toString().contains(domainClassId));
			assertTrue("Data Property has Intersecting_Domain_Class in Domain", prop.getDomain().toString().contains(intersectingDomainClassId));
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateFunctionalDataProperty(){
		String propertyId = "functional_data_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject values = new JSONObject();
		
			values.put("functional", "true");
		
			OntologyProperty prop = ont.createDataProperty(propertyId, values);
		
			assertTrue("Property is Functional", prop.getJSONRepresentation().getBoolean("functional"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateStringDataProperty() {
		String propertyId = "string_Prop";
		String propertyRange = "xsd:string";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("range", propertyRange);
		
		
			OntologyProperty prop = ont.createDataProperty(propertyId, values);
		
			// Should be created and accessible here
		
			JSONArray range = prop.getRange();
			
			assertTrue("Check Range Length", range.length() == 1);
			assertEquals("Check Range is String", range.get(0), propertyRange);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateSimpleParentChildDataProperties(){
		String parentId = "parent_data_prop";
		String childId = "child_data_prop";
		

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty parent = ont.createDataProperty(parentId, new JSONObject());
			
			JSONObject childVals = new JSONObject();
		
		
			childVals.put("subpropertyOf", idChar+parentId);
		
		
			OntologyProperty child = ont.createDataProperty(childId, childVals);
			
			assertTrue("Check Parent only has One SubProperty", parent.getSubProperties().size() == 1);
			assertTrue("Check Child is Subproperty of Parent", helper_checkSubProps(parent.getSubProperties(), child));
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
	}

	public boolean helper_checkSubProps(Set<OntologyProperty> subProps, OntologyProperty prop){
		for(OntologyProperty p : subProps){
			if(p.getId().equals(prop.getId())){
				return true;
			}
		}
		
		return false;
	}
	
	@Test
	public void test_CreateSubPropertyOfDomainedDataProperty(){
		String parentId = "domained_data_prop_parent";
		String childId = "domained_data_prop_child";
		

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("domain", idChar+domainClassId);
		
		
			OntologyProperty parent = ont.createDataProperty(parentId, values);
		
			values = new JSONObject();
		
			values.put("subpropertyOf", idChar+parentId);
		
		
			OntologyProperty child = ont.createDataProperty(childId, values);
		
			JSONArray childDom = child.getDomain();
			JSONArray parentDom = parent.getDomain();
			Set<String> childSet = new HashSet<String>();
			Set<String> parentSet = new HashSet<String>();
			
			assertTrue("Check Child Domain Length", childDom.length() == parentDom.length());
			for(int i = 0; i < childDom.length(); i++){
				childSet.add(childDom.optString(i));
				parentSet.add(parentDom.optString(i));
			}
			assertTrue("Check Child Domain", childSet.equals(parentSet));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateSubPropertyOfRangedDataProperty(){
		String parentId = "ranged_data_prop_parent";
		String childId = "ranged_data_prop_child";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject vals = new JSONObject();
		
		
			vals.put("range", "xsd:string");
		
			OntologyProperty parent = ont.createDataProperty(parentId, vals);
		
			vals = new JSONObject();
		
			vals.put("subpropertyOf", idChar+parentId);
		
		
			OntologyProperty child = ont.createDataProperty(childId, vals);
		
			assertTrue("Check Range Length of Child", child.getRange().length() == parent.getRange().length());
		
			assertTrue("Check Range of Child", child.getRange().get(0).equals(parent.getRange().get(0)));

			ds.commit();
		}
		catch (JSONException e)
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
	/**
	 * Subproperties WONT inherit the functional flag from their parent
	 */
	public void test_CreateSubPropertyOfFunctionalDataProperty(){
		String parentId = "functional_data_prop_parent";
		String childId = "functional_data_prop_child";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("functional", "true");
		
		
			ont.createDataProperty(parentId, values);
		
			values = new JSONObject();
		
			values.put("subpropertyOf", idChar+parentId);
		
			OntologyProperty child = ont.createDataProperty(childId, values);
		
			assertTrue("Child Property is not Functional "+child.getJSONRepresentation(), !child.getJSONRepresentation().getBoolean("functional"));
			
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
	}
	
	// " " " OBJ PROPS
	
	@Test
	public void test_CreateSimpleObjectProperty(){
		String propId = "obj_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			OntologyProperty prop = ont.createObjectProperty(propId, new JSONObject());
		
			Set<String> objectPropIds = ont.getObjectPropertyIdList();
			
			assertTrue("Check Contained in Object Property Id List", objectPropIds.contains(idChar + propId));
			
			helper_testSimpleObjectProperty(prop);
		
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}
	
	public void helper_testSimpleObjectProperty(OntologyProperty prop){
		assertTrue("Check Domain", prop.getDomain().length() == baseObjectDomainLength);
		assertTrue("Check Range", prop.getRange().length() == baseObjectRangeLength);
		
		JSONObject representation = prop.getJSONRepresentation();
		
		assertTrue("Data Property JSON Representation has Domain", representation.has("domain"));
		assertTrue("Data Property JSON Representation has Range", representation.has("range"));
		assertTrue("Data Property JSON Representation has Superproperties", representation.has("superproperties"));
		assertTrue("Data Property JSON Representation has No Subproperties", representation.has("subproperties"));
		assertTrue("Data Property JSON Representation has No Inverses "+representation.optString("inverse"), representation.has("inverse") && representation.optString("inverse").isEmpty());
		
		assertTrue("Data Property JSON Representation has Functional Flag", representation.has("functional") && !representation.optBoolean("functional"));
		assertTrue("Data Property JSON Representation has Inverse-Functional Flag", representation.has("inverse-functional") && !representation.optBoolean("inverse-functional"));
		
		assertTrue("Data Property JSON Representation has Symmetric Flag", representation.has("symmetric") && !representation.optBoolean("symmetric"));
		assertTrue("Data Property JSON Representation has Asymmetric Flag", representation.has("asymmetric") && !representation.optBoolean("asymmetric"));
		
		assertTrue("Data Property JSON Representation has Reflexive Flag", representation.has("reflexive") && !representation.optBoolean("reflexive"));
		assertTrue("Data Property JSON Representation has Irreflexive Flag", representation.has("irreflexive") && !representation.optBoolean("irreflexive"));
		
		assertTrue("Data Property JSON Representation has Transitive Flag", representation.has("transitive") && !representation.optBoolean("transitive"));
	}
	
	@Test
	public void test_CreateDomainedObjectProperty(){
		String propId = "domained_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			
			JSONObject values = new JSONObject();
		
			values.put("domain", domainClassId);
		
		
			OntologyProperty prop = ont.createObjectProperty(propId, values);
		
			assertTrue("Object Property has Domain_Class in Domain", prop.getDomain().toString().contains(domainClassId));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateIntersectedDomainObjectProperty(){
		String propId = "interesected_domained_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.append("domain", domainClassId);
			values.append("domain", intersectingDomainClassId);
		
		
			OntologyProperty prop = ont.createObjectProperty(propId, values);
		
			assertTrue("Object Property has Domain_Class in Domain", prop.getDomain().toString().contains(domainClassId));
			assertTrue("Object Property has Intersecting_Domain_Class in Domain", prop.getDomain().toString().contains(intersectingDomainClassId));
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
		// TODO: Test with Instances of Class
	}
	
	@Test
	public void test_CreateRangedObjectProperty(){
		String propId = "ranged_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("range", rangeClassId);
		
			OntologyProperty prop = ont.createObjectProperty(propId, values);
		
			assertTrue("Object Property has Domain_Class in Domain", prop.getDomain().toString().contains(rangeClassId));
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateFunctionalObjectProperty(){
		String propertyId = "functional_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			JSONObject values = new JSONObject();
		
			values.put("functional", "true");
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Functional", prop.getJSONRepresentation().getBoolean("functional"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateInverseFunctionalObjectProperty(){
		String propertyId = "inverse_functional_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("inverse-functional", "true");
		
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Functional", prop.getJSONRepresentation().getBoolean("inverse-functional"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateSymmetricObjectProperty(){
		String propertyId = "symmetric_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("symmetric", "true");
	
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Symmetric", prop.getJSONRepresentation().getBoolean("symmetric"));
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
		// TODO: Test with Instances
	}
	
	@Test
	public void test_CreateAsymmetricObjectProperty(){
		String propertyId = "asymmetric_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("asymmetric", "true");
		
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Asymmetric", prop.getJSONRepresentation().getBoolean("asymmetric"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateReflexiveObjectProperty(){
		String propertyId = "reflexive_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("reflexive", "true");
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Reflexive", prop.getJSONRepresentation().getBoolean("reflexive"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateIrreflexiveObjectProperty(){
		String propertyId = "irreflexive_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("irreflexive", "true");
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Irreflexive", prop.getJSONRepresentation().getBoolean("irreflexive"));
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_CreateTransitiveObjectProperty(){
		String propertyId = "transitive_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);	
		
			JSONObject values = new JSONObject();
		
			values.put("transitive", "true");
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("Property is Transitive", prop.getJSONRepresentation().getBoolean("transitive"));
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
		// TODO: Test With Instances
	}
	
	@Test
	public void test_CreateInverseObjectProperties(){
		String propertyId = "new_inverse_object_prop";
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
		
			JSONObject values = new JSONObject();
		
			values.put("inverses", idChar+existingInversePropertyId);
		
		
			OntologyProperty prop = ont.createObjectProperty(propertyId, values);
		
			assertTrue("New Property has Inverse "+prop.getJSONRepresentation().optString("inverse"), prop.getJSONRepresentation().optString("inverse").contains(existingInversePropertyId));
		
		
			OntologyProperty otherProp = ont.getProperty(existingInversePropertyId);
		
			assertTrue("Existing Property has Inverse"+otherProp.getJSONRepresentation(), otherProp.getJSONRepresentation().optString("inverse").contains(propertyId));
			

			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
		
		// TODO: Test With Instances
	}
	
	// READ
	
	@Test
	public void test_PropertyExists(){
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			assertTrue(ont.propertyExists(existingDataPropertyId));
			assertTrue(!ont.propertyExists(nonexistentPropertyId));
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_ReadProperty(){
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(existingDataPropertyId);
		
			helper_testSimpleDataProperty(prop);
		
			prop = ont.getProperty(existingObjectPropertyId);
		
			helper_testSimpleObjectProperty(prop);
		}
		finally
		{
			ds.end();
		}	
			
	}
	
	@Test
	public void test_ReadAllProperties(){
			
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			Map<String, OntologyProperty> propertyHierarchy = ont.getAllProperties();
	
			assertTrue("Property Hierarchy Top Level Contains Existing_Data_Property", propertyHierarchy.containsKey(idChar+existingDataPropertyId));
			
			OntologyProperty prop = propertyHierarchy.get(idChar+existingDataPropertyId);
			assertTrue("Existing_Data_Property has Subproperty", prop.getSubProperties().size() == 1);
			assertTrue("Existing_Data_Property has Correct Subproperty", prop.getSubProperties().iterator().next().getId().contains(existingDataSubPropertyId));
			
			
			assertTrue("Property Hierarchy Top Level Contains Existing_Object_Property", propertyHierarchy.containsKey(idChar+existingObjectPropertyId));
			
			prop = propertyHierarchy.get(idChar+existingObjectPropertyId);
			assertTrue("Existing_Object_Property has Subproperty", prop.getSubProperties().size() == 1);
			assertTrue("Existing_Object_Property has Correct Subproperty", prop.getSubProperties().iterator().next().getId().contains(existingObjectSubPropertyId));
			
			assertTrue("Property Hierarchy Top Level Contains Existing_Object_Property", propertyHierarchy.containsKey(idChar+existingInversePropertyId));
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_ReadAllDataPropertyIds(){
		
		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			Set<String> dataPropIds = ont.getDataPropertyIdList();
			
			assertTrue("Data Properties Contains Existing Data Property", dataPropIds.contains(idChar+existingDataPropertyId));
			assertTrue("Data Properties Contains Existing Data Subproperty", dataPropIds.contains(idChar+existingDataSubPropertyId));	
		}
		finally
		{
			ds.end();
		}
	}
	
	@Test
	public void test_ReadAllObjectPropertyIds(){

		ds.begin(ReadWrite.READ);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			Set<String> objectPropIds = ont.getObjectPropertyIdList();
			
			assertTrue("Data Properties Contains Existing Object Property", objectPropIds.contains(idChar+existingObjectPropertyId));
			assertTrue("Data Properties Contains Existing Object SubProperty", objectPropIds.contains(idChar+existingObjectSubPropertyId));
			assertTrue("Data Properties Contains Existing Inverse Property", objectPropIds.contains(idChar+existingInversePropertyId));
		}
		finally
		{
			ds.end();
		}
	}
	
	// UPDATE
	
	public void helper_compareAfterUpdate(JSONObject newVals, JSONObject oldRep, JSONObject newRep){
		@SuppressWarnings("unchecked")
		Iterator<String> keys = newVals.keys();
		while(keys.hasNext()){
			String key = keys.next();
			
			assertTrue("Updated Value of <"+key+"> ("+newRep.opt(key)+") Different from Old Value ("+oldRep.opt(key)+") ", !oldRep.opt(key).equals(newRep.opt(key)));
			
			if(key.equals("domain") || key.equals("range")){
				if(newVals.opt(key) instanceof JSONArray){
					JSONArray vals = newVals.optJSONArray(key);
					for(int i = 0; i < vals.length(); i++){
						assertTrue("Updated Value of <"+key+"> ("+newRep.opt(key)+") Contains New Value ("+newVals.opt(key)+")", newRep.opt(key).toString().contains(vals.optString(i)));
					}
				}else{
					assertTrue("Updated Value of <"+key+"> ("+newRep.opt(key)+") Contains New Value ("+newVals.opt(key)+")", newRep.opt(key).toString().contains(newVals.optString(key)));
				}		
			}else{
				assertTrue("Updated Value of <"+key+"> ("+newRep.opt(key)+") Equals New Value ("+newVals.opt(key)+")", newVals.optString(key).equals(newRep.optString(key)));
			}
		}
	}
	
	public void helper_compareAfterRemove(JSONObject removedVals, JSONObject newRep){
		@SuppressWarnings("unchecked")
		Iterator<String> keys = removedVals.keys();
		while(keys.hasNext()){
			String key = keys.next();
			
			if(key.equals("domain")){
				if(newRep.has("inverse-functional")){
					assertTrue("Value of <"+key+"> ("+newRep.opt(key)+") Deleted",  newRep.optJSONArray(key).length() == baseObjectDomainLength);
				}else{
					assertTrue("Value of <"+key+"> ("+newRep.opt(key)+") Deleted",  newRep.optJSONArray(key).length() == 0);
				}
			}else if(key.equals("range")){
				if(newRep.has("inverse-functional")){
					assertTrue("Value of <"+key+"> ("+newRep.opt(key)+") Deleted",  newRep.optJSONArray(key).length() == baseObjectRangeLength);
				}else{
					assertTrue("Value of <"+key+"> ("+newRep.opt(key)+") Deleted",  newRep.optJSONArray(key).length() == 0);
				}
			}else{
				assertTrue("Value of <"+key+"> ("+newRep.opt(key)+") Deleted",  newRep.optString(key).isEmpty());
			}
			
		}
	}

	public void generic_UpdateNothing(String propertyId){

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(propertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			JSONObject newVals = new JSONObject();
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}
	
	public void generic_UpdatePropertyDomain(String propertyId){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(propertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Add Domain
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("domain", domainClassId);
		
		
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Domain
		
			newVals = new JSONObject();
		
			newVals.append("domain", "");
		
			
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
			oldRep = newRep;
			
			// Add Intersecting Domains
			
			newVals = new JSONObject();
			
			newVals.append("domain", domainClassId);
			newVals.append("domain", intersectingDomainClassId);
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Intersecting Domains
		
			newVals = new JSONObject();
		
			newVals.append("domain", "");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
	}

	public void generic_UpdateFunctional(String propertyId){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(propertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("functional", "true");
		
			prop.update(newVals);
		
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("functional", "false");
		
			
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
		
			ds.commit();
		}
		catch (JSONException e)
		{
			ds.abort();
			fail(e.getMessage());
		}
		finally
		{
			ds.end();
		}
	}

	public void generic_UpdateSuperProperty(String propertyId){
			
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(propertyId);
			
			String parentPropId;
			if(prop.isDataProperty()){
				parentPropId = existingDataSubPropertyId;
			}else{
				parentPropId = existingObjectSubPropertyId;
			}
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Add SuperProperty
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("subpropertyOf", parentPropId);
		
		
			prop.update(newVals);
		
		
			newVals.put("superproperties", newVals.remove("subpropertyOf"));
		
		
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			// Check that GrandParent is in SuperProperty Array
			
			oldRep = newRep;
			
			// Remove SuperProperty
			
			newVals = new JSONObject();
		
			newVals.put("subpropertyOf", "");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateDataPropertyNothing(){
		generic_UpdateNothing(updateDataPropertyId);
	}
	
	@Test
	public void test_UpdateDataPropertyDomain(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateDataPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Add Domain
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("domain", domainClassId);
			
			
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Domain
			
			newVals = new JSONObject();
		
			newVals.append("domain", "");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
			oldRep = newRep;
			
			// Add Intersecting Domains
			
			newVals = new JSONObject();
		
			newVals.append("domain", domainClassId);
			newVals.append("domain", intersectingDomainClassId);
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Intersecting Domains
			
			newVals = new JSONObject();
		
			newVals.append("domain", "");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateDataPropertyRange(){
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateDataPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Add String Range
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("range", "xsd:string");
		
		
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Change to Integer Range
			
			newVals = new JSONObject();
		
			newVals.put("range", "xsd:integer");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			// Remove Range
			
			newVals = new JSONObject();
		
			newVals.put("range", "");
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			
			helper_compareAfterRemove(newVals, newRep);
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateDataPropertySuperProperty(){
		generic_UpdateSuperProperty(updateDataPropertyId);
	}
	
	@Test
	public void test_UpdateDataPropertyFunctional(){
		generic_UpdateFunctional(updateDataPropertyId);
	}
	
	@Test
	public void test_UpdateObjectPropertyNothing(){
		generic_UpdateNothing(updateObjectPropertyId);
	}
	
	@Test
	public void test_UpdateObjectPropertyDomain(){
		generic_UpdatePropertyDomain(updateObjectPropertyId);
	}
	
	@Test
	public void test_UpdateObjectPropertyRange(){
		fail("Not Implemented");
	}
	
	@Test
	public void test_UpdateObjectPropertySuperProperty(){
		generic_UpdateSuperProperty(updateObjectPropertyId);
	}
	
	@Test
	public void test_UpdateObjectPropertyInverse(){
		fail("Not Implemented");	
	}
	
	@Test
	public void test_UpdateObjectPropertyFunctional(){
		generic_UpdateFunctional(updateObjectPropertyId);
	}
	
	@Test
	public void test_UpdateObjectPropertyInverseFunctional(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("inverse-functional", "true");
		
			
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("inverse-functional", "false");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateObjectPropertySymmetric(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("symmetric", "true");
				
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("symmetric", "false");
	
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateObjectPropertyAsymmetric(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("asymmetric", "true");
		
		
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("asymmetric", "false");
		
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateObjectPropertyReflexive(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("reflexive", "true");
		
		
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("reflexive", "false");
		
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
		
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateObjectPropertyIrreflexive(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("irreflexive", "true");
		
		
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("irreflexive", "false");
		
			prop.update(newVals);
		
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_UpdateObjectPropertyTransitive(){
		
		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(updateObjectPropertyId);
			
			JSONObject oldRep = prop.getJSONRepresentation();
			
			// Set Functional
			
			JSONObject newVals = new JSONObject();
		
			newVals.put("transitive", "true");
		
			
			prop.update(newVals);
			
			JSONObject newRep = prop.getJSONRepresentation();
			
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			oldRep = newRep;
			
			// Remove Functional
			
			newVals = new JSONObject();
		
			newVals.put("transitive", "false");
		
			
			prop.update(newVals);
			
			newRep = prop.getJSONRepresentation();
			helper_compareAfterUpdate(newVals, oldRep, newRep);
			
			ds.commit();
		}
		catch (JSONException e)
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
	public void test_DeleteProperty(){

		ds.begin(ReadWrite.WRITE);
		try
		{
			Ontology ont = Ontology.loadOntology(ds, testOntologyName);
			OntologyProperty prop = ont.getProperty(deletePropertyId);
			
			prop.delete();
			
			assertTrue("Property Doesn't Exist", !ont.propertyExists(deletePropertyId));
		
			ds.commit();
		}
		finally
		{
			ds.end();
		}
	}

}
