package com.eduworks.ontology;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OntDocManagerVocab;
import com.hp.hpl.jena.vocabulary.RDF;

public class OntologyPolicyFile {
	
	private String fileName;
	private String directory;
	
	private Model model;

	
	// TODO: Recursively Search up Directory Hierarchy for a Policy File and use that if not one in current directory
	public OntologyPolicyFile(String directory, String name){
		this.directory = directory;
		this.fileName = name;
		
		File f;
		
		if((f = new File(directory + fileName)).exists()){
			model = RDFDataMgr.loadModel(directory + fileName);
		}else{
			model = ModelFactory.createDefaultModel();
			
			try {
				FileWriter w = new FileWriter(directory + fileName);
				model.write(w);
				w.close();
			} catch (IOException e) {
				throw new RuntimeException("Error Creating New Policy File");
			}
		}
	}
	
	public void addAltUri(String publicURI, String altURI){
		
		Resource r = model.createResource(publicURI);;
		
		if(!model.contains(r, OntDocManagerVocab.publicURI)){
			r.addProperty(RDF.type, OntDocManagerVocab.OntologySpec);
			r.addProperty(OntDocManagerVocab.publicURI, model.createResource(publicURI));
		}else{
			r.removeAll(OntDocManagerVocab.altURL);
		}
		
		r.addProperty(OntDocManagerVocab.altURL, model.createResource(altURI));
		
		try {
			FileWriter w = new FileWriter(directory + fileName);
			model.write(w);
			w.close();
		} catch (IOException e) {
			throw new RuntimeException("Error Saving after adding Alternative URI: " + altURI);
		}
	}
	
	
	public String getFullPath(){
		return directory + fileName;
	}
	
	public Model getModel(){
		return model;
	}
}
