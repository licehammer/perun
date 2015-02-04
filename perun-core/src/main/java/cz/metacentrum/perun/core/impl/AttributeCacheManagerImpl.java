/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.metacentrum.perun.core.impl;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.AttributeHolders;
import cz.metacentrum.perun.core.api.Facility;
import cz.metacentrum.perun.core.api.PerunBean;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.implApi.AttributeCacheManagerImplApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author Katarína Hrabovská <katarina.hrabovska1992@gmail.com>
 */
public class AttributeCacheManagerImpl implements AttributeCacheManagerImplApi{

	public static final Object entityForAttributes = new Object();
	private Map<Object,Map<String,AttributeDefinition>> applicationCache;

	public AttributeCacheManagerImpl() {
		applicationCache = new ConcurrentHashMap<Object,Map<String,AttributeDefinition>>();
	}

	public Map<Object,Map<String,AttributeDefinition>> getApplicationCache() {
		return Collections.unmodifiableMap(applicationCache);
	}

	public void flushCache() {
		applicationCache.clear();
	}


	public synchronized void addAttributeToCache(AttributeHolders attributeHolders, Attribute attribute) {
		if (applicationCache.get(attributeHolders)!=null) {
			applicationCache.get(attributeHolders).put(attribute.getName(), attribute);
		}
		else {
			Map<String,AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
			mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
			applicationCache.put(attributeHolders, mapOfAttributeHoldersAttributes);
		}
	}

	public void addAttributeToCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, Attribute attribute) {
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object, Map<String, AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			if (actionsInTransaction.get(attributeHolders)!=null) {
				actionsInTransaction.get(attributeHolders).put(attribute.getName(), attribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
				actionsInTransaction.put(attributeHolders, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.addAttributeToCache(attributeHolders, attribute);
		}
	}

	public void addAttributeToCacheInTransaction(PerunBean primaryHolder, Attribute attribute){
		this.addAttributeToCacheInTransaction(primaryHolder, null, attribute);
	}

	public synchronized void addAttributeToCacheForString(String key, Attribute attribute) {
		if (applicationCache.get(key)!=null) {
			applicationCache.get(key).put(attribute.getName(), attribute);
		}
		else {
			Map<String,AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
			mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
			applicationCache.put(key, mapOfAttributeHoldersAttributes);
		}
	}

	public void addAttributeToCacheForStringInTransaction(String key, Attribute attribute) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object, Map<String, AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			if (actionsInTransaction.get(key)!=null) {
				actionsInTransaction.get(key).put(attribute.getName(), attribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
				actionsInTransaction.put(key, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.addAttributeToCacheForString(key, attribute);
		}
	}

	public synchronized void addAttributeToCacheForAttributes(AttributeDefinition attribute) {
		if (applicationCache.get(entityForAttributes)!=null) {
			applicationCache.get(entityForAttributes).put(attribute.getName(), attribute);
		}
		else {
			Map<String,AttributeDefinition> mapOfAttributes = new HashMap<>();
			mapOfAttributes.put(attribute.getName(), attribute);
			applicationCache.put(entityForAttributes, mapOfAttributes);
		}
	}

	public void addAttributeToCacheForAttributesInTransaction(AttributeDefinition attribute) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object, Map<String, AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			if (actionsInTransaction.get(entityForAttributes)!=null) {
				actionsInTransaction.get(entityForAttributes).put(attribute.getName(), attribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributes = new HashMap<>();
				mapOfAttributes.put(attribute.getName(), attribute);
				actionsInTransaction.put(entityForAttributes, mapOfAttributes);
			}
		} else {
			this.addAttributeToCacheForAttributes(attribute);
		}
	}


	public void removeAttributeFromCache(AttributeHolders attributeHolders, AttributeDefinition attribute) {
		if (applicationCache.get(attributeHolders)!=null) {
			applicationCache.get(attributeHolders).remove(attribute.getName());
		}
	}

	public void removeAttributeFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, AttributeDefinition attribute) {
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			Attribute newAttribute = new Attribute(attribute);
			if (actionsInTransaction.get(attributeHolders)!=null) {
				actionsInTransaction.get(attributeHolders).put(attribute.getName(), newAttribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				mapOfAttributeHoldersAttributes.put(attribute.getName(), newAttribute);
				actionsInTransaction.put(attributeHolders, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.removeAttributeFromCache(attributeHolders, attribute);
		}
	}

	public void removeAttributeFromCacheInTransaction(PerunBean primaryHolder, AttributeDefinition attribute) {
		this.removeAttributeFromCacheInTransaction(primaryHolder, null, attribute);
	}

	public void removeAttributeFromCacheForString(String key, AttributeDefinition attribute) {
		if (applicationCache.get(key)!=null) {
			applicationCache.get(key).remove(attribute.getName());
		}
	}

	public void removeAttributeFromCacheForStringInTransaction(String key, AttributeDefinition attribute) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			Attribute newAttribute = new Attribute(attribute);
			if (actionsInTransaction.get(key)!=null) {
				actionsInTransaction.get(key).put(attribute.getName(), newAttribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				mapOfAttributeHoldersAttributes.put(attribute.getName(), newAttribute);
				actionsInTransaction.put(key, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.removeAttributeFromCacheForString(key, attribute);
		}
	}

	public void removeAllAttributesFromCache(AttributeHolders attributeHolders) {
		if (applicationCache.get(attributeHolders)!=null) {
			applicationCache.get(attributeHolders).clear();
		}
	}

	public void removeAllAttributesFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder) {
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			List<AttributeDefinition> allAttributesOfAttributeHoldersInCache = new ArrayList<>();
			allAttributesOfAttributeHoldersInCache.addAll(this.getAllAttributesFromCache(attributeHolders));
			if (actionsInTransaction.get(attributeHolders)!=null) {
				for (AttributeDefinition attributeDef: allAttributesOfAttributeHoldersInCache) {
					Attribute attribute = (Attribute) attributeDef;
					attribute.setValue(null);
					actionsInTransaction.get(attributeHolders).put(attribute.getName(), attribute);
				}
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				for (AttributeDefinition attributeDef: allAttributesOfAttributeHoldersInCache) {
					Attribute attribute = (Attribute) attributeDef;
					attribute.setValue(null);
					mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
				}
				actionsInTransaction.put(attributeHolders, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.removeAllAttributesFromCache(attributeHolders);
		}
	}

	public void removeAllAttributesFromCacheInTransaction(PerunBean primaryHolder) {
		this.removeAllAttributesFromCacheInTransaction(primaryHolder, null);
	}

	public void removeAllUserFacilityAttributesForAnyUserFromCache(PerunBean secondaryHolder) {
		for (Object object: applicationCache.keySet()) {
			if (object instanceof AttributeHolders) {
				AttributeHolders attributeHolders = (AttributeHolders) object;
				if (attributeHolders.getSecondary()!=null) {
					boolean rightEquals = ((attributeHolders.getSecondary().equals(secondaryHolder)) && (attributeHolders.getPrimary() instanceof User));
					boolean reverseEquals = ((attributeHolders.getPrimary().equals(secondaryHolder)) && (attributeHolders.getSecondary() instanceof User));
					if  (rightEquals || reverseEquals) {
						this.removeAllAttributesFromCache(attributeHolders);
					}
				}
			}
		}
	}

	public void removeAllUserFacilityAttributesForAnyUserFromCacheInTransaction(PerunBean secondaryHolder) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			for (Object object: applicationCache.keySet()) {
				if (object instanceof AttributeHolders) {
					AttributeHolders attributeHolders = (AttributeHolders) object;
					if (attributeHolders.getSecondary()!=null) {
						boolean rightEquals = ((attributeHolders.getSecondary().equals(secondaryHolder)) && (attributeHolders.getPrimary() instanceof User));
						boolean reverseEquals = ((attributeHolders.getPrimary().equals(secondaryHolder)) && (attributeHolders.getSecondary() instanceof User));
						if (rightEquals || reverseEquals) {
							if (actionsInTransaction.get(attributeHolders)!=null) {
								for (AttributeDefinition attributeDef: applicationCache.get(attributeHolders).values()) {
									Attribute attribute = (Attribute) attributeDef;
									attribute.setValue(null);
									actionsInTransaction.get(attributeHolders).put(attribute.getName(), attribute);
								}
							}
							else {
								Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
								for (AttributeDefinition attributeDef: applicationCache.get(attributeHolders).values()) {
									Attribute attribute = (Attribute) attributeDef;
									attribute.setValue(null);
									mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
								}
								actionsInTransaction.put(attributeHolders, mapOfAttributeHoldersAttributes);
							}
						}
					}
				}
			}
		} else {
			this.removeAllUserFacilityAttributesForAnyUserFromCache(secondaryHolder);
		}
	}

	public void removeAllUserFacilityAttributesFromCache(PerunBean primaryHolder) {
		for (Object object: applicationCache.keySet()) {
			if (object instanceof AttributeHolders) {
				AttributeHolders attributeHolders = (AttributeHolders) object;
				if (attributeHolders.getSecondary()!=null) {
					boolean rightEquals = ((attributeHolders.getPrimary().equals(primaryHolder)) && (attributeHolders.getSecondary() instanceof Facility));
					boolean reverseEquals = ((attributeHolders.getSecondary().equals(primaryHolder)) && (attributeHolders.getPrimary() instanceof Facility));
					if  (rightEquals || reverseEquals) {
						this.removeAllAttributesFromCache(attributeHolders);
					}
				}
			}
		}
	}

	public void removeAllUserFacilityAttributesFromCacheInTransaction(PerunBean primaryHolder) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			for (Object object: applicationCache.keySet()) {
				if (object instanceof AttributeHolders) {
					AttributeHolders attributeHolders = (AttributeHolders) object;
					if (attributeHolders.getSecondary()!=null) {
						boolean rightEquals = ((attributeHolders.getPrimary().equals(primaryHolder)) && (attributeHolders.getSecondary() instanceof Facility));
						boolean reverseEquals = ((attributeHolders.getSecondary().equals(primaryHolder)) && (attributeHolders.getPrimary() instanceof Facility));
						if (rightEquals || reverseEquals) {
							if (actionsInTransaction.get(attributeHolders)!=null) {
								for (AttributeDefinition attributeDef: applicationCache.get(attributeHolders).values()) {
									Attribute attribute = (Attribute) attributeDef;
									attribute.setValue(null);
									actionsInTransaction.get(attributeHolders).put(attribute.getName(), attribute);
								}
							}
							else {
								Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
								for (AttributeDefinition attributeDef: applicationCache.get(attributeHolders).values()) {
									Attribute attribute = (Attribute) attributeDef;
									attribute.setValue(null);
									mapOfAttributeHoldersAttributes.put(attribute.getName(), attribute);
								}
								actionsInTransaction.put(attributeHolders, mapOfAttributeHoldersAttributes);
							}
						}
					}
				}
			}
		} else {
			this.removeAllUserFacilityAttributesFromCache(primaryHolder);
		}
	}

	public void removeAttributeFromCacheForAttributes(AttributeDefinition attribute) {
		if (applicationCache.get(entityForAttributes)!=null) {
			applicationCache.get(entityForAttributes).remove(attribute.getName());
		}
	}

	public void removeAttributeFromCacheForAttributesInTransaction(AttributeDefinition attribute) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if(actionsInTransaction == null) {
				actionsInTransaction = new HashMap<Object,Map<String,AttributeDefinition>>();
				TransactionSynchronizationManager.bindResource(this, actionsInTransaction);
			}
			Attribute newAttribute = new Attribute(attribute);
			newAttribute.setValue(null);
			if (actionsInTransaction.get(entityForAttributes)!=null) {
				actionsInTransaction.get(entityForAttributes).put(attribute.getName(), newAttribute);
			}
			else {
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
				mapOfAttributeHoldersAttributes.put(attribute.getName(), newAttribute);
				actionsInTransaction.put(entityForAttributes, mapOfAttributeHoldersAttributes);
			}
		} else {
			this.removeAttributeFromCacheForAttributes(attribute);
		}
	}

	public Attribute getAttributeFromCache(AttributeHolders attributeHolders, String attributeName) {
		if (applicationCache.get(attributeHolders)==null) {
			return null;
		}
		Map<String,AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
		mapOfAttributeHoldersAttributes = applicationCache.get(attributeHolders);
		if (mapOfAttributeHoldersAttributes.get(attributeName) == null) {
			return null;
		}
		Attribute attribute = new Attribute((Attribute) mapOfAttributeHoldersAttributes.get(attributeName));
		return attribute;
	}

	public Attribute getAttributeFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, String attributeName) {
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(attributeHolders))!=null) {
					if (actionsInTransaction.get(attributeHolders).get(attributeName)!=null) {
						return new Attribute((Attribute)actionsInTransaction.get(attributeHolders).get(attributeName));
					}
				}
			}
		}
		return this.getAttributeFromCache(attributeHolders, attributeName);
	}

	public Attribute getAttributeFromCacheInTransaction(PerunBean primaryHolder, String attributeName) {
		return this.getAttributeFromCacheInTransaction(primaryHolder, null, attributeName);
	}

	public Attribute getAttributeFromCacheForString(String key, String attributeName) {
		if (applicationCache.get(key)==null) {
			return null;
		}
		Map<String,AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
		mapOfAttributeHoldersAttributes = applicationCache.get(key);
		if (mapOfAttributeHoldersAttributes.get(attributeName) == null) {
			return null;
		}
		Attribute attribute = new Attribute((Attribute) mapOfAttributeHoldersAttributes.get(attributeName));
		return attribute;
	}

	public Attribute getAttributeFromCacheForStringInTransaction(String key, String attributeName) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(key))!=null) {
					if (actionsInTransaction.get(key).get(attributeName)!=null) {
						return new Attribute((Attribute)actionsInTransaction.get(key).get(attributeName));
					}
				}
			}
		}
		return this.getAttributeFromCacheForString(key, attributeName);
	}

	public List<AttributeDefinition> getAllAttributesFromCache(AttributeHolders attributeHolders) {
		List<AttributeDefinition> listOfAttributes = new ArrayList<>();
		if (applicationCache.get(attributeHolders)!=null) {
			listOfAttributes.addAll(applicationCache.get(attributeHolders).values());
		}
		return listOfAttributes;
	}

	public List<AttributeDefinition> getAllAttributesFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder) {
		List<AttributeDefinition> listOfAttributes = new ArrayList<>();
		List<AttributeDefinition> listOfAttributesFromTransaction = new ArrayList<>();
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(attributeHolders))!=null) {
					listOfAttributesFromTransaction.addAll(actionsInTransaction.get(attributeHolders).values());
				}
			}
		}
		listOfAttributes.addAll(this.getAllAttributesFromCache(attributeHolders));
		for (AttributeDefinition attribute: listOfAttributesFromTransaction) {
			if (!listOfAttributes.contains(attribute)) {
				listOfAttributes.add(attribute);
			}
		}
		return listOfAttributes;
	}

	public List<AttributeDefinition> getAllAttributesFromCacheInTransaction(PerunBean primaryHolder) {
		return this.getAllAttributesFromCacheInTransaction(primaryHolder, null);
	}

	public Attribute getAttributeByIdFromCache(AttributeHolders attributeHolders, int id) {
		if (applicationCache.get(attributeHolders)==null) {
			return null;
		}
		Map<String,AttributeDefinition> mapOfAttributeHoldersAttributes = new HashMap<>();
		mapOfAttributeHoldersAttributes = applicationCache.get(attributeHolders);
		List<AttributeDefinition> attributes = new ArrayList<>();
		attributes.addAll(mapOfAttributeHoldersAttributes.values());
		for (AttributeDefinition attributeFromList: attributes) {
			if (attributeFromList.getId()==id) {
				return new Attribute((Attribute)attributeFromList);
			}
		}
		return null;
	}

	public Attribute getAttributeByIdFromCacheInTransaction(PerunBean primaryHolder, PerunBean secondaryHolder, int id) {
		AttributeHolders attributeHolders = new AttributeHolders(primaryHolder, secondaryHolder);
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(attributeHolders))!=null) {
					List<AttributeDefinition> attributes = new ArrayList<>();
					attributes.addAll(actionsInTransaction.get(attributeHolders).values());
					for (AttributeDefinition attributeFromList: attributes) {
						if (attributeFromList.getId()==id) {
							return new Attribute((Attribute)attributeFromList);
						}
					}
				}
			}
		}
		return this.getAttributeByIdFromCache(attributeHolders, id);
	}

	public Attribute getAttributeByIdFromCacheInTransaction(PerunBean primaryHolder, int id) {
		return this.getAttributeByIdFromCacheInTransaction(primaryHolder, null, id);
	}

	public AttributeDefinition getAttributeFromCacheForAttributes(String attributeName) {
		if (applicationCache.get(entityForAttributes)==null) {
			return null;
		}
		Map<String,AttributeDefinition> mapOfAttributes = new HashMap<>();
		mapOfAttributes = applicationCache.get(entityForAttributes);
		if (mapOfAttributes.get(attributeName) == null) {
			return null;
		}
		AttributeDefinition attribute = new AttributeDefinition(mapOfAttributes.get(attributeName));
		return attribute;
	}

	public AttributeDefinition getAttributeFromCacheForAttributesInTransaction(String attributeName) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(entityForAttributes))!=null) {
					if (actionsInTransaction.get(entityForAttributes).get(attributeName)!=null) {
						return new AttributeDefinition(actionsInTransaction.get(entityForAttributes).get(attributeName));
					}
				}
			}
		}
		return this.getAttributeFromCacheForAttributes(attributeName);
	}

	public AttributeDefinition getAttributeByIdFromCacheForAttributes(int id) {
		if (applicationCache.get(entityForAttributes)==null) {
			return null;
		}
		Map<String,AttributeDefinition> mapOfAttributes = new HashMap<>();
		mapOfAttributes = applicationCache.get(entityForAttributes);
		for (AttributeDefinition attribute: mapOfAttributes.values()) {
			if (attribute.getId() == id) {
				return new AttributeDefinition(attribute);
			}
		}
		return null;
	}

	public AttributeDefinition getAttributeByIdFromCacheForAttributesInTransaction(int id) {
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.getResource(this);
			if (actionsInTransaction!=null) {
				if ((actionsInTransaction.get(entityForAttributes))!=null) {
					Map<String,AttributeDefinition> mapOfAttributes = new HashMap<>();
					mapOfAttributes = applicationCache.get(entityForAttributes);
					for (AttributeDefinition attribute: mapOfAttributes.values()) {
						if (attribute.getId() == id) {
							return new AttributeDefinition(attribute);
						}
					}
				}
			}
		}
		return this.getAttributeByIdFromCacheForAttributes(id);
	}

	public void clean() {
		Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.unbindResourceIfPossible(this);
	}

	public void flush() {
		Map<Object,Map<String,AttributeDefinition>> actionsInTransaction = (Map<Object,Map<String,AttributeDefinition>>) TransactionSynchronizationManager.unbindResourceIfPossible(this);
		if(actionsInTransaction == null) {
			return;
		}

		for(Object object: actionsInTransaction.keySet()) {
			if (object instanceof AttributeHolders) {
				AttributeHolders attributeHolders = (AttributeHolders) object;
				Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = actionsInTransaction.get(attributeHolders);
				for(String attributeName: mapOfAttributeHoldersAttributes.keySet()) {
					Attribute attribute = new Attribute(mapOfAttributeHoldersAttributes.get(attributeName));
					if (attribute.getValue()==null) {
						this.removeAttributeFromCache(attributeHolders, mapOfAttributeHoldersAttributes.get(attributeName));
					}
					else {
						this.addAttributeToCache(attributeHolders, attribute);
					}
				}
			}
			else {
				if (object instanceof String) {
					String key = (String) object;
					Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = actionsInTransaction.get(key);
					for(String attributeName: mapOfAttributeHoldersAttributes.keySet()) {
						Attribute attribute = new Attribute(mapOfAttributeHoldersAttributes.get(attributeName));
						if (attribute.getValue()==null) {
							this.removeAttributeFromCacheForString(key, mapOfAttributeHoldersAttributes.get(attributeName));
						}
						else {
							this.addAttributeToCacheForString(key, attribute);
						}
					}
				}
				else {
					if (object.equals(entityForAttributes)) {
						Map<String, AttributeDefinition> mapOfAttributeHoldersAttributes = actionsInTransaction.get(entityForAttributes);
						for(String attributeName: mapOfAttributeHoldersAttributes.keySet()) {
							if (mapOfAttributeHoldersAttributes.get(attributeName) instanceof Attribute) {
								Attribute attribute = new Attribute(mapOfAttributeHoldersAttributes.get(attributeName));
								if (attribute.getValue()==null) {
									this.removeAttributeFromCacheForAttributes(mapOfAttributeHoldersAttributes.get(attributeName));
								}
							}
							else {
								this.addAttributeToCacheForAttributes(mapOfAttributeHoldersAttributes.get(attributeName));
							}
						}
					}
				}
			}
		}
	}
}
