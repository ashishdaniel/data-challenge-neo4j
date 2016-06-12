package com.soundcloud.util;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 * For holding constant values
 * 
 * @author Ashish
 *
 */
public class Constants {
	public static final Label PERSON = () -> "Person";
	public static final RelationshipType FRIEND_OF = () -> "FRIEND_OF";
	public static final String NAME = "name";
	public static final String CONNECTIONS = "connections";
	public static final String TAB_CHAR = "\t";
}
