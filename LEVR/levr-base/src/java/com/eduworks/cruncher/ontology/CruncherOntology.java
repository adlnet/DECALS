package com.eduworks.cruncher.ontology;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ContextEvent;
import com.eduworks.resolver.Cruncher;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public abstract class CruncherOntology extends Cruncher {

	static ReadWrite lastRW;
	
	static synchronized Ontology getOntology(String ontologyId, Dataset tdbDataset, Context c)
	{
		Ontology o = null;
		String cId = (String) c.get("tdbOntologyId");
		
		o = (Ontology) c.get("tdbOntology");
		if(cId != null && cId.equals(ontologyId)){
			if (o != null && !o.getJenaModel().isClosed()){
				return o;
			}
		}
		
		o = Ontology.loadOntology(tdbDataset, ontologyId);
	
		c.put("tdbOntologyId", ontologyId);
		c.put("tdbOntology", o);
		
		final Ontology ont = o;
		
		if(lastRW == ReadWrite.WRITE)
			c.onFinally(new ContextEvent()
			{
				@Override
				public void go()
				{
					ont.getJenaModel().close();
				}
			});
		
		return o;
	}

	static synchronized void clearContextData(Context c)
	{
		if (c.get("tdbOntology") != null)
		{
			Ontology o = null;
			String cId = (String) c.get("tdbOntologyId");
			
			o = (Ontology) c.get("tdbOntology");
			o.close(false);
		}
		c.remove("tdbOntology");
		c.remove("tdbOntologyId");
		
		if (c.get("tdbDataset") != null)
			try
			{
				Dataset d = null;
				d = (Dataset) c.get("tdbDataset");
				ReadWrite trw = (ReadWrite) c.get("tdbReadWrite");
				if (trw == ReadWrite.WRITE && d.isInTransaction()){
					d.commit();
					d.end();
				}
				d.close();			
			}
			catch (ClosedException ex)
			{
				ex.printStackTrace();
			}
		c.remove("tdbDataset");
		c.remove("tdbReadWrite");
	}
	
	static synchronized Dataset getDataSet(String directory, ReadWrite rw, Context c)
	{
		Dataset d = null;
		d = (Dataset) c.get("tdbDataset");
		ReadWrite trw = (ReadWrite) c.get("tdbReadWrite");
		if (d != null && trw != null && trw.equals(rw) == false)
		{
			if (trw == ReadWrite.WRITE && d.isInTransaction()){
				d.commit();
				d.end();
			}
			
			c.remove("tdbOntology");
			c.remove("tdbOntologyId");
			
			d = null;
		}
		if (d != null){
			if(!d.isInTransaction()){
				d.begin(rw);
				lastRW = rw;
				c.put("tdbReadWrite", rw);
			}
			
			return d;
		}
			
		d = Ontology.setTDBLocation(directory);
		d.begin(rw);
		lastRW = rw;
		
		final Dataset e = d;
		c.put("tdbDataset", d);
		c.put("tdbReadWrite", rw);
	
		if (rw == ReadWrite.WRITE)
			c.onSuccess(new ContextEvent()
			{
				@Override
				public void go()
				{
					if (e.isInTransaction()){
						e.commit();
						e.end();
					}
				}
			});
		if (rw == ReadWrite.WRITE)
			c.onFailure(new ContextEvent()
			{
				@Override
				public void go()
				{
					if (e.isInTransaction())
						e.abort();
				}
			});
		if (rw == ReadWrite.WRITE)
			c.onFinally(new ContextEvent()
			{
				@Override
				public void go()
				{
					e.close();
				}
			});
		return e;
	}

}
