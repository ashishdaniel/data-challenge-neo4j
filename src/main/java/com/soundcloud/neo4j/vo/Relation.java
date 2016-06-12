package com.soundcloud.neo4j.vo;

import org.neo4j.graphdb.RelationshipType;

/**
 * Value Object representing two people and their relation
 * 
 * @author Ashish
 *
 */
public class Relation {

	private String personA;
	private String personB;
	private RelationshipType relationType;

	public Relation(String personA, String personB, RelationshipType relationType) {
		this.personA = personA;
		this.personB = personB;
		this.relationType = relationType;
	}

	public String getPersonA() {
		return personA;
	}

	public String getPersonB() {
		return personB;
	}
	
	public RelationshipType getRelationType() {
		return relationType;
	}

	@Override
	public String toString() {
		return "Relation [personA=" + personA + ", personB=" + personB + "]";
	}

}
